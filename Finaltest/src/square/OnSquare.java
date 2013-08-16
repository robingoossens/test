package square;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPutException;
import exceptions.IllegalRemoveException;
import game.Update;
import grid.EffectGenerator;

import java.util.ArrayList;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

/**
 * An OnSquare is an object in the Game.
 * An OnSquare has different states that alters its use.
 * OnSquares can have an OnSquaerContainer that they are placed in.
 * An OnSquare has certain Effects that it can cause.
 * OnSquares have policies to manage these Effects.
 */
public abstract class OnSquare implements Cloneable{
	private boolean active = false;
	private boolean destroyed = false;
	private boolean uppickable = false;
	private boolean puttable = true;
	private OnSquareContainer container;
	private OnSquareType type;
	private ArrayList<CombinationPolicy> combinationPolicies = new ArrayList<CombinationPolicy>();
	private ArrayList<EffectPriorityPolicy> effectPriorityPolicies = new ArrayList<EffectPriorityPolicy>();
	private ArrayList<UpdatePriorityPolicy> updatePriorityPolicies = new ArrayList<UpdatePriorityPolicy>();
	private ArrayList<Effect> effects = new ArrayList<Effect>();
	
	/**
	 * Create an OnSquare.
	 * @param type 
	 * @param effects2 
	 */
	public OnSquare(OnSquareType type, ArrayList<Effect> effects)throws InvalidAttributesException{
		if(type==null||effects==null)
			throw new InvalidAttributesException();
		this.type = type;
		for(Effect f:effects){
			this.effects.add(f);
			f.setCreator(this);
		}
	}
	
