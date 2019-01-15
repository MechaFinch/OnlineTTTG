package main.panels;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
	JLabel title = new JLabel("Player Data"),
		   fileMessage = new JLabel("");
	
	JTextField nameField = new JTextField() {{setSize(100, 30); setMaximumSize(getSize());}};
	
	JTextArea dataArea = new JTextArea("No Data");
	
	JScrollPane dataPane = new JScrollPane(dataArea) {{setSize(400, 240); setMaximumSize(getSize());}};
	
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
		load("Guest");
		
		//Set layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//Set font
		title.setFont(new Font(getFont().getFontName(), Font.PLAIN, 36));
		
		//Setup text area
		dataArea.setEditable(false);
		
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
		fileMessage.setAlignmentX(CENTER_ALIGNMENT);
		
		dataPane.setAlignmentX(CENTER_ALIGNMENT);
		
		nameField.setAlignmentX(CENTER_ALIGNMENT);
		
		loadButton.setAlignmentX(CENTER_ALIGNMENT);
		createButton.setAlignmentX(CENTER_ALIGNMENT);
		backButton.setAlignmentX(CENTER_ALIGNMENT);
		
		//Add components with rigidArea separators
		add(title);
		add(Box.createRigidArea(new Dimension(0, 10)));
		
		add(dataPane);
		add(nameField);
		add(loadButton);
		add(createButton);
		add(fileMessage);
		add(Box.createRigidArea(new Dimension(0, 10)));
		
		add(backButton);
	}
	
	/**
	 * Updates the textarea with the player's data
	 */
	public void updateDataArea() {
		String s = playerData.getName() + "\n" +
				   "\n" + 
				   "Wins\n" + 
				   playerData.getWins() + "\n" +
				   "\n" +
				   "Losses\n" +
				   playerData.getLosses() + "\n" +
				   "\n" +
				   "Draws\n" +
				   playerData.getDraws() + "\n" +
				   "\n" +
				   "Plays\n" +
				   playerData.getPlays();
		
		dataArea.setText("");
		dataArea.setText(s);
	}
	
	/**
	 * Loads player data from the given name
	 * @param name The name of the player to load
	 */
	void load(String name) {
		File f = new File("players/" + name + ".pd");
		
		try {
			playerData = new PlayerData(f);
		} catch(FileNotFoundException e) {
			if(name.equals("Guest")) {
				playerData = new PlayerData("Guest");
				try {
					playerData.save();
				} catch(IOException e1) {
					e1.printStackTrace();
				}
			} else {
				fileMessage.setText("Player data not found");
			}
		} catch(IOException e) {
			fileMessage.setText("IOException while loading data");
			e.printStackTrace();
		}
		
		updateDataArea();
	}
	
	/**
	 * Loads player data based on the name given in the text field
	 */
	void loadPlayer() {
		String s = nameField.getText();
		
		if(s.equals(null) || s.equals("")) {
			fileMessage.setText("Please enter a name to load");
			return;
		}
		
		if(!fileMessage.getText().equals("")) fileMessage.setText("");
		
		load(s);
	}
	
	/**
	 * Creates a new empty save
	 */
	void createPlayer() {
		String s = nameField.getText();
		
		if(s.equals("") || s.equals(null)) {
			fileMessage.setText("Please enter a name");
			return;
		} else if(new File("players/" + s + ".pd").exists()) {
			int sel = JOptionPane.showConfirmDialog(this, "This will overwrite a save. Continue?", "Overwite", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			
			if(sel == 2) return;
		}
		
		if(!fileMessage.getText().equals("")) fileMessage.setText("");
		
		playerData = new PlayerData(s);
		
		try {
			playerData.save();
			
			fileMessage.setText("Player Data created successfully");
		} catch(IOException e) {
			fileMessage.setText("IOException while saving new player data");
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns to the main menu
	 */
	void back() {
		((CardLayout)(m.panel.getLayout())).show(m.panel, "Connect Panel");
	}
	
	/**
	 * Gets the player's data
	 * @return The player data
	 */
	public PlayerData getPlayerData() {
		return playerData;
	}
	
	/**
	 * Updates the player's data
	 * @param pd The new version of playerData
	 */
	public void setPlayerData(PlayerData pd) {
		playerData = pd;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
			case "load":
				loadPlayer();
				break;
			
			case "create":
				createPlayer();
				break;
			
			case "back":
				back();
				break;
			
			default:
				System.out.println(e.getActionCommand());
		}
	}
}
