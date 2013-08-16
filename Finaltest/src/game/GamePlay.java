package game;

import grid.Direction;
import grid.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

import square.Item;
import square.OnSquare;
import square.Player;

import exceptions.DimensionException;
import exceptions.HaveToMoveException;
import exceptions.IllegalActionException;
import exceptions.IllegalMoveException;
import exceptions.IllegalPickupException;
import exceptions.IllegalPositionException;
import exceptions.IllegalUseException;
import exceptions.InventoryFullException;

/**
 * GamePlay is a class that allows people to play a game. It offers methods to perform start, stop the game and
 * perform all performable actions in the game.
 *
 */
public class GamePlay {
	private Game game;
	
	public GamePlay(Game game) throws DimensionException{
			this.game = game;		
		}

	/**
	 * @return Returns the number of Actions that the current Player has left in this turn.
	 */
	public int getNumberOfActionsLeft(){
		return game.getPlayerOnTurn().getNumberOfActionsLeft();
	}

	/**
	 * Get all the OnSquares in the grid of this game.
	 * @return a HashMap with the Positions and a HashSet of the OnSquares on the matching Square
	 */
	public HashMap<Position, ArrayList<OnSquare>> getOnsquares(){
		return game.getGrid().getOnSquares();
	}
	
	/**
	 * Return the Player whose turn it is.
	 */
	public int getTurn(){
		return game.getPlayerOnTurn().getIndex();
	}
	
	/**
	 * 
	 */
	public Item[] showInventory(){
		return game.getPlayerOnTurn().showInventory();
	}
	
	/**
	 * Return the Player that wins game.
	 */
	public Player getWinner(){
		return game.getWinner();
	}

	public boolean isOver(){
		return game.isOver();
	}

	public boolean isStarted(){
		return game.isStarted();
	}
	
	/**
	 * End the turn of the current Player.
	 * If game is not yet started or game is already over, do nothing.
	 * If the Player has not yet confirmed the end-of-turn request, ask him for confirmation.
	 * @throws HaveToMoveException: is thrown when this is the last action of this turn, and the
	 * current Player has not yet moved.
	 */
	public void endTurn() throws  IllegalActionException{
		if(game.isStarted()&&!game.isOver()){
				game.getPlayerOnTurn().endTurn();
				game.updateGame();
		}
	}
	
	/**
	 * Move the Player whose turn it is, to a given Position.
	 * If game has not yet started or game is already over, do nothing.
	 * @param p: move the current Player to this Position
	 * @throws IllegalMoveException: is thrown when the current Player can't move to Position.
	 * @throws IllegalActionException: Thrown if the player is unable to move, for instance when he is trapped inside
	 * 									a forcefield.
	 */
	public void move(Direction d) throws IllegalMoveException, IllegalActionException{
		if(game.isStarted()&&!game.isOver()){
			if(game.getGrid().canMoveFromTo(game.getPlayerOnTurn(), d)){
					game.getPlayerOnTurn().move(d);
					game.updateGame();
			}
			else
				throw new IllegalMoveException("You can't move here!");
		}
	}
	
	/**
	 * The current Player picks up a given Item.
	 * If game is not yet started or is already over, do nothing.
	 * @param i: the Item the Player tries to pick up.
	 * @throws InventoryFullException: is thrown when the Inventory of the current 
	 * @throws HaveToMoveException: is thrown when this is the last action of this turn, and the
	 * current Player has not yet moved
	 * @throws IllegalPickupException: is thrown when i does not lay on the Square the Player is currently standing on
	 * @throws IllegalActionException: Thrown when a player is unable to perform the PICKUP action.
	 */
	public void pickup(Item i) throws InventoryFullException, HaveToMoveException, IllegalPickupException, IllegalActionException{
		if(game.isStarted()&&!game.isOver()) {
			game.getPlayerOnTurn().pickup(i);
			game.updateGame();
		}
			
	}
	
	/**
	 * If the current Player wants to move, show him a list of the Items that lay on the Square he currently stands on.
	 * @return a HashSet of Items that lay on the Square that the Player stands on
	 * @return null if game is not yet started or is already over 
	 */
	public HashSet<Item> requestPickup(){
		if(game.isStarted()&&!game.isOver())
			return game.getPlayerOnTurn().viewItems();
		else return null;
	}
	
	public void start(){
		game.start();
	}
	
	public void stop(){
		game.stop();
	}
	
	/**
	 * Use a given Item on the Square the Player whose turn it is, stands on.
	 * If game is not yet started or is already over, do nothing.
	 * @param i: the Item the current Player wants to use
	 * @throws InvalidAttributesException: is thrown when the current Player tries to use an Item that 
	 * he does not possess in his inventory.
	 * @throws HaveToMoveException: is thrown when this is the last action of the current turn,
	 * and the Player has not yet moved.
	 * @throws IllegalActionException 
	 * @throws IllegalUseException 
	 */
	public void useItem(Item i, Direction d) throws InvalidAttributesException, HaveToMoveException, IllegalActionException, IllegalUseException{
		if(game.isStarted()&&!game.isOver()){
			game.getPlayerOnTurn().use(i,d);
			game.updateGame();
		}
	}	
}
