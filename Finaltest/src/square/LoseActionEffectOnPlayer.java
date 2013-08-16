package square;

public class LoseActionEffectOnPlayer extends Effect {
	
	public LoseActionEffectOnPlayer() {
		super(EffectType.LOSEACTIONEFFECTONPLAYER);
	}

	@Override
	public void execute(OnSquare p) {
		if(!getCreator().isActive() && p instanceof Player){
			((Player) p).addCurse(new LoseActionCurse(1));

		}
		
	}

}
