package placer;

import exceptions.IllegalPositionException;
import game.PositionRule;
import grid.Grid;
import grid.OnSquareGenerator;
import grid.Position;

import java.util.ArrayList;
import java.util.HashSet;

import square.EffectType;
import square.OnSquare;
import square.OnSquareType;
import square.Square;

public class OnSquareNearPlayerPolicy extends PlaceInSquarePolicy{
	private int numberOfOnsq;
	private OnSquareType type;
	private int length;
	
	public OnSquareNearPlayerPolicy(Position rule, int length, int numberOfOnsq, OnSquareType type){
		if(rule==null||type==null||length<0||numberOfOnsq<=0)
			throw new IllegalArgumentException();
		initialise(rule);
		this.numberOfOnsq = numberOfOnsq;
		this.type = type;
	}
	
	private void initialise(Position center){
		int leftUnderX = (int) (center.getX()-Math.floor(length/2));
		int leftUnderY = (int) (center.getY()-Math.floor(length/2));
		int rightUpX = (int) (center.getX()+Math.floor(length/2));
		int rightUpY = (int) (center.getY()+Math.floor(length/2));
		setLeftUnder(new Position(leftUnderX, leftUnderY));
		setRightUp(new Position(rightUpX, rightUpY));
	}
	public void place(Grid grid, ArrayList<OnSquare> input){
		ArrayList<OnSquare> newInput = new ArrayList<OnSquare>();
		int recent = 0;
		ArrayList<OnSquare> removals = new ArrayList<OnSquare>();
		for(int i=0;i<numberOfOnsq;i++){
			for(int j=recent;j<input.size()&&numberOfOnsq>0;j++){
				if(input.get(j).getType()==type){
					newInput.add(input.get(j));
					removals.add(input.get(j));
					recent = j+1;
					numberOfOnsq--;
				}
			}
		}
		super.place(grid, newInput);
		for(OnSquare o:removals)
			if(!newInput.contains(o))
				input.remove(o);
	}
	
	/**
	 * Return an ArrayList containing all the Squares where a given OnSquare can be put on.
	 * The StartingPositions of the Players are excluded.
	 */
	protected ArrayList<Square> getAvailableSquares(Grid grid, Position leftUnder, Position rightUp, OnSquare onSquare){
		if(grid==null || onSquare==null)
			throw new IllegalArgumentException();
		ArrayList<Square> out = new ArrayList<Square>();
		Square sq;
		OnSquare temp = OnSquareGenerator.getInstance().makeOnSquare(onSquare.getType(), new HashSet<EffectType>(), null);
		for(int x = leftUnder.getX(); x<=rightUp.getX(); x++){
			for(int y = leftUnder.getY(); y<=rightUp.getY(); y++){
				try {
					sq = grid.getSquare(new Position(x,y));
					if(temp.isCompatibleWith(sq)){
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
	
}
