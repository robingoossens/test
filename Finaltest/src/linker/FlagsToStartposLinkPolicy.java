package linker;

import java.util.ArrayList;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

import square.Flag;
import square.OnSquare;
import square.OnSquareType;
import square.Player;
import square.StartingPosition;

public class FlagsToStartposLinkPolicy extends LinkingPolicy{

	@Override
	public void link(ArrayList<OnSquare> onsquares)
			throws InvalidAttributesException {
		HashSet<StartingPosition> startPoss = getStartPos(onsquares);
		ArrayList<Flag> flags = getFlags(onsquares);
		if(startPoss.size()!=flags.size())
			throw new InvalidAttributesException();
		Flag temp;
		for(StartingPosition p:startPoss){
			temp = flags.remove(0);
			temp.setStartingPosition(p);
		}
	}
	
	private ArrayList<Flag> getFlags(ArrayList<OnSquare> onsquares) {
		ArrayList<Flag> flags = new ArrayList<Flag>();
		for(OnSquare on:onsquares)
			if(on.getType() == OnSquareType.FLAG)
				flags.add((Flag) on);
		return flags;
	}

	private HashSet<StartingPosition> getStartPos(ArrayList<OnSquare> onsq){
		HashSet<StartingPosition> startingPosition = new HashSet<StartingPosition>();
		for(OnSquare on:onsq)
			if(on.getType()==OnSquareType.STARTINGPOSITION)
				startingPosition.add((StartingPosition) on);
		return startingPosition;
	}

}
