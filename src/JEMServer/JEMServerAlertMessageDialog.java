package JEMServer;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;

/*************************************
 * 
 * @author Johan Engwall
 * opens a dialog and sends this message to all logged in users.
 ************************************/

public class JEMServerAlertMessageDialog extends JDialog {
	
	private JTextArea messageArea;
	private JEMServerGui owner;

	public JEMServerAlertMessageDialog(JEMServerGui Owner)
	{
		
		owner = Owner;
		setLayout(null);
		setMinimumSize(new Dimension(250,200));
		Point ownerPos = owner.getLocation();
		setLocation(ownerPos.x+200, ownerPos.y+200);
		setTitle("Alert Message");
		JLabel TextMessage = new JLabel("Text:");
		TextMessage.setBounds(15, 0, 190, 20);
		JButton okButton = new JButton("Send");
		JButton cancelButton = new JButton("Cancel");
		messageArea = new JTextArea();
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);
		messageArea.setBounds(15,20,210,100);
		messageArea.setBorder(BorderFactory.createEtchedBorder());
		okButton.setBounds(15, 125, 75, 25);
		cancelButton.setBounds(150,125,75,25);
		okButton.addActionListener(new ActionListener() 
	    {
	        public void actionPerformed(ActionEvent e) 
	        {
	            okButtonActionPerformed(e);
	        }
	    });
		cancelButton.addActionListener(new ActionListener() 
	    {
	        public void actionPerformed(ActionEvent e) 
	        {
	            cancelButtonActionPerformed(e);
	        }
	    });
		
		messageArea.addKeyListener(new KeyAdapter() {
     		public void keyPressed(KeyEvent e)
     		{
     			if(e.getKeyCode()==e.VK_ENTER)
     			{
     				if((!messageArea.getText().equals("")))
     						{
		     				owner.sendAlertToUsers(messageArea.getText());
		     				setVisible(false);
     						}
     				
     			}
     		}
		});	  
		
		
		
		add(messageArea);
		add(TextMessage);
		add(okButton);
		add(cancelButton);
		
	    pack();
	    setVisible(true);
		
		}
		
		private void okButtonActionPerformed(ActionEvent e)
		{
			owner.sendAlertToUsers(messageArea.getText());
			setVisible(false);
		}
		
		private void cancelButtonActionPerformed(ActionEvent e)
		{
			setVisible(false);
		}
}

