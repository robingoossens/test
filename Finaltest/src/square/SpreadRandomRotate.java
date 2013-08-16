package square;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPositionException;
import exceptions.IllegalPutException;
import grid.Direction;
import grid.EffectGenerator;

import java.util.ArrayList;
import java.util.Random;

import javax.naming.directory.InvalidAttributesException;

/**
 * Spreading strategy for objects that spread in a random direction 
 * then rotate in a random direction every two actions around the initial object.
 * (currently only PowerFailures)
 */
public class SpreadRandomRotate extends Spread{
	
	private final Direction[] dir;
	private final int rotDirection;
	private int direction;
	private int counter;
	
	/**
	 * The rotating spreading strategy.
	 */
	public void spread(Square square){
		if (counter == 1){
			try {
				if(square.getNeighbour(dir[direction]) != null){
					PowerFailureSec pf = new PowerFailureSec(getEffectGenerator(),direction);
					boolean compatible = true;
					for(OnSquare os : square.getNeighbour(dir[direction]).getOnSquares()){
						if(!pf.isCompatibleWith(os)){
							compatible = false;
						}
					}
					if(compatible){
						square.getNeighbour(dir[direction]).putOnSquare(pf);
					}
				}
			} catch (IllegalPositionException e) {
				// Position outside grid, don't spread there.
			} catch (IllegalOnSquareException e) {
				e.printStackTrace();
				throw new Error();
			} catch (IllegalPutException e) {
				e.printStackTrace();
				throw new Error();
			} catch (InvalidAttributesException e) {
				e.printStackTrace();
				throw new Error();
			}
			direction = (direction + 1) % 8;
			counter = 0;
		} else {
			counter++;
		}
	}
	
	/**
	 * Initializes the spreading strategy.
	 */
	public SpreadRandomRotate(EffectGenerator gen){
		super(gen);
		final Random RND1 = new Random();
		rotDirection = RND1.nextInt(2); // 0 = clockwise; 1 = counterclockwise;
		 
		final Random RND2 = new Random();
		direction = RND2.nextInt(8);
		
		if(rotDirection == 0){
			dir = Direction.generateOrdered(true);
		} else {
			dir = Direction.generateOrdered(false);
		}
		counter = 1;
	}
}
