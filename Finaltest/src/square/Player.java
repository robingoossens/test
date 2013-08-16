package square;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

import square.Inventory;
import square.Square;

import exceptions.IllegalActionException;
import exceptions.IllegalMoveException;
import exceptions.IllegalOnSquareException;
import exceptions.IllegalPickupException;
import exceptions.IllegalPositionException;
import exceptions.IllegalPutException;
import exceptions.IllegalUseException;
import exceptions.InventoryFullException;
import exceptions.NotCompatibleWithInventoryException;
import game.Update;
import grid.Direction;
import grid.EffectGenerator;
import grid.Position;


/**
 * A Player is an OnSquare that the user controls.
 * Players have an Inventory to store Items in, a LightTrail they leave behind 
 * and Goals they need to accomplish to win the game.
 * To play the Game Players have access to different actions: move, use, pickup and end turn.
 * Players can be affected by Curses and Effects in the Game that will alter their behaviour.
 */
public class Player extends OnSquare {
	private String name;
	private Inventory inventory;
	private LightTrail trail;
	private int actionsLeft;
	private ArrayList<Goal> goals = new ArrayList<Goal>();
	private ArrayList<TurnCurse> turnCurses = new ArrayList<TurnCurse>();
	private ArrayList<ActionCurse> actionCurses = new ArrayList<ActionCurse>();
	private int index;
	private HashMap<Action,Boolean> performables = new HashMap<Action,Boolean>();
	private boolean hasMoved;
	private int skip = 0;
	
	/**
	 * Creates a new Player.
	 * @param name the Player's name.
	 * @param capacity the capacity of the Player's Inventory.
	 * @param index the index of this Player, 1 if this Player is the first Player, 2 if he is the second, ...
	 * @throws InvalidAttributesException | name==null || capacity<1 || lightTrailTTL<0 || goal==null || index<1
	 */
	public Player(String name, int capacity, int index, ArrayList<Effect> effects) throws InvalidAttributesException{
		super(OnSquareType.PLAYER, effects);
		if(name==null  || index<1){
			setEffects(new ArrayList<Effect>());
			throw new InvalidAttributesException();
		}
		setName(name);
		inventory = new Inventory(capacity);
		this.index = index;
		performables.put(Action.MOVE, false);
		performables.put(Action.USE, false);
		performables.put(Action.PICKUP, false);
		performables.put(Action.ENDTURN, false);
	}
	
	/**
	 * A Player has no in range constraint except the default one.
	 */
	@Override
	public boolean canHaveInRange(OnSquare o, int range) {
		if(range==0){
			return isCompatibleWith(o);
		}
		return true;
	}
	
	public ArrayList<Goal> getGoals(){
		ArrayList<Goal> out = new ArrayList<Goal>();
		for(Goal g:goals)
			out.add(g);
		return out;
	}
	
	/**
	 * Returns whether this Player can perform the given Action.
	 * @param a the given Action to be checked.
	 * @return	true if this Player can perform the given Action.
	 * 			false if this Player can not perform the given Action.
	 */
	public boolean canPerformAction(Action a){
		return performables.get(a);
	}
	
	/**
	 * Returns the index of this player.
	 */
	public int getIndex(){
		return index;
	}
	
	/**
	 * Returns the Inventory of this Player.
	 */
	public Inventory getInventory(){
		return inventory;
	}
	
	/**
	 * Returns the LighTtrail of this Player.
	 */
	public LightTrail getLightTrail(){
		return trail;
	}
	
	/**
	 * Returns the name of this Player.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the number of actions this Player has left.
	 */
	public int getNumberOfActionsLeft(){
		return actionsLeft;
	}
	
	/**
	 * Returns the position of this Player.
	 */
	public Position getPosition(){
		return getSquare().getPosition();
	}
	
	/**
	 * Returns the Square this Player is on.
	 * @return |this.square.
	 */
	protected Square getSquare(){
		return (Square) getOnSquareContainer();
	}
	
	/**
	 * Returns whether this Player has moved this turn.
	 * @return	true if this Player has moved this turn.
	 * 			false if this Player has not move this turn.
	 */
	public boolean hasMoved() {
		return hasMoved;
	}
	
	/**
	 * Check if this Player is compatible with another OnSquare.
	 * @return false when the given OnSquare is a Wall, a LightTrail or another Player
	 */
	@Override
	public boolean isCompatibleWith(OnSquare on) {
		if(on instanceof Wall || on instanceof LightTrail || on instanceof Player)
			return false;
		else return true;
	}
	
