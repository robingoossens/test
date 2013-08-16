package linker;

import java.util.ArrayList;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

import square.OnSquare;

public abstract class LinkingPolicy {

	public abstract void link(ArrayList<OnSquare> onsquares) throws InvalidAttributesException;

}
