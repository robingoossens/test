package square;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPutException;
import exceptions.IllegalRemoveException;
import exceptions.InventoryFullException;
import exceptions.NotCompatibleWithInventoryException;


/**
 * An Inventory is an OnsquareContainer that can contain Items with a certain capacity.
 * @invar An Inventory has a capacity and it cannot contain more Items than it
 *        has capacity.
 */
public class Inventory implements OnSquareContainer{
	private ArrayList<Item> items = new ArrayList<Item>();
	private int capacity;
	
	/**
	 * Creates an Inventory with the given capacity.
	 * @param capacity the maximum amount of Items that the Inventory can contain.
	 * @throws InvalidAttributesException thrown when capacity<0.
	 */
	public Inventory(int capacity) throws InvalidAttributesException{
		if(capacity<0)
			throw new InvalidAttributesException();
		setCapacity(capacity);
	}
	
	/**
	 * Check if this Inventory contains a given Item.
	 * @param i the Item to be checked for in this Inventory.
	 */
	public boolean contains(Item i){
		return items.contains(i);
	}
	
	/**
	 * Adds the given Item to this Inventory.
	 * @param item the Item to add to this Inventory.
	 * @throws InventoryFullException thrown when this Inventory is full and you cannot add another Item.
	 * @throws IllegalPutException 
	 * @throws InvalidAttributesException 
	 * @throws NotCompatibleWithInventoryException thrown when the given Item conflicts with being in the same 
	 * 			Inventory as another Item in this Inventory.
	 * @post items.contains(item) == true
	 */
	public void addItem(Item item) throws InventoryFullException, InvalidAttributesException, NotCompatibleWithInventoryException{
		for(Item i: items){
			if(!i.isCompatibleWithInInventory(item)){
				throw new NotCompatibleWithInventoryException();
			}
		}
		try {
			putOnSquare(item);
		} catch (IllegalOnSquareException e) {
			
		} catch (IllegalPutException e) {
			throw new InventoryFullException("Inventory full!");
		}
	}
	
	/**
	 * Removes the given Item from this Inventory.
	 * @param item the Item to be removed from this Inventory.
	 * @throws InvalidAttributesException 
	 * @post !items.contains(item)
	 */
	public void removeItem(Item item) throws InvalidAttributesException{
		try {
			removeOnSquare(item);
		} catch (IllegalOnSquareException e) {
			throw new InvalidAttributesException();
		}
	}
	
	/**
	 * Returns the size of this Inventory.
	 * @return the size of this Inventory.
	 */
	public int getSize(){
		return items.size();
	}
	
	/**
	 * Returns the capacity of this Inventory.
	 * @return the capacity of this Inventory.
	 */
	public int getCapacity(){
		return this.capacity;
	}
	
	/**
	 * Sets the capacity of this Inventory to the given capacity.
	 * @param capacity the given capacity to be set for this Inventory.
	 * @post getCapacity() == capacity
	 */
	private void setCapacity(int capacity){
		this.capacity = capacity;
	}
	
	/**
	 * Returns an array of all the Items in this Inventory.
	 * @return an array of all the Items in this Inventory.
	 */
	public Item[] showInventory(){
		Item[] items = new Item[this.items.size()];
		for(int i=0;i<this.items.size();i++)
			items[i] = this.items.get(i);
		return items;
	}
	
	/**
	 * Checks whether the given OnSquare is an Item in this Inventory.
	 * @return 	true if the given OnSquare is an Item in this Inventory.
	 * 			false if the given OnSquare is not an Item in this Inventory.
	 */
	@Override
	public boolean contains(OnSquare on) {
		if(on instanceof Item)
			return contains((Item)on);
		else return false;
	}
	
	/**
	 * Places the given OnSquare in this Inventory when possible.
	 * @param on the OnSquare to be placed in this Inventory.
	 */
	@Override
	public void putOnSquare(OnSquare on) throws IllegalOnSquareException,
			IllegalPutException, InvalidAttributesException {
		if(on==null)
			throw new InvalidAttributesException();
		else if(!on.isCompatibleWith(this)){
				throw new IllegalOnSquareException();
			}
		else{
			if(items.size()>=capacity) {
				throw new InvalidAttributesException();
			}
			else{ 
				items.add((Item)on);
				on.put(this);
			}
		}
		
	}
	
	/**
	 * Removes the given OnSquare from this Inventory when possible.
	 * @param on the OnSquare to be removed from this Inventory.
	 */
	@Override
	public void removeOnSquare(OnSquare on) throws IllegalOnSquareException {
		if(!items.contains(on))
			throw new IllegalOnSquareException();
		else{
			items.remove((Item)on);
			try {
				on.remove(this);
			} catch (InvalidAttributesException e) {
			} catch (IllegalRemoveException e) {
			}
		}		
	}
	
	/**
	 * Returns whether the given Onsquare can be added to this Inventory.
	 * An Onsquare can be added to this Inventory when the capacity is not reached and the OnSquare is an Item.
	 * @return 	true when the given Onsquare can be added to this Inventory.
	 * 			false when the given Onsquare can not be added to this Inventory.
	 */
	@Override
	public boolean canAdd(OnSquare on) {
		if(items.size()<capacity && on instanceof Item)
			return true;
		else return false;
	}
}
