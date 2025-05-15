package JEMServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Calendar;

/***********************************
 * 
 * @author Johan Engwall
 * This class defines the userobject used
 * by the JEMServer.
 * Handles all the socketcommunication with the 
 * corresponding client.
 ***********************************/


public class JEMServerUser implements Runnable{

	private boolean loggedIn;
	private String userName;
	private String password;
	private ArrayList myFriends;
	//private String myInfo;
	private String firstName;
	private String lastName;
	private String userNote = " ";
	private Calendar cal = Calendar.getInstance();
	private long timeOfLastRecievedKeepAlive;
	private String myImage = "no image";
	 
	
	//Status can currently be "Online", "Away"  "Busy" or "Offline"
	private String status = "Offline";
	Socket socket;
	JEMServerMessageHandler messageHandler;
	ArrayList mySendQueue = new ArrayList();
	
	public JEMServerUser(String UserName, String Password,String Firstname, String Lastname) 
	{
		userName = UserName;
		password = Password;
		loggedIn = false;
		firstName = Firstname;
		lastName = Lastname;
		myFriends = new ArrayList();
		myFriends.add(userName);
	}

	public void addToSendQueue(JEMServerMessage sendMessage)
	{
		mySendQueue.add(sendMessage);
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
	public void setStatus(String newStatus)
	{
		if(newStatus.equals("Online"))
		{
			status = newStatus;
		}
		if(newStatus.equals("Away"))
		{
			status = newStatus;
		}
		if(newStatus.equals("Busy"))
		{
			status = newStatus;
		}
		if(newStatus.equals("Offline"))
		{
			status = newStatus;
		}
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public void addFriend(String newFriend)
	{
		//make sure we dont add an already existing friend.
		boolean unique = true;
		for(int i=0; i<myFriends.size(); i++)
		{
			String tmpUser = (String)myFriends.get(i);
			if(tmpUser.equals(newFriend))
			{
				unique = false;
				System.out.println("User "+userName+ " already have "+newFriend+" as a friend!");
			}
		}
		if(unique)
		{
		myFriends.add(newFriend);
		//now.. how do we add this to file?
		System.out.println("User "+userName+ " added "+newFriend+" as a friend!");
		}
	}
	
	public void removeFriend(String friend)
	{
	   for(int i=0; i<myFriends.size(); i++)
      {
         String tmpUser = (String)myFriends.get(i);
         if(tmpUser.equals(friend))
         {
           
            myFriends.remove( i );
         }
      }
	}
	
	public ArrayList getFriends()
	{
		return myFriends;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public void setNote(String newNote)
	{
		userNote = newNote;
	}
	
	public String getNote()
	{
		return userNote; 
	}
	
	public void setImage(String imageName)
	{
		myImage = imageName;
	}
	
	public String getImage()
	{
		return myImage;
	}
	
	
	public Socket getSocket() {
		return socket;
	}
	
	public long getLastKeepAlive(){
		return timeOfLastRecievedKeepAlive;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
		cal = Calendar.getInstance();
		timeOfLastRecievedKeepAlive=cal.getTimeInMillis();
	}
	
	public void run()
	{
		int timeToSendKeepalive = 0;
		try
		{
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    timeOfLastRecievedKeepAlive  = cal.getTimeInMillis();
		    while(loggedIn)
		    {
		    	//recieving and forwarding to messagehandler
		    	if(in.ready())
		    	{
			    	String tmpString = in.readLine();
			    	//Debug-line
			    	//System.out.println(tmpString);
			    	JEMServerMessage recievedMessage = convertStringToMessage(tmpString);
			    	if(recievedMessage.getTypeOfMessage()==3)
					{
						//keepalive recieved from server. Update the time from the last keepalive.
			    		cal = Calendar.getInstance();
						timeOfLastRecievedKeepAlive=cal.getTimeInMillis();
					}
			    	if(!(recievedMessage.getTypeOfMessage()==3))
					{
			    	messageHandler.addRecieved(recievedMessage);
			    	cal = Calendar.getInstance();
					timeOfLastRecievedKeepAlive=cal.getTimeInMillis();
					}
			    	Thread.sleep(100);
		    	}
		    	if(!mySendQueue.isEmpty())
		    	{
		    		JEMServerMessage tmpMessage =  (JEMServerMessage)mySendQueue.get(0);
		    		mySendQueue.remove(0);
		    		String toSend = convertMessageToString(tmpMessage);
		    		out.println(toSend);
		    		Thread.sleep(100);
		    	}
		    	Thread.sleep(100);
				timeToSendKeepalive++;
				if(timeToSendKeepalive==20)
				{
					timeToSendKeepalive=0;
					String keepAliveMessage = "3¤Server¤"+userName+"¤null";
					out.println(keepAliveMessage);
				}
		    	//picking up the next message from the messagehandler and sending it
		    	
		    }
			out.close();
			in.close();
			socket.close();
		}

		catch (IOException e) {
		    e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("User "+userName+" was logged off from the server");
	}
	
	public void setMessageHandler(JEMServerMessageHandler messageHandler)
	{
		this.messageHandler = messageHandler;
	}
	
	private String convertMessageToString(JEMServerMessage message)
	{
		String ret = message.getTypeOfMessage()+ "¤" + message.getHeader1() + "¤" + message.getHeader2() + "¤" + message.getMessageBody();
		return ret;
	}
	
	private JEMServerMessage convertStringToMessage(String message)
	{
		String body ="";
		//here we have some converting to do. 
		StringTokenizer st = new StringTokenizer(message, "¤");
		int mType = Integer.parseInt(st.nextToken());
		String Header1 = st.nextToken();
		String Header2 = st.nextToken();
		body = st.nextToken();
		while(st.hasMoreTokens())
		{
		body =body + st.nextToken() + "incorrect character";
		}
		//body = st.nextToken();
		JEMServerMessage tmpJEMMessage = new JEMServerMessage(mType,Header1,Header2,body);
		return tmpJEMMessage;
	}

}
