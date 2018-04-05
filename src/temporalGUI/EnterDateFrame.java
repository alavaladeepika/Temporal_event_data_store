package temporalGUI;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class EnterDateFrame {

	private JFrame frame;
	Map<String,String> pk = new HashMap<String,String>();
	String selCol=null,selTable=null;
	/**
	 * @wbp.nonvisual location=43,34
	 */
	private final JLabel label = new JLabel("New label");

	/**
	 * Create the application.
	 */
	public EnterDateFrame(Map<String,String> val,String table,String col) {
		pk = val;
		selTable = table;
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
