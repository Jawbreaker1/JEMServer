package JEMServer;

import java.util.ArrayList;
import java.util.StringTokenizer;

/*****************************************
 * 
 * @author Johan Engwall
 * Engine of the server where all messages from and 
 * to other users are handled.
 * 
 *****************************************/

public class JEMServerEngine extends Thread{

	private int port = 666;
	private JEMServerMessageHandler MessageHandler;
	private JEMServerMailmanThread mailman;
	private ArrayList users;
	private JEMServerLogger guiInterface;
	
	public JEMServerEngine(ArrayList Users,JEMServerLogger logHandler)
	{
		users = Users;
		guiInterface = logHandler;
	}
	
	public void run()
	{
		System.out.println("starting connectionthreading");
		//create the messagehandler
		MessageHandler= new JEMServerMessageHandler(this);
		//create the mailmanthread
		mailman = new JEMServerMailmanThread(this,MessageHandler,users);
		mailman.start();
		//create the serversocket thread
		JEMServerConnectionThread connectionThread = new JEMServerConnectionThread(port,MessageHandler,this);
	}
	
	
	public void recieveJEMMessage(JEMServerMessage message)
	{
		//1 = Login		1;user;password;null
		if(message.getTypeOfMessage()==1)
		{
			guiInterface.addToLog(message.getHeader1()+" logged in");
			updateUsers();
		
		}
		//2 = Message		2;fromUser;toUser;message
		if(message.getTypeOfMessage()==2)
		{
			
			MessageHandler.addSend(message);
			//should be easy. Just send text to the recieving user
			
		}
		//3 = KeepAlive    3;null;null;null
		if(message.getTypeOfMessage()==3)
		{
			//return a message of the same type
			
		}
		//4 = List			4;null;null;user1.user2.user3.userN
		if(message.getTypeOfMessage()==4)
		{
			//add users to the userlist
			
		}
		//5 = Register		5;user;password;null
		if(message.getTypeOfMessage()==5)
		{
			
		}
		
		//6 = Disconnect	6;user;null;null
		if(message.getTypeOfMessage()==6)
		{
			//check which user, set his status to logged off, and end his thread.
			//send a new listupdate
			String user = message.getHeader1();
			for(int i=0; i<users.size();i++)
			{
				JEMServerUser tmpUser = (JEMServerUser)users.get(i);
					if(tmpUser.getUserName().equals(user))
					{	
						tmpUser.setStatus("Offline");
						tmpUser.setLoggedIn(false);
						
					}
			}
			guiInterface.addToLog(message.getHeader1()+" logged out");
			updateUsers();
			
		}
		
		///7 = update status 7;user;status;null
		if(message.getTypeOfMessage()==7)
		{
			String user = message.getHeader1();
			for(int i=0; i<users.size();i++)
			{
				JEMServerUser tmpUser = (JEMServerUser)users.get(i);
					if(tmpUser.getUserName().equals(user))
					{
						tmpUser.setStatus(message.getHeader2());
					}
			}
			updateUsers();
		}
		//8= user password change	8;user;password;null
		if(message.getTypeOfMessage()==8)
		{
			//find the user
			String user = message.getHeader1();
			for(int i=0; i<users.size();i++)
			{
				JEMServerUser tmpUser = (JEMServerUser)users.get(i);
					if(tmpUser.getUserName().equals(user))
					{
						tmpUser.setPassword(message.getHeader2());
						guiInterface.addToLog(message.getHeader1()+" changed password.");
						guiInterface.updateUserFile();
						JEMServerMessage passwordReply = new JEMServerMessage(8,"Server",user,"Password changed successfully!");
						MessageHandler.addSend(passwordReply);
					}
			}
			//need to change the userObjects password
			//as well as the saved textfile
		}
		
		//* 10 = Multichat message	10;fromUser;toUser;user1,user2..userN;message
		if(message.getTypeOfMessage()==10)
		{
			MessageHandler.addSend(message);
		}
		 
		 //* 11 = Invite multichat    11;fromUser;toUser;user1,user2..userN
		if(message.getTypeOfMessage()==11)
		{
			MessageHandler.addSend(message);
		}
		 //* 12 = Leave multichat		12;fromUser;toUser;user1,user2..userN
		if(message.getTypeOfMessage()==12)
		{
			MessageHandler.addSend(message);
		}
		 //* 13 = Add multichatuser	13;toUser;userToAdd;user1,user2..userN
		if(message.getTypeOfMessage()==13)
		{
			MessageHandler.addSend(message);
		}
		//* new note
		if(message.getTypeOfMessage()==14)
		{
			String user = message.getHeader1();
			for(int i=0; i<users.size();i++)
			{
				JEMServerUser tmpUser = (JEMServerUser)users.get(i);
					if(tmpUser.getUserName().equals(user))
					{
						tmpUser.setNote(message.getHeader2());
					}
			}
			updateUsers();
		}
		
		//new image
		if(message.getTypeOfMessage()==15)
		{
			String user = message.getHeader1();
			for(int i=0; i<users.size();i++)
			{
				JEMServerUser tmpUser = (JEMServerUser)users.get(i);
					if(tmpUser.getUserName().equals(user))
					{
						tmpUser.setImage(message.getHeader2());
					}
			}
			updateUsers();
		}
		
		//searchrequest for a specific user
		if(message.getTypeOfMessage()==16)
		{
			int nrOfArgs = 0;
			boolean userFound = false;
			String searchFirstName = "";
			String searchLastName = "";
			String searchNickName = "";
			System.out.println("Search recieved");
			String searchCriterias = message.getMessageBody();
			StringTokenizer st = new StringTokenizer(searchCriterias,",");
			//get the search arguments from the messagebody
            while (st.hasMoreTokens()) 
            {
            	searchFirstName = st.nextToken();
            	searchLastName = st.nextToken();
            	searchNickName = st.nextToken();
            }
            if(!searchFirstName.equals(" "))
            {
            	nrOfArgs++;
            }
            if(!searchLastName.equals(" "))
            {
            	nrOfArgs++;
            }
            if(!searchNickName.equals(" "))
            {
            	nrOfArgs++;
            }
            
            if(!searchFirstName.equals(" "))
            {
            	//get all the users with a matching name
            	for(int i=0; i<users.size();i++)
            	{
    				JEMServerUser tmpUser = (JEMServerUser)users.get(i);
    					if(tmpUser.getFirstName().equals(searchFirstName))
    					{	
    						String messageText = tmpUser.getFirstName() + ","+ tmpUser.getLastName() +","+ tmpUser.getUserName() + ",";
    						JEMServerMessage foundUserMessage = new JEMServerMessage(16,"server",message.getHeader1(),messageText);
    						MessageHandler.addSend(foundUserMessage);
    						userFound=true;
    					}
            	}
            }
            
            if(!searchLastName.equals(" "))
            {
            	//get all the users with a matching name
            	for(int i=0; i<users.size();i++)
            	{
    				JEMServerUser tmpUser = (JEMServerUser)users.get(i);
    					if(tmpUser.getLastName().equals(searchLastName))
    					{	
    						String messageText = tmpUser.getFirstName() + ","+ tmpUser.getLastName() +","+ tmpUser.getUserName() + ",";
    						JEMServerMessage foundUserMessage = new JEMServerMessage(16,"server",message.getHeader1(),messageText);
    						MessageHandler.addSend(foundUserMessage);
    						userFound=true;
    					}
            	}
            }
            
            if(!searchNickName.equals(" "))
            {
            	//get all the users with a matching name
            	for(int i=0; i<users.size();i++)
            	{
    				JEMServerUser tmpUser = (JEMServerUser)users.get(i);
    					if(tmpUser.getUserName().equals(searchNickName))
    					{	
    						String messageText = tmpUser.getFirstName() + ","+ tmpUser.getLastName() +","+ tmpUser.getUserName() + ",";
    						JEMServerMessage foundUserMessage = new JEMServerMessage(16,"server",message.getHeader1(),messageText);
    						MessageHandler.addSend(foundUserMessage);
    						userFound=true;
    					}
            	}
            }
            
            if(userFound==false)
            {
               JEMServerMessage foundUserMessage = new JEMServerMessage(20,"server",message.getHeader1(),"User not found!");
               MessageHandler.addSend(foundUserMessage);
            }
            //lets start by outputting all the possible users... and improve later.
         	//send a search message back for each user we find.
		}
		
		//request to add a user to friendlist
		if(message.getTypeOfMessage()==17)
		{
			boolean userExist = false;
			System.out.println("Request to add user "+message.getHeader2());
			//find this user
			for(int i=0; i<users.size(); i++)
			{
				JEMServerUser tmpUser = (JEMServerUser)users.get(i);
				if(tmpUser.getUserName().equals(message.getHeader1()))
				{
					//we found the user
					//make sure the user we are adding exist as well
					for(int j=0; j<users.size();j++)
					{
						
						JEMServerUser tmpUser2 = (JEMServerUser)users.get(j);
						if(tmpUser2.getUserName().equals(message.getHeader2()))
						{
							userExist = true;
						}
					}
					
					if(userExist)
					{
						//user found, and friend exist. Lets add!
						tmpUser.addFriend(message.getHeader2());
						guiInterface.updateUserFile();
						//lets tell the friend we added him
						JEMServerMessage toSend = new JEMServerMessage(20,"Server",message.getHeader2(),"User "+tmpUser.getUserName()+" has added you.");
						MessageHandler.addSend( toSend );
						//and add to file as well?
					}
					//otherwise return a fail message
				}
			}
			//check which friends this user already have. (may already have this user added)
			//add user to the friendlist and to file
			//send a list update
			updateUsers();
		}
		
		if(message.getTypeOfMessage()==18)
      {
		   System.out.println("User "+message.getHeader1()+" requests to remove "+ message.getHeader2());
		   //lets get the user
		   for(int i=0; i<users.size(); i++)
         {
            JEMServerUser tmpUser = (JEMServerUser)users.get(i);
            if(tmpUser.getUserName().equals(message.getHeader1()))
            {
               tmpUser.removeFriend( message.getHeader2() );
               guiInterface.updateUserFile();
            }
         }
		   updateUsers();
      }
		
	}
	//create methods for handling all types of messages
	
	
	public void successfulLogin(/*user socket*/)
	{
		//hand over the socket to the user.
	}
	
