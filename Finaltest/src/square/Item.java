package square;

import java.util.ArrayList;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPutException;
import exceptions.IllegalUseException;
import exceptions.InventoryFullException;
import exceptions.NotCompatibleWithInventoryException;
import grid.Direction;
import grid.EffectGenerator;

import javax.naming.directory.InvalidAttributesException;

/**
 * An Item is an Onsquare that can be used and picked up.
 * Items can also be contained in a Players Inventory.
 * An Item has a list of constraints bound to it.
 */
public abstract class Item extends OnSquare {
	
	private boolean used;
	private ArrayList<Constraint> constraints = new ArrayList<Constraint>();
	
	/**
	 * Create an Item.
	 * @param effects 
	 * @throws InvalidAttributesException 
	 */
	public Item(OnSquareType type, ArrayList<Effect> effects) throws InvalidAttributesException {
		super(type,effects);
	}
	
	/**
	 * Create an Item with the given Effects.
	 * @param effects the Effects this Item is created with.
	 */
	public Item(EffectGenerator eg, OnSquareType type){
		super(eg,type);
	}
	
	/**
	 * Return whether this Item is used.
	 * @return	true if this Item is used.
	 * 			false if this Item is not used.
	 */
	public boolean isUsed(){
		return used;
	}
	
	/**
	 * Update the constraints of this Item with the given Effect.
	 * @param effect the Effect that has to update the constraints of this Item.
	 */
	protected void updateConstraints(Effect effect){
		for(Constraint c:constraints)
			c.update(effect);
	}
	
	/**
	 * Returns the list of constraints of this Item.
	 * @return the list of constraints of this Item.
	 */
	public ArrayList<Constraint> getConstraints(){
		return (ArrayList<Constraint>)constraints.clone();
	}
	
	/**
	 * Add a constraint to this Item.
	 * @param c the constraint to be added to this Item.
	 */
	protected void addConstraint(Constraint c){
		constraints.add(c);
	}
	
	/**
	 * Clears the constraints for this Item.
	 */
	protected void clearConstraints(){
		for(Constraint c:constraints)
			c.clear();
	}
	
	/**
	 * Sets this Item to the given used state.
	 * @param used the used state to be set.
	 * @post this.used == true
	 */
	protected void setUsed(boolean used){
		this.used = used;
	}
	
	/**
	 * Returns whether this Item is puttable on the given OnSquareContainer.
	 * @param container the OnSquareContainer to be checked.
	 * @return	true if the given OnSquareContainer is not null.
	 * 			false if the given OnSquareContainer is null.
	 */
	public boolean isPuttableOn(OnSquareContainer container){
		return container!=null;
	}
	
	/**
	 * Puts this Item in the given OnSquareContainer.
	 * @param container the OnSquareContainer this Item is put in.
	 */
	protected void put(OnSquareContainer container) throws InvalidAttributesException, IllegalPutException, IllegalOnSquareException{
		super.put(container);
		setUppickable(true);
	}
	/**
	 * Player uses this Item in the given Direction.
	 * @param p the Player that uses this Item.
	 * @param d the Direction this Item is used in.
	 * @throws InvalidAttributesException 
	 * @throws IllegalPutException 
	 * @throws IllegalUseException 
	 */
	protected void use(Player p, Direction d) throws InvalidAttributesException, IllegalUseException{
		if(p==null)
			throw new InvalidAttributesException();
		setUsed(true);
	}
	
	/**
	 * Player picks up this Item.
	 * @param p the Player that picks up this Item.
	 * @throws InventoryFullException thrown when the given Players Inventory is full.
	 * @throws InvalidAttributesException
	 * @throws NotCompatibleWithInventoryException thrown when this Item is not compatible with an Item in the given Players Inventory.
	 */
	protected void pickup(Player p) throws InventoryFullException, InvalidAttributesException, NotCompatibleWithInventoryException{
		p.addToInventory(this);
		setUppickable(false);
	}
	
	/**
	 * An Item has no in range constraint except from the default one.
	 */
	@Override
	public boolean canHaveInRange(OnSquare o, int range){
		if(range == 0){
			return isCompatibleWith(o);
		}
		return true;
	}
	
	/**
	 * An Item is by default always compatible with other Items in an Inventory.
	 */
	public boolean isCompatibleWithInInventory(Item i){
		return true;
	}
}