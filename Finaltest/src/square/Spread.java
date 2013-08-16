package square;

import grid.EffectGenerator;

import java.util.ArrayList;

/**
 * Interface for spreading strategies.
 * (currently only PowerFailures)
 * @author dries
 */
public abstract class Spread {
	private EffectGenerator generator;
	
	public Spread(EffectGenerator gen){
		generator = gen;
	}
	
	public EffectGenerator getEffectGenerator(){
		return generator;
	}
	abstract void spread(Square square);
	
}
