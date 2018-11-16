package connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Class that manages the connection to the server
 * Connects to the server and relays requested information, calls client updates
 * @author Alex Pickering
 */
public class ClientConnection implements Connection {
	Socket s;
	InetAddress ip;
	int port;
	
	/**
	 * Default constructor
	 * @param ip The IP Address object to connect to
	 */
	public ClientConnection(InetAddress ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	@Override
	public String connect() throws IOException {
		s = new Socket(ip, port);
		
		return "";
	}

	@Override
	public String receive() throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String send(String msg) throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}
}
