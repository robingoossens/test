package square;

import java.util.HashSet;

public class PowerExplosionComboPolicy extends CombinationPolicy {

	public CombinationEffect combine(HashSet<Effect> effects, OnSquare onSquare) {
		if(onSquare instanceof Player){
			LoseTurnEffectOnPlayer pf = null;
			ExplosionEffect lg = null;
			for(Effect effect:effects)
				if(effect instanceof LoseTurnEffectOnPlayer){
					pf = (LoseTurnEffectOnPlayer)effect;
				}
				else if(effect instanceof ExplosionEffect){
					lg = (ExplosionEffect)effect;
				}
			if(pf!=null && lg!=null){
				HashSet<OnSquare> creators = new HashSet<OnSquare>();
				creators.add(pf.getCreator());
				creators.add(lg.getCreator());
				effects.remove(lg);
				effects.remove(pf);
				return new PowerExplosionEffect(creators);
			}
			else return null;
		}
		else return null;
	}
}
