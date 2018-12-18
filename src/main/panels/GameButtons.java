package main.panels;

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
	JButton back = new JButton("Back"),
			cont = new JButton("Continue");
	
	boolean continueEnabled = false;
	
	/**
	 * Default Constructor
	 * @param al The ActionListener to be attached to them
	 */
	public GameButtons(ActionListener al) {
		//Add listeners to the buttons
		back.addActionListener(al);
		cont.addActionListener(al);
		
		//Set the button ids
		back.setActionCommand("back");
		cont.setActionCommand("continue");
		
		//Get them in the panel
		add(back);
	}
	
	public void toggleContinue() {
		if(continueEnabled) {
			remove(cont);
		} else {
			add(cont);
		}
		
		continueEnabled = !continueEnabled;
	}
}
