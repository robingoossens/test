package square;

public class CursePlayerEffect extends Effect{

	/**
	 * Creates a forcefieldeffect to curse a player.
	 */
	public CursePlayerEffect(){
		super(EffectType.CURSEPLAYEREFFECT);
	}
	
	@Override
	public void execute(OnSquare p) {
		if(((Forcefield)getCreator()).isOn()){
			if(p instanceof Player){
				if(((Forcefield)getCreator()).getAffectedCycles((Player)p) == null)
					((Player) p).addCurse(new NoMoveCurse(1));
				else if(((Forcefield)getCreator()).getAffectedCycles((Player)p).size()==1 && 
						((Forcefield)getCreator()).getAffectedCycles((Player)p).get(0) != 
							((Forcefield)getCreator()).getCycle()){
					((Player) p).destroy();
				}
				else
					((Player) p).addCurse(new NoMoveCurse(1));
				((Forcefield)getCreator()).affectOnSquare(p);
			}		
		}		
	}
}
