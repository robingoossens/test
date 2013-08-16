package square;

import javax.naming.directory.InvalidAttributesException;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPutException;

public class TeleportPlayerEffect extends Effect{
	
	public TeleportPlayerEffect(){
		super(EffectType.TELEPORTPLAYEREFFECT);
	}
	
	public TeleportPlayerEffect(OnSquare o){
		super(o, EffectType.TELEPORTPLAYEREFFECT);
	}

	@Override
	public void execute(OnSquare on) {
		if(on instanceof Player && getCreator().isActive() && !((Teleporter) getCreator()).isTeleporting()){
			try {
				((Teleporter) getCreator()).teleport(on);
				((Teleporter) getCreator()).deactivate();
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
