package grid;


public class Position {
	
	private int x;
	private int y;
	
	/**
	 * Constructor
	 */
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return the x-value of this Position
	 */
	public int getX(){
		return this.x;
	}
	
	/**
	 * @return the y-value of this Position
	 */	
	public int getY(){
		return this.y;
	}
	
	/**
	 * Check if this Position touches the given Position. 
	 * @param pos: the given Position.
	 * @return true if the two Positions touch.
	 */
	public boolean isAdjacentTo(Position pos){
		return (Math.abs(this.x-pos.getX())<=1 && Math.abs(this.y-pos.getY())<=1);
	}
	
	/**
	 * Check how this Position is positioned in respect to pos.
	 * @param pos: the other Position.
	 * @return Check in which Direction pos lays if you take this as center point.
	 * (if you move, you move from this to pos) 
	 * (f.e. if pos lays left of this, return EAST.)
	 */
	public Direction getDirection(Position pos){
		int x = pos.getX()-this.getX();
		int y = pos.getY()-this.getY();
		if(x!=0){
			x/=Math.abs(x);
		} 
		if(y!=0){
			y/=Math.abs(y);
		}
		return Direction.getDirection(x, y);
	}
	
	@Override
	public boolean equals(Object pos){
		if(pos.getClass()!=Position.class){
			return false;
		}
		return (((Position)pos).getX()==this.x && ((Position)pos).getY()==this.y);
	}
	
	@Override
	public String toString(){
		return "("+x+","+y+")";
	}

	/**
	 * Check if this Position is left under the given Position.
	 * @return Return true if the X-position of this is smaller or equal to the X-position of rightUp AND
	 * the Y-position of this is smaller or equal to the Y-position of rightUp.
	 */
	public boolean isLeftUnderOf(Position rightUp) {
		return this.getX()<=rightUp.getX() && this.getY()<=rightUp.getY();
	}
}
