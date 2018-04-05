package temporalGUI;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

public class EnterColValFrame {

	private JFrame frame;
	Map<String,String> pk = new HashMap<String,String>();
	String selCol,selColType,choice;

	/**
	 * Create the application.
	 */
	public EnterColValFrame(Map<String,String> val,String col,String cType,String c) {
		pk = val;
		selCol = col;
		selColType = cType;
		choice = c;
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
