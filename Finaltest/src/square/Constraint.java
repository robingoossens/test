package square;

/**
 * A Constraint is a rule that can be violated when the rule is broken.
 */
public abstract class Constraint {
	
	private boolean violated;
	
	/**
	 * Returns whether this Constraint is violated.
	 * @return true when this Constraint has been violated.
	 * 		   false when this Constraint has not been violated.
	 */
	public boolean isViolated(){
		return violated;
	}

	/**
	 * Clearing removes the violation of this Constraint.
	 * @post this.violated == false;
	 */
	public void clear(){
		violated=false;
	}
	
	public abstract void update(Effect effect);
	
	/**
	 * Violates this Constraint, making this Constraint violated.
	 * @post this.violated == true;
	 */
	protected void violate(){
		violated=true;
	}
	
}
