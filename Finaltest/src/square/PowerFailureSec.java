package square;

import java.util.ArrayList;

import game.Update;
import grid.EffectGenerator;

/**
 * A secondary PowerFailure, spreads out from a primary PowerFailure.
 * Spreads out to a tertiary PowerFailure.
 * A tertiary PowerFailure has a line spreading strategy and has a time to live of two actions.
 * @author dries
 */
public class PowerFailureSec extends PowerFailure{
	
	/**
	 * Creates a secondary PowerFailure with a line spreading strategy in the given direction.
	 * @param direction: the direction the PowerFailure will spread further in.
	 */
	public PowerFailureSec(EffectGenerator eg, int direction){
		super(eg, OnSquareType.POWERFAILURESEC);
		setSpread(new SpreadLineSpread(eg,direction));
		setTTL(2);
	}

	@Override
	protected void update(Update update) {
		if(update == Update.ACTION){
			decrementTTL();
		}
		if(getTTL()>0)
			super.update(update);
	}
}
