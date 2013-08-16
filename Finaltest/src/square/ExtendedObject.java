package square;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPositionException;
import exceptions.IllegalPutException;
import exceptions.IllegalRemoveException;
import grid.Direction;

/**
 * An ExtendedObject is an OnSquare that lies on multiple Squares.
 * These OnSquares have similar behavior when created, placed and removed.
 */
public abstract class ExtendedObject extends OnSquare{
	
	private int size;
	private Direction direction;
	
	public ExtendedObject(OnSquareType type, ArrayList<Effect> effects) throws InvalidAttributesException{
		super(type,effects);
	}
	/**
	 * Returns the Square this ExtendedObject lays on
	 * @return the Square this ExtendedObject lays on
	 */
	protected Square getSquare(){
		return (Square)getOnSquareContainer();
	}
	
	/**
	 * Returns the size of this ExtendedObject
	 * @return the size of this ExtendedObject
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Sets the size of this ExtendedObject to the given size
	 * @param size the size to be set for this ExtendedObject
	 */
	protected void setSize(int size) {
		this.size = size;
	}
	
	/**
	 * Returns the direction of this ExtendedObject
	 * @return the direction of this ExtendedObject
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * Sets the Direction of this ExtendedObject to the given Direction
	 * @param direction the Direction to be set for this ExtendedObject
	 */
	protected void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	/**
	 * Puts this ExtendedObject in the given OnSquareContainer
	 * @param container the OnSquareContainer this ExtendedObject will be put in
	 */
	protected void put(OnSquareContainer container) throws IllegalPutException{
		if(isPuttableOn(container)){
			if(getOnSquareContainer()==null){
				Square temp = (Square)container;
				setOnSquareContainer(container);
				for(int i=1;i<size;i++){
					try {
						temp = temp.getNeighbour(getDirection());
						temp.putOnSquare(this);
					} catch (IllegalPositionException e) {
						throw new IllegalPutException();
					} catch (InvalidAttributesException e) {
						throw new IllegalPutException();
					} catch (IllegalOnSquareException e) {
						throw new IllegalPutException();
					}
				}
				setPuttable(false);
			}
			else if(!isPuttable())
				throw new IllegalPutException();
		}
		else throw new IllegalPutException();
	}
	
	/**
	 * Removes this ExtendedObject from the given OnSquareContainer
	 * @param container the OnSquareContainer this ExtendedObject has to be removed from
	 */
	protected void remove(OnSquareContainer container) throws InvalidAttributesException, IllegalRemoveException{
		if(!isPuttableOn(container))
			throw new InvalidAttributesException();
		if(!isPuttable()){
			if(getSquare() != null && isPartOf((Square)container) && !container.contains(this)){
				if(!container.equals(getOnSquareContainer()))
					try {
						getOnSquareContainer().removeOnSquare(this);
					} catch (IllegalOnSquareException e) {
					}
				else{
					Square temp = getSquare();
					super.remove(getOnSquareContainer());
					setPuttable(false);
					for(int i=0;i<getSize()-1;i++){
						try {
							temp = temp.getNeighbour(getDirection());
							if(temp.contains(this))
								temp.removeOnSquare(this);
						} catch (IllegalPositionException e) {
						} catch (IllegalOnSquareException e) {
						}
					}
					setPuttable(true);
				}
			}
		}
		else throw new IllegalRemoveException();
	}
	
	/**
	 * Returns whether this ExtendedObject is part of the given Square
	 * @param container the Square this ExtendedObject may be part of
	 * @return	true if this ExtendedObject is part of the given Square
	 * 			false if this ExtendedObject is not part of the given Square
	 */
	protected boolean isPartOf(Square container) {
		if(container==null)
			return false;
		boolean ispart = false;
		Square temp = getSquare();
		for(int i=0;i<getSize();i++){
			if(temp.equals(container)){
				ispart = true;
				break;
			}
			try {
				temp = temp.getNeighbour(getDirection());
			} catch (IllegalPositionException e) {
				ispart=false;
			}
		}
		return ispart;
	}
	
	/**
	 * Returns whether this Wall is compatible with the given OnSquareContainer
	 * @return	true if this Wall is compatible with the given OnSquareContainer
	 * 			false if this Wall is not compatible with the given OnSquareContainer
	 */
	public boolean isCompatibleWith(OnSquareContainer container){		
		if(!isPuttableOn(container))
			return false;
		else if(getOnSquareContainer()==null){	
			Square s = (Square)container;
			boolean canAdd=true;
			for(int i=0;i<getSize()-1;i++){
				if(!s.canAdd(this)){
					return false;
				}
				try {
					s = s.getNeighbour(getDirection());
				} catch (IllegalPositionException e) {
					canAdd=false;
				}
			}
			return canAdd;
		}
		else return isPartOf((Square)container);
	}
	
}
