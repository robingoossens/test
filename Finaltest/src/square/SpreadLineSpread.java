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
 * Spreading strategy for objects that spread randomly in a line every action.
 * (currently only PowerFailures)
 */
public class SpreadLineSpread extends Spread{
	
	private final Direction[] dirCW;
	private final Direction[] dir;
	private int direction;
	
	/**
	 * The line spreading strategy.
	 */
	public void spread(Square square) {
		try {
			if(square.getNeighbour(dir[direction]) != null){
				PowerFailure pf = new PowerFailureTert(getEffectGenerator());
				boolean compatible = true;
				for(OnSquare os : square.getNeighbour(dir[direction]).getOnSquares()){
					if(pf.isCompatibleWith(os)){
						compatible = false;
					}
				}
				if(compatible){
					square.getNeighbour(dir[direction]).putOnSquare(pf);
				}
			}
		} catch (IllegalPositionException e) {
		} catch (IllegalOnSquareException e) {
		} catch (IllegalPutException e) {
			// do nothing, cannot spread.
		} catch (InvalidAttributesException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes the line spreading strategy in the given direction.
	 * @param prevDir: the direction to spread in.
	 */
	public SpreadLineSpread(EffectGenerator gen, int prevDir){
		super(gen);
		dirCW = Direction.generateOrdered(true);
		final Random RND2 = new Random();
		direction = RND2.nextInt(3);
		
		dir = new Direction[3];
		dir[0] = dirCW[((prevDir - 1) + 8) % 8];
		dir[1] = dirCW[prevDir];
		dir[2] = dirCW[(prevDir + 1) % 8];
	}
}
