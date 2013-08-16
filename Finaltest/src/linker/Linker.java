package linker;


import grid.GoalGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;


import square.Goal;
import square.GoalType;
import square.OnSquare;
import square.OnSquareType;
import square.Player;

public class Linker {
	
	private static Linker instance = null;
	
	private Linker(){}
	
	public static Linker getInstance(){
		if(instance==null)
			instance = new Linker();
		return instance;
	}
	
	public void link(ArrayList<OnSquare> onsquares,
			ArrayList<LinkingPolicy> linkingPolicies) throws InvalidAttributesException {
		for(LinkingPolicy link:linkingPolicies)
			link.link(onsquares);
		
	}

	public void link(ArrayList<OnSquare> onsquares,
			HashMap<Integer, HashMap<GoalType, Integer>> goaltypes, ArrayList<GoalLinkPolicy> policies) throws InvalidAttributesException {
		HashMap<Integer, ArrayList<Goal>> goals = new HashMap<Integer, ArrayList<Goal>>();
		Integer count;
		ArrayList<Goal> temp = new ArrayList<Goal>();
		Goal tempgoal;
		for(Integer i:goaltypes.keySet()){
			temp = new ArrayList<Goal>();
			for(GoalType gt:goaltypes.get(i).keySet()){
				count = goaltypes.get(i).get(gt);
				for(int j=0;j<count;j++){
					tempgoal = GoalGenerator.getInstance().makeGoal(gt);
					temp.add(tempgoal);
				}
			}
			goals.put(i, temp);
		}
		addGoalsToPlayers(onsquares,goals);
		for(GoalLinkPolicy glp:policies)
			glp.link(onsquares, goals);
	}

	private void addGoalsToPlayers(ArrayList<OnSquare> onsquares, HashMap<Integer, ArrayList<Goal>> goals) {
		for(OnSquare on:onsquares)
			if(on.getType()==OnSquareType.PLAYER){
				for(Goal g:goals.get(((Player)on).getIndex()))
						((Player)on).addGoal(g);
			}
		
	}

}
