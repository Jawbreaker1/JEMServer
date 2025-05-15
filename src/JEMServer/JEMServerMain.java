package JEMServer;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class JEMServerMain {

	 public static void main(String[] args) {
	        SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                createAndShowGUI(); 
	            }
	        });
	    }
	    
	    private static void createAndShowGUI(){
	    	//create main window
	    	try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	JEMServerGui serverGui = new JEMServerGui();
	    }	
}
