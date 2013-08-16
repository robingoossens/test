package square;

import java.util.ArrayList;

/**
 * An EffectPriorityPolicy determines the order in which certain Effects have to be executed.
 */
public abstract class EffectPriorityPolicy {
	/**
	 * Rearranges the given list of Effects so the Effects with a higher priority come first.
	 * @param effects the list of Effects to be rearranged.
	 */
	public abstract void prioritise(ArrayList<Effect> effects);
}
