package main;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * The canvas object with the game board in it
 * @author Alex Pickering
 */
@SuppressWarnings("serial")
public class GameCanvas extends Canvas {
	//Mouse variables
	int mouseX = 0, mouseY = 0;
	boolean mousePressed = false, mouseOnCanvas = false;
	
	//Graphics Variables
	/*BufferedImage img;*/
	
	//Game variables
	int[][] board = new int[3][3];
	boolean gameEnded = false;
	EndState endState = EndState.IN_PROGRESS;
	
	/**
	 * Default constructor
	 */
	public GameCanvas() {
		/*img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);*/
	}
	
	/**
	 * Paint override, draws the board
	 */
	@Override
	public void paint(Graphics g1d) {
		Graphics2D g = (Graphics2D) /*img.getGraphics();*/ g1d;
		g.setStroke(new BasicStroke((float) 2));
		
		//Draw background squares, darker if the mouse is over them and darker still if it is clicked
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++) {
				if(mouseOnCanvas && mouseX >= (getWidth() / 3) * i && mouseX < (getWidth() / 3) * (i + 1) &&
                   mouseY >= (getHeight() / 3) * j && mouseY < (getHeight() / 3) * (j + 1))
					if(mousePressed)
						g.setColor(new Color(0, 200, 210));
					else
						g.setColor(new Color(40, 240, 250));
				else
					g.setColor(new Color(150, 240, 250));
				
				g.fillRect((getWidth() / 3) * i, (getHeight() / 3) * j, getWidth(), getHeight());
			}
		
		//Draw border boxes
		g.setColor(new Color(0, 0, 0));
		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				g.drawRect((getWidth() / 3) * i, (getHeight() / 3) * j, getWidth() / 3, getHeight() / 3);
		
		//Draw x's and o's
		g.setFont(new Font(getFont().getFontName(), Font.PLAIN, 100));
		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++) {
				Rectangle r = new Rectangle((getWidth() / 3) * i, (getHeight() / 3) * j, getWidth() / 3, getHeight() / 3);
				
				if(board[i][j] == 1)
					//g.drawString("X", (getWidth() / 3) * i, (getHeight() / 3) * (j + 1));
					drawCenteredString(g, "X", r, g.getFont());
				else if(board[i][j] == 2)
					drawCenteredString(g, "O", r, g.getFont());
				else if(board[i][j] != 0)	//Board value is outside range
					throw new IllegalArgumentException("Board value at (" + i + ", " + j + ") is invalid: " + board[i][j]);
			}
		
		//Draw line through board if someone won
		g.setStroke(new BasicStroke((float) 5));
		int w = getWidth() / 3,
			h = getHeight() / 3,
			wc = w / 4,
			hc = h / 4;
		
		switch(endState) {
			case IN_PROGRESS:
				break;
			
			case HORIZONTAL_TOP_X: case HORIZONTAL_TOP_O:
				g.drawLine(wc, hc * 2, (w * 2) + (wc * 3), hc * 2);
				break;
			
			case HORIZONTAL_CENTER_X: case HORIZONTAL_CENTER_O:
				g.drawLine(wc, h + (hc * 2), (w * 2) + (wc * 3), h + (hc * 2));
				break;
			
			case HORIZONTAL_BOTTOM_X: case HORIZONTAL_BOTTOM_O:
				g.drawLine(wc, (h * 2) + (hc * 2), (w * 2) + (wc * 3), (h * 2) + (hc * 2));
				break;
			
			case VERTICAL_LEFT_X: case VERTICAL_LEFT_O:
				g.drawLine(wc * 2, hc, wc * 2, (h * 2) + (hc * 3));
				break;
			
			case VERTICAL_CENTER_X: case VERTICAL_CENTER_O:
				g.drawLine(w + (wc * 2), hc, w + (wc * 2), (h * 2) + (hc * 3));
				break;
			
			case VERTICAL_RIGHT_X: case VERTICAL_RIGHT_O:
				g.drawLine((w * 2) + (wc * 2), hc, (w * 2) + (wc * 2), (h * 2) + (hc * 3));
				break;
			
			case DIAGONAL_LR_X: case DIAGONAL_LR_O:
				g.drawLine(wc, hc, (w * 2) + (wc * 3), (h * 2) + (hc * 3));
				break;
			
			case DIAGONAL_RL_X: case DIAGONAL_RL_O:
				g.drawLine((w * 2) + (wc * 3), hc, wc, (h * 2) + (hc * 3));
				break;
		}
		
		//Draw img to the screen
		/*g1d.drawImage(img, 0, 0, null);*/
		try {	//This may give a npe if the main thread is too slow
			getBufferStrategy().show();
		} catch(NullPointerException e) {
			System.out.println("Paint called too early");
		}
	}
	
	/**
	 * Updates mouse variables, called each time the listener in GamePanel gets something useful
	 * @param x The x-coordinate of the mouse on the canvas
	 * @param y The y-coordinate of the mouse on the canvas
	 * @param p Weather or not the mouse is pressed
	 */
	void updateMouse(int x, int y, boolean p, boolean c) {
		mouseX = x;
		mouseY = y;
		mousePressed = p;
		mouseOnCanvas = c;
		
		//System.out.println(mouseX + " " + mouseY + " " + mousePressed);
	}
	
	/**
	 * Updates game variables relevant to graphics
	 * @param b The board object
	 */
	void updateGame(int[][] b) {
		board = b;
	}
	
	/**
	 * Updates the end state of the game for graphics
	 * @param es The new end state to use
	 */
	void setEndState(EndState es) {
		endState = es;
	}
	
	/**
	 * Creates the canvas image once possible
	 */
	public void createImage() {
		//img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		createBufferStrategy(2);
	}
	
	/**
	 * Draw a String centered in the middle of a Rectangle.
	 *
	 * @param g The Graphics instance.
	 * @param text The String to draw.
	 * @param rect The Rectangle to center the text in.
	 * @author Daniel Kvist
	 */
	public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    // Set the font
	    g.setFont(font);
	    // Draw the String
	    g.drawString(text, x, y);
	}
}
