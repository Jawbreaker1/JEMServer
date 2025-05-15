package JEMServer;

import java.net.*;
import java.util.StringTokenizer;
import java.io.*;
import java.util.ArrayList;

/*******************************************
 * 
 * @author Johan Engwall
 * This class handles the loginsession after the initial
 * connection was established by the ConnectionThread class.
 * If the login is successful the socket will be handed over to 
 * the corresponding server user, and this thread will be ended.
 *******************************************/

public class JEMServerThreadFactory extends Thread {

	private Socket socket = null;
	private boolean alive = true;
	private JEMServerMessageHandler messageHandler;
	JEMServerEngine engine;
	
	public JEMServerThreadFactory(JEMServerMessageHandler MessageHandler,Socket socket,JEMServerEngine Engine)
	{
		this.socket = socket;
		messageHandler = MessageHandler;
		engine = Engine;
	}
	
	public void setAlive(boolean val)
	{
		alive = val;
	}
	
	
	//this is not correctly constructed. This thread is only supposed to handle initial login. The socket will then be sent to the correct user object.
	//hence, only the login and the handeling of the socket has to be handled here. Logins dont even have to be sent further?
	//maybe direct communication with the engine is needed?
	//if login is ok, send socket to engine. Otherwise send an incorrect login to the client. :)
	
	public void run() 
	{
		try
		{
		    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    while(alive)
		    {
		    	String tmpString = in.readLine();
		    	//Debug-line
		    	System.out.println(tmpString);
		    	JEMServerMessage recievedMessage = convertStringToMessage(tmpString);
		    	ArrayList tmpUsers = engine.getUsers();
		    	
		    	if(recievedMessage.getTypeOfMessage()==1)
		    	{
					if(login(recievedMessage.getHeader1(),recievedMessage.getHeader2(),tmpUsers))
					{
						System.out.println("User " +recievedMessage.getHeader1() +" logged in correct!");
						alive = false;
						//since the login was successful we forward the message to the server so it can transfer a new userlist.
						messageHandler.addRecieved(recievedMessage);
						//hand over the socket and start a new thread under the user, end this thread.
					}
					else
					{
						out.println("1¤Server¤"+ recievedMessage.getHeader1()+ "¤incorrect login attempt or already logged in");
						System.out.println("incorrect login attempt");
						alive = false;
						//return a negative login message
					}		    	
		    	}else if(recievedMessage.getTypeOfMessage()==9)
		    	{
		    		//if we got here we need to create a new user. This means adding the user to both userlist, file and GUI.
		    		//first make sure no user with this name already exist.
		    		if(uniqueUser(recievedMessage.getHeader1()))
		    		{
		    		   String name = recievedMessage.getMessageBody();
		    		   StringTokenizer st = new StringTokenizer(name,",");
		    		   String firstName = st.nextToken();
		    		   String lastName = st.nextToken();
		    			JEMServerUser newUser = new JEMServerUser(recievedMessage.getHeader1(),recievedMessage.getHeader2(),firstName,lastName);
		    			engine.registerNewUser(newUser);
		    			out.println("9¤Server¤"+ recievedMessage.getHeader1()+ "¤User created! Please log in.");
		    			alive = false;
		    			//then add to list and update, and update guilist
			    		//after this is done disconnect the user and let him log in again.
		    		}
		    		else
		    		{
		    			out.println("9¤Server¤"+ recievedMessage.getHeader1()+ "¤User already exist!");
		    		}
		    		
		    	}
		    }
		    
		    //out.close();
		    //in.close();
		    //socket.close();
		}
		catch (IOException e) {
		    e.printStackTrace();
		}
	System.out.println("A LoginThread was ended. Waiting for new attempt.");
		
	}
	
	private String convertMessageToString(JEMServerMessage message)
	{
		String ret = message.getTypeOfMessage()+ "¤" + message.getHeader1() + "¤" + message.getHeader2() + "¤" + message.getMessageBody();
		return ret;
	}
	
	private JEMServerMessage convertStringToMessage(String message)
	{
		//here we have some converting to do. 
		StringTokenizer st = new StringTokenizer(message, "¤");
		int mType = Integer.parseInt(st.nextToken());
		String Header1 = st.nextToken();;
		String Header2 = st.nextToken();
		String body = st.nextToken();
		JEMServerMessage tmpJEMMessage = new JEMServerMessage(mType,Header1,Header2,body);
		return tmpJEMMessage;
	}
	
	public boolean uniqueUser(String username)
	{
		boolean unique=true;
		ArrayList users = engine.getUsers();
		for(int i=0; i<users.size(); i++)
		{
			JEMServerUser tmpServerUser = (JEMServerUser)users.get(i);
			String tmpUserName = tmpServerUser.getUserName();
			if(tmpUserName.equals(username))
			{
				unique = false;
			}
		}
		return unique;
	}
	
	public boolean login(String username, String password, ArrayList users)
	{
		boolean correct = false;
		for(int i=0; i<users.size(); i++)
		{
			JEMServerUser tmpServerUser = (JEMServerUser)users.get(i);
			String tmpUser = tmpServerUser.getUserName();
			String tmpPassword = tmpServerUser.getPassword();
			if(username.equals(tmpUser) && password.equals(tmpPassword) && !(tmpServerUser.isLoggedIn()))
			{
				correct = true;
				tmpServerUser.setLoggedIn(true);
				tmpServerUser.setSocket(socket);
				tmpServerUser.setMessageHandler(messageHandler);
				tmpServerUser.setStatus("Online");
//				tmpServerUser.start();
				new Thread( tmpServerUser ).start();
			}
		}
		
		return correct;
	}
	
}
