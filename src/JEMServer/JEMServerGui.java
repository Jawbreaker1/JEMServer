package JEMServer;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.SwingUtilities;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import java.util.StringTokenizer;
import java.io.File;
import javax.swing.JScrollPane;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//imports for minimize
import java.net.URL;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.AWTException;
import static java.awt.SystemTray.getSystemTray;
//end imports for minimize


 /******************************************************
 * Server GUI. Mainly used for creating Users(to file) *
 * checking the log, and starting/stopping the server  *
 ******************************************************/


public class JEMServerGui extends JFrame
{
	private JTextField userNameText;
	private JTextField PasswordText;
	private JTextField FirstNameText;
	private JTextField LastNameText;
	private JTextArea serverLogText;
	private JList GUIuserList;
	private ArrayList ServerUserList = new ArrayList();
	private boolean GUIStartFlag = false;
	DefaultListModel GUIlistModel = new DefaultListModel();
	JScrollPane userListScroll;// = new JScrollPane(GUIuserList);
	JScrollPane LogListScroll;
	JEMServerEngine engine;
	
	
	public JEMServerGui()
	{
		setTitle("JEMServer");
		setLayout(null);
		setMinimumSize(new Dimension(600,550));
		setVisible(true);
		setLocation(350, 250);	
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		JButton addButton = new JButton();
		JButton removeButton = new JButton();
		JButton startButton = new JButton();
		JButton alertButton = new JButton();
		JButton kickButton = new JButton();
		JLabel userName = new JLabel();
		JLabel firstName = new JLabel();
		JLabel lastName = new JLabel();
		JLabel password = new JLabel();
		JLabel LogLabel = new JLabel("Log:");
		userNameText = new JTextField();
		PasswordText = new JTextField();
		FirstNameText = new JTextField();
		LastNameText = new JTextField();
		serverLogText = new JTextArea();
		serverLogText.setEditable(false);
		serverLogText.setLineWrap(true);
		serverLogText.setWrapStyleWord(true);
		
		
		GUIuserList= new JList(GUIlistModel);
		LogListScroll = new JScrollPane(serverLogText, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		userListScroll = new JScrollPane(GUIuserList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		
		userName.setBounds(50, 50, 100, 20);
		userName.setText("Username:");
		firstName.setBounds(50,80,100,20);
		firstName.setText("First Name:");
		lastName.setBounds(50,110,100,20);
		lastName.setText("Last Name:");
		password.setBounds(50, 150, 100, 20);
		password.setText("Password:");
		
		userNameText.setBounds(160, 50, 100, 20);
		FirstNameText.setBounds(160,80,100,20);
		LastNameText.setBounds(160,110,100,20);
		PasswordText.setBounds(160, 150, 100, 20);
		
		addButton.setText("Add");
		addButton.setBounds(50, 300, 95, 30);
		
		removeButton.setText("Remove");
		removeButton.setBounds(150, 300, 95, 30);
		
		startButton.setText("Start/Stop");
		startButton.setBounds(300, 300, 95, 30);
		
		alertButton.setText("Broadcast");
		alertButton.setBounds(300, 250, 95, 30);
		
		kickButton.setText("Kick user");
		kickButton.setBounds(300, 50, 95, 30);
		
		LogLabel.setBounds(25, 345, 30, 20);
		LogListScroll.setBounds(20,370,560,130);
		
		//GUIuserList.setBounds(425,10,150,325);
		
		userListScroll.setBounds(425,10,150,325);
		
		this.add(userName);
		this.add(userNameText);
		this.add(firstName);
		this.add(FirstNameText);
		this.add(lastName);
		this.add(LastNameText);
		this.add(password);
		this.add(PasswordText);
		this.add(addButton);
		this.add(removeButton);
		this.add(startButton);
		this.add(alertButton);
		this.add(kickButton);
		this.add(userListScroll);
		this.add(LogLabel);
		this.add(LogListScroll);
		
	
		
		URL resource = getClass().getResource("images/JEMServer_tray.GIF");
		Image image = Toolkit.getDefaultToolkit().getImage(resource);
		setIconImage(image);
		
		if (SystemTray.isSupported()) {
            final TrayIcon icon = new TrayIcon(image);
            icon.setToolTip("JEM Server");
            icon.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                	JEMServerGui.this.setVisible(true);
                	JEMServerGui.this.setExtendedState(JEMServerGui.NORMAL);
                    getSystemTray().remove(icon);
                }

            });
            addWindowListener(new WindowAdapter() {

                @Override
                public void windowIconified(WindowEvent e) {
                	JEMServerGui.this.setVisible(false);
                    try {
                        getSystemTray().add(icon);
                    } catch (AWTException e1) {
                        e1.printStackTrace();
                    }
                }

            });
        }

		
		
		addButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                addButtonActionPerformed(e);
            }
        });
		
		removeButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                removeButtonActionPerformed(e);
            }
        });
		
		startButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                startButtonActionPerformed(e);
            }
        });	
		
		alertButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                alertButtonActionPerformed(e);
            }
        });	
		
		
		kickButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                kickButtonActionPerformed(e);
            }
        });	
		
		
		GUIuserList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) 
            {
            	int listIndex =  GUIuserList.getSelectedIndex();
            	String tmpUser = (String)GUIlistModel.get(listIndex);
            	//lets get the ServerUser object with this name
            	for (int i=0; i<ServerUserList.size();i++)
            	{
            		JEMServerUser tmpServerUser = (JEMServerUser)ServerUserList.get(i);
            		if(tmpServerUser.getUserName().equals(tmpUser))
            		{
            			userNameText.setText(tmpServerUser.getUserName());
            			PasswordText.setText(tmpServerUser.getPassword());
            			FirstNameText.setText(tmpServerUser.getFirstName());
            			LastNameText.setText(tmpServerUser.getLastName());
            		}
            		
            	}
            	
            }
        });

		createUsersFromFile();
		
		
	}
	
	private void addButtonActionPerformed(ActionEvent e)
	{
		boolean uniqueUser = true;
		if(!(userNameText.getText().isEmpty()) && !(PasswordText.getText().isEmpty()) && !(FirstNameText.getText().isEmpty())&& !(LastNameText.getText().isEmpty()))
		{
			//create a user from the fields if it doesnt already exist 
			
			for (int i=0; i<ServerUserList.size();i++)
        	{
        		JEMServerUser tmpServerUser = (JEMServerUser)ServerUserList.get(i);
        		if(tmpServerUser.getUserName().equals(userNameText.getText()))
        		{
        			uniqueUser = false;
        			JEMServerMessageDialog UserAlreadyExistDialog = new JEMServerMessageDialog("User Already Exist!", this);
    				userNameText.setText("");
    				PasswordText.setText("");
    				FirstNameText.setText("");
    				LastNameText.setText("");
        		}
        		
        	}
			
			
			if(uniqueUser)
			{
				//String textToWriteToFile  = userNameText.getText() + "	" + PasswordText.getText();
				saveUserToFile(userNameText.getText(),PasswordText.getText(),FirstNameText.getText(),LastNameText.getText());
				JEMServerUser tmpUser = new JEMServerUser(userNameText.getText(), PasswordText.getText(),FirstNameText.getText(),LastNameText.getText());
				ServerUserList.add(tmpUser);
				GUIlistModel.addElement(userNameText.getText());
				userNameText.setText("");
				PasswordText.setText("");
				FirstNameText.setText("");
				LastNameText.setText("");
			}
			
			//add the user to the ArrayList
			//add the user to the userList
			//save the user to file
		}else
		{
			JEMServerMessageDialog InformationMissingDialog = new JEMServerMessageDialog("Username or Password Missing!", this);
		}
		
	}
	
	private void removeButtonActionPerformed(ActionEvent e)
	{
		//check if the user exists
		removeUser(userNameText.getText());
		
	}
	
	private void  kickButtonActionPerformed(ActionEvent e)
	{
		//check if server is running
		//get the selected user
		if(GUIStartFlag==true)
		{
			String userName = (String)GUIuserList.getSelectedValue();
			if(userName==null)
			{
				JEMServerMessageDialog noSelection = new JEMServerMessageDialog("Please select a user!",this);
			}
			else
			{
				if(engine.chkIfUserOnline(userName))
				{
					engine.kickUser(userName);
					JEMServerMessageDialog noSelection = new JEMServerMessageDialog("User "+userName +" was kicked!",this);
					addLogText("User "+userName +" was kicked!");
				}
				else
				{
					JEMServerMessageDialog notOnline = new JEMServerMessageDialog("User "+userName +" not logged in!",this);
				}
			
			}
				
		}
		else
		{
			JEMServerMessageDialog notRunning = new JEMServerMessageDialog("Server not started!",this);
		}
		//follow the disconnect procedure
	}
	
	
	private void startButtonActionPerformed(ActionEvent e)
	{
		GUIStartFlag = true;
		JEMServerLogger logHandler = new JEMServerLogger(this);
		engine = new JEMServerEngine(ServerUserList,logHandler);
		engine.start();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JEMServerTimeoutChecker timeOutChecker = new JEMServerTimeoutChecker(ServerUserList,logHandler,engine);
		new Thread( timeOutChecker ).start();
		JEMServerMessageDialog alert = new JEMServerMessageDialog("Server started!", this);
		addLogText("Server Started");
	}
	
	private void alertButtonActionPerformed(ActionEvent e)
	{
		JEMServerAlertMessageDialog  Alert = new JEMServerAlertMessageDialog(this);
	}
	
	public void sendAlertToUsers(String sendText)
	{
		if(GUIStartFlag==true)
		{
		engine.alertUsers(sendText);
		}
		else
		{
			JEMServerMessageDialog notRunning = new JEMServerMessageDialog("Server not started!",this);
		}
	}
	
	
    public void createUsersFromFile()
    {
    	
    	BufferedReader inputStream = null;

    	try
    	{
            inputStream = new BufferedReader(new FileReader("JEMUsers.txt"));

            String l;
        	while ((l = inputStream.readLine()) != null) 
        		{
	            String tmpText = l;

	            
	            StringTokenizer st = new StringTokenizer(l);
	            while (st.hasMoreTokens()) 
	            {
	            	String friends = "";
	            	String name = st.nextToken();
	            	String password = st.nextToken();
	            	String fname = st.nextToken();
	            	String lname = st.nextToken();
	            	if(st.hasMoreElements()){
	            	friends = st.nextToken();
	            	}
	            	JEMServerUser tmpUser = new JEMServerUser(name, password,fname,lname);
	            	StringTokenizer sto = new StringTokenizer(friends,",");
	            	while (sto.hasMoreElements())
	            	{
	            		tmpUser.addFriend(sto.nextToken());
	            	}
	            	tmpUser.setStatus("Offline");
	    			ServerUserList.add(tmpUser);
	    			GUIlistModel.addElement(name);  	
	     		}

             
			}
             
           

    	}   
    	catch (IOException e) {
            System.err.println("Caught IOException: " 
                    +  e.getMessage());
    	}

    	finally 
    	{
            if (inputStream != null) {
            	try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }	
    	}   		
    }
    
    
    
    
    public void saveUserToFile(String name, String password, String firstName, String lastName)
    {
    	BufferedWriter outputStream = null;

    	try
    	{
    		 outputStream =  new BufferedWriter(new FileWriter("JEMUsers.txt",true));
             outputStream.write(name+"	"+password+" "+firstName+" "+lastName+" ,"+name);
             outputStream.newLine();
		

    	}   
    	catch (IOException e) {
            System.err.println("Caught IOException: " 
                    +  e.getMessage());
    	}

    	finally 
    	{
            if (outputStream != null) {
                try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }	
    	}
    	
    }
    
    public void updateUserFileData()
    {
    	//create a new file from the current users
    	BufferedWriter outputStream = null;
    	BufferedReader inputStream = null;
    	try
    	{
    		 outputStream =  new BufferedWriter(new FileWriter("JEMUsers.txt"));
    	    	for (int i=0; i<ServerUserList.size();i++)
    	    	{
    	    		JEMServerUser tmpUser = (JEMServerUser)ServerUserList.get(i);
    	    		ArrayList tmpList = tmpUser.getFriends();
    	    		String textToWrite = tmpUser.getUserName() + "	" + tmpUser.getPassword() + " " +tmpUser.getFirstName() + " " + tmpUser.getLastName() + " ";
    	    		for(int j=0;j<tmpList.size();j++)
    	    		{
    	    			textToWrite = textToWrite + "," + tmpList.get(j);
    	    		}
    	    		outputStream.write(textToWrite);
    	    		outputStream.newLine();
    	    	}
    	}   
    	catch (IOException e) {
            System.err.println("Caught IOException: " 
                    +  e.getMessage());
    	}

    	finally 
    	{
            if (outputStream != null) {
                try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }	
    	}
    	
    }
    
    //completely remove a user
    public void removeUser(String user)
    {
    	BufferedWriter outputStream = null;
    	BufferedReader inputStream = null;
    	//remove user from the ServerUserList and GUIlistmodel, then clear the file and create a new file from the new arraylist.
    	for (int i=0; i<ServerUserList.size();i++)
    	{
    		JEMServerUser tmpUser = (JEMServerUser)ServerUserList.get(i);
    		if(tmpUser.getUserName().equals(user))
    		{
    			ServerUserList.remove(i);
    		}
    		
    	}
    	for (int i=0; i<GUIlistModel.size();i++)
    	{
    		String tmpUser = (String)GUIlistModel.get(i);
    		if(tmpUser.equals(user))
    		{
    			GUIlistModel.remove(i);
    		}
    	}
    	
    	
    	//rewrite all the users to the JEMUsers.txt file
    	try
    	{
    		 outputStream =  new BufferedWriter(new FileWriter("JEMUsers.txt"));
    	    	for (int i=0; i<ServerUserList.size();i++)
    	    	{
    	    		JEMServerUser tmpUser = (JEMServerUser)ServerUserList.get(i);
    	    		ArrayList tmpList = tmpUser.getFriends();
    	    		String textToWrite = tmpUser.getUserName() + "	" + tmpUser.getPassword() + " " +tmpUser.getFirstName() + " " + tmpUser.getLastName() + " ";
    	    		for(int j=0;j<tmpList.size();j++)
    	    		{
    	    			textToWrite = textToWrite + "," + tmpList.get(j);
    	    		}
    	    		outputStream.write(textToWrite);
    	    		outputStream.newLine();
    	    	}
    	}   
    	catch (IOException e) {
            System.err.println("Caught IOException: " 
                    +  e.getMessage());
    	}

    	finally 
    	{
            if (outputStream != null) {
                try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }	
    	}  	
    }
    
    //this method is only called when a remote user registers an account
    public void registerNewUser(JEMServerUser newUser)
    {
    	//do the same as when the add-button is pressed
		saveUserToFile(newUser.getUserName(),newUser.getPassword(),newUser.getFirstName(),newUser.getLastName());
		ServerUserList.add(newUser);
		addUserToGuiList(newUser.getUserName());
    }
    
    
    public void addUserToGuiList(final String name)
    {
    	SwingUtilities.invokeLater( new Runnable()
	    {
	        public void run()
	        {
	        	GUIlistModel.addElement(name);
	        }
	    });
    }
    
    //add text to the log
    public void addLogText(String newLogEntry)
    {
    	
    	DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm a");
    	Date date = new Date();
    	
    	System.out.println();
    	
    	serverLogText.append("\n");
    	serverLogText.append("<"+dateFormat.format(date)+">" + newLogEntry);
    	serverLogText.setCaretPosition(serverLogText.getDocument().getLength() );
    }
    
    // Method used to delete a specified file. Is this method even needed? saving in case
    public void DeleteFile(String file) {
    	  
    	    String fileName = file;
    	    // A File object to represent the filename
    	    File f = new File(fileName);

    	    // Make sure the file or directory exists and isn't write protected
    	    if (!f.exists())
    	      throw new IllegalArgumentException(
    	          "Delete: no such file or directory: " + fileName);

    	    if (!f.canWrite())
    	      throw new IllegalArgumentException("Delete: write protected: "
    	          + fileName);

    	    // If it is a directory, make sure it is empty
    	    if (f.isDirectory()) {
    	      String[] files = f.list();
    	      if (files.length > 0)
    	        throw new IllegalArgumentException(
    	            "Delete: directory not empty: " + fileName);
    	    }

    	    // Attempt to delete it
    	    boolean success = f.delete();

    	    if (!success)
    	      throw new IllegalArgumentException("Delete: deletion failed");
    	  

    	}

    
    
    
}
