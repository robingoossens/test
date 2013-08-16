package square;

import java.util.HashSet;

public class PowerExplosionEffect extends CombinationEffect{
	
		public PowerExplosionEffect(HashSet<OnSquare> creators){
			//TODO: invalidattributes exceptions toevoegen bij foutieve parameters
			super(creators);
		}
		
		/**
		 * Execute this Effect on a given Player.
		 * 
		 */
		public void execute(OnSquare on){
			LightGrenade creator_lg = null;
			PowerFailure creator_pf = null;
			for(OnSquare onsq:getCreators())
				if(onsq instanceof LightGrenade)
					creator_lg = (LightGrenade)onsq;
				else
					creator_pf = (PowerFailure)onsq;
			if(on instanceof Player){
				if(creator_lg.isActive() && !creator_lg.isDestroyed()){
					((Player)on).setActions(((Player)on).getNumberOfActionsLeft()-4);
					creator_lg.destroy();
					creator_pf.deactivate();
				}
				else
					creator_pf.deactivate();
			}
		}
}
