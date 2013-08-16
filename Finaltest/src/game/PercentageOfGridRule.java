package game;

import grid.Grid;

public class PercentageOfGridRule extends NumberRule{
	private double percentage;
	public PercentageOfGridRule(double percentage){
		this.percentage = percentage;
	}
	@Override
	public int getNumber(Grid grid, int nPlayers) {
		return (int) Math.ceil(grid.getDimension().getX()*grid.getDimension().getY()*percentage);
	}
	
	
}	
