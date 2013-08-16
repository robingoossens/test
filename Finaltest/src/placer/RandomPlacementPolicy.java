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

public class RandomPlacementPolicy extends PlacingPolicy{
	private OnSquareType type;
	public RandomPlacementPolicy(OnSquareType type){
		this.type = type;
	}
	
	public OnSquareType getType(){
		return type;
	}
	@Override
	public void place(Grid grid, ArrayList<OnSquare> onSquares) {
		if(onSquares!=null){		
			ArrayList<OnSquare> usables = getUsables(onSquares);
			ArrayList<Square> availableSquares = getAvailableSquares(grid, new Position(0,0), new Position(grid.getDimension().getX()-1, grid.getDimension().getY()-1), usables.get(0));
			Square currentSquare;
			OnSquare temp;
			int index = 0;
			while(index<usables.size() && !availableSquares.isEmpty()){
				currentSquare = getRandomSquare(availableSquares);
				temp = usables.get(index);
				try {
					currentSquare.putOnSquare(temp);
					availableSquares.remove(currentSquare);
					onSquares.remove(temp);
					index++;
				} catch (IllegalOnSquareException e) {
				} catch (InvalidAttributesException e) {
				} catch (IllegalPutException e) {
				} 
			}
		}
	}
	
	private ArrayList<OnSquare> getUsables(ArrayList<OnSquare> onSquares) {
		ArrayList<OnSquare> out = new ArrayList<OnSquare>();
		for(OnSquare on:onSquares)
			if(on.getType()==type)
				out.add(on);
		return out;
	}
}
