package square;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

/**
 * A Flag is an OnSquare that is part of the CTF GameMode.
 * A Flag has a StartingPosition as default position and can be picked up and dropped.
 */
public class Flag extends Item{
	
	private StartingPosition startPos;
	
	/**
	 * Create a new Flag for the given StartingPosition.
	 * @param effects 
	 * @param startPos the StartinPostion of this Flag.
	 * @throws InvalidAttributesException 
	 */
	public Flag(ArrayList<Effect> effects) throws InvalidAttributesException{
		super(OnSquareType.FLAG,effects);
	}
	
	public void setStartingPosition(StartingPosition pos){
		this.startPos = pos;
	}
	
	/**
	 * Returns the StartingPostion of this Flag.
	 * @return the StartingPostion of this Flag.
	 */
	public StartingPosition getStartingPos(){
		return startPos;
	}
	
	/**
	 * Return whether this Flag belongs to the given Player.
	 * @param player the Player that this Flag may belong to.
	 * @return true if this Flag belongs to the given Player.
	 * 		   false if this Flag belongs to the given Player.
	 */
	public boolean belongsTo(Player player){
		return startPos.getPlayer().equals(player);
	}
	
	/**
	 * A flag is visible on the field.
	 */
	@Override
	public boolean isVisible() {
		return true;
	}
	
	/**
	 * A flag can lay on a Square with any OnSquare except a Wall.
	 */
	@Override
	public boolean isCompatibleWith(OnSquare on) {
		return !(on instanceof Wall);
	}
	
	/**
	 * A flag has no other distance rules except the default one.
	 */
	@Override
	public boolean canHaveInRange(OnSquare o, int range) {
		if(range == 0){
			return isCompatibleWith(o);
		}
		return true;
	}
	
	/**
	 * Returns whether this Flag can be put in an Inventory with the given Item.
	 * @return	true if the given Item is not a Flag.
	 * 			false if the given Item is a Flag.
	 */
	@Override
	public boolean isCompatibleWithInInventory(Item i){
		return !(i instanceof Flag);
	}

}
