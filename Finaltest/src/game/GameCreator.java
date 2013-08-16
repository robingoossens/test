package game;

import grid.Grid;

import java.util.HashMap;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

import square.EffectType;
import square.OnSquareType;

public abstract class GameCreator {
	public abstract Game createGame(GameMode mode, String[] noOfPlayers, Grid grid) throws InvalidAttributesException;
	
	protected HashMap<OnSquareType, HashSet<EffectType>> getStandardEffectTypes() {
		HashMap<OnSquareType, HashSet<EffectType>> out = new HashMap<OnSquareType, HashSet<EffectType>>();
		out.put(OnSquareType.CHARGEDIDENTITYDISK, getDiskTypes());
		out.put(OnSquareType.IDENTITYDISK, getDiskTypes());
		out.put(OnSquareType.FLAG, new HashSet<EffectType>());
		out.put(OnSquareType.WALL, new HashSet<EffectType>());
		out.put(OnSquareType.FORCEFIELD, getFfTypes());
		out.put(OnSquareType.FORCEFIELDGENERATOR, getFfTypes());
		out.put(OnSquareType.LIGHTGRENADE, getLGTypes());
		out.put(OnSquareType.LIGHTTRAIL, new HashSet<EffectType>());
		out.put(OnSquareType.PLAYER, getPlayerTypes());
		out.put(OnSquareType.POWERFAILUREPRIM, getPowerfailureTypes());
		out.put(OnSquareType.POWERFAILURESEC, getPowerfailureTypes());
		out.put(OnSquareType.POWERFAILURETERT, getPowerfailureTypes());
		out.put(OnSquareType.STARTINGPOSITION, getStartposTypes());
		out.put(OnSquareType.TELEPORTER, getTeleportTypes());
		return out;
	}

	protected HashSet<EffectType> getTeleportTypes() {
		HashSet<EffectType> types = new HashSet<EffectType>();
		types.add(EffectType.TELEPORTIDENTITYDISKEFFECT);
		types.add(EffectType.TELEPORTPLAYEREFFECT);
		return types;

	}

	protected HashSet<EffectType> getStartposTypes() {
		HashSet<EffectType> types = new HashSet<EffectType>();
		types.add(EffectType.STARTINGPOSITIONEFFECT);
		return types;
	}

	protected HashSet<EffectType> getPowerfailureTypes() {
		HashSet<EffectType> types = new HashSet<EffectType>();
		types.add(EffectType.LOSEACTIONEFFECTONPLAYER);
		types.add(EffectType.LOSETURNEFFECTONPLAYER);
		types.add(EffectType.DECREMENTDISKRANGEEFFECT);
		return types;
	}

	protected HashSet<EffectType> getPlayerTypes() {
		HashSet<EffectType> types = new HashSet<EffectType>();
		types.add(EffectType.PLAYEREFFECT);
		return types;
	}

	protected HashSet<EffectType> getLGTypes() {
		HashSet<EffectType> types = new HashSet<EffectType>();
		types.add(EffectType.EXPLOSIONEFFECT);
		return types;
	}

	protected HashSet<EffectType> getFfTypes() {
		HashSet<EffectType> types = new HashSet<EffectType>();
		types.add(EffectType.CURSEPLAYEREFFECT);
		types.add(EffectType.IDENTITYDISKDESTROYEFFECT);
		return types;
	}

	protected HashSet<EffectType> getDiskTypes() {
		HashSet<EffectType> types = new HashSet<EffectType>();
		types.add(EffectType.DISKEFFECT);
		return types;
	}	
}
