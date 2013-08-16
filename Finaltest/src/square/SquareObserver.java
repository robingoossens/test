package square;

import game.Update;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A SquareObserver is an observer that will notify Squares about updates in the Game.
 * These notifications will result in the updating of the Game.
 */
public class SquareObserver {
	
	/**
	 * Notify the given Squares of the given Update and update all the OnSquares on the Squares.
	 * @param squares the Squares to be notified of the given Update.
	 * @param update the Update to be done on the given Squares.
	 */
	public void notify(HashSet<Square> squares, Update update){
		ArrayList<OnSquare> onSquares = new ArrayList<OnSquare>();
		ArrayList<OnSquare> updateableOnSquares = new ArrayList<OnSquare>();
		for(Square o:squares){
			onSquares.addAll(o.getOnSquares());
			updateableOnSquares.addAll(o.getOnSquares());
		}
		for(OnSquare o:onSquares)
			for(UpdatePriorityPolicy policy:o.getUpdatePriorityPolicies())
				policy.prioritise(updateableOnSquares);
		for(int i=0;i<updateableOnSquares.size();i++)
			updateableOnSquares.get(i).update(update);
	}
}
