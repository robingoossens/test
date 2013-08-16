package square;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import exceptions.IllegalDirectionException;
import exceptions.IllegalLinkException;
import exceptions.IllegalOnSquareException;
import exceptions.IllegalPositionException;
import exceptions.IllegalPutException;
import exceptions.IllegalRemoveException;
import exceptions.IllegalSizeException;
import exceptions.IllegalUseException;
import exceptions.InventoryFullException;
import exceptions.NotCompatibleWithInventoryException;
import game.Update;
import grid.Direction;
import grid.EffectGenerator;


import javax.naming.directory.InvalidAttributesException;

/**
 * A ForceFieldGenerator is an Item that can be picked up and placed.
 * A ForceField creates a ForceField with another ForceFieldGenerator when the correct conditions have been met.
 */
public class ForcefieldGenerator extends Item{
	
	private HashMap<Forcefield,Boolean> forcefieldList = new HashMap<Forcefield,Boolean>();
	private int range = 3;
	private ArrayList<EffectType> ffTypes = new ArrayList<EffectType>();
	
	private ArrayList<Effect> getNewFfEffects(){
		ArrayList<Effect> out = new ArrayList<Effect>();
		for(EffectType t:ffTypes)
			out.add(EffectGenerator.getInstance().makeEffect(t));
		return out;
	}
	/**
	 * Creates a new ForceFieldGenerator.
	 * @param effects 
	 * @throws IllegalOnSquareException 
	 * @throws InvalidAttributesException 
	 */
	public ForcefieldGenerator(ArrayList<Effect> effects) throws InvalidAttributesException{
		super(OnSquareType.FORCEFIELDGENERATOR,new ArrayList<Effect>());
		for(Effect t:effects)
			this.ffTypes.add(t.getType());
	}
	
	/**
	 * Returns the range this ForceFieldGenerator has for creating ForceFields.
	 * @return the range this ForceFieldGenerator has for creating ForceFields.
	 */
	public int getRange(){
		return range;
	}
	
	/**
	 * Returns whether this ForceFieldGenerator currently has ForceFields created.
	 * @return	true if this ForceFieldGenerator currently has ForceFields created.
	 * 			false if this ForceFieldGenerator currently has no ForceFields created.
	 */
	public boolean isActivated(){
		return forcefieldList.size()>0;
	}
	
	/**
	 * Returns whethis this ForceFieldGenerator has created the given ForceField.
	 * @param f the ForceField to be checked in this ForceFieldGenerator.
	 * @return	true if the given ForceField is created by this ForceFieldGenerator.
	 * 			false if the given ForceField is not created by this ForceFieldGenerator.
	 */
	public boolean isSwitchForForcefield(Forcefield f){
		Boolean bool = forcefieldList.get(f);
		if(bool!=null)
			return bool;
		else
			return false;
	}
	
	/**
	 * Put this ForceField on the given OnSquareContainer.
	 * Also generates ForceFields if the ForceFieldGenerator is placed on a Square.
	 * @param s the OnSquareContainer this ForceField has to be put on.
	 */
	protected void put(OnSquareContainer s) throws IllegalPutException, InvalidAttributesException{
		try {
			super.put(s);
			if(s instanceof Square){
				generate((Square) s);
			}
		} catch (IllegalOnSquareException e) {
		}
	}
	
	/**
	 * A ForceFieldGenerator is compatible with everything but Walls.
	 */
	@Override
	public boolean isCompatibleWith(OnSquare on) {
		return !(on instanceof Wall);
	}	
	
	/**
	 * A ForceField is visible when it is not destroyed.
	 */
	@Override
	public boolean isVisible() {
		return !isDestroyed();
	}
	
	/**
	 * Add the given ForceField to the list of ForceFields created by this ForceFieldGenerator.
	 * @param f the ForceField to be added to this ForceFieldGenerator.
	 * @throws InvalidAttributesException
	 */
	protected void addForcefield(Forcefield f) throws InvalidAttributesException{
		if(f==null)
			throw new InvalidAttributesException();
		else{
			if(!forcefieldList.containsKey(f)){
				forcefieldList.put(f, false);
			}
		}
	}
	
	/**
	 * Removes all the ForceFields from the list of ForceFields this ForceFieldGenerator has created.
	 */
	private void removeAllFields(){
		HashMap<Forcefield,Boolean> temp = new HashMap<Forcefield,Boolean>();
		for(Forcefield ff:forcefieldList.keySet())
			temp.put(ff, forcefieldList.get(ff));
		for(Forcefield ff:temp.keySet()){
			removeForcefield(ff);
		}
			
	}
	
	/**
	 * Remove the given ForceField from the list of ForceFields this ForceFieldGenerator has created.
	 * @param f the ForceField to be removed from this ForceFieldGenerator.
	 */
	protected void removeForcefield(Forcefield f){
		if(!f.isDestroyed()){
			f.destroy();
			forcefieldList.remove(f);
		}
		else
			forcefieldList.remove(f);
	}
	
