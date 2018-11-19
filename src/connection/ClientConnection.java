package connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Class that manages the connection to the server
 * Connects to the server and relays requested information, calls client updates
 * @author Alex Pickering
 */
public class ClientConnection implements Connection {
	//Network variables
	Socket con;
	InetAddress ip;
	int port;
	
	//Network IO
	PrintWriter write;
	BufferedReader read;
	
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
		//Connect to server
		con = new Socket(ip, port);
		
		//Create reader/writer
		read = new BufferedReader(new InputStreamReader(con.getInputStream()));
		write = new PrintWriter(new BufferedWriter(new OutputStreamWriter(con.getOutputStream())), true);
		
		//Echo handshake
		String s = receive();
		System.out.println("Recieved handshake key: " + s);
		send(s);
		
		//Record responce
		return receive();
	}
	
	//These would be default methods but they use non-final variables so oh well
	@Override
	public String receive() throws IllegalStateException, IOException {
		//Check that the reader has been initialized
		if(read.equals(null)) throw new IllegalStateException("Not Connected");
		
		//Read and return
		return read.readLine();
	}
	
	@Override
	public void send(String msg) throws IllegalStateException, IOException {
		//Check that the writer has been initialized
		if(write.equals(null)) throw new IllegalStateException("Not Connected");
		
		//Send
		write.println(msg);
	}
}
