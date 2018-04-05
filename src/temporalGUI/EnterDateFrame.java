package temporalGUI;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

public class EnterDateFrame {

	private JFrame frame;
	Map<String,String> pk = new HashMap<String,String>();
	String selCol=null;

	/**
	 * Create the application.
	 */
	public EnterDateFrame(Map<String,String> val,String col) {
		pk = val;
		selCol = col;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
