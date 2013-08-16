package rules;

import java.util.ArrayList;

import square.ChargedIdentityDisk;
import square.DiskEffect;
import square.Effect;
import square.OnSquare;

/**
 * This class defines the default effects for a ChargedIdentityDisk.
 *
 */
public class ChargedIdentityDiskEffectRule extends EffectRule{
	/**
	 * Returns the Effects of an IdentityDisk.
	 */
	@Override
	public ArrayList<Effect> getEffects(OnSquare on) {
		if (on instanceof ChargedIdentityDisk){
			ArrayList<Effect> out = new ArrayList<Effect>();
			out.add(new DiskEffect(on));
			return out;
		} else {
			return null;
		}
	}
}
