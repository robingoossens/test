package square;


import java.util.ArrayList;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPositionException;
import exceptions.IllegalPutException;
import exceptions.IllegalRemoveException;
import exceptions.IllegalSquareException;
import game.Update;
import grid.Direction;
import grid.Position;


/**
 * A Square is an OnSquareContainer, the basic building part of the Grid
 * in the Game. Every Square is linked to its Neighbours and contains OnSquares from the Game.
 * Squares have an EffectManager to manage the Effects that the OnSquares have on a Square.
 * @invar A Square with a Wall on, has no Player or Items on it. (can contain a PowerFailure)
 * 		  | if onSquare.contains(Wall w)
 * 		  | 	then !onSquare.contains(Player p)
 * 		  |			 !onSquare.contains(Item i)
 */
public class Square implements OnSquareContainer{
	private Position position;
	private ArrayList<OnSquare> onSquares = new ArrayList<OnSquare>();
	private HashSet<Square> neighbours = new HashSet<Square>();
	private EffectManager manager = new EffectManager();

	public Square(){
		this.position = null;
	}
	
	public Square(Position pos){
		this.position = pos;
	}

	@Override
	public boolean canAdd(OnSquare on) {
		if(on==null){
			return false;
		}
		for(OnSquare onSq: onSquares){
			if(!onSq.isCompatibleWith(on)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check if this Square can have an OnSquare in its range.
	 */
	public boolean canHaveInRange(OnSquare onSquare, int range){
		for(OnSquare o: onSquares){
			if(!o.canHaveInRange(onSquare, range)){
				return false;
			}
		}
		return true;
	}

	/**
	 * Check of this Square has a given OnSquare on it. 
	 * @return Return true if this Square contains the given OnSquare.
	 */
	public boolean contains(OnSquare on){
		return onSquares.contains(on);
	}
	
	public HashSet<Item> getItems(){
		HashSet<Item> out =  new HashSet<Item>();
		for(OnSquare on : onSquares){
			if(on instanceof Item){
				out.add((Item) on);
			}
		}
		return out;
	}
	
	/**
	 * @return the neighbour Square that lays in Direction dir from this Square.
	 * @throws IllegalPositionException is thrown when this Square has no neighbour in the given Direction.
	 * This can happen when this Square is placed on a border in a Grid. 
	 */
	public Square getNeighbour(Direction dir) throws IllegalPositionException{
		for(Square sq: neighbours){
			if(dir.moveOne(this.getPosition()).equals(sq.getPosition())){
				return sq;
			}
		}
		throw new IllegalPositionException();
	}
	
	public HashSet<Square> getNeighbours(){
		return neighbours;
	}
	
	protected ArrayList<OnSquare> getOnSquares(){
		return this.onSquares;
	}

	public Position getPosition(){
		return this.position;
	}
	
	public ArrayList<OnSquare> showOnSquares(){
		ArrayList<OnSquare> returnOns = new ArrayList<OnSquare>();
		for(OnSquare on:onSquares)
			returnOns.add(on.clone());
		return returnOns;
	}
	/**
	 * If square is adjacent to this Square 
	 * @param square
	 * @throws IllegalSquareException Is thrown when square is no neighbour if this Square.
	 */
	public void addNeighbour(Square square) throws IllegalSquareException{
		if(!square.getPosition().isAdjacentTo(this.position)){
			throw new IllegalSquareException();
		}
		neighbours.add(square);
	}
	
	protected void executeEffects(OnSquare p){
		ArrayList<OnSquare> tempOn = new ArrayList<OnSquare>();
		for(OnSquare on:onSquares)
			if(!on.isDestroyed())
				tempOn.add(on);
		manager.executeEffects(tempOn,p);
	}
	
	/**
	 * Put a given OnSquare on this Square. If the Square can be (and is) put on this Square,
	 * update the Effects on this Square.
	 * If the given OnSquare is already on this square, nothing happens.
	 * @param on the OnSquare to be put on this Square.
	 * @throws IllegalOnSquareException thrown when the given Onsquare is not compatible with the other OnSquares on this Square.
	 * @throws IllegalPutException thrown when 
	 * 			| !on.isPuttable()
	 * @throws InvalidAttributesException thrown when the given OnSquare is null.
	 * @post if(isCompatibleWith(on))
	 * 			| then onSquares.contains(on)
	 * @post if(!isCompatibleWith(on))
	 * 			| then !onSquares.contains(on) 
	 */
	public void putOnSquare(OnSquare on) throws IllegalOnSquareException, IllegalPutException, InvalidAttributesException{
		if(on==null)
			throw new InvalidAttributesException();
		else if(!on.isCompatibleWith(this)){
				throw new IllegalOnSquareException();
		} 
		else {				

			for(OnSquare ons: onSquares){
				ons.trigger(on);
			}
				onSquares.add(on);
				try {
					on.put(this);
				} catch (InvalidAttributesException e) {
				}
				executeEffects(on);
		}	
	}
	
	/**
	 * Remove an OnSquare from this Square. Update the Effects from this Square.
	 * @param on the OnSquare to removed from this Square.
	 * @throws IllegalOnSquareException thrown when the given OnSquare does not lay on this Square.
	 */
	public void removeOnSquare(OnSquare on) throws IllegalOnSquareException{
		if(!onSquares.contains(on)){
			throw new IllegalOnSquareException();
		}
		onSquares.remove(on);
		try {
			on.remove(this);
		} catch (InvalidAttributesException e) {
		} catch (IllegalRemoveException e) {
		}
	}
	
	/**
	 * Update this Square.
	 * Decrement the time to lives.
	 */
	public void updateSquare(Update update){
		ArrayList<OnSquare> temp = new ArrayList<OnSquare>();
		for(OnSquare o:this.onSquares){
			temp.add(o);
		}
		for(OnSquare o:onSquares)
			for(UpdatePriorityPolicy policy:o.getUpdatePriorityPolicies())
				policy.prioritise(temp);
		for(OnSquare o: temp){
			o.update(update);
		}
	}
}
