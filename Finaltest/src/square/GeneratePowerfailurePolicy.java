package square;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPutException;
import game.Update;
import grid.EffectGenerator;
import grid.EffectRules;
import grid.GridBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.naming.directory.InvalidAttributesException;

public class GeneratePowerfailurePolicy extends UpdatePolicy {
	
	private double chance;
	private HashSet<EffectType> effects;
	
	public GeneratePowerfailurePolicy(double chance, HashSet<EffectType> effects){
		this.chance = chance;
		this.effects = effects;
	}
	@Override
	public void update(Update update, HashSet<Square> squares) {
			if(update == Update.START || update == Update.TURN){
				PowerFailure power=null;
				Random rand = new Random();
				int bool;
				ArrayList<Effect> temp;
				for(Square s:squares){
					bool = rand.nextInt(100);
					if(bool<chance*100){
						temp = new ArrayList<Effect>();
						for(EffectType t:effects)
							temp.add(EffectGenerator.getInstance().makeEffect(t));
						try {
							power = new PowerFailurePrim(temp);
						} catch (InvalidAttributesException e1) {
							e1.printStackTrace();
						}
						try {
							s.putOnSquare(power);
						} catch (InvalidAttributesException e) {
							new Error();
						} catch (IllegalOnSquareException e) {
							new Error();
						} catch (IllegalPutException e) {
							new Error();
						}
					}
				}
			}		
	}

}
