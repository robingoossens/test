package gui;

import exceptions.DimensionException;
import exceptions.HaveToMoveException;
import exceptions.IllegalActionException;
import exceptions.IllegalMoveException;
import exceptions.IllegalPickupException;
import exceptions.IllegalPlayerNumberException;
import exceptions.IllegalUseException;
import exceptions.InventoryFullException;
import game.CTF;
import game.Game;
import game.GameMode;
import game.GamePlay;
import game.Race;
import grid.Direction;
import grid.Position;

import java.awt.Image;

import javax.naming.directory.InvalidAttributesException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JTextPane;

import square.ChargedIdentityDisk;
import square.Flag;
import square.Forcefield;
import square.ForcefieldGenerator;
import square.IdentityDisk;
import square.Item;
import square.LightGrenade;
import square.LightTrail;
import square.OnSquare;
import square.Player;
import square.PowerFailure;
import square.StartingPosition;
import square.Teleporter;
import square.Wall;

import javax.swing.JLabel;

public class SwingGUI extends JFrame {

	private JPanel contentPane;
	private int xDim;
	private int yDim;
	private int numberofplayers;
	private JLabel[][] field;
	private String gamemode;
	private Selection s;
	private JButton btnNW;
	private JButton btnN;
	private JButton btnNE;
	private JButton btnW;
	private JButton btnE;
	private JButton btnSW;
	private JButton btnS;
	private JButton btnSE;
	private JTextPane textPaneStatus;
	private GamePlay gameplay = null;
	private JButton btnEndTurn;
	private JButton btnI0;
	private JButton btnI1;
	private JButton btnI2;
	private JButton btnI3;
	private JButton btnI4;
	private JButton btnI5;
	private JButton[] inventory;
	private JButton btnPickup;
	private JLabel lblP1;
	private JLabel lblP2;
	private JLabel lblP3;
	private JLabel lblP4;
	private JLabel[] lblPS;
	private JLabel lblp1;
	private JLabel lblp2;
	private JLabel lblp3;
	private JLabel lblp4;
	private JLabel[] lblps;
	private JLabel lblp1t;
	private JLabel lblp2t;
	private JLabel lblp3t;
	private JLabel lblp4t;
	private JLabel[] lblpst;

	private ImageIcon cell;
	private ImageIcon playerRed;
	private ImageIcon playerBlue;
	private ImageIcon playerYellow;
	private ImageIcon playerWhite;
	private ImageIcon[] players;
	private ImageIcon wall;
	private ImageIcon lightTrail;
	private ImageIcon finishRed;
	private ImageIcon finishBlue;
	private ImageIcon finishYellow;
	private ImageIcon finishWhite;
	private ImageIcon[] finish;
	private ImageIcon lightGrenade;
	private ImageIcon idDisk;
	private ImageIcon chargedIdDisk;
	private ImageIcon teleporter;
	private ImageIcon teleporterIn;
	private ImageIcon teleporterOut;
	private ImageIcon teleporterBoth;
	private ImageIcon powerFailure;
	private ImageIcon forceField;
	private ImageIcon forceFieldGeneratorInactive;
	private ImageIcon forceFieldGeneratorActive;
	private ImageIcon flagRed;
	private ImageIcon flagBlue;
	private ImageIcon flagYellow;
	private ImageIcon flagWhite;
	private ImageIcon[] flags;
	
	private Direction[] fourWinds = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
	private JPanel panel_playing_field;



	/**
	 * Create the frame.
	 */
	public SwingGUI(int xDim, int yDim,String gamemode, int players) {
		loadIcons();
		this.xDim = xDim;
		this.yDim = yDim;
		setup();
		createEvents();
		createGame(xDim,yDim,gamemode,players);
		setupField();
		paint();
	}
	
