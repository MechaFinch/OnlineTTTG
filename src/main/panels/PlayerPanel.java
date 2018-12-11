package main.panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.OnlineTicTacToe;
import main.PlayerData;

/**
 * The panel with player information and controls
 * @author Alex Pickering
 */
@SuppressWarnings("serial")
public class PlayerPanel extends JPanel implements ActionListener{
	//Instance of main to switch windows
	OnlineTicTacToe m;
	
	//Graphics objects
	JLabel title = new JLabel("Player Data");
	
	JTextArea dataArea = new JTextArea("No Data", 14, 20);
	
	JTextField nameField = new JTextField() {{setSize(100, 30); setMaximumSize(getSize());}};
	
	JButton backButton = new JButton("Back") {{setSize(100, 50); setMaximumSize(getSize());}},
			loadButton = new JButton("Load") {{setSize(100, 50); setMaximumSize(getSize());}},
			createButton = new JButton("Create") {{setSize(100, 50); setMaximumSize(getSize());}};
	
	//Player data
	PlayerData playerData;
	
	/**
	 * Panel screen for the player info menu
	 * @param m
	 */
	public PlayerPanel(OnlineTicTacToe m) {
		this.m = m;
		
		//Set layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//Set font
		title.setFont(new Font(getFont().getFontName(), Font.PLAIN, 36));
		
		//Setup commands
		nameField.setActionCommand("nameSubmit");
		
		loadButton.setActionCommand("load");
		createButton.setActionCommand("create");
		backButton.setActionCommand("back");
		
		//Setup listeners
		nameField.addActionListener(this);
		
		loadButton.addActionListener(this);
		createButton.addActionListener(this);
		backButton.addActionListener(this);
		
		//Setup alignments
		title.setAlignmentX(CENTER_ALIGNMENT);
		
		dataArea.setAlignmentX(CENTER_ALIGNMENT);
		
		nameField.setAlignmentX(CENTER_ALIGNMENT);
		
		loadButton.setAlignmentX(CENTER_ALIGNMENT);
		createButton.setAlignmentX(CENTER_ALIGNMENT);
		backButton.setAlignmentX(CENTER_ALIGNMENT);
		
		//Add components with rigidArea separators
		add(title);
		add(Box.createRigidArea(new Dimension(0, 10)));
		
		add(dataArea);
		add(Box.createRigidArea(new Dimension(0, 10)));
		
		add(nameField);
		add(loadButton);
		add(createButton);
		add(Box.createRigidArea(new Dimension(0, 10)));
		
		add(backButton);
	}
	
	public PlayerData getPlayerData() {
		return playerData;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}
}
