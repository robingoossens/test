package exceptions;

/**
 * IllegalActionException is thrown when an Action is done that
 * can not be performed.
 */
public class IllegalActionException extends Exception{
	public IllegalActionException(String str){
		super(str);
	}
}
