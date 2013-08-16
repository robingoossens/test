package square;

import java.util.HashMap;

import javax.naming.directory.InvalidAttributesException;

/**
 * A LightTrailLink represents the link between Squares that contain LightTrails.
 * A LightTrailLink can be connected or not. A LightTrailLink can be decremented,
 * reducing the time to live of each Square in the LightTrailLink by one.
 * Once no Squares remain in the LightTrailLink it is timed out and removed.
 */
public class LightTrailLink {
	private HashMap<Square,Integer> nodes = new HashMap<Square,Integer>();
	private boolean connected;
	private boolean timedOut = false;
	
	/**
	 * Creates an empty LightTrailLink that is either connected or not.
	 * @param connected the Squares in the LightTrailLink are connected by LightTrail or not
	 */
	public LightTrailLink(boolean connected){
		this.connected = connected;
	}
	
	/**
	 * Creates a new LightTraiLink with the given map of Squares and time to lives and
	 * if the Squares are connected by LightTrail or not.
	 * @param nodes the Squares in the LightTrailLink with their time to lives.
	 * @param connected the Squares in the LightTrailLink are connected by LightTrail or not
	 * @throws InvalidAttributesException thrown when ttl is smaller than 1 or any of the given Squares is null.
	 */
	public LightTrailLink(HashMap<Square,Integer> nodes, boolean connected) throws InvalidAttributesException{
		for(Square s : nodes.keySet()){
			if(s == null || nodes.get(s) < 1){
				throw new InvalidAttributesException();
			}
		}
		this.nodes = nodes;
		this.connected = connected;
	}
	
	/**
	 * Adds the given Square to this LightTrailLink.
	 * @param node the Square to be added to this LightTraiLink.
	 * @param ttl the time to live from the to be added Square.
	 * @throws InvalidAttributesException thrown when ttl is smaller than 1 or any of the given Squares is null.
	 */
	protected void addNode(Square node, int ttl) throws InvalidAttributesException{
		if (node != null && ttl > 0){
			nodes.put(node, ttl);
		} else {
			throw new InvalidAttributesException();
		}
	}
	
	/**
	 * Removes the given Square from this LightTraiLink.
	 * @param node the Square to be removed from this LightTrailLink.
	 * @throws InvalidAttributesException thrown when Square is null or Square is not in this LightTrailLink.
	 */
	protected void removeNode(Square node) throws InvalidAttributesException{
		if (node != null && nodes.containsKey(node)){
			nodes.remove(node);
		} else {
			throw new InvalidAttributesException();
		}
	}
	
	/**
	 * Returns the Squares in this LightTrailLink.
	 */
	protected HashMap<Square,Integer> getNodes(){
		return nodes;
	}
	
	/**
	 * Checks if the given Square is part of the connection.
	 * @param node the Square that is checked for in this LightTrailLink.
	 * @return true if the Square is present in this LightTrailLink.
	 * 	       false if the Square is not present in this LightTraiLink.
	 * @throws InvalidAttributesException thrown if given Square is null.
	 */
	protected boolean contains(Square node) throws InvalidAttributesException{
		if (node == null){
			throw new InvalidAttributesException();
		} else {
			return nodes.containsKey(node);
		}
	}
	
	/**
	 * Returns if the Squares in this LightTrailLink are connected by LightTrail or not.
	 */
	protected boolean getConnected(){
		return connected;
	}
	
	/**
	 * Returns if this LightTrailLink has timed out.
	 */
	protected boolean getTimedOut(){
		return timedOut;
	}
	
	/**
	 * Decrements the tTL for each Square in this LightTrailLink.
	 * When a Square has a tTL < 1 it will be removed from the nodes.
	 * When no Squares remain in the LightTrailLink, the connection is timed out.
	 */
	protected void decrement(){
		HashMap<Square,Integer> newNodes = new HashMap<Square,Integer>();
		for(Square s : nodes.keySet()){
			if(nodes.get(s) > 0)
				newNodes.put(s, nodes.get(s)-1);	
		}
		if(newNodes.size() < 1)
			timedOut = true;
		nodes = newNodes;
	}
	
	/**
	 * Sets this LightTrailLink to be connected by LightTrail.
	 * @post this.connected == true;
	 */
	public void setConnected() {
		connected = true;
	}
}
