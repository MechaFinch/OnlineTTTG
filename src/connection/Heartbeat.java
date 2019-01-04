package connection;

import java.io.IOException;

/**
 * 
 * @author Alex Pickering
 */
public class Heartbeat {
	HeartbeatConnection con;
	boolean init,
			disconnected = false;
	
	/**
	 * Default constructor
	 * @param con The connection to run the Heartbeat on
	 * @param init Whether the Heartbeat initializes the pulse or not
	 */
	public Heartbeat(HeartbeatConnection con, boolean init) {
		this.con = con;
		this.init = init;
	}
	
	/**
	 * Begins the heartbeat
	 */
	public void start() {
		Thread t;
		
		if(init) {
			t = new Thread(new Sender(con, this));
		} else {
			t = new Thread(new Listener(con, this));
		}
		
		t.start();
	}
	
	/**
	 * Tells if there's been a disconnect
	 * @return Whether or not there has been a disconnection
	 */
	public boolean isDisconnected() {
		return disconnected;
	}
	
	/**
	 * Sets whether or not there's been a disconnect
	 * @param d
	 */
	public void setDisconnected(boolean d) {
		disconnected = d;
	}
}

/**
 * Heartbeat that listens for and then echoes the pulses
 * @author Alex Pickering
 */
class Listener implements Runnable {
	Connection c;
	Heartbeat h;
	
	/**
	 * Default constructor
	 * @param c The connection to use
	 * @param h The heartbeat instance this is under
	 */
	public Listener(Connection c, Heartbeat h) {
		this.c = c;
		this.h = h;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				if(!c.receive().equals("heartbeat")) {
					h.setDisconnected(true);
					break;
				}
				
				c.send("heartbeat");
			} catch(IOException e) {
				e.printStackTrace();
				h.setDisconnected(true);
				break;
			} catch(IllegalStateException e) {
			}
			
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
			}
		}
	}
}

/**
 * Heartbeat that sends pulses and listens for the echoes
 * @author Alex Pickering
 */
class Sender implements Runnable {
	Connection c;
	Heartbeat h;
	
	/**
	 * Default constructor
	 * @param c The connection to use
	 * @param h The heartbeat instance this is under
	 */
	public Sender(Connection c, Heartbeat h) {
		this.c = c;
		this.h = h;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				c.send("heartbeat");
				
				if(!c.receive().equals("heartbeat")) {
					h.setDisconnected(true);
					break;
				}
			} catch(IOException e) {
				e.printStackTrace();
				h.setDisconnected(true);
				break;
			} catch(IllegalStateException e) {
			}
			
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
			}
		}
	}
}
