package grid;

import java.util.ArrayList;
import java.util.Random;

public enum Direction {

	NORTH(0,1) {
		
		@Override
		public ArrayList<Position> getSurroundingPositions(Position begin,
				int size) {
			ArrayList<Position> out = new ArrayList<Position>();
			out.add(new Position(begin.getX(), begin.getY()-1));
			out.add(new Position(begin.getX(), begin.getY()+size));
			for(int i = -1; i<=size; i++){
				out.add(new Position(begin.getX()-1, begin.getY()+i));
				out.add(new Position(begin.getX()+1, begin.getY()+i));
			}
			return out;
		}
	},
	EAST(1,0) {
	
		@Override
		public ArrayList<Position> getSurroundingPositions(Position begin,
				int size) {
			ArrayList<Position> out = new ArrayList<Position>();
			out.add(new Position(begin.getX()-1, begin.getY()));
			out.add(new Position(begin.getX()+size, begin.getY()));
			for(int i = -1; i<=size; i++){
				out.add(new Position(begin.getX()+i, begin.getY()-1));
				out.add(new Position(begin.getX()+i, begin.getY()+1));
			}
			return out;
		}
	},
	SOUTH(0,-1) {
	
		@Override
		public ArrayList<Position> getSurroundingPositions(Position begin,
				int size) {
			ArrayList<Position> out = new ArrayList<Position>();
			out.add(new Position(begin.getX(), begin.getY()+1));
			out.add(new Position(begin.getX(), begin.getY()-size));
			for(int i = -1; i<=size; i++){
				out.add(new Position(begin.getX()+1, begin.getY()-i));
				out.add(new Position(begin.getX()-1, begin.getY()-i));
			}
			return out;
		}
	},
	WEST(-1,0) {
		
		@Override
		public ArrayList<Position> getSurroundingPositions(Position begin,
				int size) {
			ArrayList<Position> out = new ArrayList<Position>();
			out.add(new Position(begin.getX()+1, begin.getY()));
			out.add(new Position(begin.getX()-size, begin.getY()));
			for(int i = -1; i<=size; i++){
				out.add(new Position(begin.getX()-i, begin.getY()+1));
				out.add(new Position(begin.getX()-i, begin.getY()-1));
			}
			return out;
		}
	},
	
	NORTHEAST(1,1){
		
		@Override
		public ArrayList<Position> getSurroundingPositions(Position begin,
				int size) {
			return null;
		}
	},
	
	NORTHWEST(-1,1){
		@Override
		public ArrayList<Position> getSurroundingPositions(Position begin,
				int size) {
			return null;
		}
	},
	
	SOUTHEAST(1,-1){
		
		@Override
		public ArrayList<Position> getSurroundingPositions(Position begin,
				int size) {
			return null;
		}
	},
	
	SOUTHWEST(-1,-1){
		
		@Override
		public ArrayList<Position> getSurroundingPositions(Position begin,
				int size) {
			return null;
		}
	};
	
	private Direction(int xValue, int yValue){
		this.xValue = xValue;
		this.yValue = yValue;
	}
	
	private int xValue;
	private int yValue;
	
	
	public int getX(){
		return xValue;
	}
	
	public int getY(){
		return yValue;
	}
	
	/**
	 * @param begin the beginPosition
	 * @param size the length of the line to surround
	 * @return a list of Positions that surround a "line" (can be negative)
	 */
	public abstract ArrayList<Position> getSurroundingPositions(Position begin, int size);

	public static Direction getOpposite(Direction d){
		return getDirection(-1*d.getX(), -1*d.getY());
	}
	
	public static Direction getDirection(int x, int y){
		if(x==0){
			if(y==1) return Direction.NORTH;
			if(y==-1) return Direction.SOUTH;
		}
		if(x==1){
			if(y==0) return Direction.EAST;
			if(y==1) return Direction.NORTHEAST;
			if(y==-1) return Direction.SOUTHEAST;
		}
		if(x==-1){
			if(y==-1) return Direction.SOUTHWEST;
			if(y==0) return Direction.WEST;
			if(y==1) return Direction.NORTHWEST;
		}
		return null;
	}
	
	/**
	 * @param begin: the beginPosition
	 * @param size: the number of Positions to calculate
	 * @return Return all the Positions that lay between begin and getEndWall(begin, size), begin included
	 */
	public ArrayList<Position> getPositionsBetween(Position begin, int size){
		ArrayList<Position> out = new ArrayList<Position>();
		out.add(begin);
		for(int i = 1; i<size; i++){
			out.add(new Position(begin.getX()+xValue*i, begin.getY()+yValue*i));
		}
		return out;
	}
	
	/**
	 * @return return a random Direction from the eight directions
	 */
	public static Direction getRandomAllDirection(Random rand){
		int number = rand.nextInt(8);
		if(number==0){
			return NORTH;
		} else if(number==1){
			return NORTHEAST;
		} else if(number==2){
			return EAST;
		} else if(number==3){
			return SOUTHEAST;
		} else if(number==4){
			return SOUTH;
		} else if(number==5){
			return SOUTHWEST;
		} else if(number==6){
			return WEST;
		} else{
			return NORTHWEST;
		}
	}
	
	/**
	 * Generate the next direction in the specified order.
	 * @param dir the previous direction.
	 * @param clockwise the specified order, clockwise or counterclockwise.
	 * @return the next direction in the specified order.
	 */
	protected static Direction getNextDir(Direction dir,boolean clockwise){
		Direction next = null;
		int x = dir.getX();
		int y = dir.getY();
		int nextX = x;
		int nextY = y;
		int a = 1;
		if (!clockwise)
			a = -1;
		if(y == a && x != 1)
			nextX += 1;
		else if (x == a && y != -1)
			nextY -= 1;
		else if (y == -a && x != -1)
			nextX -= 1;
		else if (x == -a && y != 1)
			nextY += 1;
		for(Direction d : Direction.values()){
			if(d.getX() == nextX && d.getY() == nextY)
				next = d;
		}
		return next;
	}
	
	/**
	 * @return return a random Direction from the four main directions
	 */
	public static Direction getRandomDirection(Random rand){
		int number = rand.nextInt(4);
		if(number==0){
			return NORTH;
		} else if(number==1){
			return EAST;
		} else if(number==2){
			return SOUTH;
		} else {
			return WEST;
		}
	}
	
	/**
	 * Generate all the directions ordered clockwise or counterclockwise.
	 * @return all the directions in the specified order.
	 */
	public static Direction[] generateOrdered(boolean clockwise){
		Direction[] dirCW = new Direction[Direction.values().length];
		Direction dir = Direction.NORTH;
		for(int i = 0; i < Direction.values().length; i++){
			dirCW[i] = dir;
			dir = getNextDir(dir,clockwise);
		}
		return dirCW;
	}
	
	/**
	 * @param pos the beginPosition
	 * @return the Position you stand on if you start on pos and move in this Direction.
	 */
	public Position moveOne(Position pos){
		return new Position(pos.getX()+xValue, pos.getY()+yValue);
	}
	
	/**
	 * Check if this Direction is Diagonal.
	 * @return true if this Direction is NORTHEAST, NORTHWEST, SOUTHWEST or SOUTHEAST.
	 */
	public boolean isDiagonal(){
		return (Math.abs(xValue)+Math.abs(yValue)>1);
	}
}