	public OnSquareType getType(){
		return type;
	}
	/**
	 * Return a clone of this OnSquare.
	 */
	public OnSquare clone(){
		try {
			return (OnSquare)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Return the list of CombinationPolicies of this OnSquare.
	 * @return the list of CombinationPolcies of this OnSquare.
	 */
	protected HashSet<CombinationPolicy> getCombinationPolicies(){
		HashSet<CombinationPolicy> tempCombinationPolicies = new HashSet<CombinationPolicy>();
		for(CombinationPolicy policy:combinationPolicies)
			tempCombinationPolicies.add(policy);
		return tempCombinationPolicies;
	}
	
	/**
	 * Return the list of EffectPriorityPolicies of this OnSquare.
	 * @return the list of EffectPriorityPolcies of this OnSquare.
	 */
	protected ArrayList<EffectPriorityPolicy> getEffectPriorityPolicies(){
		ArrayList<EffectPriorityPolicy> tempPriorityPolicies = new ArrayList<EffectPriorityPolicy>();
		for(EffectPriorityPolicy policy:effectPriorityPolicies)
			tempPriorityPolicies.add(policy);
		return tempPriorityPolicies;
	}
	
	/**
	 * Add a CombinationPolicy to this OnSquare.
	 * @param policy the CombinationPolicy to be added to this OnSquare.
	 */
	protected void addCombinationPolicy(CombinationPolicy policy){
		combinationPolicies.add(policy);
	}
	
	/**
	 * Add an EffectPriorityPolicy to this OnSquare.
	 * @param policy the EffectPriorityPolicy to be added to this OnSquare.
	 */
	protected void addEffectPriorityPolicy(EffectPriorityPolicy policy){
		effectPriorityPolicies.add(policy);
	}	
	
	/**
	 * Return whether this OnSquare is active.
	 * @return	true when this OnSquare is active.
	 * 			false when this OnSquare is not active.
	 */
	public boolean isActive(){
		return active;
	}
	
	/**
	 * Return whether this OnSquare is able to be picked up.
	 * @return	true if this OnSquare is able to be picked up.
	 * 			false if this OnSquare is not able to be picked up.
	 */
	public boolean isUpPickable(){
		return uppickable;
	}
	
	/**
	 * Deactivated this OnSquare.
	 * @post this.active == false.
	 */
	protected void deactivate(){
		active=false;
	}
	
	/**
	 * Return whether this OnSquare is able to be added to the given OnSquareContainer.
	 * @param container the OnSquareContainer to be checked.
	 * @return	true if this OnSquare can be added to the given OnSquareContainer.
	 * 			false if this OnSquare is not puttable in or can not be added to the given OnsquareContainer.
	 */
	public boolean isCompatibleWith(OnSquareContainer container){
		if(!isPuttableOn(container)){
			return false;
		}
		else{
			return container.canAdd(this);
		}
	}
	
	/**
	 * Returns whether this OnSquare is destroyed.
	 * @return	true when this OnSquare is destroyed.
	 * 			false when this OnSquare is not destroyed.
	 */
	public boolean isDestroyed(){
		return destroyed;
	}
	
	/**
	 * Destroy this OnSquare.
	 * @post this.destroyed == true.
	 */
	protected void destroy(){
		destroyed=true;
	}
	
	/**
	 * Return whether this OnSquare is visible.
	 */
	public abstract boolean isVisible();
	
	/**
	 * Return whether this OnSquare is puttable.
	 * @return	true if this OnSquare is puttable.
	 * 			false if this OnSquare is not puttable.
	 */
	public boolean isPuttable(){
		return puttable;
	}
	
	/**
	 * Return whether this OnSquare can be put in the given OnSquareContainer.
	 * @param container the OnSquareContainer to be checked.
	 * @return	true if the given OnSquareContainer is a Square.
	 * 			false if the given OnSquareContainer is not a Square.
	 */
	public boolean isPuttableOn(OnSquareContainer container){
		if(container instanceof Square)
			return true;
		else
			return false;
	}
	
	/**
	 * Puts this OnSquare in the given OnSquareContainer.
	 * @param container the OnSquareContainer this OnSquare is put in.
	 * @throws IllegalPutException thrown when the OnSquareContainer of this OnSquare is null.
	 * 			| getOnSquareContainer()==null || 
	 * 			| !(container==null || !isPuttableOn(container)) && !container.contains(this)
	 * @throws InvalidAttributesException thrown when the given OnSquareContainer is null or 
	 * 			this OnSquare cannot be put on the given OnSquareContainer.
	 * 			| container==null || !isPuttableOn(container)
	 * @throws IllegalOnSquareException thrown when an illegal action involving OnSquares is done.
	 * @invar some OnSquares can only be in one OnSquareContainer at the same time.
	 */
	protected void put(OnSquareContainer container) throws IllegalPutException, InvalidAttributesException, IllegalOnSquareException{
		if(container==null || !isPuttableOn(container))
			throw new InvalidAttributesException();
		else if(container.contains(this)){
				if(getOnSquareContainer()==null){
					this.container = container;
				}
				else
					throw new IllegalPutException();
			puttable = false;
		}
		else throw new IllegalPutException();
	}	
	
	/**
	 * Sets the puttable state of this OnSquare to the given puttable state.
	 * @param puttable the puttable state to be set for this OnSquare.
	 */
	protected void setPuttable(boolean puttable){
		this.puttable = puttable;
	}
	
	/**
	 * Sets the OnSquareContainer of this OnSquare to the given OnSquareContainer.
	 * @param container the given OnSquareContainer to be set for this OnSquare.
	 */
	protected void setOnSquareContainer(OnSquareContainer container){
		this.container = container;
	}
	
	/**
	 * Removes this OnSquare from the given OnSquareContainer.
	 * @param container the OnSquareContainer this OnSquare is removed from.
	 * @throws InvalidAttributesException thrown when the given OnSquareContainer is null or already contains this OnSquare.
	 * @throws IllegalRemoveException thrown when the OnSquareContainer of this OnSquare does not equal the given OnSquareContainer.
	 */
	protected void remove(OnSquareContainer container) throws InvalidAttributesException, IllegalRemoveException{
			if(container==null || container.contains(this))
				throw new InvalidAttributesException();
			else if(getOnSquareContainer().equals(container)){
				this.container = null;
				puttable = true;
			}
			else
				throw new IllegalRemoveException();
	}
	
	/**
	 * Updates this OnSquare with the given Update.
	 * @param update the given Update that needs to update this OnSquare.
	 */
	protected void update(Update update){
		deactivate();
	}
	
	/**
	 * Activates this OnSquare.
	 * @post this.active == true.
	 */
	protected void activate(){
		active=true;
	}
	
	/**
	 * Checks if this OnSquare can have another OnSquare in a given range.
	 */
	public abstract boolean canHaveInRange(OnSquare o, int range);
	
	/**
	 * Checks if this OnSquare can be picked up
	 * @return	true if it is a Item that can be picked up
	 * 			false if it is not an Item
	 */
	public boolean canPickUp(){
		return uppickable;
	}
	
	/**
	 * Sets the uppickable state of this OnSquare to the given uppickable state.
	 * @param u the given uppickable state to be set for this OnSquare.
	 */
	protected void setUppickable(boolean u){
		uppickable=u;
	}

	/**
	 * Returns the OnSquareContainer of this OnSquare.
	 * @return the OnSquareContainer of this OnSquare.
	 */
	protected OnSquareContainer getOnSquareContainer(){
		return container;
	}
	
	/**
	 * Returns the list of Effects of this OnSquare.
	 * @return the list of Effects of this OnSquare.
	 */
	public ArrayList<Effect> getEffects(){
		ArrayList<Effect> out = new ArrayList<Effect>();
		for(Effect f:effects)
			out.add(f);
		return out;
	}
	
	/**
	 * Revives this OnSquare putting the destroyed state of this OnSquare to false.
	 * @post this.destroyed == false.
	 */
	protected void revive(){
		destroyed=false;
	}
	
	/**
	 * Add the given Effect to the Effects of this OnSquare.
	 * @param f the Effect to be added to this OnSquare.
	 */
	protected void addEffect(Effect f){
		effects.add(f);
	}
	
	/**
	 * Trigger this OnSquare with a given OnSquare. If necessary, bring this OnSquare to the next state.
	 * @param on the given OnSquare that triggers this OnSquare.
	 */
	protected void trigger(OnSquare on){}
	
	/**
	 * Sets the Effects of this OnSquare to the given Effects.
	 * @param eff the Effects to be set for this OnSquare.
	 */
	public void setEffects(ArrayList<Effect> eff) throws IllegalArgumentException{
		if(eff==null)
			throw new IllegalArgumentException();
		effects = eff;
	}
	
	/**
	 * Returns whether this OnSquare is compatible with the given OnSquare.
	 * @param on the given OnSquare that is checked for compatibility with this OnSquare.
	 * @return	true if this OnSquare and the given OnSquare are compatible.
	 * 			false if this OnSquare and the given OnSquare are not compatible.
	 */
	public abstract boolean isCompatibleWith(OnSquare on);
	
	/**
	 * Returns the UpdatePriorityPolicies of this OnSquare.
	 * @return the UpdatePriorityPolicies of this OnSquare.
	 */
	public ArrayList<UpdatePriorityPolicy> getUpdatePriorityPolicies() {
		return updatePriorityPolicies;
	}
	
	/**
	 * Add an UpdatePriorityPolicy to the UpdatePriorityPolicies of this OnSquare.
	 * @param updatePriorityPolicy the UpdatePriorityPolicy to be added to this OnSquare.
	 */
	protected void addUpdatePriorityPolicy(UpdatePriorityPolicy updatePriorityPolicy) {
		this.updatePriorityPolicies.add(updatePriorityPolicy);
	}
}
