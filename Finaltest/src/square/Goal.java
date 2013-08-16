package square;


/**
 * A Goal is a state that can be met.
 * Goals are used to determine a succesfull winner of the Game.
 */
public abstract class Goal {
	
	public Goal(GoalType type){
		this.type = type;
	}
	protected abstract void update(Player player, StartingPosition updater);
	private GoalType type;
	private boolean met;
	
	public GoalType getType(){
		return type;
	}
	/**
	 * Sets this goal to be met.
	 * @post this.met == true.
	 */
	protected void meet(){
		met=true;
	}
	
	/**
	 * Checks whether this Goal has been met.
	 * @return	true if this Goal has been met.
	 * 			false if this Goal has not been met.
	 */
	public boolean hasBeenMet(){
		return met;
	}
}
