package square;


public class ReachSquare extends Goal{

	public ReachSquare(){
		super(GoalType.REACHSQUARE);
	}
	
	@Override
	protected void update(Player player, StartingPosition updater) {
		if(!updater.getPlayer().equals(player))
			meet();
	}
	
}
