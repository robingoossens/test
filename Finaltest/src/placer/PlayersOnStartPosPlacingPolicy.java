package placer;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

import square.Flag;
import square.OnSquare;
import square.OnSquareType;
import square.Player;
import square.Square;
import square.StartingPosition;
import exceptions.IllegalOnSquareException;
import exceptions.IllegalPositionException;
import exceptions.IllegalPutException;
import game.PositionRule;
import grid.Grid;
import grid.Position;

public class PlayersOnStartPosPlacingPolicy extends PlacingPolicy{
	private Position rule;
	private int index;
	
	public PlayersOnStartPosPlacingPolicy(Position rule, int index){
		this.rule = rule;
		this.index = index;
	}

	@Override
	public void place(Grid grid, ArrayList<OnSquare> onSquares) {
		if(grid.pointOnGrid(rule)){
			try {
				Square toPuton = grid.getSquare(rule);
				Player p = findPlayer(index,onSquares);
				StartingPosition sp = findSPos(index,onSquares);
				toPuton.putOnSquare(p);
				toPuton.putOnSquare(sp);
				onSquares.remove(p);
				onSquares.remove(sp);
			} catch (IllegalPositionException | InvalidAttributesException | IllegalOnSquareException | IllegalPutException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	private StartingPosition findSPos(int index2, ArrayList<OnSquare> onSquares) {
		for(OnSquare on:onSquares)
			if(on.getType()==OnSquareType.STARTINGPOSITION && ((StartingPosition)on).getPlayer().getIndex()==index2)
				return (StartingPosition)on;
		return null;
	}
	private Player findPlayer(int index2, ArrayList<OnSquare> onSquares) {
		for(OnSquare on:onSquares)
			if(on.getType()==OnSquareType.PLAYER && ((Player)on).getIndex()==index2)
				return (Player) on;
		return null;
	}
	
}
