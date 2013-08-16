package square;

/**
 * An Effect has an OnSquare as its creator and can be executed on 
 * an OnSquare to change that OnSquare.
 */
public abstract class Effect {
	private OnSquare creator;
	private EffectType type;
	
	public EffectType getType(){
		return type;
	}
	/**
	 * Create an Effect.
	 */
	public Effect(EffectType type){
		this.type = type;
	}
	
	/**
	 * Create an Effect with the given OnSquare as creator.
	 * @param creator the given creator of this Effect.
	 */
	public Effect(OnSquare creator, EffectType type){
		this.creator = creator;
		this.type = type;
	}
	
	/**
	 * Returns the OnSquare that is the creator of this Effect.
	 * @return the creator of this Effect.
	 */
	public OnSquare getCreator(){
		return creator;
	}
	
	protected void setCreator(OnSquare on){
		creator = on;
	}
	
	/**
	 * Executes this Effect on the given OnSquare.
	 * @param the OnSquare this Effect is executed on.
	 */
 	public abstract void execute(OnSquare p);
}
