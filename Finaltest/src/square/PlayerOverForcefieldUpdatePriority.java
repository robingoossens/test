package square;

import java.util.ArrayList;

public class PlayerOverForcefieldUpdatePriority extends UpdatePriorityPolicy{

	@Override
	public void prioritise(ArrayList<OnSquare> onSquares) {
		for(int i=0;i<onSquares.size();i++)
			if(onSquares.get(i) instanceof Player)
				for(int j=0; j<i;j++)
					if(onSquares.get(j) instanceof ForcefieldGenerator){
						OnSquare temp = onSquares.get(i);
						onSquares.set(i, onSquares.get(j));
						onSquares.set(j, temp);
						j=i;
					}
	}
}
