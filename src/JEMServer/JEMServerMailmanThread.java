package JEMServer;

import java.util.ArrayList;


/***************************************
 * 
 * @author Johan Engwall
 * Class that handles all incoming and outgoing messages.
 * All incoming messages are forwarded to the engine, and
 * all outgoing is added to the recieving users sendqueue.
 *
 ***************************************/


public class JEMServerMailmanThread extends Thread{

	JEMServerEngine engine;
	JEMServerMessageHandler input;
	boolean running = true;
	ArrayList users;
	
	public JEMServerMailmanThread(JEMServerEngine Engine,JEMServerMessageHandler Input,ArrayList Users)
	{
		engine = Engine;
		input = Input;
		users = Users;
	}
	
	public void run()
	{
		//check the MessageHandler if there are any recieved messages.
		while(running)
		{
			if(!(input.recievedIsEmpty()))
			{
				JEMServerMessage tmpMessage = input.getRecieved();
				engine.recieveJEMMessage(tmpMessage);
			}
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!(input.sendIsEmpty()))
			{
				JEMServerMessage tmpMessage = input.getSend();
				String tmpUserName = tmpMessage.getHeader2();
				for(int i=0; i<users.size();i++)
				{
					JEMServerUser tmpUser = (JEMServerUser)users.get(i);
					if(tmpUser.getUserName().equals(tmpUserName))
					{
						tmpUser.addToSendQueue(tmpMessage);
					}
				}
			}
		}
	}
	
	public void setRunning(boolean newVal)
	{
		running = newVal;
	}
	
}