	/**
	 * A Player is always visible.
	 */
	@Override
	public boolean isVisible() {
		return true;
	}
	
	/**
	 * Check if this Player has reached all his Goals.
	 */
	public boolean reachedGoal(){
		for(Goal g:goals)
			if(!g.hasBeenMet())
				return false;
		return true;
	}
	
	/**
	 * Shows the Player's Inventory.
	 * The indices of the array specify the position in the Inventory, the values of the array
	 * specify the Item at that position in the Inventory.
	 */
	public Item[] showInventory(){
		return inventory.showInventory();
	}

	/**
	 * Shows the Items that can be picked up by the Player.
	 * @return|square.getItems().
	 */
	public HashSet<Item> viewItems(){
		return getSquare().getItems();
	}
	
	/**
	 * Starts up the LightTrail for this Player.
	 */
	public void startTrailing(){
		try {
			trail = new LightTrail(getSquare());
		} catch (InvalidAttributesException e) {
			new Error();
		}
	}
	
	/**
	 * Adds a Curse to the Player's list of Curses.
	 */
	protected void addCurse(Curse curse){
		if(curse instanceof TurnCurse)
			turnCurses.add((TurnCurse)curse);
		else if(curse instanceof ActionCurse)
			actionCurses.add((ActionCurse)curse);
	}
	
	/**
	 * Adds the given Item to the Player's Inventory. 
	 * @param item the Item to add to the Player's Inventory.
	 * @throws IllegalPutException 
	 * @throws InvalidAttributesException 
	 * @throws: InventoryFullException thrown if the Player's Inventory is full.
	 * 			|inventory.getSize()==inventory.getCapacity()
	 */
	protected void addToInventory(Item item) throws InventoryFullException, InvalidAttributesException, NotCompatibleWithInventoryException{
		inventory.addItem(item);
	}
	
	/**
	 * Decrements the number of actions this Player has left by one.
	 */
	private void decrementActionsLeft(){
		actionsLeft--;
	}
	
	/**
	 * Decrements the Player's LightTrail by one.
	 */
	private void decrementLighttrail(){
		trail.decrement();
	}
	
	/**
	 * This Player tries to end his turn, so his Actions are set to 0 and his LightTrail is 
	 * decremented.
	 * @post actionsLeft = 0
	 */
	public void endTurn() throws IllegalActionException{
		if(!canPerformAction(Action.ENDTURN) && actionsLeft>0){
			setPerformable(Action.ENDTURN,true);
			throw new IllegalActionException("Are you sure you want to end your turn?");
		}
		else if(actionsLeft>0){
				executeActionCurses();
				actionsLeft = 0;
				decrementLighttrail();
				updatePerformables();
		}
	}
	
	/**
	 * Execute all the ActionCurses that affect this Player.
	 */
	private void executeActionCurses(){
		ArrayList<ActionCurse> temp = new ArrayList<ActionCurse>();
		for(ActionCurse t:actionCurses){
			t.execute(this);
			temp.add(t);			
		}
		for(ActionCurse t:temp)
			if(!t.isActive())
				actionCurses.remove(t);
	}
	
	/**
	 * Execute all the TurnCurses that affect this Player.
	 */
	private void executeTurnCurses(){
		ArrayList<TurnCurse> temp = new ArrayList<TurnCurse>();
		for(TurnCurse t:turnCurses){
			t.execute(this);
			temp.add(t);			
		}
		for(TurnCurse t:temp)
			if(!t.isActive())
				turnCurses.remove(t);
	}
	
	/**
	 * Grant this Player a Turn by giving him a new number of actions. The Effects of the Square 
	 * the Player is currently on are executed on the Player.
	 * If the Player has to skip a turn, nothing happens.
	 * @throws InvalidAttributesException thrown when actions<0.
	 */
	public void grantTurn(int actions) throws InvalidAttributesException{
		if(actions<0)
			throw new InvalidAttributesException();
		else if(skip>0)
			skip--;
		else{
			actionsLeft += actions;
			executeTurnCurses();
			if(getNumberOfActionsLeft()>0){
				setPerformables(true);
				setPerformable(Action.ENDTURN,false);
			}
			updatePerformables();
		}
	}
	
