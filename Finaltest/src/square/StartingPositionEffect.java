package square;

/**
 * A StartingPositionEffect is an Effect that belongs to a StartingPostion and
 * updates the Goals of Players.
 */
public class StartingPositionEffect extends Effect{
	
	/**
	 * Create a StartingPositionEffect for the given StartingPosition.
	 */
	public StartingPositionEffect(){
		super(EffectType.STARTINGPOSITIONEFFECT);
	}
	
	/**
	 * Executes this StartingPositionEffect on the given OnSquare.
	 * This will update the Goals of the given OnSquare (Player).
	 * @param p the OnSquare this StartingPositionEffect is executed on.
	 */
	@Override
	public void execute(OnSquare p) {
		if(p instanceof Player && getCreator().isActive()){
			((Player)p).updateGoals((StartingPosition)getCreator());
		}
	}

}
