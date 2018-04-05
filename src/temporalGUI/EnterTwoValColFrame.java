package temporalGUI;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class EnterTwoValColFrame {

	private JFrame frame;
	Map<String,String> pk = new HashMap<String,String>();
	String selCol,selColType,choice;

	/**
	 * Create the application.
	 */
	public EnterTwoValColFrame(Map<String,String> val,String col,String cType,String c) {
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
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGap(0, 450, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGap(0, 300, Short.MAX_VALUE)
		);
		frame.getContentPane().setLayout(groupLayout);
	}

}
