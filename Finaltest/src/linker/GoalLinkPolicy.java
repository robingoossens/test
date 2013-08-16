package linker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

import square.Goal;
import square.GoalType;
import square.OnSquare;

public abstract class GoalLinkPolicy {
	public abstract void link(ArrayList<OnSquare> onSquares, HashMap<Integer, ArrayList<Goal>> goals) throws InvalidAttributesException;
}
