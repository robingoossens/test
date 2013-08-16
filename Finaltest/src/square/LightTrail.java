package square;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

import square.Square;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPutException;
import grid.Position;

/**
 * A LightTrail is an OnSquare that is placed on the Players previous positions.
 * A LightTrail also contains a list of all the LightTrailLinks that exist in the LightTrail.
 * These LightTrailLinks will always contain the Players current position as part of the last link.
 * A LightTrail has a time to live and a Square that was last added to the LightTrailLink list.
 * @invar A Player cannot cross any connected LightTrail: he cannot enter a Square that contains 
 * 		  a LightTrail, and he cannot pass through a diagonal LightTrail.
 */
public class LightTrail extends OnSquare {
	
	private final int tTL=3;
	private Square last;
	private int lastTTL;
	private ArrayList<LightTrailLink> lightTrail = new ArrayList<LightTrailLink>();

	/**
	 * Make a LightTrail that stands on a given Square and has a time to live.
	 * @throws InvalidAttributesException: Thrown when pos is null.
	 */
	public LightTrail(Square pos) throws InvalidAttributesException {
		super(OnSquareType.LIGHTTRAIL);
		if (pos == null)
			throw new InvalidAttributesException();
		HashMap<Square,Integer> firstTrail = new HashMap<Square,Integer>();
		firstTrail.put(pos, tTL + 1);
		try {
			lightTrail.add(new LightTrailLink(firstTrail,false));
		} catch (InvalidAttributesException e) {
		}
		last = pos;
		lastTTL = getTTL();
	}
	
	/**
	 * Check for a given Square, if this LightTrail passes through it. If it doesn't, return -1.
	 * If this LightTrail passes through the Square, return which time to live it has.
	 * @param pos the Square to be checked in this LightTrail.
	 * @throws InvalidAttributesException thrown when pos is null.
	 */
	public int getActivity(Square pos) throws InvalidAttributesException {
		for(LightTrailLink link : lightTrail){
			if(link.contains(pos))
				return link.getNodes().get(pos);
		}
		return -1;
	}
	
	/**
	 * Returns the last Square to which this LightTrail is linked
	 * this will always be the Players Position.
	 */
	protected Square getLast(){
		return last;
	}
	
	/**
	 * Extend this LightTrail without link to a given Square.
	 * @throws InvalidAttributesException thrown when pos is null.
	 * @post this.containsSquare(pos)
	 */
	protected void extendTrail(Square pos) throws InvalidAttributesException{
		HashMap<Square,Integer> nodes = new HashMap<Square,Integer>();
		if(lastTTL > 0) nodes.put(last, lastTTL+1);
		nodes.put(pos, getTTL() + 2);
		LightTrailLink link = new LightTrailLink(nodes, false);
		lightTrail.add(link);
		last = pos;
		lastTTL = getTTL();
	}
	
	/**
	 * Extend this LightTrail with a given list of Square.
	 * The last entry of this list must be the latest Player Position.
	 * @throws InvalidAttributesException thrown when pos is null.
	 * @post this.containsSquare(pos)
	 */
	protected void extendTrail(ArrayList<Square> pos) throws InvalidAttributesException{
		HashMap<Square,Integer> nodes = new HashMap<Square,Integer>();
		if(lastTTL > 0) nodes.put(last, lastTTL);
		for(Square s : pos){
			nodes.put(s,getTTL() + 1);
		}
		LightTrailLink link = new LightTrailLink(nodes, false);
		lightTrail.add(link);
		last = pos.get(pos.size()-1);
		lastTTL = getTTL();
	}
	
	/**
	 * Returns a string representation of this link: all the Positions of the 
	 * Squares in this link.
	 */
	public String toString(){
		String s = "";
		for(LightTrailLink link : lightTrail){
			for(Square sq : link.getNodes().keySet()){
				s += sq.getPosition().getX() +" "+sq.getPosition().getY();
			}
			s += " // ";
		}
		return s;
	}
	
