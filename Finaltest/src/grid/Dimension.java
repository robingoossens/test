package grid;
import exceptions.DimensionException;

public class Dimension {
	private int x;
	private int y;
	
	public Dimension(int x, int y) throws DimensionException{
		setDimension(x, y);
	}
	
	/**
	 * Set the dimensions.
	 * @param x: the x-value of the Dimension
	 * @param y: the y-value of the Dimension
	 * @throws DimensionException: the x or y value was smaller than 0
	 */
	private void setDimension(int x, int y) throws DimensionException{
		if (Math.min(x, y)<0){
			throw new DimensionException("Entered wrong dimension");
		} else {
			this.x = x;
			this.y = y;
		}
	}
	
	/**
	 * @return return the x-value of this Dimension
	 */
	public int getX(){
		return this.x;
	}
	
	/**
	 * @return return the y-value of this Dimension
	 */
	public int getY(){
		return this.y;
	}
}
