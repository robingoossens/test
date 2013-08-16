package square;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * An EffectManager manages Effects by combining Effects into CombinationEffects
 * when needed and executing Effects from OnSquares on another OnSquare.
 */
public class EffectManager {
	
	/**
	 * Combines specific Effects in this list by going over the entire list 
	 * and checking if 2 Effects occur that produce a CombinedEffect.
	 * @param effects the effects that have to be checked for combination.
	 * @param combinationPolicies the list of combination policies.
	 * @param 	on the OnSquare that the combined Effects can be executed on.
	 * @return 	An ArrayList of Effects from a given OnSquare
	 */
	private ArrayList<Effect> combineEffects(HashSet<Effect> effects, HashSet<CombinationPolicy> 
		combinationPolicies, OnSquare on){
		ArrayList<Effect> returnlist = new ArrayList<Effect>();
		HashSet<Effect> usedEffects = new HashSet<Effect>();
		HashSet<Effect> tempEffects = new HashSet<Effect>();
		for(CombinationPolicy policy:combinationPolicies){
			for(Effect ef: effects)
				tempEffects.add(ef);
			CombinationEffect f = policy.combine(tempEffects, on);
			if(f!=null)
				returnlist.add(f);
			for(Effect effect:effects)
				if(!tempEffects.contains(effect))
					usedEffects.add(effect);
		}
		for(Effect f:effects)
			if(!usedEffects.contains(f))
				returnlist.add(f);
		return returnlist;
	}
	
	/**
	 * Executes the Effects from the given OnSquares on the given OnSquare.
	 * @param onSquares the OnSquares the Effects are taken from.
	 * @param on the OnSquare the Effects have to be executed on.
	 */
	public void executeEffects(ArrayList<OnSquare> onSquares, OnSquare on){	
		HashSet<Effect> effects = new HashSet<Effect>();
		HashSet<CombinationPolicy> combinationPolicies = new HashSet<CombinationPolicy>();
		for(OnSquare onsq:onSquares){
			if(onsq.getEffects()!=null)
				for(Effect f:onsq.getEffects())
					if(f!=null)
						effects.add(f);
			if(onsq.getCombinationPolicies()!=null)
				for(CombinationPolicy p:onsq.getCombinationPolicies())
					if(p!=null)
						combinationPolicies.add(p);
		}
		ArrayList<Effect> combinedEffects = combineEffects(effects, combinationPolicies, on);
		prioritise(combinedEffects, on);
		for(Effect f: combinedEffects) {
			f.execute(on);
		}
	}

	/**
	 * This method sets the priorities of Effects for the given OnSquare.
	 * This way an IdentityDisk will always be stopped by a Player, even if that Player is on a
	 * Teleporter. In this case, the IdentityDisk will not be teleported, because it will no longer be active.
	 */
	private void prioritise(ArrayList<Effect> effects, OnSquare on){
		for(EffectPriorityPolicy p:on.getEffectPriorityPolicies())
			p.prioritise(effects);
	}
}
