package main;

import java.awt.event.ActionEvent;
import java.io.IOException;

import connection.Connection;

/**
 * Listens for the other player's response with their turn data, sends an event to the game panel
 * @author 21jpickering
 */
public class TurnListener implements Runnable {
	GamePanel p;
	Connection c;
	
	@Override
	public void run() {
		try {
			String s = c.receive();
			
			ActionEvent e = new ActionEvent(this, 0, "turnFinished " + s);
			p.actionPerformed(e);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		} catch(IOException e) {
			if(e.getMessage().contains("Connection reset")) {
				p.gameMessage.setText("Connection Lost");
			} else {
				p.gameMessage.setText("IO Exception while listening for response");
			}
			
			e.printStackTrace();
		}
	}
	
	public TurnListener(GamePanel p, Connection c) {
		this.p = p;
		this.c = c;
	}
}
