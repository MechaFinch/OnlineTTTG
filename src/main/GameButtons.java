package main;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The panel with the button controls for the game
 * @author Alex Pickering
 */
@SuppressWarnings("serial")
public class GameButtons extends JPanel {
	//Them Buttons
	JButton test1 = new JButton("Test 1"),
			test2 = new JButton("Test 2"),
			back = new JButton("Back");
	
	/**
	 * Default Constructor
	 * @param al The ActionListener to be attached to them
	 */
	public GameButtons(ActionListener al) {
		//Add listeners to the buttons
		test1.addActionListener(al);
		test2.addActionListener(al);
		back.addActionListener(al);
		
		//Set the button ids
		test1.setActionCommand("test1");
		test2.setActionCommand("test2");
		back.setActionCommand("back");
		
		//Get them in the panel
		add(test1);
		add(test2);
		add(back);
	}
}
