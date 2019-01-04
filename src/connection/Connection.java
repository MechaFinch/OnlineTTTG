package connection;

import java.io.IOException;
import java.util.Random;

/**
 * The interface for client and server connections
 * @author Alex Pickering
 */
public interface Connection {
	/**
	 * Connect the connection to the specified IP or port
	 * @return Success status
	 * @throws IOException
	 */
	public abstract String connect() throws IOException;
	
	/**
	 * Disconnects
	 * @throws IOException
	 */
	public abstract void disconnect() throws IOException;
	
	/**
	 * Tells if the other connection has disconnected
	 */
	public abstract boolean isDisconnected();
	
	/**
	 * Starts the connection monitor heartbeat
	 */
	public abstract void startHeartbeat();
	
	/**
	 * Receive a message from the connection
	 * @return Message received
	 * @throws IllegalStateException if not connected
	 */
	public abstract String receive() throws IllegalStateException, IOException;
	
	/**
	 * Send a message to the connection
	 * @param msg The message to be sent
	 * @throws IllegalStateException if not connected
	 */
	public abstract void send(String msg) throws IllegalStateException, IOException;
	
	/**
	 * Creates a random string for handshakes
	 * @param len The length of the generated string
	 * @return A random string
	 */
	default String createRandomString(int len) {
		String s = "";
		Random r = new Random();
		
		for(int i = 0; i < len; i++)
			s += (char)(r.nextInt(93) + 32);
		
		return s;
	}
}
