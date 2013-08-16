package square;

import game.Update;

import java.util.ArrayList;
import java.util.HashSet;

public class GridUpdater {
	
	ArrayList<UpdatePolicy> policies = new ArrayList<UpdatePolicy>();
	public GridUpdater(ArrayList<UpdatePolicy> policies){
		for(UpdatePolicy p:policies)
			this.policies.add(p);
	}
	
	public void update(Update update, HashSet<Square> squares){
		for(UpdatePolicy p:policies)
			p.update(update,squares);
	}
}