	private void loadIcons(){
		Image img = null;
		Image newimg = null;
		
		cell = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/cell.png"));
		img = cell.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    cell = new ImageIcon(newimg); 
	    
		playerRed = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/player_red.png"));
		
		playerBlue = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/player_blue.png"));
		
		playerYellow = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/player_yellow.png"));
		img = playerYellow.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    playerYellow = new ImageIcon(newimg); 
	    
		playerWhite = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/player_white.png"));
	    img = playerWhite.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    playerWhite = new ImageIcon(newimg); 
	    
		players = new ImageIcon[4];
		players[0] = playerRed;
		players[1] = playerBlue;
		players[2] = playerYellow;
		players[3] = playerWhite;
		
		wall = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/wall.png"));
		img = wall.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    wall = new ImageIcon(newimg); 
	    
		lightTrail = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/cell_lighttrail.png"));
		img = lightTrail.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    lightTrail = new ImageIcon(newimg); 
	    
		finishRed = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/cell_finish_red.png"));
		img = finishRed.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    finishRed = new ImageIcon(newimg); 
	    
		finishBlue = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/cell_finish_blue.png"));
		img = finishBlue.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    finishBlue = new ImageIcon(newimg); 
	    
		finishYellow = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/cell_finish_yellow.png"));
		img = finishYellow.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    finishYellow = new ImageIcon(newimg); 
	    
		finishWhite = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/cell_finish_white.png"));
		img = finishWhite.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    finishWhite = new ImageIcon(newimg); 
	    
		finish = new ImageIcon[4];
		finish[0] = finishRed;
		finish[1] = finishBlue;
		finish[2] = finishYellow;
		finish[3] = finishWhite;
		
		lightGrenade = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/lightgrenade.png"));
		img = wall.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    lightGrenade = new ImageIcon(newimg); 
	    
		idDisk = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/iddisk.png"));
		img = idDisk.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    idDisk = new ImageIcon(newimg); 
	    
		chargedIdDisk = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/chargediddisk.png"));
		img = chargedIdDisk.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    chargedIdDisk = new ImageIcon(newimg); 
	    
		teleporter = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/teleporter.png"));
		img = teleporter.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    teleporter = new ImageIcon(newimg); 
	    
		teleporterIn = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/teleporterIn.png"));
		teleporterOut = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/teleporterOut.png"));
		teleporterBoth = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/teleporterBoth.png"));
		
		powerFailure = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/powerfailure.png"));
		img = powerFailure.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    powerFailure = new ImageIcon(newimg); 
	    
		forceField = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/force_field.png"));
		img = forceField.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    forceField = new ImageIcon(newimg); 
	    
		forceFieldGeneratorActive = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/force_field_generator_active.png"));
		img = forceFieldGeneratorActive.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    forceFieldGeneratorActive = new ImageIcon(newimg); 
	    
		forceFieldGeneratorInactive = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/force_field_generator_inactive.png"));
		img = forceFieldGeneratorInactive.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    forceFieldGeneratorInactive = new ImageIcon(newimg); 
		
		flagRed = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/flag_red.png"));
		img = flagRed.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    flagRed = new ImageIcon(newimg); 
	    
		flagBlue = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/flag_blue.png"));
		img = flagBlue.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    flagBlue = new ImageIcon(newimg); 
	    
		flagYellow = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/flag_yellow.png"));
		img = flagYellow.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    flagYellow = new ImageIcon(newimg); 
	    
		flagWhite = new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/flag_white.png"));
		img = flagWhite.getImage();  
	    newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);  
	    flagWhite = new ImageIcon(newimg); 
		
