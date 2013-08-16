package square;

/**
 * An action curse affects the amount of available actions from OnSquares in the game. 
 * Currently only the Player has actions to perform and can be affected by this curse.
 * Action curses have a duration time to live that they are active.
 */
public abstract class ActionCurse extends Curse{
	
	/**
	 * Create an action curse with a given duration.
	 * @param tTL the duration the curse is active.
	 */
	public ActionCurse(int tTL){
		super(tTL);
	}
}
