package square;

public class PlayerEffect extends Effect{
	public PlayerEffect(){
		super(EffectType.PLAYEREFFECT);
	}
	
	@Override
	public void execute(OnSquare p) {
		//TODO: opschonen
		if(p instanceof IdentityDisk && ((IdentityDisk) p).isUsed() && ((Player)getCreator()).isActive()){
			((IdentityDisk)p).stop((Player)getCreator());
		}
	}
}
