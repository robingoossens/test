package game;

import grid.Grid;

public class EqToNPlayers extends NumberRule{

	@Override
	public int getNumber(Grid grid, int nPlayers) {
		return nPlayers;
	}

}
