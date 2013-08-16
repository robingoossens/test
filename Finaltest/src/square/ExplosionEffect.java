package square;

/**
 * An ExplosionEffect is created for a LightGrenade and can
 * be executed on a Player reducing his number of actions left.
 */
public class ExplosionEffect extends Effect {
	
	
	public ExplosionEffect(){
		super(EffectType.EXPLOSIONEFFECT);
	}

	/**
	 * Execute an ExplosionEffect on a Player: the given Player can perform 3
	 * actions less. Set 'exploded' on true: the corresponding LightGrenade has
	 * exploded.
	 */
	public void execute(OnSquare on) {
		if (((LightGrenade) getCreator()).isActive() && !getCreator().isDestroyed()) {
			if (on instanceof Player) {
				Player p = (Player) on;
				p.setActions(p.getNumberOfActionsLeft() - 3);
				((LightGrenade) getCreator()).destroy();
			}
		}
	}
}
