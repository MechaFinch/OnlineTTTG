package connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Special connection class specific to heartbeats, doesn't work as normal.
 * @author Alex Pickering
 */
public class HeartbeatConnection implements Connection {
	//Sockets
	ServerSocket server;
	Socket con;
	
	//IO
	PrintWriter write;
	BufferedReader read;
	
	//Others
	boolean type;	//true = server
	String ip;
	
	/**
	 * Default constructor
	 * @param type Whether this is a server or client's heartbeat (true = server)
	 * @param ip Either the ip/port to connect to or the port to listen on
	 */
	public HeartbeatConnection(boolean type, String ip) {
		this.type = type;
		this.ip = ip;
	}
	
	/**
	 * Creates the heartbeat
	 * @return Connection status
	 */
	public String create() {
		try {
			if(type) {	//Server
				server = new ServerSocket(Integer.parseInt(ip));
				con = server.accept();
				server.close();
				
				read = new BufferedReader(new InputStreamReader(con.getInputStream()));
				write = new PrintWriter(new BufferedWriter(new OutputStreamWriter(con.getOutputStream())), true);
			} else {	//Client
				//Clean localhost and debug
				System.out.println("IP: " + ip);
				
				if(ip.contains("localhost")) ip = "localhost:" + ip.split(":")[1];
				con = new Socket(InetAddress.getByName(ip.split(":")[0].substring(1)), Integer.parseInt(ip.split(":")[1]));
				
				read = new BufferedReader(new InputStreamReader(con.getInputStream()));
				write = new PrintWriter(new BufferedWriter(new OutputStreamWriter(con.getOutputStream())), true);
			}
		} catch(IOException e) {
			e.printStackTrace();
			return "failed";
		}
		
		return "success";
	}
	
	@Override
	public String connect() throws IOException {
		//UNUSED
		return null;
	}

	@Override
	public void disconnect() throws IOException {
		con.close();
	}

	@Override
	public boolean isDisconnected() {
		//UNUSED
		return false;
	}

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
