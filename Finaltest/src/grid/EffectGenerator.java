package grid;

import java.util.ArrayList;

import rules.EffectRule;
import square.CursePlayerEffect;
import square.DecrementDiskRangeEffect;
import square.DiskEffect;
import square.DropFlagEffect;
import square.Effect;
import square.EffectType;
import square.ExplosionEffect;
import square.IdentityDiskDestroyEffect;
import square.LoseActionEffectOnPlayer;
import square.LoseTurnEffectOnPlayer;
import square.OnSquare;
import square.PlayerEffect;
import square.StartingPositionEffect;
import square.TeleportIdentityDiskEffect;
import square.TeleportPlayerEffect;

public class EffectGenerator {
private static EffectGenerator eg = null;
	
	private EffectGenerator(){}
	
	public static EffectGenerator getInstance(){
		if(eg==null)
			eg = new EffectGenerator();
		return eg;
	}
	
	public Effect makeEffect(EffectType type){
		switch (type) {
			case DECREMENTDISKRANGEEFFECT: return new DecrementDiskRangeEffect();
			case DISKEFFECT: return new DiskEffect();
			case DROPFLAGEFFECT: return new DropFlagEffect();
			case EXPLOSIONEFFECT: return new ExplosionEffect();
			case CURSEPLAYEREFFECT: return new CursePlayerEffect();
			case IDENTITYDISKDESTROYEFFECT: return new IdentityDiskDestroyEffect();
			case LOSEACTIONEFFECTONPLAYER: return new LoseActionEffectOnPlayer();
			case LOSETURNEFFECTONPLAYER: return new LoseTurnEffectOnPlayer();
			case PLAYEREFFECT: return new PlayerEffect();
			case STARTINGPOSITIONEFFECT: return new StartingPositionEffect();
			case TELEPORTIDENTITYDISKEFFECT: return new TeleportIdentityDiskEffect();
			case TELEPORTPLAYEREFFECT: return new TeleportPlayerEffect();
			default: return null;
		}
	}
}
