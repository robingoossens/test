package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;

public class CreateGame extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldX;
	private JTextField textFieldY;
	private JButton btnCreateGame;
	private JButton btnExit;
	private JLabel lblNumberOfPlayers;
	private JTextField textFieldP;
	private JToggleButton btnRace;
	private JToggleButton btnCTF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateGame frame = new CreateGame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CreateGame() {
		setup();
		createEvents();
	}
	private void setup(){
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 245, 298);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnCreateGame = new JButton("Create Game");
		btnCreateGame.setBounds(10, 203, 100, 50);
		contentPane.add(btnCreateGame);
		
		JLabel lblXDimension = new JLabel("X dimension");
		lblXDimension.setBounds(10, 11, 91, 14);
		contentPane.add(lblXDimension);
		
		JLabel lblYDimension = new JLabel("Y dimension");
		lblYDimension.setBounds(10, 36, 91, 14);
		contentPane.add(lblYDimension);
		
		textFieldX = new JTextField();
		textFieldX.setBounds(134, 8, 86, 20);
		contentPane.add(textFieldX);
		textFieldX.setColumns(10);
		
		textFieldY = new JTextField();
		textFieldY.setColumns(10);
		textFieldY.setBounds(134, 33, 86, 20);
		contentPane.add(textFieldY);
		
		btnExit = new JButton("Exit");
		btnExit.setBounds(120, 203, 100, 50);
		contentPane.add(btnExit);
		
		JLabel lblSelectGameMode = new JLabel("Select game mode");
		lblSelectGameMode.setBounds(10, 117, 151, 14);
		contentPane.add(lblSelectGameMode);
		
		lblNumberOfPlayers = new JLabel("Number of players");
		lblNumberOfPlayers.setBounds(10, 61, 118, 14);
		contentPane.add(lblNumberOfPlayers);
		
		textFieldP = new JTextField();
		textFieldP.setBounds(10, 86, 86, 20);
		contentPane.add(textFieldP);
		textFieldP.setColumns(10);
		
		btnRace = new JToggleButton("Race");

		btnRace.setBounds(10, 142, 100, 50);
		contentPane.add(btnRace);
		
		btnCTF = new JToggleButton("CTF");
		btnCTF.setBounds(120, 142, 100, 50);
		contentPane.add(btnCTF);
		
	}
	
	private void createEvents(){
		btnCreateGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Integer.parseInt(textFieldX.getText()) > 9 && Integer.parseInt(textFieldY.getText()) > 9){
					if((btnRace.isSelected())){
						if(Integer.parseInt(textFieldP.getText()) > 0 && Integer.parseInt(textFieldP.getText()) < 3){
							new SwingGUI(Integer.parseInt(textFieldX.getText()),Integer.parseInt(textFieldY.getText()),"Race",Integer.parseInt(textFieldP.getText()));
							dispose();
						} else {
							new ErrorWindow("Select a correct number of players for this game mode.");
						}
					} else if (btnCTF.isSelected()){
						if(Integer.parseInt(textFieldP.getText()) > 0 && Integer.parseInt(textFieldP.getText()) < 5){
						new SwingGUI(Integer.parseInt(textFieldX.getText()),Integer.parseInt(textFieldY.getText()),"Ctf",Integer.parseInt(textFieldP.getText()));
						dispose();
						} else {
							new ErrorWindow("Select a correct number of players for this game mode.");
						}
					} else {
						new ErrorWindow("Select a game mode.");
					}
				} else {
					new ErrorWindow("Input correct dimensions.");
				}
			}
		});
		
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		btnRace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnRace.setSelected(true);
				btnCTF.setSelected(false);
			}
		});
		btnCTF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnCTF.setSelected(true);
				btnRace.setSelected(false);
			}
		});
	}
}