		flags = new ImageIcon[4];
		flags[0] = flagRed;
		flags[1] = flagBlue;
		flags[2] = flagYellow;
		flags[3] = flagWhite;
	}
	
	private void setup(){
		setVisible(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int width = Math.max(810, 40 + (xDim * 55));
		int height = (320 + (yDim * 55));
		setBounds(100, 100, width, height);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_controls = new JPanel();
		panel_controls.setBorder(new TitledBorder(null, "Controls", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_controls.setBounds(10, 11, 290, 185);
		contentPane.add(panel_controls);
		panel_controls.setLayout(null);
		
		btnN = new JButton("");
		btnN.setIcon(new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/arrow_N.png")));
		btnN.setBounds(65, 15, 50, 50);
		panel_controls.add(btnN);
		
		btnS = new JButton("");
		btnS.setIcon(new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/arrow_S.png")));
		btnS.setBounds(65, 125, 50, 50);
		panel_controls.add(btnS);
		
		btnNW = new JButton("");
		btnNW.setIcon(new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/arrow_NW.png")));
		btnNW.setBounds(10, 15, 50, 50);
		panel_controls.add(btnNW);
		
		btnW = new JButton("");
		btnW.setIcon(new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/arrow_W.png")));
		btnW.setBounds(10, 70, 50, 50);
		panel_controls.add(btnW);
		
		btnSW = new JButton("");
		btnSW.setIcon(new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/arrow_SW.png")));
		btnSW.setBounds(10, 125, 50, 50);
		panel_controls.add(btnSW);
		
		btnNE = new JButton("");
		btnNE.setIcon(new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/arrow_NE.png")));
		btnNE.setBounds(120, 15, 50, 50);
		panel_controls.add(btnNE);
		
		btnE = new JButton("");
		btnE.setIcon(new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/arrow_E.png")));
		btnE.setBounds(120, 70, 50, 50);
		panel_controls.add(btnE);
		
		btnSE = new JButton("");
		btnSE.setIcon(new ImageIcon(SwingGUI.class.getResource("/simpleguidemo/arrow_SE.png")));
		btnSE.setBounds(120, 125, 50, 50);
		panel_controls.add(btnSE);
		
		btnEndTurn = new JButton("End turn");
		btnEndTurn.setBounds(180, 15, 100, 50);
		panel_controls.add(btnEndTurn);
		
		JPanel panel_inventory = new JPanel();
		panel_inventory.setBorder(new TitledBorder(null, "Inventory", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_inventory.setBounds(310, 11, 190, 185);
		contentPane.add(panel_inventory);
		panel_inventory.setLayout(null);
		
		btnI0 = new JButton("");
		btnI0.setBounds(10, 15, 50, 50);
		panel_inventory.add(btnI0);
		
		btnI1 = new JButton("");
		btnI1.setBounds(70, 15, 50, 50);
		panel_inventory.add(btnI1);
		
		btnI2 = new JButton("");
		btnI2.setBounds(130, 15, 50, 50);
		panel_inventory.add(btnI2);
		
		btnI3 = new JButton("");
		btnI3.setBounds(10, 76, 50, 50);
		panel_inventory.add(btnI3);
		
		btnI4 = new JButton("");
		btnI4.setBounds(70, 76, 50, 50);
		panel_inventory.add(btnI4);
		
		btnI5 = new JButton("");
		btnI5.setBounds(130, 76, 50, 50);
		panel_inventory.add(btnI5);
		
		inventory = new JButton[6];
		inventory[0] = btnI0;
		inventory[1] = btnI1;
		inventory[2] = btnI2;
		inventory[3] = btnI3;
		inventory[4] = btnI4;
		inventory[5] = btnI5;
		
		btnPickup = new JButton("Pickup");
		btnPickup.setBounds(57, 137, 77, 37);
		panel_inventory.add(btnPickup);
		
		panel_playing_field = new JPanel();
		panel_playing_field.setBorder(new TitledBorder(null, "Playing field", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_playing_field.setBounds(10, 240, 25 + (xDim *55), 35 + (yDim * 55));
		contentPane.add(panel_playing_field);
		panel_playing_field.setLayout(null);
		
		JPanel panel_players = new JPanel();
		panel_players.setBorder(new TitledBorder(null, "Players", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_players.setBounds(510, 11, 273, 228);
		contentPane.add(panel_players);
		panel_players.setLayout(null);
		
		lblP1 = new JLabel("");
		lblP1.setBounds(10, 20, 50, 50);
		panel_players.add(lblP1);
		
		lblP2 = new JLabel("");
		lblP2.setBounds(10, 70, 50, 50);
		panel_players.add(lblP2);
		
		lblP3 = new JLabel("");
		lblP3.setBounds(10, 120, 50, 50);
		panel_players.add(lblP3);
		
		lblP4 = new JLabel("");
		lblP4.setBounds(10, 170, 50, 50);
		panel_players.add(lblP4);
		
		lblp1 = new JLabel("");
		lblp1.setBounds(70, 40, 100, 14);
		panel_players.add(lblp1);
		
		lblp2 = new JLabel("");
		lblp2.setBounds(70, 90, 100, 14);
		panel_players.add(lblp2);
		
		lblp3 = new JLabel("");
		lblp3.setBounds(70, 140, 100, 14);
		panel_players.add(lblp3);
		
		lblp4 = new JLabel("");
		lblp4.setBounds(70, 190, 100, 14);
		panel_players.add(lblp4);
		
		lblp1t = new JLabel("");
		lblp1t.setBounds(180, 40, 46, 14);
		panel_players.add(lblp1t);
		
		lblp2t = new JLabel("");
		lblp2t.setBounds(180, 90, 46, 14);
		panel_players.add(lblp2t);
		
		lblp3t = new JLabel("");
		lblp3t.setBounds(180, 140, 46, 14);
		panel_players.add(lblp3t);
		
		lblp4t = new JLabel("");
		lblp4t.setBounds(180, 190, 46, 14);
		panel_players.add(lblp4t);
		
		textPaneStatus = new JTextPane();
		textPaneStatus.setBounds(20, 205, 480, 30);
		contentPane.add(textPaneStatus);
		
		lblPS = new JLabel[4];
		lblPS[0] = lblP1;
		lblPS[1] = lblP2;
		lblPS[2] = lblP3;
		lblPS[3] = lblP4;
		
		lblps = new JLabel[4];
		lblps[0] = lblp1;
		lblps[1] = lblp2;
		lblps[2] = lblp3;
		lblps[3] = lblp4;
		
		lblpst = new JLabel[4];
		lblpst[0] = lblp1t;
		lblpst[1] = lblp2t;
		lblpst[2] = lblp3t;
		lblpst[3] = lblp4t;
	}
	
	private void createEvents(){
		btnN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					gameplay.move(Direction.NORTH);
					textPaneStatus.setText("Player moved north.");
				} catch (IllegalMoveException e) {
					textPaneStatus.setText("Invalid movement.");
				} catch (IllegalActionException e) {
					textPaneStatus.setText("You cannot perform this action.");
				}
				paint();
			}
		});
		btnNE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					gameplay.move(Direction.NORTHEAST);
					textPaneStatus.setText("Player moved northeast.");
				} catch (IllegalMoveException e) {
					textPaneStatus.setText("Invalid movement.");
				} catch (IllegalActionException e) {
					textPaneStatus.setText("You cannot perform this action.");
				}
				paint();
			}
		});
		btnE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
				gameplay.move(Direction.EAST);
					textPaneStatus.setText("Player moved east.");
				} catch (IllegalMoveException e) {
					textPaneStatus.setText("Invalid movement.");
				} catch (IllegalActionException e) {
					textPaneStatus.setText("You cannot perform this action.");
				}
				paint();
			}
		});
		btnSE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					gameplay.move(Direction.SOUTHEAST);
					textPaneStatus.setText("Player moved southeast.");
				} catch (IllegalMoveException e) {
					textPaneStatus.setText("Invalid movement.");
				} catch (IllegalActionException e) {
					textPaneStatus.setText("You cannot perform this action.");
				}
				paint();
			}
		});
		btnS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					gameplay.move(Direction.SOUTH);
					textPaneStatus.setText("Player moved south.");
				} catch (IllegalMoveException e) {
					textPaneStatus.setText("Invalid movement.");
				} catch (IllegalActionException e) {
					textPaneStatus.setText("You cannot perform this action.");
				}
				paint();
			}
		});
		btnSW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					gameplay.move(Direction.SOUTHWEST);
					textPaneStatus.setText("Player moved southwest.");
				} catch (IllegalMoveException e) {
					textPaneStatus.setText("Invalid movement.");
				} catch (IllegalActionException e) {
					textPaneStatus.setText("You cannot perform this action.");
				}
				paint();
			}
		});
		btnW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					gameplay.move(Direction.WEST);
					textPaneStatus.setText("Player moved west.");
				} catch (IllegalMoveException e) {
					textPaneStatus.setText("Invalid movement.");
				} catch (IllegalActionException e) {
					textPaneStatus.setText("You cannot perform this action.");
				}
				paint();
			}
		});
		btnNW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					gameplay.move(Direction.NORTHWEST);
					textPaneStatus.setText("Player moved northwest.");
				} catch (IllegalMoveException e) {
					textPaneStatus.setText("Invalid movement.");
				} catch (IllegalActionException e) {
					textPaneStatus.setText("You cannot perform this action.");
				}
				paint();
			}
		});
		
		btnEndTurn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					gameplay.endTurn();
					textPaneStatus.setText("The player has ended his turn.");
				} catch (IllegalActionException a) {
					textPaneStatus.setText("You cannot end your turn.");
				}
				paint();
			}
		});
		
		btnI0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Item[] items = gameplay.showInventory();
				if(items[0] != null){
					try {
						if(items[0] instanceof IdentityDisk){
							new SelectionWindow(fourWinds, s);
						}
						gameplay.useItem(items[0],(Direction) s.getSelection());
					} catch (HaveToMoveException ex) {
						textPaneStatus.setText("You cannot end a turn on the position you started.");
					} catch (InvalidAttributesException ex) {
						textPaneStatus.setText("You cannot use this.");
					} catch (IllegalActionException ex) {
						textPaneStatus.setText("You cannot perform this action.");
					} catch (IllegalUseException ex) {
						textPaneStatus.setText("You cannot use this.");
					}
					s.clear();
					paint();
				} else{
					textPaneStatus.setText("You do not have an item to use in this slot.");
				}
			}
		});
		btnI1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Item[] items = gameplay.showInventory();
				if(items[1] != null){
					try {
						if(items[1] instanceof IdentityDisk){
							new SelectionWindow(fourWinds, s);
						}
						gameplay.useItem(items[1],(Direction) s.getSelection());
					} catch (HaveToMoveException ex) {
						textPaneStatus.setText("You cannot end a turn on the position you started.");
					} catch (InvalidAttributesException ex) {
						textPaneStatus.setText("You cannot use this.");
					} catch (IllegalActionException ex) {
						textPaneStatus.setText("You cannot perform this action.");
					} catch (IllegalUseException ex) {
						textPaneStatus.setText("You cannot use this.");
					}
					s.clear();
					paint();
				} else{
				textPaneStatus.setText("You do not have an item to use in this slot.");
				}
			}
		});
		btnI2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Item[] items = gameplay.showInventory();
				if(items[2] != null){
					try {
						if(items[2] instanceof IdentityDisk){
							new SelectionWindow(fourWinds, s);
						}
						gameplay.useItem(items[2],(Direction) s.getSelection());
					} catch (HaveToMoveException ex) {
						textPaneStatus.setText("You cannot end a turn on the position you started.");
					} catch (InvalidAttributesException ex) {
						textPaneStatus.setText("You cannot use this.");
					} catch (IllegalActionException ex) {
						textPaneStatus.setText("You cannot perform this action.");
					} catch (IllegalUseException ex) {
						textPaneStatus.setText("You cannot use this.");
					}
					s.clear();
					paint();
				} else{
					textPaneStatus.setText("You do not have an item to use in this slot.");
				}
			}
		});
		btnI3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Item[] items = gameplay.showInventory();
				if(items[3] != null){
					try {
						if(items[3] instanceof IdentityDisk){
							new SelectionWindow(fourWinds, s);
						}
						gameplay.useItem(items[3],(Direction) s.getSelection());
					} catch (HaveToMoveException ex) {
						textPaneStatus.setText("You cannot end a turn on the position you started.");
					} catch (InvalidAttributesException ex) {
						textPaneStatus.setText("You cannot use this.");
					} catch (IllegalActionException ex) {
						textPaneStatus.setText("You cannot perform this action.");
					} catch (IllegalUseException ex) {
						textPaneStatus.setText("You cannot use this.");
					}
					s.clear();
					paint();
				} else{
					textPaneStatus.setText("You do not have an item to use in this slot.");
				}
			}
		});
		btnI4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Item[] items = gameplay.showInventory();
				if(items[4] != null){
					try {
						if(items[4] instanceof IdentityDisk){
							new SelectionWindow(fourWinds, s);
						}
						gameplay.useItem(items[4],(Direction) s.getSelection());
					} catch (HaveToMoveException ex) {
						textPaneStatus.setText("You cannot end a turn on the position you started.");
					} catch (InvalidAttributesException ex) {
						textPaneStatus.setText("You cannot use this.");
					} catch (IllegalActionException ex) {
						textPaneStatus.setText("You cannot perform this action.");
					} catch (IllegalUseException ex) {
						textPaneStatus.setText("You cannot use this.");
					}
					s.clear();
					paint();
				} else{
					textPaneStatus.setText("You do not have an item to use in this slot.");
				}
			}
		});
		btnI5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Item[] items = gameplay.showInventory();
				if(items[5] != null){
					try {
						if(items[5] instanceof IdentityDisk){
							new SelectionWindow(fourWinds, s);
						}
						gameplay.useItem(items[5],(Direction) s.getSelection());
					} catch (HaveToMoveException ex) {
						textPaneStatus.setText("You cannot end a turn on the position you started.");
					} catch (InvalidAttributesException ex) {
						textPaneStatus.setText("You cannot use this.");
					} catch (IllegalActionException ex) {
						textPaneStatus.setText("You cannot perform this action.");
					} catch (IllegalUseException ex) {
						textPaneStatus.setText("You cannot use this.");
					}
					s.clear();
					paint();
				} else{
					textPaneStatus.setText("You do not have an item to use in this slot.");
				}
			}
		});
		
		btnPickup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean hasItem = false;
				ArrayList<Item> items = new ArrayList<Item>();
				for (Item it : gameplay.requestPickup()){
					if(it.canPickUp()){
						items.add(it);
						hasItem = true;
					}
				}
				Item[] it = new Item[items.size()];
				for(int i = 0; i < items.size(); i++){
					it[i] = items.get(i);
				}
				if (hasItem){
					new SelectionWindow(it, s);
					try {
						gameplay.pickup((Item) s.getSelection());
						textPaneStatus.setText("Item picked up.");
					} catch (InventoryFullException ex) {
						textPaneStatus.setText("You cannot pick up an item because your inventory is full.");
					} catch (HaveToMoveException ex) {
						textPaneStatus.setText("You cannot end a turn on the position you started.");
					} catch (IllegalPickupException ex) {
						textPaneStatus.setText("You cannot pick this up.");
					} catch (IllegalActionException ex) {
						textPaneStatus.setText("You cannot perform this action.");
					}
					paint();
				} else{
					textPaneStatus.setText("There is no item to pick up here.");
				}
			}
		});
	}
	
	private void createGame(int xDim, int yDim, String gm, int players){
		GameMode gamemode = null;
		if (gm.equals("Race")){
			gamemode = new Race();
		} else if (gm.equals("Ctf")){
			gamemode = new CTF();
		}
		String[] ps = new String[players];
		for(int i = 0; i < players; i++){
		ps[i] = "player" + (i+1);
		}
		try {
			Game game = new Game(ps, xDim, yDim, 3, gamemode);
			gameplay = new GamePlay(game);
			this.gamemode = gm;
			numberofplayers = players;
			game.start();
		} catch (DimensionException e1) {
			textPaneStatus.setText("Invalid dimension.");
		} catch (InvalidAttributesException e) {
			textPaneStatus.setText("Invalid parameters");
		} catch (IllegalPlayerNumberException e) {
			textPaneStatus.setText("Invalid number of players");
		}
	}
	
	private void setupField(){
		field = new JLabel[xDim][yDim];
		for (int i = 0; i < xDim; i++){
			for (int j = 0; j < yDim; j++){
				JLabel label = new JLabel("");
				label.setBounds(15 + (55 * i), (yDim * 55) - (55 * j) - 30, 50, 50);
				panel_playing_field.add(label);
				field[i][j] = label;
			}
		}
	}
	private void paint(){
		paintInventory();
		paintPlayers();
		paintPlayingField();
	}
	
	private void paintInventory(){
		for(int i = 0; i < inventory.length ; i++){
			JButton button = inventory[i];
			ImageIcon icon = cell;
			button.setEnabled(false);
			if (i < gameplay.showInventory().length){
				if (gameplay.showInventory()[i] != null){
					button.setEnabled(true);
					if (gameplay.showInventory()[i] instanceof LightGrenade){
						icon = lightGrenade;
					} else if (gameplay.showInventory()[i] instanceof ChargedIdentityDisk){
						icon = chargedIdDisk;
					} else if (gameplay.showInventory()[i] instanceof IdentityDisk){
						icon = idDisk;
					} else if (gameplay.showInventory()[i] instanceof Flag){
						for (int j = 0; j < numberofplayers; j++){
							if (((Flag) gameplay.showInventory()[i]).getStartingPos().getPlayer().getIndex() == (j+1))
								icon = flags[j];
						}
					}
				}
			}
			button.setIcon(icon);
		}
	}
	
	private void paintPlayers(){
		for (int i = 0; i < numberofplayers; i++){
			lblPS[i].setIcon(players[i]);
			lblps[i].setText("player " + (i+1));
			lblpst[i].setText("");
			if(gameplay.getTurn() == (i+1)){
				lblpst[i].setText(Integer.toString(gameplay.getNumberOfActionsLeft()));
			}
		}
	}
	
	private void paintPlayingField(){
		//Tekent alle objecten in de grid.
		HashMap<Position, ArrayList<OnSquare>> objects = gameplay.getOnsquares();
		for (Position pos : objects.keySet()){
			int i = pos.getX();
			int j = pos.getY();
			field[i][j].setIcon(cell);
			ArrayList<OnSquare> onsqs = objects.get(pos);
			for (OnSquare onsq : onsqs){
				System.out.println(onsq);
				if (onsq.isVisible()){
					if (onsq instanceof Player){
						for (int k = 0; k < numberofplayers; k ++){
							if (((Player) onsq).getIndex() == (k+1)){
								field[i][j].setIcon(players[k]);
							}
						}
					}
					if (onsq instanceof StartingPosition){
						for (int k = 0; k < numberofplayers; k ++){
							if (((StartingPosition) onsq).getPlayer().getIndex() == (k+1)){
								field[i][j].setIcon(finish[k]);
							}
						}
					}
					if (onsq instanceof Flag){
						for (int k = 0; k < numberofplayers; k ++){
							if (((Flag) onsq).getStartingPos().getPlayer().getIndex() == (k+1)){
								field[i][j].setIcon(flags[k]);
							}
						}
					}
					if (onsq instanceof Wall){
						field[i][j].setIcon(wall);
					}
					if (onsq instanceof LightTrail){
						field[i][j].setIcon(lightTrail);
					}
					if (onsq instanceof Teleporter){
						field[i][j].setIcon(teleporter);
					}
					if (onsq instanceof ForcefieldGenerator){
						if(onsq.isActive()){
							field[i][j].setIcon(forceFieldGeneratorActive);
						} else {
							field[i][j].setIcon(forceFieldGeneratorInactive);
						}
					}
					if (onsq instanceof Forcefield){
						field[i][j].setIcon(forceField);
					}
					if (onsq instanceof PowerFailure){
						field[i][j].setIcon(powerFailure);
					}
					if (onsq instanceof LightGrenade && !((LightGrenade) onsq).isUsed()){
						field[i][j].setIcon(lightGrenade);
					}
					if (onsq instanceof ChargedIdentityDisk){
						field[i][j].setIcon(chargedIdDisk);
					}
					if (onsq instanceof IdentityDisk){
						field[i][j].setIcon(idDisk);
					} 
				}
			}
		}
	}
}
