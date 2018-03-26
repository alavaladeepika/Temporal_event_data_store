import java.awt.EventQueue;
import java.util.*;

import javax.swing.JFrame;

public class MenuFrame {

	private JFrame frame;
	public static Map<String,Map<String,String>> TempTables;

	/**
	 * Create the application.
	 */
	public MenuFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
