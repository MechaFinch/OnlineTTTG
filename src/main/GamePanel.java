package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import connection.Connection;
import connection.ServerConnection;
import state.EndState;
import state.TurnState;

/**
 * The overarching panel for the game. Handles the game itself.
 * @author Alex Pickering
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener{
	//Window switching main class instance
	OnlineTicTacToe m;
	
	//Window variables
	GameCanvas canvas = new GameCanvas();
	
	GameButtons buttons = new GameButtons(this);
	
	JLabel gameMessage = new JLabel("Starting...");
	
	//Mouse variables
	int mouseX = 0, mouseY = 0;
	
	boolean mousePressed = false, mouseOnCanvas = false;
	
	//Game variables
	int[][] board = new int[3][3];
	int frameRate = 20;
	boolean player = false;
	EndState endState = EndState.IN_PROGRESS;
	TurnState turn = TurnState.THEIRS;
	Connection con;	//The connection, may be a server or client
	
	boolean canvasStarted = false;
	
	/**
	 * Default constructor
	 * @param m Instance of Main
	 */
	public GamePanel(OnlineTicTacToe m) {
		//Allow panel switching
		this.m = m;
		
		//Create layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//Setup canvas parameters
		canvas.setSize(300, 300);
		canvas.setBackground(new Color(255, 255, 255));
		
		//Setup labels
		gameMessage.setFont(new Font(getFont().getFontName(), Font.PLAIN, 20));
		gameMessage.setAlignmentX(CENTER_ALIGNMENT);
		
		//Add listeners
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		
		//Add components
		add(canvas);
		add(gameMessage);
		add(buttons);
	}
	
	/**
	 * Creates the canvas properly
	 */
	public void initCanvas() {
		if(canvasStarted) return;	//Don't start it multiple times
		
		canvas.setSize(500, 430);
		canvas.setMaximumSize(canvas.getSize());
		
		canvas.createImage();
		new CUpdater(canvas, frameRate).start();
		canvasStarted = true;
	}
	
	/**
	 * Sets the connection object
	 * @param c The Connection object to use
	 */
	public void setConnection(Connection c) {
		con = c;
	}
	
	/**
	 * Sets whether the player is connecting or hosting
	 * @param player What type of player this is. Hosting = true
	 */
	public void setPlayer(boolean player) {
		this.player = player;
		if(player)
			turn = TurnState.YOURS;
		else {
			turn = TurnState.THEIRS;
			startPlaying();
		}	
	}
	
	/**
	 * Trys to connect with the given connection object
	 * @return Status
	 * @throws IOException 
	 */
	public String tryConnection() throws IOException {
		if(con == null) {
			System.out.println(con);
			return "Connection not created";
		}
		
		return con.connect();
	}
	
	/**
	 * Checks if anyone has won the game
	 * @return A new EndState for who won
	 */
	public EndState checkForWin() {
		boolean x;
		
		//HT, VL, DLR
		if(board[0][0] != 0) {
			x = board[0][0] == 1;
			
			//HT
			if(board[0][0] == board[1][0] && board[1][0] == board[2][0])
				return x ? EndState.HORIZONTAL_TOP_X : EndState.HORIZONTAL_TOP_O;
			
			//VL
			if(board[0][0] == board[0][1] && board[0][1] == board[0][2])
				return x ? EndState.VERTICAL_LEFT_X : EndState.VERTICAL_LEFT_O;
			
			//DLR
			if(board[0][0] == board[1][1] && board[1][1] == board[2][2])
				return x ? EndState.DIAGONAL_LR_X : EndState.DIAGONAL_LR_O;
		}
		
		//HC, VC, DRL
		if(board[1][1] != 0) {
			x = board[1][1] == 1;
			
			//HC
			if(board[0][1] == board[1][1] && board[1][1] == board[2][1])
				return x ? EndState.HORIZONTAL_CENTER_X : EndState.HORIZONTAL_CENTER_O;
			
			//VC
			if(board[1][0] == board[1][1] && board[1][1] == board[1][2])
				return x ? EndState.VERTICAL_CENTER_X : EndState.VERTICAL_CENTER_O;
			
			//DRL
			if(board[0][2] == board[1][1] && board[1][1] == board[2][0])
				return x ? EndState.DIAGONAL_RL_X : EndState.DIAGONAL_RL_O;
		}
		
		//HB, VR
		if(board[2][2] != 0) {
			x = board[2][2] == 1;
			
			//HB
			if(board[0][2] == board[1][2] && board[1][2] == board[2][2])
				return x ? EndState.HORIZONTAL_BOTTOM_X : EndState.HORIZONTAL_BOTTOM_O;
			
			//VR
			if(board[2][0] == board[2][1] && board[2][1] == board[2][2])
				return x ? EndState.VERTICAL_RIGHT_X : EndState.VERTICAL_RIGHT_O;
		}
		
		//No wins found
		return EndState.IN_PROGRESS;
	}
	
	/**
	 * Swaps the turn and disables/enables buttons
	 */
	void updateTurn(TurnState t) {
		turn = t;
		
		switch(turn) {
			case YOURS:
				gameMessage.setText("It is your turn.");
				break;
			
			case THEIRS:
				gameMessage.setText("It is their turn.");
				break;
			
			case END:
				if(endState.getPlayer() == "x") {
					gameMessage.setText(player ? "You win!" : "You lose.");
				} else {
					gameMessage.setText(player ? "You lose." : "You win!");
				}
				break;
		}
		
		canvas.updateGame(board, turn);
	}
	
	/**
	 * Checks that the square the player selected is unoccupied and updates the game
	 * Tells the server that a selection has been made if it is
	 */
	void validateAndSend(){
		//Check that the square is unoccupied
		int x = -1, y = -1, width = canvas.getWidth(), height = canvas.getHeight();
		
		check:	//Breaking label
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				if(mouseX >= (width / 3) * i && mouseX < (width / 3) * (i + 1) &&
				   mouseY >= (height / 3) * j && mouseY < (height / 3) * (j + 1)) {
					x = i;
					y = j;
					break check;
				}
		
		if(board[x][y] != 0)	//Board space is occupied
			return;
		
		//Tell server
		if(player) {	//This game is hosting
			try {	//Tell the client the selection
				con.send("select " + x + " " + y);
				board[x][y] = 1;
				updateTurn(TurnState.THEIRS);
				canvas.updateGame(board, turn);
				
				//Get client's selection
				String[] move = con.receive().split(" ");
				board[Integer.parseInt(move[1])][Integer.parseInt(move[2])] = 2;
				updateTurn(TurnState.YOURS);
				canvas.updateGame(board, turn);
			} catch(IllegalStateException e) {
				e.printStackTrace();
			} catch(IOException e) {
				gameMessage.setText("IOException while sending or receiving selection");
				e.printStackTrace();
			}
		} else {	//This game is connecting
			try {	//Tell the server the selection
				con.send("select " + x + " " + y);
				board[x][y] = 2;
				updateTurn(TurnState.THEIRS);
				canvas.updateGame(board, turn);
				
				//Get the host's selection
				String[] move = con.receive().split(" ");
				board[Integer.parseInt(move[1])][Integer.parseInt(move[2])] = 1;
				updateTurn(TurnState.YOURS);
				canvas.updateGame(board, turn);
			} catch(IllegalStateException e) {
				e.printStackTrace();
			} catch(IOException e) {
				gameMessage.setText("IOException when sending or receiving selection");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Client-only method run to start listening for the host's selection the first time
	 */
	void startPlaying() {
		//Get the host's selection
		String[] move;
		try {
			move = con.receive().split(" ");
			board[Integer.parseInt(move[1])][Integer.parseInt(move[2])] = 1;
			updateTurn(TurnState.YOURS);
			canvas.updateGame(board, turn);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		} catch(IOException e) {
			gameMessage.setText("IOException when sending or receiving selection");
			e.printStackTrace();
		}
	}
	
	/**
	 * ActionListener method for the buttons
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
			case "test1":	//Tests
				endState = checkForWin();
				canvas.setEndState(endState);
				break;
			
			case "test2":
				Random r = new Random();
				for(int i = 0; i < board.length; i++)
					for(int j = 0; j < board[i].length; j++)
						board[i][j] = r.nextInt(3);
				
				switch(turn) {
					case END:
						break;
					
					case THEIRS:
						updateTurn(TurnState.YOURS);
						break;
					
					case YOURS:
						updateTurn(TurnState.THEIRS);
						break;
					
					default:
						break;
				}
				
				canvas.updateGame(board, turn);
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
		
		if(turn == TurnState.YOURS)	//validate selected square and tell the server
			validateAndSend();
		
		mousePressed = true;
		canvas.updateMouse(mouseX, mouseY, mousePressed, mouseOnCanvas);
	}
	
	/**
	 * Used MouseListener method
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		//System.out.println("Mouse RELEASED at " + e.getX() + ", " + e.getY());
		
		mousePressed = false;
		canvas.updateMouse(mouseX, mouseY, mousePressed, mouseOnCanvas);
	}
	
	/**
	 * Used MouseListener method
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		//System.out.println("Mouse DRAGGED to " + e.getX() + ", " + e.getY());
		
		mouseX = e.getX();
		mouseY = e.getY();
		canvas.updateMouse(mouseX, mouseY, mousePressed, mouseOnCanvas);
	}
	
	/**
	 * Used MouseListener method
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		//System.out.println("Mouse MOVED to " + e.getX() + ", " + e.getY());
		
		mouseX = e.getX();
		mouseY = e.getY();
		canvas.updateMouse(mouseX, mouseY, mousePressed, mouseOnCanvas);
	}
	
	/**
	 * Used MouseListener method
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		mouseOnCanvas = true;
		canvas.updateMouse(mouseX, mouseY, mousePressed, mouseOnCanvas);
	}
	
	/**
	 * Used MouseListener method
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		mouseOnCanvas = false;
		canvas.updateMouse(mouseX, mouseY, mousePressed, mouseOnCanvas);
	}
	
	//MouseListener Methods (Unused)
	@Override
	public void mouseClicked(MouseEvent e) {
	}
}