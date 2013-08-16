package game;



import grid.Dimension;
import grid.EffectGenerator;
import square.GoalType;
import grid.Grid;
import grid.GridBuilder;
import grid.PlacingRules;
import grid.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

import placer.PlacingPolicy;

import linker.GoalLinkPolicy;
import linker.LinkingPolicy;

import rules.ChargedIdentityDiskEffectRule;
import rules.EffectRule;
import rules.ForcefieldEffectRule;
import rules.IdentityDiskEffectRule;
import rules.LightGrenadeEffectRule;
import rules.PlayerEffectRule;
import rules.PowerfailureEffectRule;
import rules.TeleporterEffectRule;
import square.EffectType;
import square.OnSquare;
import square.OnSquareType;
import square.Player;
import square.TurnManager;
import square.UpdatePolicy;

import exceptions.DimensionException;

/**
 * This is a game. A game has a grid, a maximum number of actions that can be performed, and has a turn queue 
 * to regulates the progress of the game.
 */
public class Game {
	private HashMap<Integer, HashMap<GoalType, Integer>> goalTypes = new HashMap<Integer, HashMap<GoalType, Integer>>();
	private HashMap<OnSquareType, HashSet<EffectType>> effectTypes = new HashMap<OnSquareType, HashSet<EffectType>>();
	private ArrayList<UpdatePolicy> updatePolicies = new ArrayList<UpdatePolicy>();	
	private ArrayList<LinkingPolicy> linkingPolicies = new ArrayList<LinkingPolicy>();
	private ArrayList<GoalLinkPolicy> goalLinks = new ArrayList<GoalLinkPolicy>();
	private HashMap<OnSquareType, Integer> numberRules = new HashMap<OnSquareType,Integer>();
	private ArrayList<CreationConstraint> ccs = new ArrayList<CreationConstraint>();
	private int totalNumberOfActions;
	private GameMode mode;
	private ArrayList<PlacingPolicy> placingPolicies = new ArrayList<PlacingPolicy>();
	
	public HashMap<OnSquareType, Integer> getNumberRules(){
		HashMap<OnSquareType, Integer> out = new HashMap<OnSquareType, Integer>();
		for(OnSquareType o:numberRules.keySet())
			out.put(o, numberRules.get(o));
		return out;
	}
	
	protected void setNumberRules(HashMap<OnSquareType, Integer> numberRules){
		for(OnSquareType o:numberRules.keySet())
			this.numberRules.put(o, numberRules.get(o));
	}
	
	public Game(GameMode mode, int totalNumberOfActions, HashMap<Integer, HashMap<GoalType, Integer>> goalTypes,
			HashMap<OnSquareType, HashSet<EffectType>> effectTypes, ArrayList<UpdatePolicy> updatePolicies,
			ArrayList<LinkingPolicy> linkingPolicies, ArrayList<GoalLinkPolicy> goalLinks,
			HashMap<OnSquareType, Integer> numberRules, ArrayList<PlacingPolicy> placingPolicies,
			ArrayList<CreationConstraint> ccs){
		setMode(mode);
		setNumberOfActions(totalNumberOfActions);
		setGoalTypes(goalTypes);
		setEffectTypes(effectTypes);
		setUpdatePolicies(updatePolicies);
		setLinkingPolicies(linkingPolicies);
		setGoalLinks(goalLinks);
		setNumberRules(numberRules);
		setPlacingPolicies(placingPolicies);
		setCcs(ccs);
	}
	
	private void setNumberOfActions(int totalNumberOfActions) {
		this.totalNumberOfActions = totalNumberOfActions;
		
	}


	public void setMode(GameMode mode) {
		this.mode = mode;
	}

	public ArrayList<UpdatePolicy> getUpdatePolicies(){
		ArrayList<UpdatePolicy> updatePolicies = new ArrayList<UpdatePolicy>();
		for(UpdatePolicy p:this.updatePolicies)
			updatePolicies.add(p);
		return updatePolicies;
	}
	
	protected void setUpdatePolicies(ArrayList<UpdatePolicy> policies){
		for(UpdatePolicy p:policies)
			this.updatePolicies.add(p);
	}
	
	
	

	public ArrayList<GoalLinkPolicy> getGoalLinks() {
		ArrayList<GoalLinkPolicy> out = new ArrayList<GoalLinkPolicy>();
		for(GoalLinkPolicy p:goalLinks)
			out.add(p);
		return out;
	}

