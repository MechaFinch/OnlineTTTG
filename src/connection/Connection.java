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
	 * Receive a message from the connection
	 * @return Message received
	 * @throws IllegalStateException if not connected
	 */
	public abstract String receive() throws IllegalStateException;
	
	/**
	 * Send a message to the connection
	 * @param msg The message to be sent
	 * @return Success
	 * @throws IllegalStateException if not connected
	 */
	public abstract String send(String msg) throws IllegalStateException;
	
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
