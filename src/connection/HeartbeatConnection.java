package connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Special connection class specific to heartbeats, doesn't work as normal.
 * @author Alex Pickering
 */
public class HeartbeatConnection implements Connection {
	ServerSocket server;
	Socket con;
	
	/**
	 * Default constructor
	 * @param type Whether this
	 * @param ip
	 */
	public HeartbeatConnection(boolean type, String ip) {
		
	}
	
	@Override
	public String connect() throws IOException {
		//UNUSED
		return null;
	}

	@Override
	public void disconnect() throws IOException {
		//TODO
	}

	@Override
	public boolean isDisconnected() {
		//UNUSED
		return false;
	}

	@Override
	public void startHeartbeat() {
		//UNUSED
	}

	@Override
	public String receive() throws IllegalStateException, IOException {
		//TODO
		return null;
	}

	@Override
	public void send(String msg) throws IllegalStateException, IOException {
		//TODO
	}
}