	public ArrayList getUsers()
	{
		return users;
	}
	
	public void updateUsers()
	{
		String listOfUsers="";
		ArrayList completeList = new ArrayList();
		//construct a list message and send to all logged in users.
		//first get all the logged in users
		for(int i=0; i<users.size();i++)
		{
			JEMServerUser tmpUser = (JEMServerUser)users.get(i);
			//if (tmpUser.isLoggedIn())
			//{
				//listOfUsers = listOfUsers + "," + tmpUser.getUserName() + "," + tmpUser.getStatus() + "," +tmpUser.getNote() + "," + tmpUser.getImage();
				completeList.add(tmpUser.getUserName() + "," + tmpUser.getStatus() + "," +tmpUser.getNote() + "," + tmpUser.getImage());
			//}
		}
		//then send lists to all users
		for(int i=0; i<users.size();i++)
		{
			JEMServerUser tmpUser = (JEMServerUser)users.get(i);
			if (tmpUser.isLoggedIn())
			{
				//keep only the users which are in the tmpUsers friendlist.
				for(int j=0;j<completeList.size();j++)
				{
					String tmpListEntry = (String)completeList.get(j);
					StringTokenizer st = new StringTokenizer(tmpListEntry,",");
					String tmpUsername = st.nextToken();
					
					ArrayList tmpUserFriends = tmpUser.getFriends();
					for(int k = 0; k<tmpUserFriends.size();k++)
					{
						String tmpFriend = (String)tmpUserFriends.get(k);
						if(tmpUsername.equals(tmpFriend))
						{
							listOfUsers = listOfUsers + ","+ tmpListEntry;
							//we need to add our own info here too!!
						}
					}
					//if the tmpUsername exist in the tmpUserFriends list, add the tmpListEntry to the listOfUsers
					
				}
				//generate a new "listofusers" from the completeList, only using the friends.
				
				JEMServerMessage listMessage = new JEMServerMessage(4, "Server", tmpUser.getUserName(),listOfUsers);
				MessageHandler.addSend(listMessage);
				listOfUsers="";
			}
		}
	}
	
	
	public void alertUsers(String toSend)
	{
		for(int i=0; i<users.size();i++)
		{
			JEMServerUser tmpUser = (JEMServerUser)users.get(i);
			if (tmpUser.isLoggedIn())
			{
				JEMServerMessage listMessage = new JEMServerMessage(2, "Server", tmpUser.getUserName(),"20,Serif,Black,BOLD,"+"*****" + toSend + "*****");
				MessageHandler.addSend(listMessage);
			}
		}
	}
	
	public void kickUser(String userName)
	{
		for(int i=0; i<users.size();i++)
		{
			JEMServerUser tmpUser = (JEMServerUser)users.get(i);
				if(tmpUser.getUserName().equals(userName))
				{
					//JEMServerMessage kickMessage = new JEMServerMessage(2, "Server", tmpUser.getUserName(),"*****Kicked by server*****");
					//MessageHandler.addSend(kickMessage);
					tmpUser.setStatus("Offline");
					tmpUser.setLoggedIn(false);
					updateUsers();
				}
		}
	}
	
	public void registerNewUser(JEMServerUser newUser)
	{
		guiInterface.addRegisteredUser(newUser);
	}
	
	public boolean chkIfUserOnline(String userName)
	{
		boolean online = false;
		for(int i=0; i<users.size();i++)
		{
			JEMServerUser tmpUser = (JEMServerUser)users.get(i);
				if(tmpUser.getUserName().equals(userName))
				{
					if(tmpUser.isLoggedIn())
					{
						online = true;
					}
				}
		}
	return online;
	}
	
	
	
}
