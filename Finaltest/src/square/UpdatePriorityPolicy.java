package square;

import java.util.ArrayList;

/**
 * Policies for in which order onsquares have to be updated by the gridobserver.
 *
 */
public abstract class UpdatePriorityPolicy {
	public abstract void prioritise(ArrayList<OnSquare> onSquares);
}
