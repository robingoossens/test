package square;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPutException;
import exceptions.IllegalRemoveException;
import grid.EffectGenerator;

/**
 * A Teleporter is an OnSquare that teleports OnSquares that trigger it from its
 * Position to its destination Position.
 */
public class Teleporter extends OnSquare{
	private boolean teleporting;
	private Square square;
	private Teleporter destination;
	
	public Teleporter(ArrayList<Effect> effects) throws InvalidAttributesException {
		super(OnSquareType.TELEPORTER, effects);
	}

	@Override
	public boolean canHaveInRange(OnSquare o, int range) {
		if(range==0){
			return isCompatibleWith(o);
		}
		return true;
	}
	
	public Teleporter getDestination(){
		return destination;
	}
	
	protected Square getSquare(){
		return square;
	}
	
	/**
	 * Checks if the given OnSquare is compatible with the square of this teleporter.
	 */
	protected boolean isBlockedFor(OnSquare on){
		for(OnSquare o: square.getOnSquares()){
			if(!o.equals(this)){
				if(!o.isCompatibleWith(on)){
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean isCompatibleWith(OnSquare on) {
		//Eerste check is nodig zodat een player niet op een vakje kan stappen met een teleporter
		//wanneer er een player of lighttrail op de square van de destination ligt.
		if(isTeleporting())
			return true;
		else{
			if(on instanceof Player){
				if(destination!=null){
					return destination.isBlockedFor(on);
				} else {
					return true;
				}
			}
			else if(on instanceof Wall || on instanceof Teleporter)
				return false;
			else {
				return true;
			}
		}
	}
	
	public boolean isTeleporting(){
		return teleporting;
	}
	
	@Override
	public boolean isVisible() {
		return true;
	}
	
	protected void put(Square s) throws InvalidAttributesException, IllegalPutException, IllegalOnSquareException, IllegalRemoveException{
		super.put(s);
		if(s.contains(this))
			square = s;
		else{
			super.remove(s);
			throw new IllegalPutException();
		}
	}
	
	protected void remove(Square s) throws InvalidAttributesException, IllegalRemoveException{
		if(s.equals(square)){
			super.remove(s);
			square = null;
		}
	}
	
	/**
	 * Sets the destination of this teleporter to the given destination.
	 * If this teleporter already has a destination, nothing happens.
	 * 
	 * @throws InvalidAttributesException: Thrown when destination==null;
	 */
	public void setDestination(Teleporter destination) throws InvalidAttributesException{
		if(destination==null)
			throw new InvalidAttributesException();
		else if(this.destination==null)
			this.destination = destination;
	}
	
	/**
	 * Sets the square this teleporter is on to the given square.
	 * @throws IllegalOnSquareException: Thrown when the given square can't contain 
	 * 
	 * @throws InvalidAttributesException: Thrown when s==null;
	 * @throws IllegalPutException 
	 */
	public void setSquare(Square s) throws IllegalOnSquareException, InvalidAttributesException, IllegalPutException{
		if(s==null)
			throw new InvalidAttributesException();
		if(!s.getOnSquares().contains(this))
			s.putOnSquare(this);
		square = s;
	}
	
	/**
	 * Teleports the given OnSquare from this teleporter to its destination.
	 * 
	 * @throws IllegalOnSquareException: Thrown when the square of the destination teleporter is
	 * occupied.
	 * @throws InvalidAttributesException: Thrown when the given OnSquare ==null.
	 * @throws IllegalPutException 
	 */
	public void teleport(OnSquare on) throws IllegalOnSquareException, InvalidAttributesException, IllegalPutException{
		if(on==null)
			throw new InvalidAttributesException();
		//volgorde omgekeerd zodat teleporteffect een exception kan gooien en players niet op het
		//vakje van een teleporter kunnen stappen als een andere player of lighttrail op de 
		//destination ervan staat.
		teleportFrom(on);
		destination.teleportTo(on);
	}
	
	/**
	 * Removes the onSquare from the square of this teleporter and deactivates the teleporter.
	 */
	private void teleportFrom(OnSquare on){
		try {
			//deactivatie is nodig om te zorgen dat wnn een identitydisk geplaatst wordt op een teleporter
			//en de player die ze gegooid heeft ook op de teleporter staat.
			//indien hij niet gedeactiveerd wordt zal bij grantturn() in Player het teleporteffect
			//worden uitgevoerd indien de player die beurt niet meer vd teleporter af beweegt.
			deactivate();
			square.removeOnSquare(on);
		} catch (IllegalOnSquareException e) {
		}
	}
	
	/**
	 * Puts the given onsquare on the square of this teleporter.
	 * 
	 * @throws IllegalOnSquareException: Thrown when this onsquare can't be put on this square.
	 * @throws InvalidAttributesException: Thrown when on==null.
	 * @throws IllegalPutException 
	 */
	protected void teleportTo(OnSquare on) throws IllegalOnSquareException, InvalidAttributesException, IllegalPutException{
		if(on==null)
			throw new InvalidAttributesException();
		else{
			//teleporting variabele is nodig omdat de Square een teleporteffect heeft liggen dat niet
			//mag uitgevoerd worden, omdat een teleportatie anders in een eeuwige lus komt.
			teleporting=true;
			square.putOnSquare(on);
			//deactivatie gebeurt omdat wnn de player geteleporteerd wordt en zijn beurt afgelopen is,
			//hij bij het begin van zijn volgende beurt niet opnieuw mag geteleporteerd worden.
			deactivate();
			teleporting=false;
		}
	}

	protected void trigger(OnSquare on){
		if(on instanceof IdentityDisk || on instanceof Player)
			activate();
	}
}
