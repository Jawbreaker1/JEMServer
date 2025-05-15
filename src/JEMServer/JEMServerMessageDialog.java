package JEMServer;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/***************************************************
 * @author v068554
 *Messagedialog class used when events occur that
 *the user needs to be informed about. 
 ***************************************************/


public class JEMServerMessageDialog extends JDialog{

	
	public JEMServerMessageDialog(String Message, JEMServerGui owner)
	{
	setLayout(null);
	setMinimumSize(new Dimension(200,150));
	Point ownerPos = owner.getLocation();
	setLocation(ownerPos.x+200, ownerPos.y+200);
	setTitle("Warning!");
	JLabel TextMessage = new JLabel(Message);
	TextMessage.setBounds(5, 10, 190, 50);
	JButton okButton = new JButton("Ok");
	okButton.setBounds(15, 80, 75, 25);
	okButton.addActionListener(new ActionListener() 
    {
        public void actionPerformed(ActionEvent e) 
        {
            okButtonActionPerformed(e);
        }
    });
	
	add(TextMessage);
	add(okButton);
	
    pack();
    setVisible(true);
	
	}
	
	private void okButtonActionPerformed(ActionEvent e)
	{
		setVisible(false);
	}
	
}
