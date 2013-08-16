package square;

import java.util.HashSet;

/**
 * A CombinationPolicy is a Policy that creates a CombinationEffect from
 * a list of effects and an OnSquare on which the effects has to be performed.
 */
public abstract class CombinationPolicy extends Policy{
	
	/**
	 * Create a CombinationEffect from the given OnSquare creators.
	 * @param effects list of effects to be combined with the others to form a CombinationEffect.
	 * @param onSquare OnSquare on which the effects will ultimately be performed.
	 * @return the CombinationEffect that resulted from the combination of OnSquare creators.
	 */
	public abstract CombinationEffect combine(HashSet<Effect> effects, OnSquare onSquare);
	
}
