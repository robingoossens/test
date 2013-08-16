package square;

import java.util.ArrayList;

import game.Update;
import grid.EffectGenerator;

/**
 * A tertiary PowerFailure, spreads out from a secondary PowerFailure.
 * Does not spread out to anything.
 * A tertiary PowerFailure has a time to live of one action.
 * @author dries
 */
public class PowerFailureTert extends PowerFailure{
	
	/**
	 * Creates a tertiary PowerFailure.
	 */
	public PowerFailureTert(EffectGenerator eg){
		super(eg, OnSquareType.POWERFAILURETERT);
		setSpread(new SpreadNoSpread(eg));
		setTTL(1);
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