	/**
	 * Move this Player from his current Square to the Square in the given Direction. After this, the Player can perform
	 * one Action less.
	 * @throws IllegalMoveException thrown when this Player can't move in the specified Direction.
	 * @throws IllegalActionException thrown when this Player can't perform the move action.
	 * @post newSquare.getOnSquares().contains(this) == true && this.getSquare().equals(to)
	 */
	public void move(Direction d) throws IllegalMoveException, IllegalActionException{
		executeActionCurses();
		if(!canPerformAction(Action.MOVE))
			throw new IllegalActionException("This action is illegal right now!");
			else{
				int prevActions = getNumberOfActionsLeft();
				Square prev = getSquare();
				try{
					Square to = getSquare().getNeighbour(d);
					if(this.isCompatibleWith(to)){
						decrementActionsLeft();
						prev.removeOnSquare(this);
						to.putOnSquare(this);
						setHasMoved(true);
						decrementLighttrail();
						trail.link(prev,to);
						updatePerformables();
					} else
						throw new IllegalMoveException("This square is occupied.");
				}
				catch(IllegalOnSquareException exc){
					setActions(prevActions);
					try {
						prev.putOnSquare(this);
					} catch (InvalidAttributesException e) {
						e.printStackTrace();
					} catch (IllegalOnSquareException e) {
						e.printStackTrace();
					} catch (IllegalPutException e) {
						e.printStackTrace();
					}
					throw new IllegalMoveException("This position is occupied!");
				} catch (IllegalPositionException e) {
					setActions(prevActions);
					throw new IllegalMoveException("This position is an illegal direction!");
				} catch (InvalidAttributesException e1) {
				} catch (IllegalPutException e1) {
				}
			}
		}
	
	/**
	 * Pick up a given Item and add it to this Player's Inventory. After this, this Player can 
	 * perform one Action less in his current turn.
	 * @param i the Item to be picked up by this Player.
	 * @throws IllegalPickupException: is thrown when the given Item does not lay on the Square this Player is currently on.
	 * @throws InventoryFullException: is thrown when the Inventory is already full.
	 * @throws IllegalActionException 
	 */
	public void pickup(Item i) throws IllegalPickupException, InventoryFullException, IllegalActionException{
		executeActionCurses();
		if(canPerformAction(Action.PICKUP)){
			if(!getSquare().contains(i))
				throw new IllegalPickupException("This item is not available for the player to pick up!");
			else{
				try {
					getSquare().removeOnSquare(i);
					i.pickup(this);
					decrementActionsLeft();
					decrementLighttrail();
					updatePerformables();
				} catch (InvalidAttributesException e1) {
				} catch (IllegalOnSquareException e) {
				} catch (InventoryFullException e2) {
					try {
						getSquare().putOnSquare(i);
					} catch (InvalidAttributesException e3){
					} catch (IllegalOnSquareException e){
					} catch (IllegalPutException e){
					}
				} catch (NotCompatibleWithInventoryException e) {
					try {
						getSquare().putOnSquare(i);
						throw new IllegalPickupException("This item is not available for the player to pick up!");
					} catch (InvalidAttributesException e1){
						e1.printStackTrace();
					} catch (IllegalOnSquareException e2){
						e2.printStackTrace();
					} catch (IllegalPutException e3){
						e3.printStackTrace();
					}
				}
			}
		}
		else
			throw new IllegalActionException("This action is illegal right now!");
			
	}
	
	/**
	 * Puts this Player on the given OnSquareContainer.
	 * After that the Player's LightTrail will be extended.
	 * @param on the OnSquareContainer this Player is put on.
	 */
	protected void put(OnSquareContainer on) throws InvalidAttributesException, IllegalPutException, IllegalOnSquareException{
		super.put(on);
		if(trail != null){
			trail.getLast().putOnSquare(trail);
			trail.extendTrail(((Square) on));
		}
	}
	
	/**
	 * Removes the given Curse from this Player.
	 * @param curse the Curse to be remove from this Player.
	 */
	protected void removeCurse(Curse curse){
		if(curse instanceof TurnCurse){
			turnCurses.remove(curse);
		}
		else if(curse instanceof ActionCurse)
			actionCurses.remove(curse);
	}
	
	/**
	 * Removes the given Item from the Inventory of this Player.
	 * @param i the Item to be removed from the Inventory of this Player.
	 * @throws InvalidAttributesException
	 */
	protected void removeFromInventory(Item i) throws InvalidAttributesException{
		inventory.removeItem(i);
	}
		
	/**
	 * Sets the actions this Player has left to the given number n.
	 * @param n the new number of actions left for this Player.
	 */
	protected void setActions(int n){
		actionsLeft = n;
	}
	
	/**
	 * Sets the Goals of this Player to the given Goals.
	 * @param goal the Goals to be set for this Player.
	 */
	public void setGoals(ArrayList<Goal> goal){
		this.goals = goal;
	}
	
