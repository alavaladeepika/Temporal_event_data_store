package temporalGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JButton;
import dbResource.DatabaseConnection;

public class EnterDateFrame {

	private JFrame frame;
	Map<String,String> pk = new HashMap<String,String>();
	String selCol=null,selTable=null;
	private JTextField yyText;
	private JTextField monText;
	private JTextField dayText;
	private JButton btnBackToMenu;
	private JButton btnNext;
	private JTextField hhText;
	private JTextField mmText;
	private JTextField ssText;
	private JLabel label;

	/**
	 * Create the application.
	 */
	public EnterDateFrame(Map<String,String> val,String table,String col) {
		pk = val;
		selTable = table;
		selCol = col;
		initialize();
		
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				String date = yyText.getText() + "-" + monText.getText() + "-" + dayText.getText() + " "
						+ hhText.getText() + ":" + mmText.getText() + ":" + ssText.getText();
				label.setText(DatabaseConnection.getInstance().getColumn_Value(pk, selTable, selCol, date));
			}
		});
		
		btnBackToMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TemporalMenuFrame m = new TemporalMenuFrame();
			}
		});
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
		
		yyText = new JTextField();
		yyText.setColumns(10);
		
		JLabel lblMonthmm = new JLabel("Month(mm):");
		
		monText = new JTextField();
		monText.setColumns(10);
		
		JLabel lblDaydd = new JLabel("Day(dd):");
		
		dayText = new JTextField();
		dayText.setColumns(10);
		
		btnBackToMenu = new JButton("Back to Menu");
		
		btnNext = new JButton("Next");
		
		JLabel lblHourhh = new JLabel("Hours(hh):");
		JLabel lblMinutesmm = new JLabel("Minutes(mm);");
		JLabel lblSecondsss = new JLabel("Seconds(ss):");
		
		hhText = new JTextField();
		hhText.setColumns(10);
		
		mmText = new JTextField();
		mmText.setColumns(10);
		
		ssText = new JTextField();
		ssText.setColumns(10);
		
		JLabel lblColumnName = new JLabel("'"+selCol+"': ");
		
		label = new JLabel("");
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
								.addComponent(lblDaydd)
								.addComponent(lblHourhh)
								.addComponent(lblMinutesmm)
								.addComponent(lblSecondsss)
								.addComponent(lblColumnName))
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(43)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(ssText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(mmText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(hhText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(dayText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(monText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(yyText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(66)
									.addComponent(label))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(156)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnBackToMenu, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnNext, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))))
					.addContainerGap(685, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(40)
					.addComponent(lblEnterTheDesired)
					.addGap(40)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblYearyyyy)
						.addComponent(yyText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(31)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMonthmm)
						.addComponent(monText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(27)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDaydd)
						.addComponent(dayText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(34)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblHourhh)
						.addComponent(hhText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(36)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMinutesmm)
						.addComponent(mmText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(33)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSecondsss)
						.addComponent(ssText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(66)
					.addComponent(btnNext)
					.addGap(142)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblColumnName)
						.addComponent(label))
					.addGap(240)
					.addComponent(btnBackToMenu)
					.addContainerGap(87, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
