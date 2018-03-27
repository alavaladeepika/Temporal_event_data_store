import java.awt.EventQueue;

import javax.swing.JFrame;

public class DeleteFrame {

	private JFrame frame;

	

	/**
	 * Create the application.
	 */
	public DeleteFrame(String selTable) {
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
