package square;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPositionException;
import exceptions.IllegalPutException;
import exceptions.IllegalUseException;
import grid.Direction;

public abstract class Disk extends Item{
	private int defaultRange;
	private int activeRange;
	
	public Disk(OnSquareType type, ArrayList<Effect> effects) throws InvalidAttributesException {
		super(type,effects);
		initialiseDisk();
	}
	
	protected abstract void initialiseDisk();

	/**
	 * Decrements this identity disk's current range.
	 */
	protected void decrementRange(){
		activeRange--;
	}
	
	/**
	 * Stops this IdentityDisk in its motion and executes its Effect on the OnSquare that caused
	 * it to stop.
	 * If this IdentityDisk is not in motion (!isActive()), then nothing happens.
	 */
	protected void stop(OnSquare on){
		if(isUsed()){
			activeRange = 0;
			if(on instanceof Player){
				for(Effect f: getEffects()){
					f.execute(on);
				}
			}
			deactivate();
			setUsed(false);
		}
		else
			setUsed(false);
	}
	
	/**
	 * Sets the range of this IdentityDisk to the given range.
	 * @param range the range to be set for this IdentityDisk.
	 */
	protected void setRange(int range){
		defaultRange = range;
	}
	
	public abstract boolean isValidDirection(Direction d);
	/**
	 * Makes the IdentityDisk move in the given Direction starting from the Square on which the
	 * given Player is on.
	 * The IdentityDisk is repeatedly put on the Squares in the given Direction, at which point the possible
	 * Effects of that Square are executed on the disk.
	 * This continues until the IdentityDisk has moved the number of Squares equal to its range or when
	 * it has been stopped by some Effect.
	 * @throws InvalidAttributesException thrown when p==null || d==null.
	 * @throws IllegalUseException 
	 */
	protected void use(Player p, Direction d) throws InvalidAttributesException, IllegalUseException{
		if(d==null || !isValidDirection(d))
			throw new InvalidAttributesException();
		activeRange = defaultRange;
		try {
			//effecten op de disk (zoals bv powerfailures) worden in deze methode uitgevoerd.
			//TODO getonsquareocntainer.removeonsquare(this)
			((Square)p.getOnSquareContainer()).putOnSquare(this);
		} catch (IllegalOnSquareException e2) {
		} catch (IllegalPutException e) {
		}
		//als de disk door een teleporter opnieuw op het vakje vd player komt moet het playereffect
		//er wel voor zorgen dat de disk stopt, dus wordt de used variabele hier op true gezet.
		super.use(p, d);
		boolean violated = false;
		Square test=null;
		do{
			try {
				test = (Square) getOnSquareContainer();
				
				test.removeOnSquare(this);
				test.getNeighbour(d).putOnSquare(this);
				
				for(Constraint c:this.getConstraints())
					if(c.isViolated())
						violated=true;
				if(violated)
					stop(null);
				else
					decrementRange();
			} catch (IllegalPositionException e1) {
					try {
						stop(null);
						test.putOnSquare(this);
					} catch (IllegalOnSquareException e) {
					} catch (IllegalPutException e) {
					}
			} catch (IllegalOnSquareException e) {
					try {
						stop(null);
						test.putOnSquare(this);
					} catch (IllegalOnSquareException e1) {
					} catch (IllegalPutException e1) {
					}
			} catch (IllegalPutException ed) {
				}
		}while(activeRange>0);
		setUsed(false);
		clearConstraints();
	}
	
	/**
	 * An IdentityDisk has no in range contraints except the default one.
	 */
	@Override
	public boolean canHaveInRange(OnSquare o, int range) {
		if(range == 0){
			return isCompatibleWith(o);
		} else {
			return true;
		}
	}
	
	/**
	 * An IdentityDisk can be picked up when an Item can be picked up
	 * and when this IdentityDisk is not destroyed.
	 * @return 	true if this IdentityDisk can be picked up.
	 * 			false if this IdentityDisk can not be picked up.
	 */
	@Override
	public boolean canPickUp() {
		return super.canPickUp() && !isDestroyed();
	}
	
	/**
	 * An IdentityDisk is compatible with every Onsquare except Walls.
	 */
	@Override
	public boolean isCompatibleWith(OnSquare on) {
		if(on instanceof Wall)
			return false;
		else return true;
	}
	
	public boolean isVisible(){
		return !isDestroyed();
	}
}
