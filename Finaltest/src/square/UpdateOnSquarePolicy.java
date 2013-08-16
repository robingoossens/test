package square;

import game.Update;

import java.util.ArrayList;
import java.util.HashSet;

public class UpdateOnSquarePolicy extends UpdatePolicy{

	@Override
	public void update(Update update, HashSet<Square> squares) {
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
