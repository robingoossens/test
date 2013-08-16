package linker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.naming.directory.InvalidAttributesException;

import square.OnSquare;
import square.OnSquareType;
import square.Teleporter;

public class RandomTeleportersLinkPolicy extends LinkingPolicy{

	@Override
	public void link(ArrayList<OnSquare> onsquares)
			throws InvalidAttributesException {
		ArrayList<Teleporter> teleporters = getTeleporters(onsquares);
		Teleporter destination;
		Random random = new Random();
		for(Teleporter t: teleporters){
			do{
				destination = teleporters.get(random.nextInt(teleporters.size()));
			} while(t.equals(destination));
			try {
				t.setDestination(destination);
			} catch (InvalidAttributesException e) {
				throw new Error();
			}
		}
	}

	private ArrayList<Teleporter> getTeleporters(ArrayList<OnSquare> onsquares) {
		ArrayList<Teleporter> list = new ArrayList<Teleporter>();
		for(OnSquare on:onsquares)
			if(on.getType()==OnSquareType.TELEPORTER)
				list.add((Teleporter) on);
		return list;
	}

}
