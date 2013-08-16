package game;

import exceptions.DimensionException;
import grid.Grid;
import grid.OnSquareGenerator;
import grid.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

import placer.EqualDistanceFromPlayerPlacing;
import placer.OnSquareNearPlayerPolicy;
import placer.PlaceInSquarePolicy;
import placer.Placer;
import placer.PlacingPolicy;
import placer.PlayerAndFlagOnStartPosPlacingPolicy;
import placer.PlayersOnStartPosPlacingPolicy;
import placer.RandomPlacementPolicy;
import placer.WallRandomPlacementPolicy;

import linker.FlagsToCTFPolicy;
import linker.FlagsToStartposLinkPolicy;
import linker.GoalLinkPolicy;
import linker.Linker;
import linker.LinkingPolicy;
import linker.RandomTeleportersLinkPolicy;
import linker.StartingpositionToPlayerLinkPolicy;

import square.EffectType;
import square.GeneratePowerfailurePolicy;
import square.GoalType;
import square.OnSquare;
import square.OnSquareType;
import square.Square;
import square.UpdateOnSquarePolicy;
import square.UpdatePolicy;

public class StandardGameCreator extends GameCreator{
	public static void main(String[] moo){
		StandardGameCreator c = new StandardGameCreator();
		try {
			String[] k = new String[2];
			k[0]="e";
			k[1]="b";
//			k[2]="e2";
//			k[3]="b2";
			Grid grid = new Grid(10,10);
			Game game = c.createGame(GameMode.RACE, k, grid);
			
			int count;
			OnSquareType temp;
			ArrayList<OnSquare> onsquares = new ArrayList<OnSquare>();

			for(OnSquareType type:game.getNumberRules().keySet()){
				count = game.getNumberRules().get(type);
				for(int i=0;i<count;i++){
					onsquares.add(OnSquareGenerator.getInstance().makeOnSquare(type, game.getEffectTypes().get(type),game.getCcs()));
				}
			}
			ArrayList<PlacingPolicy> updatePolicies = game.getPlacingPolicies();
			System.out.println(updatePolicies.size());
			for(PlacingPolicy p:updatePolicies)
				System.out.println(p);

			Linker.getInstance().link(onsquares, game.getLinkingPolicies());
			Linker.getInstance().link(onsquares, game.getGoalTypes(), game.getGoalLinks());
			Placer.getInstance().place(grid, onsquares, game.getPlacingPolicies());
//			for(Square s:grid.getSquares()){
//				System.out.println(s.getPosition());
//				for(OnSquare on:s.showOnSquares())
//					System.out.println(on.getType());
//			}
//			for(OnSquare on:onsquares){
//				if(on.getType()==OnSquareType.STARTINGPOSITION){
//
////					System.out.println(on.getType());
////					System.out.println(on+" with spos: "+((StartingPosition)on).getPlayer());
//				}
//				else if(on.getType() == OnSquareType.PLAYER){
//					System.out.println(on);
//					for(Goal g:((Player)on).getGoals()){
//						if(g.getType()==GoalType.CAPTUREFLAG){
//							System.out.println(on+" hastocatch "+((CaptureFlag)g).getFlag().getStartingPos().getPlayer()+"'s flag");
//						}
//						else
//							System.out.println(g);
//
//							
//					}
//						
//				}
//				else if(on.getType() == OnSquareType.FLAG){
//					System.out.println(on.getType());
//
//					System.out.println(on+" with spos: "+((Flag)on).getStartingPos());
//					System.out.println();
				
			

//			for(OnSquare on:onsquares){
//				System.out.println(on.getType());
//				for(Effect f:on.getEffects())
//					System.out.println(f.getType());
//				System.out.println();
//			}
//			ArrayList<CreationConstraint> s = game.getCcs();
//			for(CreationConstraint e:s)
//				System.out.println(e);
////			
//			HashMap<OnSquareType, Integer> o = game.getNumberRules();
//			for(OnSquareType on:o.keySet()){
//				System.out.println(on);
//				System.out.println(o.get(on));
//				System.out.println();
//			}
//			

//			HashMap<OnSquareType, HashSet<EffectType>> effectTypes = game.getEffectTypes();
////			for(OnSquareType on:effectTypes.keySet())
////				System.out.println(on);
////			System.out.println();
//			for(OnSquareType on:effectTypes.keySet()){
//				System.out.println(on);
//				for(EffectType t:effectTypes.get(on))
//					System.out.println(t);
//				System.out.println();
//			}
			
//			HashMap<Integer, HashMap<GoalType, Integer>> goalTypes = game.getGoalTypes();
//			for(Integer i:goalTypes.keySet())
//				System.out.println(i);
//			System.out.println();
//			for(Integer i:goalTypes.keySet())
//				for(GoalType t:goalTypes.get(i).keySet())
//				System.out.println(t);
//			System.out.println();
//			for(Integer i:goalTypes.keySet())
//				for(GoalType t:goalTypes.get(i).keySet())
//				System.out.println(goalTypes.get(i).get(t));
		} catch (InvalidAttributesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DimensionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public Game createGame(GameMode mode, String[] playerNames, Grid grid) throws InvalidAttributesException {
		if(mode==null || playerNames==null||(mode == GameMode.CTF && playerNames.length>4) || 
				(mode == GameMode.RACE && playerNames.length>2))
			throw new InvalidAttributesException();
		HashMap<OnSquareType, HashSet<EffectType>> effectTypes = getStandardEffectTypes();
		if(mode==GameMode.CTF)
			addCTF(effectTypes);
		ArrayList<UpdatePolicy> updatePolicies = makeUpdatePolicies();
		HashMap<Integer, HashMap<GoalType, Integer>> goalTypes = makeGoalTypes(mode,playerNames.length);
		ArrayList<GoalLinkPolicy> goalLinks = makeGoalLinks(mode);
		ArrayList<LinkingPolicy> links = makeLinkingPolicies(mode);
		HashMap<OnSquareType, Integer> numberRules = makeNumberRules(mode,grid,playerNames.length);
		grid.setUpdatePolicies(updatePolicies);
		ArrayList<PlacingPolicy> placingPolicies = makePP(mode,grid, playerNames.length,numberRules);
		ArrayList<CreationConstraint> ccs = makeCcs(mode, grid, playerNames);
		return new Game(mode, 4, goalTypes, effectTypes, updatePolicies, links, goalLinks, numberRules,
				placingPolicies,ccs);
	}
	
	private ArrayList<CreationConstraint> makeCcs(GameMode mode, Grid grid, String[] playerNames) {
		ArrayList<CreationConstraint> ccs = new ArrayList<CreationConstraint>();
		ccs.add(new WallCreationConstraint(grid.getDimension().getX(),grid.getDimension().getY(),0.05));
		ccs.add(new PlayerCreationConstraint(playerNames,6));
		return ccs;
	}

	private ArrayList<Position> getStartpos(Grid g){
		ArrayList<Position> sp = new ArrayList<Position>();
		sp.add(new Position(0,0));
		sp.add(new Position(g.getDimension().getX()-1,g.getDimension().getY()-1));
		sp.add(new Position(0,g.getDimension().getY()-1));
		sp.add(new Position(g.getDimension().getX()-1,0));
		return sp;
	}
	private ArrayList<PlacingPolicy> makePP(GameMode mode, Grid g, int np, HashMap<OnSquareType, Integer> numberRules) {
		ArrayList<PlacingPolicy> pp = new ArrayList<PlacingPolicy>();
		ArrayList<Position> sp = getStartpos(g);
		if(mode == GameMode.CTF){
			for(int i=0;i<np;i++)
				pp.add(new PlayerAndFlagOnStartPosPlacingPolicy(sp.get(i),i+1));
		}
		else{
			for(int i=0;i<np;i++)
				pp.add(new PlayersOnStartPosPlacingPolicy(sp.get(i),i+1));
		}
//		pp.add(new WallRandomPlacementPolicy(numberRules.get(OnSquareType.WALL)));
//		pp.add(new EqualDistanceFromPlayerPlacing(OnSquareType.CHARGEDIDENTITYDISK,1));
//		pp.add(new RandomPlacementPolicy(OnSquareType.TELEPORTER));
		int i=0;
		for(Position p:sp){
			while(i<np--){
				pp.add(new OnSquareNearPlayerPolicy(p,7,1,OnSquareType.IDENTITYDISK));
				pp.add(new OnSquareNearPlayerPolicy(p,5,1,OnSquareType.LIGHTGRENADE));
			}
		}
//		pp.add(new PlaceInSquarePolicy());
		return pp;
	}

	private HashMap<OnSquareType, Integer> makeNumberRules(GameMode mode, Grid grid, int nPlayers) {
		HashMap<OnSquareType, Integer> numberRules = new HashMap<OnSquareType, Integer>();
		numberRules.put(OnSquareType.WALL, (new PercentageOfGridRule(0.2)).getNumber(grid, nPlayers)/2);
		numberRules.put(OnSquareType.CHARGEDIDENTITYDISK, new SetNumberRule(1).getNumber(grid, nPlayers));
		numberRules.put(OnSquareType.FORCEFIELDGENERATOR, new PercentageOfGridRule(0.07).getNumber(grid, nPlayers));
		numberRules.put(OnSquareType.IDENTITYDISK, new PercentageOfGridRule(0.02).getNumber(grid, nPlayers));
		numberRules.put(OnSquareType.TELEPORTER, new PercentageOfGridRule(0.03).getNumber(grid, nPlayers));
		numberRules.put(OnSquareType.LIGHTGRENADE, new PercentageOfGridRule(0.02).getNumber(grid, nPlayers));
		numberRules.put(OnSquareType.PLAYER, new EqToNPlayers().getNumber(grid, nPlayers));
		numberRules.put(OnSquareType.STARTINGPOSITION, new EqToNPlayers().getNumber(grid, nPlayers));
		if(mode==GameMode.CTF)
			numberRules.put(OnSquareType.FLAG, new EqToNPlayers().getNumber(grid, nPlayers));
		return numberRules;
	}

	private ArrayList<GoalLinkPolicy> makeGoalLinks(GameMode mode){
		ArrayList<GoalLinkPolicy> out = new ArrayList<GoalLinkPolicy>();
		if(mode == GameMode.CTF){
			out.add(new FlagsToCTFPolicy());
		}
		return out;
	}
	private ArrayList<LinkingPolicy> makeLinkingPolicies(GameMode mode){
		ArrayList<LinkingPolicy> out = new ArrayList<LinkingPolicy>();
		out.add(new RandomTeleportersLinkPolicy());
		out.add(new StartingpositionToPlayerLinkPolicy());
		if(mode == GameMode.CTF)
			out.add(new FlagsToStartposLinkPolicy());
		return out;
	}
	
	private HashMap<Integer, HashMap<GoalType, Integer>> makeGoalTypes(GameMode mode, int n) {
		HashMap<Integer, HashMap<GoalType, Integer>> out = new HashMap<Integer, HashMap<GoalType, Integer>>();
		switch (mode) {
			case CTF: return makeGoalsCTF(n);
			case RACE: return makeGoalsRace(n);
			default: return null;
		}
	}

	private HashMap<Integer, HashMap<GoalType, Integer>> makeGoalsRace(int n) {
		HashMap<Integer, HashMap<GoalType, Integer>> out = new HashMap<Integer, HashMap<GoalType, Integer>>();
		HashMap<GoalType, Integer> temp;
		for(int i=1;i<n+1;i++){
			temp = new HashMap<GoalType, Integer>();
			temp.put(GoalType.REACHSQUARE, 1);
			out.put(i, temp);
		}
		return out;
	}

	private HashMap<Integer, HashMap<GoalType, Integer>> makeGoalsCTF(int n) {
		HashMap<Integer, HashMap<GoalType, Integer>> out = new HashMap<Integer, HashMap<GoalType, Integer>>();
		HashMap<GoalType, Integer> temp;
		for(int i=1;i<n+1;i++){
			temp = new HashMap<GoalType, Integer>();
			temp.put(GoalType.CAPTUREFLAG, n-1);
			out.put(i, temp);
		}
		return out;
	}

	protected void addToEffectTypes(HashMap<OnSquareType, HashSet<EffectType>> effectTypes, OnSquareType 
			oType, EffectType eType){
		if(oType!=null && eType!=null && (!effectTypes.keySet().contains(oType) || !effectTypes.get(oType).contains(eType)))
			effectTypes.get(oType).add(eType);
	}
	
	private void addCTF(HashMap<OnSquareType, HashSet<EffectType>> effectTypes) {
		addToEffectTypes(effectTypes, OnSquareType.CHARGEDIDENTITYDISK, EffectType.DROPFLAGEFFECT);
		addToEffectTypes(effectTypes, OnSquareType.IDENTITYDISK, EffectType.DROPFLAGEFFECT);
		addToEffectTypes(effectTypes, OnSquareType.TELEPORTER, EffectType.DROPFLAGEFFECT);
		addToEffectTypes(effectTypes, OnSquareType.LIGHTGRENADE, EffectType.DROPFLAGEFFECT);
	}

	private ArrayList<UpdatePolicy> makeUpdatePolicies() {
		 ArrayList<UpdatePolicy> out = new  ArrayList<UpdatePolicy>();
		 out.add(new UpdateOnSquarePolicy());
		 out.add(new GeneratePowerfailurePolicy(0.01, getPowerfailureTypes()));
		 return out;
	}

	

}
