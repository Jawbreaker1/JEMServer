package JEMServer;

import java.util.ArrayList;
import java.util.Calendar;

/********************************************************************************
 * 
 * @author Johan Engwall
 * Class used for "cleaning" out users which has lost connection to 
 * the server. This is checked by controlling the "getLastKeepAlive" variable
 * from the user, and comparing this to the current time. If no keepAlive message
 * has been sent in >20seconds, this connection will be ended. 
 ********************************************************************************/


public class JEMServerTimeoutChecker implements Runnable {

	private ArrayList users;
	private JEMServerLogger logHandler;
	private Calendar cal;
	private JEMServerEngine engine;
	
	public JEMServerTimeoutChecker(ArrayList Users,JEMServerLogger LogHandler,JEMServerEngine Engine)
	{
		users = Users;
		logHandler = LogHandler;
		engine = Engine;
		
	}
	
	public void run()
	{
		while(true)
		{
			cal = Calendar.getInstance();
			for(int i=0;i<users.size();i++)
			{
				JEMServerUser tmpUser = (JEMServerUser)users.get(i);
				if(tmpUser.isLoggedIn())
				{
					long tmpTimeDiff = cal.getTimeInMillis() - tmpUser.getLastKeepAlive();
					//if it has been more than 20sec since the server last recieved a message from the user, kick him >:)
					if(tmpTimeDiff>20000)
					{
						engine.kickUser(tmpUser.getUserName());
						logHandler.addToLog(("User "+tmpUser.getUserName()+" timed out!"));
					}
				}
			}
			//keep going trough the userlist,check if the user is logged on, and compare currentTime with the keepAlive time.
			//if the time differance is over 10 sec, log out the user, and add a message the the logHandler.
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
