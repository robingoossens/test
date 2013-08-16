package grid;

import exceptions.IllegalDirectionException;
import exceptions.IllegalSizeException;
import game.CreationConstraint;

import java.util.ArrayList;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

import square.ChargedIdentityDisk;
import square.Effect;
import square.EffectType;
import square.Flag;
import square.ForcefieldGenerator;
import square.IdentityDisk;
import square.LightGrenade;
import square.LightTrail;
import square.OnSquare;
import square.OnSquareType;
import square.Player;
import square.PowerFailure;
import square.PowerFailurePrim;
import square.PowerFailureSec;
import square.StartingPosition;
import square.Teleporter;
import square.Wall;

public class OnSquareGenerator {
	private static OnSquareGenerator instance = null;
	
	private OnSquareGenerator(){}
	
	public static OnSquareGenerator getInstance(){
		if(instance==null)
			instance = new OnSquareGenerator();
		return instance;
	}
	
	public OnSquare makeOnSquare(OnSquareType type, HashSet<EffectType> effectTypes, ArrayList<CreationConstraint> ccs){
		if(ccs!=null)
			for(CreationConstraint c:ccs)
				if(type == c.getType())
					return c.getOnSquare(effectTypes);
		switch (type) {
			case PLAYER: return makePlayer(effectTypes);
			case POWERFAILUREPRIM: return makePowerFailure(type,effectTypes);
			case LIGHTGRENADE: return makeLightGrenade(effectTypes);
			case CHARGEDIDENTITYDISK: return makeChargedID(effectTypes);
			case FLAG: return makeFlag(effectTypes);
			case FORCEFIELDGENERATOR: return makeFfGen(effectTypes);
			case IDENTITYDISK: return makeIdentityDisk(effectTypes);
			case STARTINGPOSITION: return makeStartingPosition(effectTypes);
			case TELEPORTER: return makeTeleporter(effectTypes);
			case WALL: return makeWall(effectTypes);
			default: return null;
		}
	}
	
	private ArrayList<Effect> makeEffects(HashSet<EffectType> effectTypes){
		 ArrayList<Effect> out = new  ArrayList<Effect>();
		for(EffectType et:effectTypes)
			out.add(EffectGenerator.getInstance().makeEffect(et));
		return out;
			
	}
	
	private OnSquare makeWall(HashSet<EffectType> effectTypes) {
		Wall w=null;
		try {
			w = new Wall(Direction.NORTH,2, makeEffects(effectTypes));
		} catch (IllegalSizeException | IllegalDirectionException | InvalidAttributesException e) {
			e.printStackTrace();
		}
		return w;
	}

	private OnSquare makeTeleporter(HashSet<EffectType> effectTypes) {
		Teleporter t=null;
		try {
			t = new Teleporter(makeEffects(effectTypes));
		} catch (InvalidAttributesException e) {
			e.printStackTrace();
		}
		return t;
	}

	private OnSquare makeStartingPosition(HashSet<EffectType> effectTypes) {
		StartingPosition p=null;
		try {
			p = new StartingPosition(makeEffects(effectTypes));
		} catch (InvalidAttributesException e) {
			e.printStackTrace();
		}
		return p;
	}

	private OnSquare makeIdentityDisk(HashSet<EffectType> effectTypes) {
		IdentityDisk id = null;
		try {
			id = new IdentityDisk(makeEffects(effectTypes));
		} catch (InvalidAttributesException e) {
			e.printStackTrace();
		}
		return id;
	}

	private OnSquare makeFfGen(HashSet<EffectType> effectTypes) {
		ForcefieldGenerator fg = null;
		try {
			fg = new ForcefieldGenerator(makeEffects(effectTypes));
		} catch (InvalidAttributesException e) {
			e.printStackTrace();
		}
		return fg;
	}

	private OnSquare makeFlag(HashSet<EffectType> effectTypes) {
		Flag fg = null;
		try {
			fg = new Flag(makeEffects(effectTypes));
		} catch (InvalidAttributesException e) {
			e.printStackTrace();
		}
		return fg;
	}

	private OnSquare makeChargedID(HashSet<EffectType> effectTypes) {
		ChargedIdentityDisk id = null;
		try {
			id = new ChargedIdentityDisk(makeEffects(effectTypes));
		} catch (InvalidAttributesException e) {
			e.printStackTrace();
		}
		return id;
	}

	private OnSquare makeLightGrenade(HashSet<EffectType> effectTypes) {
		LightGrenade l = null;
		try {
			l = new LightGrenade(makeEffects(effectTypes));
		} catch (InvalidAttributesException e) {
			e.printStackTrace();
		}
		return l;
	}

	private OnSquare makePowerFailure(OnSquareType type,
			HashSet<EffectType> effectTypes) {
		PowerFailure fail=null;
		try {
			fail = new PowerFailurePrim(makeEffects(effectTypes));
			} catch (InvalidAttributesException e) {
				e.printStackTrace();
			}
		return fail;
	}

	private OnSquare makePlayer(HashSet<EffectType> effectTypes) {
		Player p=null;
		try {
			p= new Player("Monster",10000,666,makeEffects(effectTypes));
		} catch (InvalidAttributesException e) {
			e.printStackTrace();
		}
		return p;
	}
}
