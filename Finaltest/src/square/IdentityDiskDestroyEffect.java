package square;

public class IdentityDiskDestroyEffect extends Effect{

	public IdentityDiskDestroyEffect(){
		super(EffectType.IDENTITYDISKDESTROYEFFECT);
	}
	
	@Override
	public void execute(OnSquare p) {
		if(((Forcefield)getCreator()).isOn()){
			if(getCreator().isActive()){
				if(p instanceof Disk && ((Disk) p).isUsed()){
					((IdentityDisk) p).stop(null);
					p.destroy();
					((Forcefield)getCreator()).affectOnSquare(p);
				}
			}
		}
	}
}