	/**
	 * Check if this LightTrail lays on a given Square.
	 * @param p the Square to be checked in this LightTrail.
	 * @return 	true if this LightTrail lays on the given Square.
	 * 			false if this Lighttrail does not lay on the given Square.
	 * @throws InvalidAttributesException: thrown if p is null.
	 */
	protected boolean containsSquare(Square p) throws InvalidAttributesException{
		for (LightTrailLink link : lightTrail){
			if (link.contains(p)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if there is a LightTrail connection between the two given Squares.
	 * @param s1 the first Square to be checked for a connection.
	 * @param s2 the second Square to be checked for a connection.
	 * @return true if the two given Squares are connected by a LightTrailLink.
	 *         false if the two given Squares are not connected by a LightTrailLink.
	 * @throws InvalidAttributesException thrown if s1 or s2 is null.
	 */
	public boolean hasLink(Square s1, Square s2) throws InvalidAttributesException{
		for (LightTrailLink link : lightTrail){
			if(link.contains(s1) && link.contains(s2) && link.getConnected()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return a new HashSet that contains all the Positions from all the Squares where this LightTrail lays on.
	 */
	public HashSet<Position> getPositions(){
		HashSet<Position> ps = new HashSet<Position>();
		for(LightTrailLink link : lightTrail){
			for(Square s : link.getNodes().keySet()){
				if(!ps.contains(s.getPosition())){
					ps.add(s.getPosition());
				}
			}
		}
		return ps;
	}	
	
	/**
	 * Return a new HashSet that contains all the Squares this LightTrail lays on.
	 */
	public HashSet<Square> getSquares(){
		HashSet<Square> ss = new HashSet<Square>();
		for(LightTrailLink link : lightTrail){
			for(Square s : link.getNodes().keySet()){
				if(!ss.contains(s)){
					ss.add(s);
				}
			}
		}
		return ss;
	}	 
	
	/**
	 * Decrement the time to live from all the Squares of every link of this LightTrail.
	 * If the LightTrail has timed out, the link is removed.
	 * Also removes this LightTrail from Squares it no longer lies on.
	 */
	protected void decrement(){
		ArrayList<LightTrailLink> newLT = new ArrayList<LightTrailLink>();
		for(LightTrailLink link : lightTrail){
			link.decrement();
			if(!link.getTimedOut()){
				newLT.add(link); 
			}
		}
		for(LightTrailLink link : lightTrail){
			for(Square s : link.getNodes().keySet()){ 
				try {
					if(getActivity(s) < 1){
						s.removeOnSquare(this);
					}
				} catch (InvalidAttributesException e) {
				} catch (IllegalOnSquareException e) {	
				}
			}
		}
		for(LightTrailLink link : lightTrail){
			for(Square s : link.getNodes().keySet()){ 
				boolean contains = false;
				for(LightTrailLink l : newLT){
					try {
						if(l.contains(s))
							contains = true;
					} catch (InvalidAttributesException e) {
					}
				}
				if(!contains){
					try {
						s.removeOnSquare(this);
					} catch (IllegalOnSquareException e) {
					}
				}

			}
		}
		lightTrail = newLT;
		if(lastTTL > 0) lastTTL -= 1;
	}
	
	/**
	 * Puts the given OnSquareContainer as the container of this LightTrail.
	 * @param container the OnSquareContainer to be set for this LightTrail.
	 */
	protected void put(OnSquareContainer container) throws InvalidAttributesException{
		if(container==null)
			throw new InvalidAttributesException();
		if(container.contains(this)){
				setOnSquareContainer(container);
		}
	}
	
	/**
	 * Check if this LightTrail is compatible with another OnSquare.
	 * A LightTrail is not compatible with a Wall, a Player or another LightTrail.
	 */
	@Override
	public boolean isCompatibleWith(OnSquare on) {
		if(on instanceof Wall || on instanceof Player || on instanceof LightTrail)
			return false;
		else
			return true;
	}
	
	/**
	 * A LightTrail has no in range constraint except the default one.
	 */
	@Override
	public boolean canHaveInRange(OnSquare o, int range) {
		if(range==0){
			return isCompatibleWith(o);
		}
		return true;
	}
	
	/**
	 * A LightTrail is always visible.
	 */
	@Override
	public boolean isVisible() {
		return true;
	}

	/**
	 * Returns the time to live of this LightTrail.
	 */
	public int getTTL() {
		return tTL;
	}
	
	/**
	 * Connects the LightTrailLink between the two given Squares.
	 * @param prev the first Square to be connected.
	 * @param to the second Square to be connected.
	 */
	protected void link(Square prev, Square to) {
		for(LightTrailLink ltl : lightTrail){
			try {
				if(ltl.contains(prev) && ltl.contains(to)){
					ltl.setConnected();
				}
			} catch (InvalidAttributesException e) {
				e.printStackTrace();
			}		
		}
	}
}
