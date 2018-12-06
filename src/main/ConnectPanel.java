package main;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import connection.ClientConnection;
import connection.Connection;
import connection.ServerConnection;

/**
 * The panel for the connect to or create a game view
 * @author Alex Pickering
 */
@SuppressWarnings("serial")
public class ConnectPanel extends JPanel implements ActionListener{
	//Instance of the main class to switch windows with
	OnlineTicTacToe m;
	
	//Graphics objects
	JLabel title = new JLabel("Online TTT"),
		   ipMessage = new JLabel(""),
		   portMessage = new JLabel("");
	
	JTextField ipField = new JTextField() {{setSize(100, 30); setMaximumSize(getSize());}},
			   portField = new JTextField() {{setSize(100, 30); setMaximumSize(getSize());}};
	
	//The bracket thing is necessary to resize them, placed on one line for organization
	JButton conButton = new JButton("Connect") {{setSize(100, 50); setMaximumSize(getSize());}},
			hostButton = new JButton("Host") {{setSize(100, 50); setMaximumSize(getSize());}},
			exitButton = new JButton("Exit") {{setSize(100, 50); setMaximumSize(getSize());}},
			testButton = new JButton("Test") {{setSize(100, 50); setMaximumSize(getSize());}};
	
	/**
	 * Panel screen for the main menu
	 * @param m
	 */
	ConnectPanel(OnlineTicTacToe m){
		//Get main instance so we can switch panels
		this.m = m;
		
		//Setup layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//Misc unique setup things
		title.setFont(new Font(getFont().getFontName(), Font.PLAIN, 36));
		
		//Setup Commands
		ipField.setActionCommand("ipSubmit");
		portField.setActionCommand("portSubmit");
		
		conButton.setActionCommand("connect");
		hostButton.setActionCommand("host");
		exitButton.setActionCommand("exit");
		testButton.setActionCommand("test");
		
		//Setup Listeners
		ipField.addActionListener(this);
		portField.addActionListener(this);
		
		conButton.addActionListener(this);
		hostButton.addActionListener(this);
		exitButton.addActionListener(this);
		testButton.addActionListener(this);
		
		//Setup alignments
		title.setAlignmentX(CENTER_ALIGNMENT);
		ipMessage.setAlignmentX(CENTER_ALIGNMENT);
		portMessage.setAlignmentX(CENTER_ALIGNMENT);
		
		ipField.setAlignmentX(CENTER_ALIGNMENT);
		portField.setAlignmentX(CENTER_ALIGNMENT);
		
		conButton.setAlignmentX(CENTER_ALIGNMENT);
		hostButton.setAlignmentX(CENTER_ALIGNMENT);
		exitButton.setAlignmentX(CENTER_ALIGNMENT);
		testButton.setAlignmentX(CENTER_ALIGNMENT);
		
		//Add components, rigidAreas are separators
		add(title);
		add(Box.createRigidArea(new Dimension(0, 10)));
		
		add(conButton);
		add(ipField);
		add(ipMessage);
		add(Box.createRigidArea(new Dimension(0, 10)));
		
		add(hostButton);
		add(portField);
		add(portMessage);
		add(Box.createRigidArea(new Dimension(0, 10)));
		
		add(exitButton);
		add(testButton);
	}
	
	/**
	 * Switch view to the game
	 */
	void startGamePanel(boolean player){
		((CardLayout)(m.panel.getLayout())).show(m.panel, "Game Panel");
		m.gamePanel.initCanvas();	//Note in case this is changed - initCanvas MUST come before setPlayer (start graphics update thread before playing)
		m.gamePanel.setPlayer(player);
	}
	
