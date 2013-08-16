package square;

/**
 * A Curse affects the behavior of objects in the game.
 * A Curse has a duration and a state of being active or not.
 */
public abstract class Curse {
	private boolean active=true;
	private int tTL;
	
	/**
	 * Creates a new curse with the given tTL as its time to live.
	 * @param tTL the given time to live for this Curse.
	 */
	public Curse(int tTL){
		this.tTL = tTL;
	}
	
	/**
	 * Returns the time to live of this Curse.
	 * @return the time to live of this Curse.
	 */
	public int getTTL(){
		return tTL;
	}

	/**
	 * Checks whether or not this Curse is still active.
	 * @return	true if this Curse is active.
	 * 			false if this Curse is inactive.
	 */
	public boolean isActive(){
		return active;
	}
	
	/**
	 * Deactivates this curse.
	 * @post this.active == false;
	 */
	protected void deactivate(){
		active=false;
	}
	
	/**
	 * Decrements the time to live of this Curse. If the time to live is decremented all the way to 0, this Curse
	 * is deactivated.
	 */
	protected void decrementTTL(){
		tTL--;
		if(tTL<=0)
			deactivate();
	}
	
	/**
	 * Executes this Curse on the given OnSquare.
	 * @param p the OnSquare this Curse is executed on.
	 */
	public abstract void execute(OnSquare p);
}
