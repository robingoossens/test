package square;

/**
 * This is the Effect of an IdentityDisk.
 */
public class DiskEffect extends Effect{
	
	/**
	 * Creates a new DiskEffect with the given IdentityDisk as it's creator.
	 * @param disk the creator of this Effect.
	 */
	public DiskEffect(){
		super(EffectType.DISKEFFECT);
	}
	
	public DiskEffect(OnSquare o){
		super(o, EffectType.DISKEFFECT);
	}
	
	/**
	 * Executes this DiskEffect on the given OnSquare.
	 * @param the OnSquare this DiskEffect is executed on.
	 */
	@Override
	public void execute(OnSquare p) {
		if(p instanceof Player && ((IdentityDisk)getCreator()).isUsed()){
			((Player)p).skipTurn();
		}
	}

}
