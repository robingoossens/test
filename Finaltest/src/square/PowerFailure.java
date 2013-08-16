package square;


import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPutException;
import game.Update;
import grid.EffectGenerator;
import rules.EffectRule;
import square.PowerFailureEffect;

public abstract class PowerFailure extends OnSquare {
	
	private Spread spread;
	private int tTL;
	private boolean hasTTL;
	
	public PowerFailure(OnSquareType powerfailureprim, ArrayList<Effect> effects) throws InvalidAttributesException {
		super(powerfailureprim, effects);
		addCombinationPolicy(new PowerExplosionComboPolicy());
		addUpdatePriorityPolicy(new PlayerOverPowerfailureUpdatePriority());
	}

	/**
	 * A PowerFailure can be put on any Square. It is compatible
	 * with all the other OnSquares.
	 */
	@Override
	public boolean isCompatibleWith(OnSquare on) {
		return true;
	}
	
	public int getTTL(){
		return tTL;
	}
	/**
	 *  Applies the spreading strategy of this PowerFailure.
	 */
	public void spread(){
		ArrayList<EffectRule> g = new ArrayList<EffectRule>();
		if (getSquare() != null)
			this.spread.spread(getSquare());
	}
	
	@Override
	public boolean canHaveInRange(OnSquare o, int range) {
		if(range==0) {
			return isCompatibleWith(o);
		}
		return true;
	}
	
	@Override
	public boolean isVisible() {
		return true;
	}
	
	/**
	 * Returns the container this PowerFailure is in. This container will always be a Square.
	 * @return: the square this PowerFailure is on.
	 */
	protected Square getSquare() {
		return (Square) getOnSquareContainer();
	}
	
	/**
	 * Reduces the time to live from this PowerFailure by one. 
	 * When the time to live has reached less than 1 it will be removed from its Square.
	 */
	protected void decrementTTL() {
		tTL = tTL - 1;
		if (tTL < 1) {
			try {
				getSquare().removeOnSquare(this);
			} catch (IllegalOnSquareException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns whether the PowerFailure already has a time to live.
	 * @return true if the PowerFailure has a time to live.
	 * 		   false if the PowerFailure does not have a time to live.
	 */
	private boolean hasTTL() {
		return hasTTL;
	}
	
	protected void put(OnSquareContainer container) throws InvalidAttributesException, IllegalPutException, IllegalOnSquareException{
		super.put(container);
		executeEffects();
	}
	
	/**
	 * Initialises the PowerFailures time to live.
	 * @param tTL: the Powerfailures time to live.
	 */
	protected void setTTL(int tTL) {
		if (!hasTTL() && tTL > 0) {
			this.tTL = tTL;
			hasTTL = true;
		}
	}
	
	/**
	 * Determines the spreading strategy for this PowerFailure.
	 * @param spread: the spreading strategy for this PowerFailure.
	 */
	protected void setSpread(Spread spread){
		this.spread = spread;
	}
	
	private void executeEffects(){
		for(OnSquare o: this.getSquare().getOnSquares()){
			for(Effect e: this.getEffects()){
				e.execute(o);
			}
		}
	}
	
	protected void trigger(OnSquare on){
		if((on instanceof IdentityDisk && ((IdentityDisk) on).isUsed()) || on instanceof Player)
			activate();
	}
	@Override
	protected void update(Update update){
		if(update==Update.ACTION || update == Update.TURN){
			spread();
			executeEffects();
		}
		deactivate();
	}
}