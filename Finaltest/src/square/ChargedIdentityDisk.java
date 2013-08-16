package square;

import grid.Direction;
import grid.EffectGenerator;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

/**
 * A charged IdentityDisk is an IdentityDisk with unlimited range.
 */
public class ChargedIdentityDisk extends Disk{
	
	/**
	 * Create a charged IdentityDisk with the given effects.
	 * @param effects the effects this charged IdentityDisk will be created with.
	 * @throws InvalidAttributesException 
	 */
	public ChargedIdentityDisk(ArrayList<Effect> effects) throws InvalidAttributesException{
		super(OnSquareType.CHARGEDIDENTITYDISK,effects);
	}
	
	/**
	 * Initializes this IdentityDisk with a range of 4 and adds
	 * the combination use with Teleporters.
	 */
	protected void initialiseDisk(){
		setRange(Integer.MAX_VALUE);
		addEffectPriorityPolicy(new TeleportPlayerEffectPriorityPolicy());
		addConstraint(new maxTwoTeleportsConstraint());
	}
	
	/**
	 * Returns whether the given Direction is a valid one to use this IdentityDisk in.
	 * @param d the Direction to be checked.
	 * @return	true if the given Direction is one of the main 4 winddirections.
	 * 			false if the given Direction is not one of the main 4 winddirections.
	 */
	public boolean isValidDirection(Direction d){
		return (d==Direction.NORTH || d==Direction.EAST || d==Direction.SOUTH || d==Direction.WEST);
	}
}
