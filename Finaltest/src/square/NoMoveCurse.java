package square;

/**
 * A NoMoveCurse is a Curse that makes a Player unable to move.
 */
public class NoMoveCurse extends ActionCurse{

	/**
	 * Create a NoMoveCurse with the given time to live.
	 * @param tTL the time to live this NoMoveCurse is created with.
	 */
	public NoMoveCurse(int tTL) {
		super(tTL);
	}
	
	/**
	 * Executes this NoMoveCurse on the given OnSquare (Player).
	 * Making the Player unable to move.
	 * @param p the given OnSquare (Player).
	 */
	public void execute(OnSquare p) {
		((Player)p).setPerformable(Action.MOVE,false);
	}
}
