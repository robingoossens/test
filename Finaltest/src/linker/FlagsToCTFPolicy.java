package linker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

import square.CaptureFlag;
import square.Flag;
import square.Goal;
import square.GoalType;
import square.OnSquare;
import square.OnSquareType;
import square.Player;

public class FlagsToCTFPolicy extends GoalLinkPolicy{

	@Override
	public void link(ArrayList<OnSquare> onSquares, HashMap<Integer, ArrayList<Goal>> goals)
			throws InvalidAttributesException {
		for(OnSquare on:onSquares)
			if(on.getType()==OnSquareType.FLAG){
				int indexPlayer = ((Flag)on).getStartingPos().getPlayer().getIndex();
				for(int i=1;i<goals.keySet().size()+1;i++){
					if(i!=indexPlayer)
						initCTF(on,goals.get(i));
				}
			}
//		ArrayList<Flag> flags = getFlags(onSquares);
//		HashSet<CaptureFlag> ctfs = getCTFs(goals);
//		if(flags.size()!=ctfs.size())
//			throw new InvalidAttributesException();
//		Flag temp;
//		for(CaptureFlag cf:ctfs){
//			temp = flags.remove(0);
//			cf.setFlag(temp);
//		}
			
	}

	private void initCTF(OnSquare on, ArrayList<Goal> goals) {
		boolean done=false;;
		for(Goal g: goals)
			if(g.getType()==GoalType.CAPTUREFLAG && ((CaptureFlag)g).getFlag()==null&&!done){
				((CaptureFlag)g).setFlag((Flag) on);
				done=true;
			}
		
	}

//	private HashSet<CaptureFlag> getCTFs(HashSet<Goal> goals) {
//		HashSet<CaptureFlag> ctfs = new HashSet<CaptureFlag>();
//		for(Goal g:goals)
//			if(g.getType()==GoalType.CAPTUREFLAG)
//				ctfs.add((CaptureFlag) g);
//		return ctfs;
//	}
//
//	private ArrayList<Flag> getFlags(HashSet<OnSquare> onSquares) {
//		ArrayList<Flag> flags = new ArrayList<Flag>();
//		for(OnSquare on:onSquares)
//			if(on.getType()==OnSquareType.FLAG)
//				flags.add((Flag) on);
//		return flags;
//	}

}
