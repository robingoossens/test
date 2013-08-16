package square;

import java.util.HashSet;

/**
 * A CombinationEffect is an Effect that has multiple
 * OnSquares as creators.
 */
public abstract class CombinationEffect extends Effect{
	
	private HashSet<OnSquare> creators = new HashSet<OnSquare>();
	
	/**
	 * Create a CombinationEffect with a given list of OnSquares as creators.
	 * @param creators list of OnSquares this CombinationEffect will have as creators.
	 */
	public CombinationEffect(HashSet<OnSquare> creators, EffectType type){
		super(type);
		for(OnSquare c:creators)
			this.creators.add(c);
	}
	
	/**
	 * Returns the list of OnSquare creators of this CombinationEffect.
	 * @return a cloned list of OnSquare creators of this CombinationEffect.
	 */
	public HashSet<OnSquare> getCreators(){
		return (HashSet<OnSquare>)creators.clone();
	}
}
