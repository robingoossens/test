package square;

import javax.naming.directory.InvalidAttributesException;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPutException;

public class TeleportIdentityDiskEffect extends Effect{
	
	public TeleportIdentityDiskEffect(){
		super(EffectType.TELEPORTIDENTITYDISKEFFECT);
	}
	
	public TeleportIdentityDiskEffect(OnSquare o){
		super(o, EffectType.TELEPORTIDENTITYDISKEFFECT);
	}
	
	@Override
	public void execute(OnSquare on) {
		if(on instanceof IdentityDisk && ((IdentityDisk) on).isUsed() && getCreator().isActive()
				&& !((Teleporter) getCreator()).isTeleporting()){
			try {
				((Teleporter) getCreator()).teleport(on);
				((Teleporter) getCreator()).deactivate();
				((IdentityDisk) on).updateConstraints(this);
			} catch (InvalidAttributesException e) {
				e.printStackTrace();
			} catch (IllegalOnSquareException e) {
				e.printStackTrace();
			} catch (IllegalPutException e) {
				e.printStackTrace();
			}
		}
		
	}
}
