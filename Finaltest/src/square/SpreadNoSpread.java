package square;

import grid.EffectGenerator;

import java.util.ArrayList;

/**
 * Spreading strategy for objects that do not spread.
 */
public class SpreadNoSpread extends Spread{
	
	public SpreadNoSpread(EffectGenerator gen){
		super(gen);
	}
	public void spread(Square square) {
		// do nothing.
	}

}
