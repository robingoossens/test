package square;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

public class TurnManager {
	ArrayList<Player> players = new ArrayList<Player>();
	
	public TurnManager(ArrayList<Player> players) throws InvalidAttributesException{
		if(players == null || !areValidPlayers(players))
			throw new InvalidAttributesException();
		for(Player p:players)
			this.players.add(p);
		Player p = this.players.remove(players.size()-1);
		this.players.add(0, p);
	}
	
	public ArrayList<Player> test(){
		return players;
	}
	public Player getPlayerOnTurn(){
		return players.get(0);
	}
	
	public void removeDestroyedPlayers(){
		for(int i=1;i<=players.size()-1;i++){
			if(players.get(i).isDestroyed())
				players.remove(i);
		}
	}
	
	public int getNumberOfPlayers(){
		return players.size();
	}
	public void grantNewTurn(int actions, boolean reAdd) throws InvalidAttributesException{
		Player prev;
		do{
			removeDestroyedPlayers();
			prev = players.remove(0);
			if(!reAdd && !prev.isDestroyed())
				prev.destroy();
			if(!prev.isDestroyed())
				players.add(prev);
			if(players.size()>0)
				players.get(0).grantTurn(actions);
		}while(players.get(0).getNumberOfActionsLeft()<=0 && players.size()!=0);

	}
	
	private boolean areValidPlayers(ArrayList<Player> players){
		for(Player p:players)
			if(p==null || p.isDestroyed())
				return false;
		return true;
	}
}
