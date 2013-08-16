package placer;

import exceptions.IllegalPositionException;
import grid.Grid;
import grid.Position;

import java.util.ArrayList;
import java.util.Random;

import square.OnSquare;
import square.Square;
import square.StartingPosition;

public abstract class PlacingPolicy {

	private Random random = new Random();
	
	public abstract void place(Grid grid, ArrayList<OnSquare> onSquares);

	/**
	 * Return an ArrayList containing all the Squares where a given OnSquare can be put on.
	 * The StartingPositions of the Players are excluded.
	 */
	protected ArrayList<Square> getAvailableSquares(Grid grid, Position leftUnder, Position rightUp, OnSquare onSquare){
		if(grid==null || onSquare==null)
			throw new IllegalArgumentException();
		ArrayList<Square> out = new ArrayList<Square>();
		Square sq;
		for(int x = leftUnder.getX(); x<=rightUp.getX(); x++){
			for(int y = leftUnder.getY(); y<=rightUp.getY(); y++){
				try {
					sq = grid.getSquare(new Position(x,y));
					if(onSquare.isCompatibleWith(sq)){
						if(!containsStartingPositionPlayer(sq)){
							out.add(sq);
						}
					}
				} catch (IllegalPositionException e) {
					//can be that a given Position does not lay on the Grid. Just ignore that Position.
				}
			}
		}
		return out;
	}
	/**
	 * Get a random Square from a given ArrayList of Squares.
	 */
	protected Square getRandomSquare(ArrayList<Square> squares){
		return squares.get(getRandom().nextInt(squares.size()));
	}
	
	/**
	 * Check if the given Square contains a StartingPosition of a Player.
	 */
	protected boolean containsStartingPositionPlayer(Square square){
		for(OnSquare o: square.showOnSquares()){
			if(o instanceof StartingPosition){
				return true;
			}
		}
		return false;
	}
	
	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}
	
	
}
