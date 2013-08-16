package placer;

import grid.Grid;
import grid.PlacingRules;

import java.util.ArrayList;
import java.util.HashSet;

import square.OnSquare;

public class Placer {
	private static Placer instance = null;
	
	public Placer(){}
	
	public static Placer getInstance() {
		if(instance == null)
			instance = new Placer();
		return instance;
	}
	
	public void place(Grid grid, ArrayList<OnSquare> onsquares, ArrayList<PlacingPolicy> pp){
		for(PlacingPolicy p:pp){
			p.place(grid, onsquares);
		}
	}

}
