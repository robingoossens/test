package square;

/**
 * A LoseActionCurse is a TurnCurse that will result in the loss of one
 * action when executed on a Player.
 */
public class LoseActionCurse extends TurnCurse{

	/**
	 * Creates a LoseActionCurse with the given time to live.
	 * @param tTL the time to live this LoseActionCurse is created with.
	 */
	public LoseActionCurse(int tTL) {
		super(tTL);
	}
	
	/**
	 * Executes this LoseActionCurse on the given OnSquare.
	 * @param p the OnSquare this LoseActionCurse is executed on.
	 */
	public void execute(OnSquare p) {
		if(p instanceof Player){
			((Player) p).setActions(((Player) p).getNumberOfActionsLeft()-1);
			deactivate();
		}
		
	}
	
}
