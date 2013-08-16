package game;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

import exceptions.DimensionException;

import rules.ChargedIdentityDiskEffectRule;
import rules.ChargedIdentityDiskEffectRuleCTF;
import rules.EffectRule;
import rules.ForcefieldEffectRule;
import rules.IdentityDiskEffectRule;
import rules.IdentityDiskEffectRuleCTF;
import rules.LightGrenadeEffectRule;
import rules.LightGrenadeEffectRuleCTF;
import rules.PlayerEffectRule;
import rules.PowerfailureEffectRule;
import rules.TeleporterEffectRule;
import rules.TeleporterEffectRuleCTF;
import square.OnSquareType;
import square.Player;
import square.TurnManager;
import square.UpdatePolicy;

import grid.Dimension;
import grid.EffectGenerator;
import grid.EffectRules;
import grid.Grid;
import grid.GridBuilder;
import grid.PlacingRules;
import grid.Position;

public enum GameMode {
	CTF, RACE;
}
