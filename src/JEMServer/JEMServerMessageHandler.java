package JEMServer;

import java.util.ArrayList;


/*********************************
 * 
 * @author Johan Engwall
 * Class that handles all the incoming
 * messages, which are picked up by the 
 * mailman object.
 *********************************/

public class JEMServerMessageHandler {

	private ArrayList sendList;
	private ArrayList recievedList;
	private JEMServerMessage message;
	private JEMServerEngine engine;
	
	public  JEMServerMessageHandler(JEMServerEngine Engine)
	{
		recievedList = new ArrayList();
		sendList = new ArrayList();
		engine = Engine;
	}
	
	public synchronized void addRecieved(JEMServerMessage recieved)
	{
		recievedList.add(recieved);
	}
	
	//picks up the top message from the recievedlist. (if it isnt empty)
	public synchronized JEMServerMessage getRecieved()
	{
		message = (JEMServerMessage)recievedList.get(0);
		recievedList.remove(0);
		return message;
	}
	
	public synchronized void addSend(JEMServerMessage send)
	{
		sendList.add(send);
	}
	
	//picks up the top message from the sendlist. (if it isnt empty)
	public synchronized JEMServerMessage getSend()
	{
		message = (JEMServerMessage)sendList.get(0);
		sendList.remove(0);
		return message;
	}
	
	
	public synchronized boolean recievedIsEmpty()
	{
		boolean empty = false;
		if(recievedList.size()==0)
		{
			empty = true;
		}
		return empty;
		
	}
	
	public synchronized boolean sendIsEmpty()
	{
		boolean empty = false;
		if(sendList.size()==0)
		{
			empty = true;
		}
		return empty;
		
	}
}
