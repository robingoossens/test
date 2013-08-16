package rules;

import java.util.ArrayList;

import square.ChargedIdentityDisk;
import square.DiskEffect;
import square.DropFlagEffect;
import square.Effect;
import square.OnSquare;

/**
 * This class defines the effects for a ChargedIdentityDisk in CTF.
 *
 */
public class ChargedIdentityDiskEffectRuleCTF extends EffectRule{
	/**
	 * Returns the Effects of a ChargedIdentityDisk in CTF.
	 */
	@Override
	public ArrayList<Effect> getEffects(OnSquare on) {
		if (on instanceof ChargedIdentityDisk){
			ArrayList<Effect> out = new ArrayList<Effect>();
			out.add(new DiskEffect(on));
			out.add(new DropFlagEffect());
			return out;
		} else {
			return null;
		}
	}
}