	/**
	 * Sets the hasMoved state of this Player to the given hasMoved state.
	 * @param hasMoved the given hasMoved state to be set for this Player.
	 */
	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}

	/**
	 * Sets the name of this Player to the given name.
	 * @param name the name that will be assigned to this Player.
	 */
	private void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the actions this Player can perform or not.
	 * @param a the action to be set for this Player.
	 * @param bool the performability of the action to be set for this Player.
	 */
	protected void setPerformable(Action a,boolean bool){
		performables.put(a,bool);
	}
	
	/**
	 * Sets all the actions this Player has to be performable or not.
	 * @param bool the performability of the actions to be set for this Player.
	 */
	private void setPerformables(boolean bool){
		for(Action a: performables.keySet())
			performables.put(a, bool);
	}
	
	/**
	 * This Player skips a turn.
	 */
	protected void skipTurn(){
		skip++;
	}
	
	/**
	 * Starts up the engine of the Players bike, initializing the LightTrail.
	 */
	public void startEngine(){
		try {
			trail = new LightTrail(getSquare());
		} catch (InvalidAttributesException e) {
			e.printStackTrace();
			throw new Error();
		}
	}
	
	/**
	 * This Player is triggered by the given OnSquare.
	 * If the Player is triggered by a used IdentityDisk he is activated.
	 */
	protected void trigger(OnSquare on){
		if(on instanceof IdentityDisk && ((IdentityDisk) on).isUsed())
			activate();
	}
	
	/**
	 * Updates the Goals of this Player with the given StartingPosition.
	 * @param updater the given StartingPosition with which the Player's Goals get updated.
	 */
	protected void updateGoals(StartingPosition updater){
		for(Goal goal:goals){
			goal.update(this,updater);
		}
	}
	
	/**
	 * Updates this Player with the given Update.
	 * @param update the Update to be performed for this Player.
	 */
	protected void update(Update update){
		if(update == Update.ACTION){
			updateActionCurses();
		}
		else if(update == Update.TURN || update == Update.START){
			updateActionCurses();
			updateTurnCurses();
		}
		deactivate();
	}
	
	/**
	 * Updates the ActionCurses for this Player.
	 */
	private void updateActionCurses(){
		ArrayList<ActionCurse> tempcurses = new ArrayList<ActionCurse>();			
		for(ActionCurse curse:actionCurses)
			tempcurses.add(curse);
		for(ActionCurse curse:tempcurses){
			curse.decrementTTL();
			if(!curse.isActive())
				actionCurses.remove(curse);
		}
	}
	
	/**
	 * Updates the Performables for this Player.
	 */
	private void updatePerformables(){
		if(actionsLeft==1 && !hasMoved){
			performables.put(Action.USE, false);
			performables.put(Action.PICKUP, false);
			performables.put(Action.ENDTURN, false);
		}
		else if(actionsLeft<=0)
			setPerformables(false);
	}
	
	/**
	 * Updates the TurnCurses for this Player.
	 */
	private void updateTurnCurses(){
		ArrayList<TurnCurse> tempcurses = new ArrayList<TurnCurse>();
		for(TurnCurse curse:turnCurses)
			tempcurses.add(curse);
		for(TurnCurse curse:tempcurses){
			curse.decrementTTL();
			if(!curse.isActive())
				turnCurses.remove(curse);
		}
	}
	
	/**
	 * This Player removes an Item from his Inventory and uses this Item. After this, he can 
	 * perform one Action less in his current turn. 
	 * @throws InvalidAttributesException thrown when the given Item is not present in his Inventory
	 * @throws IllegalActionException 
	 * @throws IllegalUseException 
	 * @post !inventory.contains(item)
	 */
	public void use(Item i, Direction d) throws InvalidAttributesException, IllegalActionException, IllegalUseException{
		executeActionCurses();
		if(canPerformAction(Action.USE)){
			if(inventory.contains(i)){
				try {
					inventory.removeItem(i);
					i.use(this, d);
					
					decrementActionsLeft();
					decrementLighttrail();
					updatePerformables();
				} catch (IllegalUseException e) {
					try {
						inventory.addItem(i);
						throw new IllegalUseException();
					} catch (InventoryFullException e1) {
					} catch (NotCompatibleWithInventoryException e1) {
					}
				}
			}
			else
				throw new InvalidAttributesException();
		}
		else
			throw new IllegalActionException("This action is illegal right now!");
	}

	public void addGoal(Goal goal) {
		goals.add(goal);
		
	}
}