	public void setGoalLinks(ArrayList<GoalLinkPolicy> goalLinks) {
		for(GoalLinkPolicy p:goalLinks)
			this.goalLinks.add(p);
	}
	
	public ArrayList<LinkingPolicy> getLinkingPolicies() {
		ArrayList<LinkingPolicy> out = new ArrayList<LinkingPolicy>();
		for(LinkingPolicy p:linkingPolicies)
			out.add(p);
		return out;
	}
	
	protected void setLinkingPolicies(ArrayList<LinkingPolicy> linkingPolicies) {
		for(LinkingPolicy p:linkingPolicies)
			this.linkingPolicies.add(p);
	}
	
	
	
	public HashMap<OnSquareType, HashSet<EffectType>> getEffectTypes(){
		HashMap<OnSquareType, HashSet<EffectType>> out = new HashMap<OnSquareType, HashSet<EffectType>>();
		HashSet<EffectType> temp;
		for(OnSquareType o:effectTypes.keySet()){
			temp = new HashSet<EffectType>();
			for(EffectType et:effectTypes.get(o))
				temp.add(et);
			out.put(o, temp);
		}
		return out;		
	}
	
	public ArrayList<PlacingPolicy> getPlacingPolicies() {
		ArrayList<PlacingPolicy> out = new ArrayList<PlacingPolicy>();
		for(PlacingPolicy p:placingPolicies)
			out.add(p);
		return out;
	}
	
	protected void setPlacingPolicies(ArrayList<PlacingPolicy> placingPolicies) {
		for(PlacingPolicy p:placingPolicies)
			this.placingPolicies.add(p);
	}
	
	protected void setEffectTypes(HashMap<OnSquareType, HashSet<EffectType>> effectTypes){
		HashSet<EffectType> temp;
		for(OnSquareType o:effectTypes.keySet()){
			temp = new HashSet<EffectType>();
			for(EffectType et:effectTypes.get(o))
				temp.add(et);
			this.effectTypes.put(o, temp);
		}	
	}
	
	
	
	public GameMode getMode(){
		return mode;
	}
	
	
	public int getTotalNumberOfActions(){
		return totalNumberOfActions;
	}
	
	protected ArrayList<EffectRule> getEffectRules() {
		ArrayList<EffectRule> ers = new ArrayList<EffectRule>();
		ers.add(new PowerfailureEffectRule());
		ers.add(new LightGrenadeEffectRule());
		ers.add(new ChargedIdentityDiskEffectRule());
		ers.add(new IdentityDiskEffectRule());
		ers.add(new ForcefieldEffectRule());
		ers.add(new PlayerEffectRule());
		ers.add(new TeleporterEffectRule());
		return ers;
	}
	
	public HashMap<Integer, HashMap<GoalType, Integer>> getGoalTypes() {
		HashMap<Integer, HashMap<GoalType, Integer>> out = new HashMap<Integer, HashMap<GoalType, Integer>>();
		HashMap<GoalType,Integer> temp;
		HashMap<GoalType,Integer> deeptemp;
		for(Integer i:this.goalTypes.keySet()){
			temp = goalTypes.get(i);
			deeptemp = new HashMap<GoalType,Integer>();
			for(GoalType t:temp.keySet())
				deeptemp.put(t, temp.get(t));
			out.put(i, deeptemp);
		}
		return out;
	}
	
	public void setGoalTypes(HashMap<Integer, HashMap<GoalType, Integer>> goalTypes){
		HashMap<GoalType,Integer> temp;
		HashMap<GoalType,Integer> deeptemp;
		for(Integer i:goalTypes.keySet()){
			temp = goalTypes.get(i);
			deeptemp = new HashMap<GoalType,Integer>();
			for(GoalType t:temp.keySet())
				deeptemp.put(t, temp.get(t));
			this.goalTypes.put(i, deeptemp);
		}
	}

	public ArrayList<CreationConstraint> getCcs() {
		ArrayList<CreationConstraint> ccs = new ArrayList<CreationConstraint>();
		for(CreationConstraint c:this.ccs)
			ccs.add(c);
		return ccs;
	}

