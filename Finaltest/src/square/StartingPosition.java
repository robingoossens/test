package square;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

/**
 * A StartingPosition is an Onsquare that belongs to a Player.
 * The Player will start on the Position this StartingPosition lays on.
 * Flags will be placed on these StartingPositions and 
 * the Effect of the StartingPosition will update the Goals of the Players.
 */
public class StartingPosition extends OnSquare {
	
	Player player;
	
	/**
	 * Create a new starting position for the given player.
	 * @param effects 
	 * @param player the player that will start on this startingposition.
	 * @throws InvalidAttributesException 
	 */
	public StartingPosition(ArrayList<Effect> effects) throws InvalidAttributesException{
		super(OnSquareType.STARTINGPOSITION,effects);
	}

	public void setPlayer(Player p){
		player = p;
	}
	@Override
	public boolean canHaveInRange(OnSquare o, int range) {
		if(range==0){
			return isCompatibleWith(o);
		}
		return true;
	}
	
	/**
	 * Return the Player that starts on this StartingPosition.
	 * @return the Player that starts on this StartingPosition.
	 */
	public Player getPlayer(){
		return player;
	}
	
	@Override
	public boolean isCompatibleWith(OnSquare on) {
		return !(on instanceof Wall);
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	protected void trigger(OnSquare on){
		if(on instanceof Player)
			activate();
	}
}
