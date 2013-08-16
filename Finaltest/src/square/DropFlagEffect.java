package square;

import java.util.ArrayList;
import java.util.Random;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPositionException;
import exceptions.IllegalPutException;
import grid.Direction;

import javax.naming.directory.InvalidAttributesException;

/**
 * A DropFlagEffect will drop the Flag if executed on an OnSquare
 * that is holding a Flag.
 */
public class DropFlagEffect extends Effect{
	
	public DropFlagEffect(){
		super(EffectType.DROPFLAGEFFECT);
	}
	/**
	 * Executes this DropFlagEffect on the given OnSquare.
	 * @param the OnSquare this DropFlagEffect is executed on.
	 */
	@Override
	public void execute(OnSquare p) {
		if(p instanceof Player){
			for(Item i:((Player) p).getInventory().showInventory())
				if(i instanceof Flag){
					boolean placed = false;
					Square temp;
					ArrayList<Direction> usedDirections = new ArrayList<Direction>();
					Direction tempDirection;
					try {
						((Player) p).removeFromInventory(i);
						do{
							try{
								do{
									//TODO: checken dat getRandomAllDirection wel werkt zoals ik ze gebruik
									tempDirection = Direction.getRandomAllDirection(new Random());
								}while(usedDirections.contains(tempDirection));
								usedDirections.add(0, tempDirection);
								temp = ((Square)p.getOnSquareContainer()).getNeighbour(tempDirection);
								temp.putOnSquare(i);
							} catch (IllegalPositionException e) {
							} catch (IllegalOnSquareException e) {
							} catch (IllegalPutException e) {
							}
						}while(!placed);
					} catch (InvalidAttributesException e) {
						e.printStackTrace();
					} 
				}
		}
	}

}
