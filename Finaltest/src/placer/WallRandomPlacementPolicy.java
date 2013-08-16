package placer;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPutException;
import grid.Grid;
import grid.Position;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

import square.OnSquare;
import square.OnSquareType;
import square.Square;
import square.Wall;

public class WallRandomPlacementPolicy extends RandomPlacementPolicy{
	private int numberOfAvailableSquares;
	public WallRandomPlacementPolicy(int numberOfSquares){
		super(OnSquareType.WALL);
		numberOfAvailableSquares = numberOfSquares;
	}
	
	@Override
	public void place(Grid grid, ArrayList<OnSquare> onSquares) {
		if(onSquares!=null){		
			ArrayList<Wall> usables = getUsables(onSquares);
			ArrayList<Square> availableSquares = getAvailableSquares(grid, new Position(0,0), new Position(grid.getDimension().getX()-1, grid.getDimension().getY()-1), usables.get(0));
			Square currentSquare;
			Wall temp;
			int index = 0;
			while(index<numberOfAvailableSquares && !availableSquares.isEmpty()){
				currentSquare = getRandomSquare(availableSquares);
				temp = usables.get(index);
				try {
					currentSquare.putOnSquare(temp);
					availableSquares.remove(currentSquare);
					numberOfAvailableSquares -= temp.getSize();
					index++;
				} catch (IllegalOnSquareException e) {
				} catch (InvalidAttributesException e) {
				} catch (IllegalPutException e) {
				} 
			}
		}
	}
	
	
	private ArrayList<Wall> getUsables(ArrayList<OnSquare> onSquares) {
		ArrayList<Wall> out = new ArrayList<Wall>();
		for(OnSquare on:onSquares)
			if(on.getType()==getType())
				out.add((Wall) on);
		return out;
	}
	
}
