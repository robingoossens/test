package game;

import java.util.HashSet;

import square.EffectType;
import square.OnSquare;
import square.OnSquareType;

public abstract class CreationConstraint {
	private OnSquareType type;
	public abstract OnSquare getOnSquare(HashSet<EffectType> effectTypes);
	public OnSquareType getType() {
		return type;
	}
	protected void setType(OnSquareType type) {
		this.type = type;
	}
}
