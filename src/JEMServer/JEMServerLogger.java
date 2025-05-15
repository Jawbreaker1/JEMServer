package JEMServer;

/*************************************
*can use this class to access the gui 
*from the engine and send logs.
**************************************/


public class JEMServerLogger {
	
	JEMServerGui logGui;
	
	public JEMServerLogger(JEMServerGui gui)
	{
	logGui = gui;	
	}
	
	public void addToLog(String toLog)
	{
		logGui.addLogText(toLog);
	}
	
	public void updateUserFile()
	{
		logGui.updateUserFileData();
	}
	
	public void addRegisteredUser(JEMServerUser newUser)
	{
		logGui.registerNewUser(newUser);
	}

}
