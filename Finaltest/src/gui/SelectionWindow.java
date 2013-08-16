package gui;

import grid.Direction;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SelectionWindow extends JFrame {

	private JPanel contentPane;
	private JList<Direction> list;
	private JButton btnUse;

	/**
	 * This frame is used to select a direction in which an item is to be used.
	 */
	public SelectionWindow(Object[] List, final Selection s) {
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 170, 162);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		list = new JList(List);
		list.setBounds(10, 31, 127, 29);
		contentPane.add(list);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		
		JLabel lblSelectADirection = new JLabel("Select a direction");
		lblSelectADirection.setBounds(10, 11, 107, 14);
		contentPane.add(lblSelectADirection);
		
		btnUse = new JButton("Use");
		btnUse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(list.getSelectedValue() != null){
					s.setSelection(list.getSelectedValue());
					dispose();
				}
			}
		});
		btnUse.setBounds(20, 71, 100, 50);
		contentPane.add(btnUse);
	}
}
