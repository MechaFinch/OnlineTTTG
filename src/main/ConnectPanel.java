package main;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
	//Instance of main to switch windows with
	Main m;
	
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
	ConnectPanel(Main m){
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
		
		//Add components
		add(title);
		
		add(conButton);
		add(ipField);
		add(ipMessage);
		
		add(hostButton);
		add(portField);
		add(portMessage);
		
		add(exitButton);
		add(testButton);
	}
	
	/**
	 * Switch view to the game
	 */
	void startGamePanel(){
		((CardLayout)(m.panel.getLayout())).show(m.panel, "Game Panel");
		m.gamePanel.initCanvas();
	}
	
	/**
	 * Connect to a game
	 */
	void connect(){
		//Make sure IP is valid
		InetAddress testIP;
		try {
			testIP = InetAddress.getByName(ipField.getText());
		} catch(UnknownHostException e) {
			System.out.println("Unkown Host");
			ipMessage.setText("Faild: Unknown Host");
			return;
		}
		
		//Attempt to connect to a server
		Connection c = new ClientConnection(testIP);
		m.gamePanel.setConnection(c);
	}
	
	/**
	 * Host a game
	 */
	void host() {
		int port = Integer.parseInt(portField.getText());
		Connection c = new ServerConnection(port);
		m.gamePanel.setConnection(c);
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
				startGamePanel();
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
