package square;

import game.Update;

import java.util.HashSet;

public abstract class UpdatePolicy {
	public abstract void update(Update update, HashSet<Square> squares);
}
