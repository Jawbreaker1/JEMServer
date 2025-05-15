package JEMServer;

import java.io.*;
import java.net.*;


/*******************************************************
 * 
 * @author Johan Engwall
 * Class used to handle the initial connection from a user logging in.
 * Constantly waits for a connection to occur.
 * After a connection is achieved a thread will be started to handle the actual login, and this 
 * class will yet again wait for a new connection.
 *******************************************************/

public class JEMServerConnectionThread{
	
	private int port;
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private JEMServerMessageHandler messageHandler;
	private Socket socket = null;
	private boolean listening;
	private JEMServerEngine engine;
	
	public JEMServerConnectionThread(int Port, JEMServerMessageHandler MessageHandler,JEMServerEngine Engine)
	{
        ServerSocket serverSocket = null;
        listening = true;
		port = Port;
		messageHandler = MessageHandler;
		engine = Engine;
		
		
		
		
		try 
		{
			serverSocket = new ServerSocket(port);
		}
	    catch (Exception e) 
	    {
	    	System.out.println(e.getMessage());
	    }
		
		
			try 
			{
				while (listening){
					new JEMServerThreadFactory(messageHandler,serverSocket.accept(),engine).start();
				}
			}
			
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try 
			{
				serverSocket.close();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public int getPort()
	{
		return port;
	}
	
	public void setListening(boolean val)
	{
		listening = val;
	}
		
}
