package square;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

import game.Update;
import grid.EffectGenerator;

/**
 * A primary PowerFailure.
 * Spreads out to secondary PowerFailure.
 * A primary PowerFailure has a rotating spreading strategy and has a time to live of three turns.
 * @author dries
 */
public class PowerFailurePrim extends PowerFailure{
	
	/**
	 * Creates a primary PowerFailure.
	 * @param effects 
	 * @throws InvalidAttributesException 
	 */
	public PowerFailurePrim(ArrayList<Effect> effects) throws InvalidAttributesException{
		super( OnSquareType.POWERFAILUREPRIM,effects);
//		setSpread(new SpreadRandomRotate(eg));
		setTTL(3);
	}

	@Override
	protected void update(Update update) {
		if (update == Update.TURN){
			decrementTTL();
		} 
		if(getTTL()>0)
			super.update(update);
	}
}
