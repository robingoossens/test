package placer;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPositionException;
import exceptions.IllegalPutException;
import game.PositionRule;
import grid.Grid;
import grid.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.naming.directory.InvalidAttributesException;

import square.OnSquare;
import square.Square;
import square.StartingPosition;

public class PlaceInSquarePolicy extends PlacingPolicy{
	private Position leftUnder;
	public Position getLeftUnder() {
		return leftUnder;
	}

	protected void setLeftUnder(Position leftUnder) {
		this.leftUnder = leftUnder;
	}

	public Position getRightUp() {
		return rightUp;
	}

	protected void setRightUp(Position rightUp) {
		this.rightUp = rightUp;
	}

	private Position rightUp;
	
	
	public PlaceInSquarePolicy(){}
	
	public PlaceInSquarePolicy(Position leftUnder, Position rightUp){
		this.leftUnder = leftUnder;
		this.rightUp = rightUp;
	}

	
	@Override
	public void place(Grid grid, ArrayList<OnSquare> input) {
		if(grid==null || input==null)
			throw new IllegalArgumentException();
		if(leftUnder==null || rightUp==null)
			initialise(grid);
		if(!leftUnder.isLeftUnderOf(rightUp)){
			Position temp = leftUnder;
			leftUnder = rightUp;
			rightUp = temp;
		} 
		if(input.size()>0){
		int nbThings = input.size();
			Square squareToPutOn;
			ArrayList<Square> availableSquares = getAvailableSquares(grid, leftUnder, rightUp, input.get(0));
			while(nbThings!=0 && !availableSquares.isEmpty()){
				squareToPutOn = getRandomSquare(availableSquares); 
				try {
					squareToPutOn.putOnSquare((input.get(nbThings-1)));
					input.remove(nbThings-1);
					nbThings--;
				} catch (IllegalOnSquareException e) {
				} catch (InvalidAttributesException e) {
				} catch (IllegalPutException e) {
				}
				availableSquares.remove(squareToPutOn);
			}
		}
		
	}

	private void initialise(Grid grid) {
		leftUnder = new Position(0,0);
		rightUp = new Position(grid.getDimension().getX()-1,grid.getDimension().getY()-1);
	}

}
