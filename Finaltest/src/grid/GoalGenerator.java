package grid;

import square.CaptureFlag;
import square.Goal;
import square.GoalType;
import square.ReachSquare;

public class GoalGenerator {
	private static GoalGenerator instance = null;
	
	private GoalGenerator(){}
	
	public static GoalGenerator getInstance(){
		if(instance==null)
			instance = new GoalGenerator();
		return instance;
	}
	
	public Goal makeGoal(GoalType type){
		switch (type) {
			case CAPTUREFLAG: return new CaptureFlag();
			case REACHSQUARE: return new ReachSquare();
			default: return null;
		}
	}
}