	public void setCcs(ArrayList<CreationConstraint> ccs) {
		for(CreationConstraint c:ccs)
			this.ccs.add(c);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	private Grid grid;
//	private boolean started;
//	private boolean stopped;
//	private final int totalNumberOfActions;
//	private TurnManager turnManager;
//	private Player winner;
//	private GameMode gameMode;
//	/**
//	 * Creates a new Game.
//	 * 
//	 * @param playernames
//	 * @param x
//	 * @param y
//	 * @param totalActions
//	 * @param gamemode
//	 * @throws DimensionException: Thrown when x<10 or y<10.
//	 * @throws InvalidAttributesException: Thrown when playernames==null, totalActions<=0, gamemode==null or 
//	 * !gamemode.isValidNumberOfPlayers(playernames.length).
//	 */
//	public Game(String[] playernames, int x, int y, int totalActions, GameMode gamemode) throws DimensionException, InvalidAttributesException, IllegalPlayerNumberException{
//		if(playernames==null || totalActions<=0 || gamemode==null)
//			throw new InvalidAttributesException();
//		else{
//			totalNumberOfActions = totalActions;
//			grid = new Grid(x,y,gamemode);
//			this.gameMode = gamemode;
//			GridBuilder builder = new GridBuilder(gamemode.getEffectGenerator(), gamemode.getPlacingRules());
//			ArrayList<Player> players = builder.makePlayers(playernames);
//			turnManager = new TurnManager(players);
//			builder.build(grid, players);
//			for (int i = 0; i < players.size(); i++){
//				players.get(i).startTrailing();
//			}
//		}
//	}	
//	
//	/**
//	 * Returns the game's GameMode.
//	 */
//	public GameMode getGameMode(){
//		return gameMode;
//	}
//	
//	/**
//	 * Get the Grid on which is played in this Game.
//	 */
//	public Grid getGrid(){
//		return grid;
//	}
//	
//	/**
//	 * Returns the player whose turn it is.
//	 * @return
//	 */
//	public Player getPlayerOnTurn(){
//		return (Player)turnManager.getPlayerOnTurn();
//	}
//	
//	/**
//	 * Get the Winner of this Game. If there is no winner yet, null is returned.
//	 */
//	public Player getWinner(){
//		return winner;
//	}
//
//
//	/**
//	 * Check if this Game is started.
//	 */
//	public boolean isStarted(){
//		return started;
//	}
//	
//	/**
//	 * Check if this Game is over (is stopped).
//	 */
//	public boolean isOver(){
//		return stopped;
//	}
//	
//	/**
//	 * Start this Game by updating the grid with a START Update and granting a turn to the first player in the 
//	 * turnqueue.
//	 * @post isStarted()==true
//	 */
//	public void start(){
//		grid.updateGrid(Update.START);
//		try {
//			turnManager.grantNewTurn(totalNumberOfActions,true);
//		} catch (InvalidAttributesException e) {
//			e.printStackTrace();
//		}
//		started = true;
//	}
//
//	/**
//	 * Stop this Game.
//	 */
//	public void stop(){
//		stopped = true;
//	}
//	
//	/**
//	 * Update this Game. If a Player has reached his Goal or there is only one player left, stop the Game.
//	 * Otherwise if the current Player does not have any Actions left, update the grid to the new Turn and generate
//	 * random powerfailures on the grid. Next grant the next player a new turn. If the player still had actions
//	 * left, update the grid for a new Action.
//	 * Finish by checking if the updating has caused one of the player to win the game, for instance if the others
//	 * have been destroyed by a forcefield while the game was updated.
//	 */
//	public void updateGame(){
//		if(turnManager.getPlayerOnTurn().reachedGoal()){
//			stop();
//			winner = turnManager.getPlayerOnTurn();
//		} 
//		else{
//			if(turnManager.getPlayerOnTurn().getNumberOfActionsLeft()<=0){
//				grid.updateGrid(Update.TURN);
//				try{
//					if(!turnManager.getPlayerOnTurn().hasMoved())
//						turnManager.grantNewTurn(totalNumberOfActions, false);
//					else
//						turnManager.grantNewTurn(totalNumberOfActions, true);
//					} catch (InvalidAttributesException e) {
//						e.printStackTrace();
//					}
//				
//			} else{
//				grid.updateGrid(Update.ACTION);
//				if(turnManager.getPlayerOnTurn().isDestroyed())
//					try{
//							turnManager.grantNewTurn(totalNumberOfActions, false);
//						} catch (InvalidAttributesException e) {
//							e.printStackTrace();
//						}
//				else
//					turnManager.removeDestroyedPlayers();
//			}
//			if(turnManager.getNumberOfPlayers()<=1){
//				stop();
//				if(turnManager.getNumberOfPlayers()==1)
//					winner = turnManager.getPlayerOnTurn();
//			} 
//		}
//				
//	}
}
