package square;

import javax.naming.directory.InvalidAttributesException;

import exceptions.IllegalOnSquareException;
import exceptions.IllegalPutException;

/**
 * An OnSquareContainer is an interface that can be implemented by containers of OnSquares
 * to add and remove OnSquares, check if an OnSquare is in the container and to check if the
 * given OnSquare can be added to the container.
 */
public interface OnSquareContainer {
	
	/**
	 * Check if the given OnSquare is contained in this OnSquareContainer.
	 * @param on the OnSquare to be checked for in this OnSquareContainer.
	 * @return	true if this OnSquareContainer contains the given OnSquare.
	 * 			false if this OnSquareContainer does not contain the given OnSquare.
	 */
	public boolean contains(OnSquare on);
	
	/**
	 * Puts the given OnSquare in this OnSquareContainer.
	 * @param on the OnSquare to be put in this OnSquareContainer.
	 * @throws IllegalOnSquareException
	 * @throws IllegalPutException
	 * @throws InvalidAttributesException
	 */
	public abstract void putOnSquare(OnSquare on) throws IllegalOnSquareException, IllegalPutException, InvalidAttributesException; 
	
	/**
	 * Removes the given OnSquare from this OnSquareContainer.
	 * @param on the OnSquare to be removed from this OnSquareContainer.
	 * @throws IllegalOnSquareException
	 */
	public abstract void removeOnSquare(OnSquare on) throws IllegalOnSquareException;
	
	/**
	 * Checks if the given OnSquare can be added to this OnSquareContainer.
	 * @param on the OnSquare to be checked for compatibility with this OnSquareContainer.
	 * @return	true if the given OnSquare can be added to this OnSquareContainer.
	 * 			false if the given OnSquare can not be added to this OnSquareContainer.
	 */
	public abstract boolean canAdd(OnSquare on);
}
