package square;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

import exceptions.IllegalDirectionException;
import exceptions.IllegalPositionException;
import exceptions.IllegalPutException;
import exceptions.IllegalSizeException;
import grid.Direction;
import grid.Position;

/**
 * A Wall is an OnSquare with a size and a direction
 * Walls cannot be passed by Players and are not compatible with most OnSquares
 * @invar The minimum size of a Wall is 2.
 * 		  | this.getSize()>=2
 * @invar A Wall can't be put diagonally.
 * 		  | !this.getDirection().isDiagonal()
 *
 */
public class Wall extends ExtendedObject {
	
	/**
	 * Creates a Wall in a given Direction with a given size
	 * @param dir the Direction of the Wall
	 * @param size the size of the Wall
	 * @param arrayList 
	 * @throws IllegalSizeException the given size for the Wall is not valid
	 * @throws IllegalDirectionException the given Direction for the Wall is not valid
	 * @throws InvalidAttributesException 
	 */
	public Wall(Direction dir, int size, ArrayList<Effect> effects) throws IllegalSizeException, IllegalDirectionException, InvalidAttributesException{
		super(OnSquareType.WALL,effects);
		if(!isValidDirection(dir)){
			throw new IllegalDirectionException();
		}
		else if(!isLegalSize(size)){
			throw new IllegalSizeException();
		}
		else{
			setDirection(dir);
			setSize(size);
		}
	}
	
	//TODO set dir/size to ensure islegal?
	protected void setSize(int size){
		if(isLegalSize(size))
			super.setSize(size);
	}
	protected void setDirection(Direction dir){
		if(isValidDirection(dir))
			super.setDirection(dir);
	}
	
	/**
	 * Returns whether the given Direction is a valid Direction for this Wall
	 * @param dir the given Direction
	 * @return 	true if the given Direction is not diagonal
	 * 			false if the given Direction is diagonal
	 */
	private boolean isValidDirection(Direction dir){
		return !dir.isDiagonal();
	}
	
	/**
	 * Returns whether the given size is a valid size for this Wall
	 * @param size the given size 
	 * @return	true if the given size is larger or equal than two
	 * 			false if the given size is smaller than two
	 */
	private boolean isLegalSize(int size){
		return size>=2;
	}	 
	
	/**
	 * Gives the begin Position of this Wall
	 * @return the begin Position of this Wall
	 */
	public Position getBeginPosition(){
		return this.getSquare().getPosition();
	}

	/**
	 * Check if this Wall is compatible with another OnSquare
	 * @return true if the given OnSquare is a PowerFailure
	 * 		   false for all other OnSquares
	 */
	@Override
	public boolean isCompatibleWith(OnSquare on) {
		return (on instanceof PowerFailure);
	}
	
	/**
	 * Returns whether this Wall can be in the given range of the given OnSquare
	 * Walls have to be 1 Square apart
	 */
	@Override
	public boolean canHaveInRange(OnSquare o, int range) {
		if(range==0){
			return isCompatibleWith(o);
		}
		if(range==1){
			return !(o instanceof Wall);
		}
		return true;
	}
	
	/**
	 * Returns whether this Wall is visible
	 */
	@Override
	public boolean isVisible() {
		return isDestroyed();
	}
	
	
	/**
	 * Check if a Wall would lay completely on Squares that are accessible
	 * @param w: the Wall you want to check 
	 * @return return false if the Wall doesn't lay on accessible Squares
	 */
	protected boolean wallOnGrid(Square square){
		if(square==null){
			return false;
		}
		for(int i = 1; i<getSize(); i++){
			try {
				square=square.getNeighbour(getDirection());
			} catch (IllegalPositionException e) {
				return false;
			}
		}
		return true;
	}

	/**
	 * If you want to place this Wall on a beginPosition, calculate what its 
	 * end position will be.
	 * @param pos : the beginposition of the Wall
	 * @return: Return the endposition of the Wall
	 */
	public Position getEndWall(Position pos){
		return new Position(pos.getX()+getSize()*getDirection().getX(), pos.getY()+getSize()*getDirection().getY());
	}
	
	
	/**
	 * Check if the given Wall is legal.
	 * @param square: the begin Square of this Wall
	 * @return 	false if the Wall touches or intersects another Wall
	 * 			false if the Wall would not lay completely on this Grid
	 * 			true otherwise
	 */
	public boolean isLegalWall(Square square){
		if(!wallOnGrid(square)){
			return false;
		}
		Square temp;
		for(int i = 0; i<getSize(); i++){
			for(Direction d: Direction.values()){
					try {
						temp=square.getNeighbour(d);
						if(!temp.contains(this)){
							if(!temp.canHaveInRange(this, 1)){
								return false;
							}
						}
					} catch (IllegalPositionException e) {
						//can be that square lays on an edge
					}
			}
			try {
				square=square.getNeighbour(getDirection());
			} catch (IllegalPositionException e) {
				//is checked in wallOnGrid(square) and doesn't matter for the last index of the loop
			}
		}
		return true;
	}
	
	@Override
	protected void put(OnSquareContainer container) throws IllegalPutException{
		if(container==null)
			throw new IllegalPutException();
		if(isLegalWall((Square) container)){
			super.put(container);
		} else {
			throw new IllegalPutException();
		}
	}
}
