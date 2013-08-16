package grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

import placer.Placer;

import linker.Linker;

import square.EffectType;
import square.GoalType;
import square.OnSquare;
import square.OnSquareType;

import exceptions.DimensionException;
import game.Game;
import game.GameMode;
import game.GamePlay;
import game.StandardGameCreator;

public class Factory {
	public GamePlay makeGamePlay(String[] playernames, int x, int y, GameMode mode) throws DimensionException{
		Grid grid = new Grid(x,y);		
		Game game = (new StandardGameCreator()).createGame(mode, playernames,grid);

		int count;
		OnSquareType temp;
		boolean possible;		
		ArrayList<OnSquare> onsquares = new ArrayList<OnSquare>();

		for(OnSquareType type:game.getNumberRules().keySet()){
			count = game.getNumberRules().get(type);
			for(int i=0;i<count;i++){
				onsquares.add(OnSquareGenerator.getInstance().makeOnSquare(type, game.getEffectTypes().get(type),game.getCcs()));
			}
		}
		
		try {
			Linker.getInstance().link(onsquares,game.getLinkingPolicies());
			Linker.getInstance().link(onsquares, game.getGoalTypes(),game.getGoalLinks());
			Placer.getInstance().place(grid, onsquares, game.getPlacingPolicies());
		} catch (InvalidAttributesException e) {
			e.printStackTrace();
		}
		
		
		HashMap<OnSquareType, HashSet<EffectType>> effectTypes = game.getEffectTypes();
		HashMap<Integer, HashMap<GoalType,Integer>> goaltypes = game.getGoalTypes();
		HashMap<OnSquareType, Integer> numberofonsq = new HashMap<OnSquareType, Integer>();
		for(OnSquareType on:numberofonsq.keySet()){
			for(int i=0;i<numberofonsq.get(on);i++){
				onsquares.add(OnSquareGenerator.getInstance().makeOnSquare(on, effectTypes.get(on));
			}
		}
		
	
		Placer.getInstance().place(grid, onsquares, game.getPlacingRules());
		
		return null;
	}
}
