package square;

import java.util.ArrayList;

/**
 * A MaxTwoTeleportsConstraint is a Constraint that makes sure no teleportation infinite loop occurs.
 * A MaxTwoTeleportsConstraint contains a list of all the TeleportEffects crossed in a single turn.
 * When a TeleportEffect occurs that already has occurred this Constraint will be violated.
 */
public class maxTwoTeleportsConstraint extends Constraint{
	
	private ArrayList<TeleportEffect> crossedTeleporters = new ArrayList<TeleportEffect>();
	
	/**
	 * Updates this MaxTwoTeleportsConstraint with the given Effect.
	 * @param effect the Effect that this MaxTwoTeleportsConstraint has to be updated with.
	 */
	@Override
	public void update(Effect effect) {
		if(crossedTeleporters.contains(effect))
			violate();
		else if(effect instanceof TeleportEffect)
			crossedTeleporters.add((TeleportEffect)effect);
	}	
	
	/**
	 * Clears the list of TeleporterEffects.
	 */
	public void clear(){
		super.clear();
		crossedTeleporters.clear();
	}
}
