package grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import javax.naming.directory.InvalidAttributesException;

import placer.PlacingPolicy;

import square.GridUpdater;
import square.OnSquare;
import square.OnSquareType;
import square.Player;
import square.Square;
import square.SquareObserver;
import square.UpdatePolicy;

import exceptions.DimensionException;
import exceptions.IllegalPositionException;
import exceptions.IllegalSquareException;
import game.GameMode;
import game.Update;

/**
 * @invar The minimum size of a Grid is 10x10.
 * 		  | this.getDimension().getX() >= 10 && this.getDimension().getY() >= 10
 * @invar At least 1 Wall stands on this Grid.
 * @invar The maximum length of a vertical (horizontal) Wall is 50% of the vertical (horizontal) size of the grid,
 * 		  rounded up to an integer value
 * 	      | if( (wall.getDirection() == Direction.NORTH) || (wall.getDirection() == Direction.SOUTH) ) 
 * 		  |		then ( wall.getSize() <= (int) Math.ceil(this.getDimension().getY()/2) )
 *		  | else ( wall.getSize() <= (int) Math.ceil(this.getDimension().getX()/2) )
 * @invar Walls can't touch or intersect.
 */

public class Grid {
	private Dimension dimension;
	private Random rand = new Random();
	private GridUpdater updater;
	/**
	 * Contains the Squares of this Grid.
	 * getSquare(new Position(0,0)) is left bottom
	 * getSquare(new Position(dimension.getX()-1, dimension.getY()-1)) is right up
	 */
	protected HashSet<Square> squares;
	private HashSet<Player> players = new HashSet<Player>();
	
	/**
	 * Constructor for the Grid.
	 * @param dim: the Dimension for this Grid.
	 * @throws DimensionException: xValue or yValue was an invalid value (smaller than 10)
	 */
	public Grid(int x, int y) throws DimensionException{
		if(x<10 || y<10){
			throw new DimensionException("Invalid dimension");
		}
		initializeSquares(new Dimension(x,y));
	}

	/**
	 * Get the Dimension of this Grid.
	 */
	public Dimension getDimension(){
		try{
		return new Dimension(this.dimension.getX(), this.dimension.getY());
		} catch (DimensionException e) {
		}
		return null;
	}

	protected Random getRandom(){
		return rand;
	}
	
	public HashSet<Square> getSquares(){
		return squares;
	}
	
	/**
	 * Get the Square that lays a given Position.
	 * @throws IllegalPositionException : is thrown when the given Position does not lay on this Grid.
	 */
	public Square getSquare(Position p) throws IllegalPositionException{
		if(!pointOnGrid(p)){
			throw new IllegalPositionException();
		}
		for(Square sq: squares){
			if(sq.getPosition().equals(p)){
				return sq;
			}
		}
		throw new IllegalPositionException();
	}

	/**
	 * Initialize the Squares: put an empty Square on each Position of this Grid.
	 * Give each Square its neighbours.
	 */
	private void initializeSquares(Dimension dim){
		this.dimension = dim;
		squares = new HashSet<Square>();
		for(int x = 0; x<dim.getX(); x++){
			for(int y = 0; y<dim.getY(); y++){
				squares.add(new Square(new Position(x, y)));
			}
		}
		for(Square sq: squares){
			for(Direction d: Direction.values()){
				addNeighbour(d, sq);
			}
		}
	}
	
	/**
	 * Give a Square a neighbourSquare in a given Direction. 
	 */
	private void addNeighbour(Direction dir, Square square){
		try {
			square.addNeighbour(getSquare(dir.moveOne(square.getPosition())));
		} catch (IllegalPositionException e) {
		} catch (IllegalSquareException e) {
		}
	}

	/**
	 * Check if a Player can move from his current Position in a given Direction.
	 * If the Player tries to move to a destination that does not lie on this Grid, return false.
	 * If the Player moves horizontal or vertical, the LightTrails will be checked when the Player is put on the Square.
	 * If the Player moves diagonal, the LightTrails are checked.
	 * @param player: the Player that wants to move
	 * @param dir: the Direction the Player wants to move in.
	 */
	public boolean canMoveFromTo(Player player, Direction dir){
		if(player==null || dir == null){
			return false;
		}
		if(!pointOnGrid(dir.moveOne(player.getPosition()))){
			return false;
		}
		if(dir.isDiagonal()){
			Position p1 = new Position(player.getPosition().getX()+dir.getX(), player.getPosition().getY());
			Position p2 = new Position(player.getPosition().getX(), player.getPosition().getY()+dir.getY());
			Square square1=null;
			Square square2=null;
			try {
				square1 = getSquare(p1);
				square2 = getSquare(p2);
			} catch (IllegalPositionException e) {
			}
			if(square1==null || square2==null){
				return true;
			}
			for(Player p: players){
				try {
					if(p.getLightTrail().hasLink(square1, square2)){
						return false;
					}
				} catch (InvalidAttributesException e) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	
	/**
	 * Check if a given Position lays on the Grid.
	 * @param pos the Position to check
	 * @return return true if the point lays on the Grid
	 */
	public boolean pointOnGrid(Position pos){
		if(pos==null)
			return false;
		if(pos.getX() < 0 || pos.getX() >= dimension.getX())
			return false;
		return (pos.getY()>=0 && pos.getY() < dimension.getY());
	}
	
	/**
	 * Get for each Position the OnSquares that lays on the matching Square.
	 */
	public HashMap<Position, ArrayList<OnSquare>> getOnSquares(){
		HashMap<Position, ArrayList<OnSquare>> out = new HashMap<Position, ArrayList<OnSquare>>();
		ArrayList<OnSquare> hash;
		for(Square sq: squares){
			hash = sq.showOnSquares();
			if(hash!=null){
				out.put(sq.getPosition(), hash);
			}
		}
		return out;
	}
	
	/**
	 * Update the Grid after a turn.
	 */
	public void updateGrid(Update update){
		updater.update(update, squares);
	}

	public void setUpdatePolicies(ArrayList<UpdatePolicy> updatePolicies) {
		updater = new GridUpdater(updatePolicies);
		
	}
}
