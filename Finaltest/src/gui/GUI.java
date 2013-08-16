package gui;

import simplegui.Button;
import grid.Direction;
import grid.Position;
import simplegui.SimpleGUI;
import square.ChargedIdentityDisk;
import square.IdentityDisk;
import square.Item;
import square.LightGrenade;
import square.LightTrail;
import square.PowerFailure;
import square.Forcefield;
import square.ForcefieldGenerator;
import square.OnSquare;
import square.Player;
import square.Teleporter;
import square.Wall;
import square.Flag;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import javax.naming.directory.InvalidAttributesException;

import exceptions.DimensionException;
import exceptions.HaveToMoveException;
import exceptions.IllegalActionException;
import exceptions.IllegalMoveException;
import exceptions.IllegalPickupException;
import exceptions.IllegalPlayerNumberException;
import exceptions.IllegalPositionException;
import exceptions.IllegalUseException;
import exceptions.InventoryFullException;
import game.CTF;
import game.Game;
import game.GameMode;
import game.GamePlay;
import game.Race;



public class GUI {

	public static void main(String[] args) {
		// All code that accesses the simple GUI must run in the AWT event handling thread.
		// A simple way to achieve this is to run the entire application in the AWT event handling thread.
		// This is done by simply wrapping the body of the main method in a call of EventQueue.invokeLater.
		java.awt.EventQueue.invokeLater(new Runnable() {
			Image playerRed;
			Image playerBlue;
			Image playerYellow;
			Image playerWhite;
			Image cell;
			Image wall;
			Image lightTrail;
			Image finishRed;
			Image finishBlue;
			Image finishYellow;
			Image finishWhite;
			Image lightGrenade;
			Image idDisk;
			Image chargedIdDisk;
			Image teleporter;
			Image teleporterIn;
			Image teleporterOut;
			Image teleporterBoth;
			Image powerFailure;
			Image forceField;
			Image forceFieldGeneratorInactive;
			Image forceFieldGeneratorActive;
			Image flagRed;
			Image flagBlue;
			Image flagYellow;
			Image flagWhite;
			String status = "Welcome to Objectron.";
			int rows;
			int columns;
			GamePlay gameplay;
			Button startGameButton;
			Button endGameButton;
			Button pickUpButton;
			Button upLeft;
			Button up;
			Button upRight;
			Button left;
			Button right;
			Button downLeft;
			Button down;
			Button downRight;
			Button endTurnButton;
			ArrayList<Button> inventory;
			ArrayList<Button> movement;
			
			public void run() {
				
				// Begin het spel door de dimensies van de grid door te geven.
				while (gameplay == null){
					Scanner scan = new Scanner(System.in);
					System.out.println("columns: ");
					columns = scan.nextInt();
					System.out.println("rows: ");
					rows = scan.nextInt();
					System.out.println("Select game mode:");
					System.out.println("1. Race");
					System.out.println("2. Capture the flag");
					GameMode gamemode = null;
					int gm = scan.nextInt();
					if (gm == 1){
						gamemode = new Race();
					} else if (gm == 2){
						gamemode = new CTF();
					}
					// add additional game modes.
					System.out.println("Howmany players?");
					int numberPlayers = scan.nextInt();
					String[] players = new String[numberPlayers];
					for(int i = 0; i < numberPlayers; i++){
					players[i] = "player" + (i+1);
					}
					try {
						Game game = new Game(players, columns, rows, 3, gamemode);
						gameplay = new GamePlay(game);
					} catch (DimensionException e1) {
						status = "Invalid dimension.";
						e1.printStackTrace();
					} catch (InvalidAttributesException e) {
						status = "Invalid parameters";
						e.printStackTrace();
					} catch (IllegalPlayerNumberException e) {
						status = "Invalid number of players";
						e.printStackTrace();
					}
				}
				// Maakt de GUI voor het zojuist gemaakte spel.
				final SimpleGUI gui = new SimpleGUI("Objectron", 5 + columns * 45, 250 + rows * 45) {
					@Override
					public void paint(Graphics2D graphics) {
						for(int i = 0; i < gameplay.getPlayers().length; i++){
							if(i == 0)
								graphics.drawImage(playerRed, 270, 80, 40, 40, null);
							else if(i == 1)
								graphics.drawImage(playerBlue, 270, 120, 40, 40, null);
							else if(i == 2)
								graphics.drawImage(playerYellow, 270, 160, 40, 40, null);
							else if(i == 3)
								graphics.drawImage(playerWhite, 270, 200, 40, 40, null);
						}
						graphics.drawString("Inventory", 150, 20);
						/**if (gameplay.getWinner() != -1){
							graphics.drawString("The winner is: Player"+ gameplay.getWinner()+1, 150, 100);
						}**/
						if (gameplay.isStarted()){
							paintGrid(graphics);
							int i = 0;
							for(Player pl : gameplay.getPlayers()){
								if (pl == gameplay.getTurn()){
									graphics.drawString(Integer.toString(gameplay.getNumberOfActionsLeft()), 350, 80 + i * 40);
								}
								i++;
							}
							graphics.drawString(status, 10, 245);
							paintInventory(graphics);
							pickUpButton.setEnabled(false);
							// Zet pickup aan of uit afhankelijk als er een item op de huidige positie ligt.
							for (OnSquare onsq : gameplay.getOnsquares().get(gameplay.getTurn().getPosition())){
								if (onsq instanceof Item){
									pickUpButton.setEnabled(true);
								}
							}
						}
					}
					
					// Tekent de grid.  
					// aanpassen als de structuur van grid verandert.
					private void paintGrid(Graphics2D graphics){
						for (int i = 0; i < columns; i++){
							for (int j = 0; j < rows; j++){
								// Tekent leeg grid.
								graphics.drawImage(cell, 5 + i * 45, 250 + j * 45, 40, 40, null);
							}
						}
						
						//Tekent alle objecten in de grid.
						HashMap<Position, ArrayList<OnSquare>> objects = gameplay.getOnsquares();
						for (Position pos : objects.keySet()){
							ArrayList<OnSquare> onsqs = objects.get(pos);
							for (OnSquare onsq : onsqs){
								Image image = null;
								if (onsq.isVisible()){
								if (onsq instanceof IdentityDisk){
									image = idDisk;
									graphics.drawImage(image, 5 + pos.getX() * 45, (205 + rows * 45) - (pos.getY() * 45), 40, 40, null);
								} 
								if (onsq instanceof ChargedIdentityDisk){
									image = chargedIdDisk;
									graphics.drawImage(image, 5 + pos.getX() * 45, (205 + rows * 45) - (pos.getY() * 45), 40, 40, null);
								}
								if (onsq instanceof LightGrenade && !((LightGrenade) onsq).isUsed()){
									image = lightGrenade;
									graphics.drawImage(image, 5 + pos.getX() * 45, (205 + rows * 45) - (pos.getY() * 45), 40, 40, null);
								} 
								if (onsq instanceof PowerFailure){
									image = powerFailure;
									graphics.drawImage(image, 5 + pos.getX() * 45, (205 + rows * 45) - (pos.getY() * 45), 40, 40, null);
								} 
								if (onsq instanceof Forcefield){
									image = forceField;
									graphics.drawImage(image, 5 + pos.getX() * 45, (205 + rows * 45) - (pos.getY() * 45), 40, 40, null);
								} 
								if (onsq instanceof ForcefieldGenerator){
									if(onsq.isActive()){
										image = forceFieldGeneratorActive;
									} else {
										image = forceFieldGeneratorInactive;
									}
									graphics.drawImage(image, 5 + pos.getX() * 45, (205 + rows * 45) - (pos.getY() * 45), 40, 40, null);
								} 
								if (onsq instanceof Teleporter){
									image = teleporter;
									graphics.drawImage(image, 5 + pos.getX() * 45, (205 + rows * 45) - (pos.getY() * 45), 40, 40, null);
								} 
								if (onsq instanceof LightTrail){
									image = lightTrail;
									graphics.drawImage(image, 5 + pos.getX() * 45, (205 + rows * 45) - (pos.getY() * 45), 40, 40, null);
								} 
								if (onsq instanceof Wall){
									image = wall;
									graphics.drawImage(image, 5 + pos.getX() * 45, (205 + rows * 45) - (pos.getY() * 45), 40, 40, null);
								} 
								if (onsq instanceof Flag){
									if (((Flag) onsq).belongsTo(gameplay.getPlayers()[0]))
										image = flagRed;
									else if (((Flag) onsq).belongsTo(gameplay.getPlayers()[1]))
										image = flagBlue;
									else if (((Flag) onsq).belongsTo(gameplay.getPlayers()[2]))
										image = flagYellow;
									else if (((Flag) onsq).belongsTo(gameplay.getPlayers()[3]))
										image = flagWhite;
									graphics.drawImage(image, 5 + pos.getX() * 45, (205 + rows * 45) - (pos.getY() * 45), 40, 40, null);
								} 
								}
							}
						}
						/**
						// Tekent de muren in de grid.
						HashSet<Position> wallPos = gameplay.getWalls();
						for (Position pos : wallPos){
							graphics.drawImage(wall, 5 + pos.getX() * 45, (205 + rows * 45) - (pos.getY() * 45), 40, 40, null);
							//System.out.println("wall on: " + pos);
						}
						
						// Tekent de lichtgranaten in de grid.
						HashMap<Position,HashSet<Item>> lGPos = gameplay.getItems();
						for (Position pos : lGPos.keySet()){
							HashSet<Item> items = lGPos.get(pos);
							for (Item item : items){
								if (item instanceof LightGrenade){
									graphics.drawImage(lightGrenade, 5 + pos.getX() * 45, (205 + rows * 45) - (pos.getY() * 45), 40, 40, null);
									//System.out.println("grenade on: " + pos);
								}
								// Voeg andere type items toe.
							}
						}**/
						
						// Tekent de spelers op het grid en de finish.
						for (int i = 0; i < gameplay.getPlayers().length; i ++){
							if (i == 0){
								graphics.drawImage(finishRed, columns * 45 - 40, 250, 40, 40, null);
								graphics.drawImage(playerRed, 5 + gameplay.getPlayers()[0].getPosition().getX() * 45, 205 + rows * 45 - (gameplay.getPlayers()[0].getPosition().getY() * 45), 40, 40, null);
							} else if (i == 1){
								graphics.drawImage(finishBlue, 5, 205 + rows * 45, 40, 40, null);
								graphics.drawImage(playerBlue, 5 + gameplay.getPlayers()[1].getPosition().getX() * 45, 205 + rows * 45 - (gameplay.getPlayers()[1].getPosition().getY() * 45), 40, 40, null);
							} else if (i == 2){
								graphics.drawImage(finishYellow, columns * 45 - 40, 205 + rows * 45, 40, 40, null);
								graphics.drawImage(playerYellow, 5 + gameplay.getPlayers()[2].getPosition().getX() * 45, 205 + rows * 45 - (gameplay.getPlayers()[2].getPosition().getY() * 45), 40, 40, null);
							} else if (i == 3){
								graphics.drawImage(finishWhite, 5, 250, 40, 40, null);
								graphics.drawImage(playerWhite, 5 + gameplay.getPlayers()[3].getPosition().getX() * 45, 205 + rows * 45 - (gameplay.getPlayers()[3].getPosition().getY() * 45), 40, 40, null);
							}
						
						
						//System.out.println("Player 1: " + gameplay.getPlayers()[0].getPosition().getX() + ", " + gameplay.getPlayers()[0].getPosition().getY());
						//System.out.println("Player 2: " + gameplay.getPlayers()[1].getPosition().getX() + ", " + gameplay.getPlayers()[1].getPosition().getY());
						
						
						}
						
						/**
						// Tekent de actieve lichtsporen.
						HashMap<Player, LightTrail> lTs = gameplay.getLightTrails();
						for (LightTrail lT : lTs.values()){
							for (Position pos : lT.getPositions()){
								graphics.drawImage(lightTrail, 5 + pos.getX() * 45, (205 + rows * 45) - (pos.getY() * 45), 40, 40, null);
							}
						}**/
					}
					
					// Geeft de knoppen van de inventory de juiste images die bij de items horen.
					// En zet deze knoppen aan of uit afhankelijk van dat er een item is.
					private void paintInventory(Graphics2D graphics){
						for (int i = 0; i < 6; i++){
							Button button = inventory.get(i);
							Image image = cell;
							button.setEnabled(false);
							if (i < gameplay.getTurn().showInventory().length){
								if (gameplay.getTurn().showInventory()[i] != null){ // check bij meerdere items of het een lichtgranaat is.
									button.setEnabled(true);
									if (gameplay.getTurn().showInventory()[i] instanceof LightGrenade){
										image = lightGrenade;
									} else if (gameplay.getTurn().showInventory()[i] instanceof ChargedIdentityDisk){
										image = chargedIdDisk;
									} else if (gameplay.getTurn().showInventory()[i] instanceof IdentityDisk){
										image = idDisk;
									} else if (gameplay.getTurn().showInventory()[i] instanceof Flag){
										if (((Flag) gameplay.getTurn().showInventory()[i]).belongsTo(gameplay.getPlayers()[0]))
											image = flagRed;
										else if (((Flag) gameplay.getTurn().showInventory()[i]).belongsTo(gameplay.getPlayers()[1]))
											image = flagBlue;
									}
									// Voeg andere items hier toe.
								}
							}
							button.setImage(image);
						}
					}
					
					@Override
					public void handleMouseClick(int x, int y, boolean doubleClick) {
						System.out.println((doubleClick ? "Doubleclicked" : "Clicked")+" at ("+x+", "+y+")");
					}
					
				};
				
				// Laadt alle images in.
				playerRed = gui.loadImage("simpleguidemo/player_red.png", 40, 40);
				playerBlue = gui.loadImage("simpleguidemo/player_blue.png", 40, 40);
				playerYellow = gui.loadImage("simpleguidemo/player_yellow.png", 40, 40);
				playerWhite = gui.loadImage("simpleguidemo/player_white.png", 40, 40);
				cell = gui.loadImage("simpleguidemo/cell.png", 40, 40);
				wall = gui.loadImage("simpleguidemo/wall.png", 40, 40);
				lightTrail = gui.loadImage("simpleguidemo/cell_lighttrail.png", 40, 40);
				finishRed = gui.loadImage("simpleguidemo/cell_finish_red.png", 40, 40);
				finishBlue = gui.loadImage("simpleguidemo/cell_finish_blue.png", 40, 40);
				finishYellow = gui.loadImage("simpleguidemo/cell_finish_yellow.png", 40, 40);
				finishWhite = gui.loadImage("simpleguidemo/cell_finish_white.png", 40, 40);
				lightGrenade = gui.loadImage("simpleguidemo/lightgrenade.png", 40, 40);
				idDisk = gui.loadImage("simpleguidemo/iddisk.png", 40, 40);
				chargedIdDisk = gui.loadImage("simpleguidemo/chargediddisk.png", 40, 40);
				teleporter = gui.loadImage("simpleguidemo/teleporter.png", 40, 40);
				teleporterIn = gui.loadImage("simpleguidemo/teleporterIn.png", 40, 40);
				teleporterOut = gui.loadImage("simpleguidemo/teleporterOut.png", 40, 40);
				teleporterBoth = gui.loadImage("simpleguidemo/teleporterBoth.png", 40, 40);
				powerFailure = gui.loadImage("simpleguidemo/powerfailure.png", 40, 40);
				forceField = gui.loadImage("simpleguidemo/force_field.png", 40, 40);
				forceFieldGeneratorActive = gui.loadImage("simpleguidemo/force_field_generator_active.png", 40, 40);
				forceFieldGeneratorInactive = gui.loadImage("simpleguidemo/force_field_generator_inactive.png", 40, 40);
				flagRed = gui.loadImage("simpleguidemo/flag_red.png", 40, 40);
				flagBlue = gui.loadImage("simpleguidemo/flag_blue.png", 40, 40);
				flagYellow = gui.loadImage("simpleguidemo/flag_yellow.png", 40, 40);
				flagWhite = gui.loadImage("simpleguidemo/flag_white.png", 40, 40);
				
				// Stelt de inventory op als bruikbare knoppen.
				inventory = new ArrayList<Button>();
				/**for (int o = 0; o < 6; o++){
					Button button = gui.createButton(150 + o * 45, 40, 40, 40, new Runnable() {
						public void run() {
							Item[] items = game.showPlayerInventory(game.getTurn());
							game.use(items[o]);
							gui.repaint();
						}
					});
					inventory.add(o, button);
				}**/
				Button i0 = gui.createButton(150, 40, 40, 40, new Runnable() {
					public void run() {
						Item[] items = gameplay.getTurn().showInventory();
						try {
							Direction dir = null;
							if(gameplay.getTurn().showInventory()[0] instanceof IdentityDisk){
								Scanner scan = new Scanner(System.in);
								System.out.println("Pick a direction:");
								System.out.println("1. NORTH");
								System.out.println("2. EAST");
								System.out.println("3. WEST");
								System.out.println("4. SOUTH");
								int dirVal = scan.nextInt();
								if (dirVal == 1){
									dir = Direction.NORTH;
								} else if (dirVal == 2){
									dir = Direction.EAST;
								} else if (dirVal == 3){
									dir = Direction.WEST;
								} else if (dirVal == 4){
									dir = Direction.SOUTH;
								}
							}
							gameplay.useItem(items[0],dir);
						} catch (HaveToMoveException e) {
							status = "You cannot end a turn on the position you started.";
							e.printStackTrace();
						} catch (InvalidAttributesException e) {
							status = "You cannot use this.";
							e.printStackTrace();
						} catch (IllegalActionException e) {
							status = "You cannot perform this action.";
							e.printStackTrace();
						} catch (IllegalUseException e) {
							status = "You cannot use this.";
							e.printStackTrace();
						}
						gui.repaint();
					}
				});
				inventory.add(0, i0);
				Button i1 = gui.createButton(195, 40, 40, 40, new Runnable() {
					public void run() {
						Item[] items = gameplay.getTurn().showInventory();
						try {
							Direction dir = null;
							if(gameplay.getTurn().showInventory()[1] instanceof IdentityDisk){
								Scanner scan = new Scanner(System.in);
								System.out.println("Pick a direction:");
								System.out.println("1. NORTH");
								System.out.println("2. EAST");
								System.out.println("3. WEST");
								System.out.println("4. SOUTH");
								int dirVal = scan.nextInt();
								if (dirVal == 1){
									dir = Direction.NORTH;
								} else if (dirVal == 2){
									dir = Direction.EAST;
								} else if (dirVal == 3){
									dir = Direction.WEST;
								} else if (dirVal == 4){
									dir = Direction.SOUTH;
								}
							}
							gameplay.useItem(items[1],dir);
						} catch (HaveToMoveException e) {
							status = "You cannot end a turn on the position you started.";
							e.printStackTrace();
						} catch (InvalidAttributesException e) {
							status = "You cannot use this.";
							e.printStackTrace();
						} catch (IllegalActionException e) {
							status = "You cannot perform this action.";
							e.printStackTrace();
						} catch (IllegalUseException e) {
							status = "You cannot use this.";
							e.printStackTrace();
						}
						gui.repaint();
					}
				});
				inventory.add(1, i1);
				Button i2 = gui.createButton(240, 40, 40, 40, new Runnable() {
					public void run() {
						Item[] items = gameplay.getTurn().showInventory();
						try {
							Direction dir = null;
							if(gameplay.getTurn().showInventory()[2] instanceof IdentityDisk){
								Scanner scan = new Scanner(System.in);
								System.out.println("Pick a direction:");
								System.out.println("1. NORTH");
								System.out.println("2. EAST");
								System.out.println("3. WEST");
								System.out.println("4. SOUTH");
								int dirVal = scan.nextInt();
								if (dirVal == 1){
									dir = Direction.NORTH;
								} else if (dirVal == 2){
									dir = Direction.EAST;
								} else if (dirVal == 3){
									dir = Direction.WEST;
								} else if (dirVal == 4){
									dir = Direction.SOUTH;
								}
							}
							gameplay.useItem(items[2],dir);
						} catch (HaveToMoveException e) {
							status = "You cannot end a turn on the position you started.";
							e.printStackTrace();
						} catch (InvalidAttributesException e) {
							status = "You cannot use this.";
							e.printStackTrace();
						} catch (IllegalActionException e) {
							status = "You cannot perform this action.";
							e.printStackTrace();
						} catch (IllegalUseException e) {
							status = "You cannot use this.";
							e.printStackTrace();
						}
						gui.repaint();
					}
				});
				inventory.add(2, i2);
				Button i3 = gui.createButton(285, 40, 40, 40, new Runnable() {
					public void run() {
						Item[] items = gameplay.getTurn().showInventory();
						try {
							Direction dir = null;
							if(gameplay.getTurn().showInventory()[3] instanceof IdentityDisk){
								Scanner scan = new Scanner(System.in);
								System.out.println("Pick a direction:");
								System.out.println("1. NORTH");
								System.out.println("2. EAST");
								System.out.println("3. WEST");
								System.out.println("4. SOUTH");
								int dirVal = scan.nextInt();
								if (dirVal == 1){
									dir = Direction.NORTH;
								} else if (dirVal == 2){
									dir = Direction.EAST;
								} else if (dirVal == 3){
									dir = Direction.WEST;
								} else if (dirVal == 4){
									dir = Direction.SOUTH;
								}
							}
							gameplay.useItem(items[3],dir);
						} catch (HaveToMoveException e) {
							status = "You cannot end a turn on the position you started.";
							e.printStackTrace();
						} catch (InvalidAttributesException e) {
							status = "You cannot use this.";
							e.printStackTrace();
						} catch (IllegalActionException e) {
							status = "You cannot perform this action.";
							e.printStackTrace();
						} catch (IllegalUseException e) {
							status = "You cannot use this.";
							e.printStackTrace();
						}
						gui.repaint();
					}
				});
				inventory.add(3, i3);
				Button i4 = gui.createButton(330, 40, 40, 40, new Runnable() {
					public void run() {
						Item[] items = gameplay.getTurn().showInventory();
						try {
							Direction dir = null;
							if(gameplay.getTurn().showInventory()[4] instanceof IdentityDisk){
								Scanner scan = new Scanner(System.in);
								System.out.println("Pick a direction:");
								System.out.println("1. NORTH");
								System.out.println("2. EAST");
								System.out.println("3. WEST");
								System.out.println("4. SOUTH");
								int dirVal = scan.nextInt();
								if (dirVal == 1){
									dir = Direction.NORTH;
								} else if (dirVal == 2){
									dir = Direction.EAST;
								} else if (dirVal == 3){
									dir = Direction.WEST;
								} else if (dirVal == 4){
									dir = Direction.SOUTH;
								}
							}
							gameplay.useItem(items[4],dir);
						} catch (HaveToMoveException e) {
							status = "You cannot end a turn on the position you started.";
							e.printStackTrace();
						} catch (InvalidAttributesException e) {
							status = "You cannot use this.";
							e.printStackTrace();
						} catch (IllegalActionException e) {
							status = "You cannot perform this action.";
							e.printStackTrace();
						} catch (IllegalUseException e) {
							status = "You cannot use this.";
							e.printStackTrace();
						}
						gui.repaint();
					}
				});
				inventory.add(4, i4);
				Button i5 = gui.createButton(375, 40, 40, 40, new Runnable() {
					public void run() {
						Item[] items = gameplay.getTurn().showInventory();
						try {
							Direction dir = null;
							if(gameplay.getTurn().showInventory()[5] instanceof IdentityDisk){
								Scanner scan = new Scanner(System.in);
								System.out.println("Pick a direction:");
								System.out.println("1. NORTH");
								System.out.println("2. EAST");
								System.out.println("3. WEST");
								System.out.println("4. SOUTH");
								int dirVal = scan.nextInt();
								if (dirVal == 1){
									dir = Direction.NORTH;
								} else if (dirVal == 2){
									dir = Direction.EAST;
								} else if (dirVal == 3){
									dir = Direction.WEST;
								} else if (dirVal == 4){
									dir = Direction.SOUTH;
								}
							}
							gameplay.useItem(items[5],dir);
						} catch (HaveToMoveException e) {
							status = "You cannot end a turn on the position you started.";
							e.printStackTrace();
						} catch (InvalidAttributesException e) {
							status = "You cannot use this.";
							e.printStackTrace();
						} catch (IllegalActionException e) {
							status = "You cannot perform this action.";
							e.printStackTrace();
						} catch (IllegalUseException e) {
							status = "You cannot use this.";
							e.printStackTrace();
						}
						gui.repaint();
					}
				});
				inventory.add(5, i5);
				for (int i = 0; i < 6; i++){
					inventory.get(i).setEnabled(false);
				}
				// Start het spel.
				startGameButton = gui.createButton(10, 10, 100, 40, new Runnable() {
					public void run() {
						gameplay.start();
						status = "game started.";
						endGameButton.setEnabled(true);
						startGameButton.setEnabled(false);
						endTurnButton.setEnabled(true);
						for (int i = 0; i < 8; i++){
							movement.get(i).setEnabled(true);
						}
						gui.repaint();
					}
				});
				startGameButton.setText("Start game");
				
				// Stopt het spel.
				endGameButton = gui.createButton(10, 55, 100, 40, new Runnable() {
					public void run() {
						gameplay.stop();
						status = "game ended.";
						endGameButton.setEnabled(false);
						startGameButton.setEnabled(true);
						gui.repaint();
					}
				});
				endGameButton.setText("End game");
				endGameButton.setEnabled(false);
				
				// Pak het item op.
				pickUpButton = gui.createButton(150, 145, 100, 40, new Runnable() {
					public void run() {
						boolean hasItem = false;
						ArrayList<Item> items = new ArrayList<Item>();
						for (Item it : gameplay.requestPickup()){
							if(it.canPickUp()){
								items.add(it);
								hasItem = true;
							}
						}
						if (hasItem){
						for (int i = 1; i < items.size()+1; i++){
							System.out.println(i + ". " + items.get(i - 1).getClass().getSimpleName());
						}
						Scanner sc = new Scanner(System.in);
						System.out.println("Select an item.");
						int selection = sc.nextInt();
						try {
							gameplay.pickup(items.get(selection - 1));
							status = "Item picked up.";
						} catch (InventoryFullException e) {
							status = "You cannot pick up an item because your inventory is full.";
							e.printStackTrace();
						} catch (HaveToMoveException e) {
							status = "You cannot end a turn on the position you started.";
							e.printStackTrace();
						} catch (IllegalPickupException e) {
							status = "You cannot pick this up.";
							e.printStackTrace();
						} catch (IllegalActionException e) {
							status = "You cannot perform this action.";
							e.printStackTrace();
						}
						gui.repaint();
						}
					}
				});
				pickUpButton.setText("Pick up");
				pickUpButton.setEnabled(false);
				
				// Eindig je beurt.
				endTurnButton = gui.createButton(150, 190, 100, 40, new Runnable() {
					public void run() {
						try {
							gameplay.endTurn();
							status = "Beurt beëindigd.";
						} catch (IllegalActionException a) {
							status = "Are you sure you want to end the turn?";
						}
						gui.repaint();
					}
				});
				endTurnButton.setText("End turn");
				endTurnButton.setEnabled(false);
				
				// Beweeg 1 vakje in richting.
				movement = new ArrayList<Button>();
				upLeft = gui.createButton(10, 100, 40, 40, new Runnable() {
					public void run() {
						try {
							gameplay.move(Direction.NORTHWEST);
							status = "Player moved northwest.";
						} catch (IllegalMoveException e) {
							status = "Invalid movement.";
							e.printStackTrace();
						} catch (IllegalActionException e) {
							status = "You cannot perform this action.";
							e.printStackTrace();
						}
						gui.repaint();
					}
				});
				upLeft.setImage(gui.loadImage("simpleguidemo/arrow_NW.png", 40, 40));
				movement.add(upLeft);
				up = gui.createButton(55, 100, 40, 40, new Runnable() {
					public void run() {
						try {
							gameplay.move(Direction.NORTH);
							status = "Player moved north.";
						} catch (IllegalMoveException e) {
							status = "Invalid movement.";
							e.printStackTrace();
						} catch (IllegalActionException e) {
							status = "You cannot perform this action.";
							e.printStackTrace();
						}
						gui.repaint();
					}
				});
				up.setImage(gui.loadImage("simpleguidemo/arrow_N.png", 40, 40));
				movement.add(up);
				upRight = gui.createButton(100, 100, 40, 40, new Runnable() {
					public void run() {
						try {
							gameplay.move(Direction.NORTHEAST);
							status = "Player moved northeast.";
						} catch (IllegalMoveException e) {
							status = "Invalid movement.";
							e.printStackTrace();
						} catch (IllegalActionException e) {
							status = "You cannot perform this action.";
							e.printStackTrace();
						}
						gui.repaint();
					}
				});
				upRight.setImage(gui.loadImage("simpleguidemo/arrow_NE.png", 40, 40));
				movement.add(upRight);
				left = gui.createButton(10, 145, 40, 40, new Runnable() {
					public void run() {
						try {
							gameplay.move(Direction.WEST);
							status = "Player moved west.";
						} catch (IllegalMoveException e) {
							status = "Invalid movement.";
							e.printStackTrace();
						} catch (IllegalActionException e) {
							status = "You cannot perform this action.";
							e.printStackTrace();
						}
						gui.repaint();
					}
				});
				left.setImage(gui.loadImage("simpleguidemo/arrow_W.png", 40, 40));
				movement.add(left);
				right = gui.createButton(100, 145, 40, 40, new Runnable() {
					public void run() {
						try {
							gameplay.move(Direction.EAST);
							status = "Player moved east.";
						} catch (IllegalMoveException e) {
							status = "Invalid movement.";
							e.printStackTrace();
						} catch (IllegalActionException e) {
							status = "You cannot perform this action.";
							e.printStackTrace();
						}
						gui.repaint();
					}
				});
				right.setImage(gui.loadImage("simpleguidemo/arrow_E.png", 40, 40));
				movement.add(right);
				downLeft = gui.createButton(10, 190, 40, 40, new Runnable() {
					public void run() {
						try {
							gameplay.move(Direction.SOUTHWEST);
							status = "Player moved southwest.";
						} catch (IllegalMoveException e) {
							status = "Invalid movement.";
							e.printStackTrace();
						} catch (IllegalActionException e) {
							status = "You cannot perform this action.";
							e.printStackTrace();
						}
						gui.repaint();
					}
				});
				downLeft.setImage(gui.loadImage("simpleguidemo/arrow_SW.png", 40, 40));
				movement.add(downLeft);
				down = gui.createButton(55, 190, 40, 40, new Runnable() {
					public void run() {
						try {
							gameplay.move(Direction.SOUTH);
							status = "Player moved south.";
						} catch (IllegalMoveException e) {
							status = "Invalid movement.";
							e.printStackTrace();
						} catch (IllegalActionException e) {
							status = "You cannot perform this action.";
							e.printStackTrace();
						}
						gui.repaint();
					}
				});
				down.setImage(gui.loadImage("simpleguidemo/arrow_S.png", 40, 40));
				movement.add(down);
				downRight = gui.createButton(100, 190, 40, 40, new Runnable() {
					public void run() {
						try {
							gameplay.move(Direction.SOUTHEAST);
							status = "Player moved southeast.";
						} catch (IllegalMoveException e) {
							status = "Invalid movement.";
							e.printStackTrace();
						} catch (IllegalActionException e) {
							status = "You cannot perform this action.";
							e.printStackTrace();
						}
						gui.repaint();
					}
				});
				downRight.setImage(gui.loadImage("simpleguidemo/arrow_SE.png", 40, 40));
				movement.add(downRight);
				for (int i = 0; i < 8; i++){
					movement.get(i).setEnabled(false);
				}
			}
		});

	}
}

