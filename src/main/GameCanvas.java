package main;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

/**
 * The canvas object with the game board in it
 * @author Alex Pickering
 */
@SuppressWarnings("serial")
public class GameCanvas extends Canvas {
	//Mouse variables
	int mouseX = 0, mouseY = 0;
	boolean mousePressed = false;
	
	//Graphics Variables
	/*BufferedImage img;*/
	
	//Game variables
	int[][] board = new int[3][3];
	
	/**
	 * Default constructor
	 */
	public GameCanvas() {
		/*img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);*/
		
		Random r = new Random();
		for(int i = 0; i < board.length; i++)
			for(int j = 0; j < board[i].length; j++)
				board[i][j] = r.nextInt(2) + 1;
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
				if(mouseX >= (getWidth() / 3) * i && mouseX < (getWidth() / 3) * (i + 1) &&
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
		g.setFont(new Font(getFont().getFontName(), Font.PLAIN, 36));
		g.setColor(new Color(0, 0, 0));
		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				if(board[i][j] == 1)
					g.drawString("X", 0, 0);
				else if(board[i][j] == 2)
					g.drawString("O", 0, 0);
				else if(board[i][j] != 0)	//Board value is outside range
					throw new IllegalArgumentException("Board value at (" + i + ", " + j + ") is invalid: " + board[i][j]);
		
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
	void updateMouse(int x, int y, boolean p) {
		mouseX = x;
		mouseY = y;
		mousePressed = p;
		
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
	 * Creates the canvas image once possible
	 */
	public void createImage() {
		//img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		createBufferStrategy(2);
	}
}
