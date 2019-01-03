package connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
	Socket con;
	
	//Network IO
	PrintWriter write;
	BufferedReader read;
	
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
		con = ss.accept();
		
		//Create reader/writer
		read = new BufferedReader(new InputStreamReader(con.getInputStream()));
		write = new PrintWriter(new BufferedWriter(new OutputStreamWriter(con.getOutputStream())), true);
		
		//Try handshake thing
		String rand = createRandomString(10);
		System.out.println("Sent handshake key: " + rand);
		send(rand);
		
		//Give success info
		try {
			if(receive().equals(rand)) {
				send("Success");
				ss.close();
				return "Success";
			}
		} catch(NullPointerException e) {	//No response
		}
		
		send("Failure");
		return "Handshake Failed";
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

	@Override
	public void disconnect() throws IOException {
		con.close();
		con.shutdownInput();
		con.shutdownOutput();
	}
	
	@Override
	public boolean connected() {
		return true;
		//TODO: this
	}
}
