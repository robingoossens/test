package square;
import java.util.ArrayList;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

import square.PowerExplosionEffect;
import square.PowerFailureEffect;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPutException;
import exceptions.IllegalUseException;
import grid.Direction;
import grid.EffectGenerator;

/**
 * A LightGrenade is an Item that can be used and picked up.
 * A LightGrenade has two different states, an active state in which it can explode
 * and an inactive state in which it can not explode.
 * @invar An active light grenade is invisible and cannot be picked up.
 * 		  | !canPickUp()
 * @invar A player cannot use multiple light grenades on the same square.
 * 		  | !LightGrenade.isCompatibleWith(LightGrenade l)
 */
public class LightGrenade extends Item{	

	
	/**
	 * Creates a LightGrenade.
	 */
	public LightGrenade(){
		super(OnSquareType.LIGHTGRENADE);
	}
	
	/**
	 * Creates a LightGrenade with the given Effects.
	 * @param effects the Effects this LightGrenade is created with.
	 * @throws InvalidAttributesException 
	 */
	public LightGrenade(ArrayList<Effect> effects) throws InvalidAttributesException{
		
		super(OnSquareType.LIGHTGRENADE, effects);
	}
	
	/**
	 * Use this LightGrenade. This LightGrenade becomes active and is put on the current location of the given Player.
	 * The Effect of the LightGrenade is now set to a new ExplosionEffect.
	 * @throws InvalidAttributesException: Thrown when p==null.
	 * @throws IllegalUseException 
	 * @post p.getSquare().getOnSquares().contains(this)
	 */
	protected void use(Player p, Direction d) throws InvalidAttributesException, IllegalUseException {
		try {
			super.use(p, d);
			p.getSquare().putOnSquare(this);
		} catch (IllegalOnSquareException e) {
			setUsed(false);
			throw new IllegalUseException();
		} catch (IllegalPutException e) {
			setUsed(false);
			throw new IllegalUseException();
		}
	}	
	
	/**
	 * Puts this LightGrenade in the given OnSquareContainer.
	 * @param container the OnSquareContainer this LightGrenades is placed in.
	 */
	protected void put(OnSquareContainer container) throws InvalidAttributesException, IllegalPutException, IllegalOnSquareException{
		super.put(container);
		if(isUsed())
			setUppickable(false);
	}
	
	/**
	 * Triggers this LightGrenade to activate when a Player steps on it and the LightGrenade is active.
	 * @param on the OnSquare that triggers this LightGrenade.
	 */
	protected void trigger(OnSquare on){
		if(on instanceof Player && isUsed())
			activate();
	}
	
	/**
	 * A LightGrenade is compatible with all OnSquares except Walls and LightGrenades.
	 */
	@Override
	public boolean isCompatibleWith(OnSquare on) {
		if(on instanceof Wall || on instanceof LightGrenade)
			return false;
		else
			return true;
	}
	
	/**
	 * Destroys this LightGrenade, deactivating it and removing its Effects.
	 */
	protected void destroy(){
		deactivate();
		super.destroy();
	}
	
	/**
	 * A LightGrenade is visible when it is not used and not destroyed.
	 */
	@Override
	public boolean isVisible() {
		return !isUsed() && !isDestroyed();
	}
	
}
