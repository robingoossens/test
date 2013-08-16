package square;


/**
 * This goal describes the capturing of the Flag.
 */
public class CaptureFlag extends Goal{
	private Flag flag;

	/**
	 * Create a new captureflag Goal with the given Flag as the Flag to be captured.
	 * @param flag the Flag to be captured.
	 */
	public CaptureFlag(){
		super(GoalType.CAPTUREFLAG);
	}
	
	public void setFlag(Flag flag){
		this.flag = flag;
	}
	
	public Flag getFlag(){
		return flag;
	}
	/**
	 * Updates the status of this Goal.
	 * If the given updater equals the StartingPosition of the Player and the Player is currently holding a Flag,
	 * this Goal will have been met.
	 * @param player the Player stepping on a StartingPosition.
	 * @param updater the StartingPosition that is stepped on by a Player.
	 */
	protected void update(Player player, StartingPosition updater) {
		if(updater.getPlayer().equals(player)){
			if(player.getInventory().contains(flag))
				meet();
		}
	}
	
	
}