	/**
	 * Returns whether the given Direction is valid for generating a ForceField.
	 * @param direction the Direction to be checked.
	 * @return	true if the given Direction is one of the main 8 winddirections.
	 * 			false if the given Direction is not one of the main 8 winddirections.
	 */
	public boolean isValidGeneratingDirection(Direction direction){
		return direction == Direction.NORTH || direction == Direction.NORTHEAST || direction == Direction.EAST || 
				direction == Direction.SOUTHEAST || direction == Direction.SOUTH || direction == Direction.SOUTHWEST || 
					direction == Direction.WEST || direction == Direction.NORTHWEST; 
	}
	
	/**
	 * Generates ForceFields for this ForceFieldGenerator.
	 * @param s the Square this ForceFieldGenerator is on.
	 */
	private void generate(Square s){
		for(Direction d:Direction.values())
			if(isValidGeneratingDirection(d))
				generateInDirection(d,s);
	}
	
	/**
	 * Returns the ForceFieldGenerator on the given Square if it contains one.
	 * @param s the Square to be checked for a ForceFieldGenerator.
	 * @return the ForceFieldGenerator on the given Square or null when it does not contain one.
	 */
	private ForcefieldGenerator getGenerator(Square s){
		ForcefieldGenerator generator = null;
		for(OnSquare on: s.getOnSquares()){
			if(on instanceof ForcefieldGenerator)
				generator = (ForcefieldGenerator)on;
		}
		return generator;
	}
	
	/**
	 * Updates the ForceFields of this ForceFieldGenerator.
	 * @param action the kind of Update to be done.
	 */
	protected void update(Update action){
		if(action == Update.ACTION || action == Update.TURN)
			updateForcefields();
	}
	
	/**
	 * Updates each of the ForceFields of this ForceFieldGenerator individually.
	 */
	private void updateForcefields(){
		for(Forcefield ff:forcefieldList.keySet())
			if(forcefieldList.get(ff))
				ff.update();
	}
	
	/**
	 * Checks if a forcefield can be established between the given s square and the square temp.
	 * If so, establish this forcefield and add put the boolean value of this forcefield to true in the forcefieldlist.
	 * @param s the Square the other ForceFieldGenerator is on.
	 * @param gen the other ForceFieldGenerator that generates a ForceField with this ForceFieldGenerator.
	 * @param d the Direction of the generated ForceField.
	 * @param size the size of the generated ForceField.
	 * @param state the state the ForceField is generated in.
	 */
	private void establishForcefield(Square s, ForcefieldGenerator gen, Direction d, int size, boolean state){
		try {
			Forcefield ff = new Forcefield(d,size,this,gen,state,getNewFfEffects());
			s.putOnSquare(ff);
			forcefieldList.put(ff, true);
		} catch (IllegalPutException e) {
		} catch (InvalidAttributesException e) {
		} catch (IllegalOnSquareException e) {
		} 
	}
	
	/**
	 * Look for ForceFieldGenerators in the given Direction of the given Square.
	 * Create ForceFields if ForceFieldGenerator is found with this ForceFieldGenerator.
	 * @param d the Direction to look in for ForceFieldGenerators.
	 * @param s the Square to look around for ForceFieldGenerators.
	 */
	private void generateInDirection(Direction d, Square s){
		Square temp=s;
		for(int tempRange = range;tempRange>0;tempRange--){
			try {
				temp = temp.getNeighbour(d);
				if(getGenerator(temp)!=null){
					establishForcefield(s,getGenerator(temp),d,range-tempRange+2,!isUsed());
				}
			} catch (IllegalPositionException e) {
			}
		}			
	}
	
	/**
	 * Uses this ForceFieldGenerator, placing it on the Grid where the Player is at 
	 * and removing it from the Player's Inventory.
	 * @param p the Player that uses this ForceFieldGenerator
	 * @param d the Direction this ForceFieldGenerator is used in.
	 */
	@Override
	protected void use(Player p, Direction d) throws InvalidAttributesException, IllegalUseException {
		try {
			super.use(p, d);
			try {
				p.getSquare().putOnSquare(this);
			} catch (IllegalPutException e) {
				setUsed(false);
				setUppickable(false);
				throw new IllegalUseException();
			}
		} catch (IllegalOnSquareException e) {}		
	}
	
	/**
	 * Removes this ForceFieldGenerator from the given OnsquareContainer.
	 * Also removing all ForceFields this ForceFieldGenerator has created.
	 * @param s the OnSquareContainer this ForceFieldGenerator has to be removed from.
	 */
	protected void remove(OnSquareContainer s) throws IllegalRemoveException{
		try {
			super.remove(s);
			removeAllFields();
		} catch (InvalidAttributesException e) {
			
		}
	}
	
	/**
	 * Picks up this ForceFieldGenerator removing it from the Grid and placing it in the Player's Inventory.
	 * @param p the Player that picks up this ForceFieldGenerator.
	 */
	protected void pickup(Player p) throws InventoryFullException, InvalidAttributesException, NotCompatibleWithInventoryException{
		if(canPickUp()){
				super.pickup(p);
		}
	}
}
