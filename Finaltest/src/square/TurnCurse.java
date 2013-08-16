package square;

/**
 * A Curse that can only be executed on OnSquares that are granted a turn by the Game.
 */
public abstract class TurnCurse extends Curse {
	
	/**
	 * Create a TurnCurse with the given time to live.
	 * @param tTL the time to live this TurnCurse is created with.
	 */
	public TurnCurse(int tTL) {
		super(tTL);
	}
}
