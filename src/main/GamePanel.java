package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import connection.Connection;

/**
 * The overarching panel for the game
 * @author Alex Pickering
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener{
	//Window switching Main instance
	Main m;
	
	//Window variables
	GameCanvas canvas = new GameCanvas();
	
	GameButtons buttons = new GameButtons(this);
	
	//Mouse variables
	int mouseX = 0, mouseY = 0;
	
	boolean mousePressed = false;
	
	//Game variables
	int[][] board = new int[3][3];
	int frameRate = 20;
	Connection con;	//The connection, may be a server or client
	
	boolean canvasStarted = false;
	
	/**
	 * Default constructor
	 * @param m Instance of Main
	 */
	public GamePanel(Main m) {
		//Allow panel switching
		this.m = m;
		
		//Create layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//Setup canvas parameters
		canvas.setSize(300, 300);
		canvas.setBackground(new Color(255, 255, 255));
		
		//Add components
		add(canvas);
		add(buttons);
		
		//Add listeners
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
	}
	
	/**
	 * Creates the canvas properly
	 */
	public void initCanvas() {
		if(canvasStarted) return;	//Don't start it multiple times
		canvas.createImage();
		new CUpdater(canvas, frameRate).start();
		canvasStarted = true;
	}
	
	/**
	 * Sets the connection object
	 * @param c The Connection object to use
	 */
	public void setConnection(Connection c) {
		if(c != null) return;	//Don't set it twice? TODO: Confirm if this can be changed
		con = c;
	}
	
	/**
	 * ActionListener method for the buttons
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
			case "test1":	//Tests
				System.out.println("bepis");
				break;
			
			case "test2":
				System.out.println("conke");
				break;
			
			case "back":	//Switch panel to the connect/host/menu screen
				back();
				break;
			
			default:	//Something went wrong
				System.out.println("aaaaAAAAA");
				break;
		}
	}
	
	/**
	 * Switches the panel back to the connect/host/menu screen
	 */
	void back() {
		((CardLayout)(m.panel.getLayout())).show(m.panel, "Connect Panel");
	}
	
	/**
	 * Used MouseListener Method
	 * These are applied to the canvas object atm
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		//System.out.println("Mouse PRESSED at " + e.getX() + ", " + e.getY());
		
		mousePressed = true;
		canvas.updateMouse(mouseX, mouseY, mousePressed);
	}
	
	/**
	 * Used MouseListener method
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		//System.out.println("Mouse RELEASED at " + e.getX() + ", " + e.getY());
		
		mousePressed = false;
		canvas.updateMouse(mouseX, mouseY, mousePressed);
	}
	
	/**
	 * Used MouseListener method
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		//System.out.println("Mouse DRAGGED to " + e.getX() + ", " + e.getY());
		
		mouseX = e.getX();
		mouseY = e.getY();
		canvas.updateMouse(mouseX, mouseY, mousePressed);
	}
	
	/**
	 * Used MouseListener method
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		//System.out.println("Mouse MOVED to " + e.getX() + ", " + e.getY());
		
		mouseX = e.getX();
		mouseY = e.getY();
		canvas.updateMouse(mouseX, mouseY, mousePressed);
	}
	
	//MouseListener Methods (Unused)
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
}