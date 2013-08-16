package square;

import java.util.ArrayList;

/**
 * This priority policy is responsible for making sure a playereffect is can be executed before a teleportationeffect.
 * This policy for instance makes sure an identitydisk is always stopped on the Square of the player, even if that
 * player is standing on a teleporter.
 */
public class TeleportPlayerEffectPriorityPolicy extends EffectPriorityPolicy{

	/**
	 * Rearranges the given arraylist of effects and puts all the playereffects before all the teleportationeffects.
	 */
	public void prioritise(ArrayList<Effect> effects) {
		for(int i=0;i<effects.size();i++)
			if(effects.get(i) instanceof TeleportEffect)
				for(int j=i+1;j<effects.size();j++)
					if(effects.get(j) instanceof PlayerEffect){
						Effect temp = effects.get(i);
						effects.set(i, effects.get(j));
						effects.set(j, temp);
					}
	}
}
