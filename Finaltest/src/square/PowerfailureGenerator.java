package square;

import java.util.Random;

import javax.naming.directory.InvalidAttributesException;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPutException;
import grid.EffectRules;
import grid.GridBuilder;
import grid.PlacingRules;

/**
 * Generates PrimaryPowerfailures during the game.
 */
//TODO PF's EffectRules.
public class PowerfailureGenerator extends OnSquare{
	
	private double d;
	private EffectRules er;
	
	/**
	 * Create a new PowerfailureGenerator that creates PowerFailures during the game
	 * with the same effects as given to the PowerfailureGenerator. The way they are generated
	 * is also given to the PowerfailureGenerator.
	 */
	public PowerfailureGenerator(EffectRules er, PlacingRules pr){
		this.er = er;
		this.d = pr.getChancePowerfailureOnSquare();
	}
	
	/**
	 * Generates a chance of creating a PrimaryPowerFailure when it is created
	 * it is placed on the Square this PowerFailureGenerator lies on.
	 */
	public void generate(){
		PowerFailure power;
		Random rand = new Random();
		int bool;
		bool = rand.nextInt(100);
		if(bool<d*100){
			power = new PowerFailurePrim(er.getPowerfailureEffects(null));
			//TODO parameter voor effect op te vragen  wtf?????
			try {
				GridBuilder.putPowerFailure(power, (Square) getOnSquareContainer());
			} catch (InvalidAttributesException e) {
				new Error();
			} catch (IllegalOnSquareException e) {
				new Error();
			} catch (IllegalPutException e) {
				new Error();
			}
		}
	}
	
	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public boolean canHaveInRange(OnSquare o, int range) {
		return true;
	}

	@Override
	public boolean isCompatibleWith(OnSquare on) {
		return true;
	}

}
