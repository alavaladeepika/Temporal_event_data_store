package temporalGUI;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;

public class EnterDateFrame {

	private JFrame frame;
	Map<String,String> pk = new HashMap<String,String>();
	String selCol=null,selTable=null;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JButton btnBackToMenu;
	private JButton btnOk;

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
		frame.setBounds(100, 100, 1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		JLabel lblEnterTheDesired = new JLabel("Enter the desired date:");
		
		JLabel lblYearyyyy = new JLabel("Year(yyyy):");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblMonthmm = new JLabel("Month(mm):");
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		JLabel lblDaydd = new JLabel("Day(dd):");
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		
		btnBackToMenu = new JButton("Back to Menu");
		
		btnOk = new JButton("OK");
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(24)
							.addComponent(lblEnterTheDesired))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(40)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblYearyyyy)
								.addComponent(lblMonthmm)
								.addComponent(lblDaydd))
							.addGap(43)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(103)
							.addComponent(btnBackToMenu)
							.addGap(216)
							.addComponent(btnOk, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(393, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(40)
					.addComponent(lblEnterTheDesired)
					.addGap(40)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblYearyyyy)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(31)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMonthmm)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(27)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDaydd)
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(355)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnBackToMenu)
						.addComponent(btnOk))
					.addContainerGap(410, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