	/**
	 * Connect to a game
	 */
	void connect(){
		//Make sure IP and port are valid
		InetAddress testIP;
		int port;
		try {
			String[] sa;
			String s = ipField.getText();
			
			try {	//Check if its just a port, use localhost
				Integer.parseInt(s);
				sa = new String[2];
				sa[0] = "localhost";
				sa[1] = s;
			} catch(NumberFormatException e1) {	//ip:port
				sa = s.split(":");
			}
			
			//Check if both exist
			if(sa.length != 2) {
				System.out.println("Invalid ip/port");
				ipMessage.setText("Invalid IP/port - use ip:port");
				return;
			}
			
			testIP = InetAddress.getByName(sa[0]);
			port = Integer.parseInt(sa[1]);
		} catch(UnknownHostException e) {
			System.out.println("Unkown Host");
			ipMessage.setText("Failed: Unknown Host");
			return;
		} catch(NumberFormatException e) {
			System.out.println("Invalid port - Not a Number");
			ipMessage.setText("Invalid Port! Not a Number.");
			return;
		}
		
		//Make sure port is in range
		if(port < 1024 || port > 49151) {
			System.out.println("Invalid Port - Out of Range");
			portMessage.setText("Invalid Port! Out of Range.");
			return;
		}
		
		//Attempt to connect to a server
		Connection c = new ClientConnection(testIP, port);
		m.gamePanel.setConnection(c);
		
		String mes;
		
		try{
			mes = m.gamePanel.tryConnection();
		} catch(IOException e) {
			//Known IOExceptions
			if(e.getMessage().contains("Connection refused")) {
				System.out.println("Could not connect");
				ipMessage.setText("Could not connect to server.");
				return;
			}
			
			//Unknown IOException
			System.out.println("IOException:");
			e.printStackTrace();
			ipMessage.setText("IOException");
			return;
		}
		
		System.out.println(mes);
		
		//Output the result
		switch(mes) {
			case "Connection not created":
				ipMessage.setText("Failed: This message should not appear.");
				break;
			
			case "Success":
				ipMessage.setText("Success!");
				startGamePanel(false);
				break;
			
			default:
				ipMessage.setText("Failed: This message should not appear.");
				break;
		}
	}
	
	/**
	 * Host a game
	 */
	void host() {
		int port;
		
		//Get and check port
		try {
			port = Integer.parseInt(portField.getText());
		} catch(NumberFormatException e) {
			System.out.println("Not a Number");
			portMessage.setText("Failed: Not a Number");
			return;
		}
		
		if(port < 1024 || port > 49151) {
			System.out.println("Invalid Port");
			portMessage.setText("Failed: Invalid Port");
			return;
		}
		
		//Create the server
		Connection c;
		String mes;
		try {
			c = new ServerConnection(port);
			
			m.gamePanel.setConnection(c);
			
			//Listen for connection
			mes = m.gamePanel.tryConnection();
		} catch(IOException e) {
			System.out.println("IOException: ");
			e.printStackTrace();
			portMessage.setText("Failed: IOException");
			return;
		}
		
		System.out.println(mes);
		
		//Output result
		switch(mes) {
			case "Connection not created":
				portMessage.setText("Failed: This message should not appear.");
				break;
			
			case "Handshake Failed":
				portMessage.setText("Failed: Handshake failed");
			
			case "Success":
				portMessage.setText("Success!");
				startGamePanel(true);
				break;
			
			default:
				portMessage.setText("Failed: This message should not appear.");
				break;
		}
	}
	
	/**
	 * Action Listener for them buttons
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
			case "connect":
				System.out.println("Connect: " + ipField.getText());
				connect();
				break;
			
			case "exit":
				System.out.println("Exit");
				System.exit(0);
				break;
			
			case "test":
				startGamePanel(true);
				break;
			
			case "host":
				System.out.println("Host: " + portField.getText());
				host();
				break;
			
			case "ipSubmit":
				System.out.println(ipField.getText());
				break;
			
			case "portSubmit":
				System.out.println(portField.getText());
				break;
			
			default:
				System.out.println("aksdjhgkfaj");
				break;
		}
	}
}
