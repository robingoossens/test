package square;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPositionException;
import exceptions.IllegalPutException;
import exceptions.IllegalUseException;
import grid.Direction;
import grid.EffectGenerator;

/**
 * An IdentityDisk is an Item that can be used and picked up.
 * Using an IdentityDisk will throw it into a certain direction for its range.
 *
 */
public class IdentityDisk extends Disk{
	
	/**
	 * Creates an IdentityDisk with the given Effects.
	 * @param effects the Effects of the created IdentityDisk.
	 * @throws InvalidAttributesException 
	 */
	public IdentityDisk(ArrayList<Effect> effects) throws InvalidAttributesException{
		super(OnSquareType.IDENTITYDISK,effects);
//		for(Effect f: effects)
//			f.setCreator(this);
		initialiseDisk();
	}
	
	public IdentityDisk(){
		super(OnSquareType.IDENTITYDISK);
		initialiseDisk();
	}
	
	/**
	 * Initializes this IdentityDisk with a range of 4 and adds
	 * the combination use with Teleporters.
	 */
	protected void initialiseDisk(){
		setRange(4);
		addEffectPriorityPolicy(new TeleportPlayerEffectPriorityPolicy());
		addConstraint(new maxTwoTeleportsConstraint());
	}
	
	/**
	 * Creates a new identity disk with the given range.
	 * @param range the range of the created IdentityDisk.
	 */
	protected IdentityDisk(int range){
		super(OnSquareType.IDENTITYDISK);
		setRange(range);
		addEffectPriorityPolicy(new TeleportPlayerEffectPriorityPolicy());
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
