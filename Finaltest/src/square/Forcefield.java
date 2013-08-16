package square;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPositionException;
import grid.Direction;
import grid.Position;

/**
 * A ForceField is an OnSquare that is created between two ForceFieldGenerators.
 * A ForceField has an active and an inactive state, it alternates states every 2 actions.
 */
public class Forcefield extends ExtendedObject{
	
	int counter = 0;
	boolean state = false;
	private HashSet<ForcefieldGenerator> generators = new HashSet<ForcefieldGenerator>();
	private HashMap<OnSquare,ArrayList<Integer>> affectedOnSquares = new HashMap<OnSquare,ArrayList<Integer>>();
	
	/**
	 * Returns whether the given size is legal for this ForceField
	 * @param size the size to be checked
	 * @return 	true if the size is larger than 0
	 * 			false if the size is not larger than 0
	 */
	public boolean isLegalSize(int size){
		if(size>0)
			return true;
		else return false;
	}
	
	/**
	 * Triggers this ForceField with a given OnSquare
	 * If the given OnSquare is an IdentityDisk this ForceField will destroy it when active
	 */
	protected void trigger(OnSquare on){
		if(on instanceof IdentityDisk)
			activate();
	}
	
	/**
	 * Creates a ForceField between two given ForceFieldGenerators with a given size, Direction and state
	 * @param direction the Direction for this ForceField
	 * @param size the size of this ForceField
	 * @param generator1 the first ForceFieldGenerator that created this ForceField
	 * @param generator2 the second ForceFieldGenerator that created this ForceField
	 * @param state the begin state of this ForceField
	 * @throws InvalidAttributesException thrown when the size is not legal or the Direction is null
	 */
	protected Forcefield(Direction direction, int size, ForcefieldGenerator generator1, ForcefieldGenerator generator2, 
			boolean state, ArrayList<Effect> effects) throws InvalidAttributesException{
		super(OnSquareType.FORCEFIELD,effects);
		if(!isLegalSize(size) || direction==null)
			throw new InvalidAttributesException();
		else{
			setSize(size);
			setDirection(direction);
			addUpdatePriorityPolicy(new PlayerOverForcefieldUpdatePriority());
			counter = 0;
			this.state = state;
			try {
				generator1.addForcefield(this);
				generator2.addForcefield(this);
			} catch (InvalidAttributesException e) {
			}
			generators.add(generator1);
			generators.add(generator2);
		}
	}
	
	//TODO set size to ensure islegal?
	protected void setSize(int size){
		if(isLegalSize(size))
			super.setSize(size);
	}
		
	/**
	 * Affects the given OnSquare with this ForceField
	 * @param p the OnSquare affected by this ForceField
	 */
	protected void affectOnSquare(OnSquare p){
		if(affectedOnSquares.containsKey(p) && !affectedOnSquares.get(p).contains(getCycle()))
			affectedOnSquares.get(p).add(getCycle());
		else{
			affectedOnSquares.put(p, new ArrayList<Integer>());
			affectedOnSquares.get(p).add(getCycle());
		}
	}
	
	/**
	 * Returns the state counter modulo 2
	 */
	public int getState(){
		return counter%2;
	}
	
	/**
	 * Returns the cycles that the given OnSquare was affected by this ForceField
	 * @param p the OnSquare to be checked for affected cycles
	 * @return the list of cycles the given OnSquare was affected by this ForceField
	 */
	public ArrayList<Integer> getAffectedCycles(OnSquare p){
		if(affectedOnSquares.containsKey(p))
			return affectedOnSquares.get(p);
		else
			return null;
	}
	
	/**
	 * Returns whether this ForceField is compatible with the given OnSquare
	 * @return 	true if the given OnSquare is not a Wall or when active, not a Player
	 * 			false if the given OnSquare is a Wall or when active, a Player
	 */
	public boolean isCompatibleWith(OnSquare on){
		if(state)
			return ! (on instanceof Wall || on instanceof Player);
		else return ! (on instanceof Wall);
	}
	
	/**
	 * A ForceField is visible when active and not destroyed
	 */
	@Override
	public boolean isVisible() {
		return !isDestroyed() && !isActive();
	}
	
	/**
	 * A ForceField can have anything in range
	 */
	@Override
	public boolean canHaveInRange(OnSquare o, int range) {
		return true;
	}
	
	/**
	 * Returns the state of this ForceField
	 * @return 	true if this ForceField is in its active state
	 * 			false if this ForceField is in its inactive state
	 */
	public boolean isOn(){
		return state;
	}
	
	/**
	 * Returns the cycle of the counter
	 * Every 2 actions a new cycle starts
	 */
	public int getCycle(){
		return counter/2;
	}
	
	/**
	 * Updates this ForceField, increasing its counter, adjusting the state according to the counter
	 * and executing its Effects
	 */
	protected void update(){
		counter = (counter+1);
		if(counter%2==0){
			if(state)
				state=false;
			else
				state=true;
		}
		executeEffects();
		deactivate();
	}
	
	/**
	 * Executes the Effects of this ForceField
	 */
	private void executeEffects(){
		Square s = getSquare();
		ArrayList<Effect> effects = getEffects();
		for(int i=0;i<getSize();i++){
			for(OnSquare on:s.getOnSquares()){
				for(Effect f:effects){
					f.execute(on);
				}
			}
			try {
				if(i<getSize()-1)
					s = s.getNeighbour(getDirection());
			} catch (IllegalPositionException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Destroys this ForceField, putting this ForceField on destroyed and removing it
	 */
	protected void destroy(){
		try {
			super.destroy();
			getSquare().removeOnSquare(this);
			for(ForcefieldGenerator gen:generators)
				gen.removeForcefield(this);
		} catch (IllegalOnSquareException e) {
		}
	}
}
