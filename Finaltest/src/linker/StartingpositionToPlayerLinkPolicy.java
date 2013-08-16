package linker;

import java.util.ArrayList;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

import square.OnSquare;
import square.OnSquareType;
import square.Player;
import square.StartingPosition;

public class StartingpositionToPlayerLinkPolicy extends LinkingPolicy{

	@Override
	public void link(ArrayList<OnSquare> onsquares) throws InvalidAttributesException {
		HashSet<Player> players = getPlayers(onsquares);
		ArrayList<StartingPosition> spos = getSPos(onsquares);
		if(players.size()!=spos.size())
			throw new InvalidAttributesException();
		StartingPosition temp;
		for(Player p:players){
			temp = spos.remove(0);
			temp.setPlayer(p);
		}
	}

	private ArrayList<StartingPosition> getSPos(ArrayList<OnSquare> onsquares) {
		ArrayList<StartingPosition> out = new ArrayList<StartingPosition>();
		for(OnSquare on:onsquares)
			if(on.getType()==OnSquareType.STARTINGPOSITION)
				out.add((StartingPosition) on);
		return out;
		
	}

	private HashSet<Player> getPlayers(ArrayList<OnSquare> onsquares) {
		HashSet<Player> players = new HashSet<Player>();
		for(OnSquare on:onsquares)
			if(on.getType()==OnSquareType.PLAYER)
				players.add((Player) on);
		return players;
		
	}

}
