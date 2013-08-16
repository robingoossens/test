package square;

public class LoseTurnEffectOnPlayer extends Effect{
	
	public LoseTurnEffectOnPlayer() {
		super(EffectType.LOSETURNEFFECTONPLAYER);
	}

	@Override
	public void execute(OnSquare p) {
		if(getCreator().isActive() && p instanceof Player){
			if(((Player) p).getNumberOfActionsLeft()>0)
				((Player) p).setActions(0);
			getCreator().deactivate();
		}
		
	}

}
