package square;

public class DecrementDiskRangeEffect extends Effect{

	public DecrementDiskRangeEffect() {
		super(EffectType.DECREMENTDISKRANGEEFFECT);
	}

	@Override
	public void execute(OnSquare on) {
		if(getCreator().isActive()){
			if(on instanceof IdentityDisk){
				((IdentityDisk) on).decrementRange();
			}
		}
	}
}
