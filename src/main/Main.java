package main;

import java.awt.CardLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * Main program to run, doesn't do much other than hold the panels and frame
 * @author 21jpickering
 */
public class Main {
	JFrame frame = new JFrame("Graphical Online TTT");
	JPanel panel = new JPanel(new CardLayout());
	GamePanel gamePanel = new GamePanel(this);
	ConnectPanel connectPanel = new ConnectPanel(this);
	
	BufferedImage splashImage;
	
	long splashLength = 1500;
	
	/**
	 * Constructor because reasons
	 */
	public Main() {
		try {
			splashImage = ImageIO.read(new File("images/splash.png"));
		} catch(IOException e) {
			System.out.println("Could not load splash image: ");
			e.printStackTrace();
		}
	}
	
	/**
	 * Main!
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		//Look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {	//fuck it
			e.printStackTrace();
		}
		
		//Main program
		new Main().betterMain();
	}
	
	/**
	 * Main, but better!
	 * @throws InterruptedException
	 */
	void betterMain() throws InterruptedException {
		//Start the thing and show the splash screen
		setupWindow();
		
		//Sploosh
		Thread.sleep(splashLength);
		
		//Switch to connection/title screen
		((CardLayout)(panel.getLayout())).show(panel, "Connect Panel");
	}
	
	/**
	 * Create the window
	 */
	void setupWindow() {
		//Create the window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.add(panel);
		
		//Add panels
		panel.add(new JLabel(new ImageIcon(splashImage)), "Splash Image");
		panel.add(gamePanel, "Game Panel");
		panel.add(connectPanel, "Connect Panel");
		
		((CardLayout)(panel.getLayout())).show(panel, "Splash Image");
		
		//Make it visible
		frame.pack();
		frame.setVisible(true);
	}
}