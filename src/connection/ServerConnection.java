package connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class that manages the connection to the client
 * Hosts the server socket object and handles sending and receiving information
 * @author Alex Pickering
 */
public class ServerConnection implements Connection {
	//Network objects
	ServerSocket ss;
	Socket s;
	
	/**
	 * Default constructor
	 * @param port The port to host on
	 * @throws IOException 
	 */
	public ServerConnection(int port) throws IOException {
		ss = new ServerSocket(port);
	}

	@Override
	public String connect() throws IOException {
		//Accept a connection
		s = ss.accept();
		
		//Try handshake thing
		String rand = createRandomString(10);
		send(rand);
		
		//Give success info
		try {
			if(receive().equals(rand)) {
				send("Success");
				return "Success";
			}
		} catch(NullPointerException e) {	//No responce
		}
		
		send("Failure");
		return "Handshake Failed";
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
