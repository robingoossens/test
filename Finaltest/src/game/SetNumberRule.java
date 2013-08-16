package game;

import grid.Grid;

public class SetNumberRule extends NumberRule{
	private int number;
	public SetNumberRule(int number){
		this.number = number;
	}
	@Override
	public int getNumber(Grid grid, int nPlayers) {
		return number;
	}
}
