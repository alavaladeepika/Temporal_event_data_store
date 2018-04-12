package temporalGUI;

import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JRadioButton;
import javax.swing.JButton;

import dbResource.DatabaseConnection;

public class OptTimestampNameFrame {

	private JFrame frame;
	ButtonGroup group;
	Map<String,String> pk = new HashMap<String,String>();
	String selTable = null;
	String selCol = null;
	String selColVal = null;
	String timestamp_name = null;
	JRadioButton[] jRadioButton;
	JButton btnBackToMenu, btnNext;

	/**
	 * Create the application.
	 */
	public OptTimestampNameFrame(Map<String,String> pkVal,String table,String col,String cVal) {
		pk = pkVal;
		selTable = table;
		selCol = col;
		selColVal = cVal;
		initialize();
		
		btnBackToMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TemporalMenuFrame m = new TemporalMenuFrame();
			}	
		});

		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				if(jRadioButton[0].isSelected())		timestamp_name = "YEAR";
				else if(jRadioButton[1].isSelected())	timestamp_name = "MONTH";
				else if(jRadioButton[2].isSelected())	timestamp_name = "DATE";
				
				@SuppressWarnings("unused")
				ResultFrame m = new ResultFrame(DatabaseConnection.getInstance().getColumn_Timestamp_Name(pk, selTable, selCol, selColVal, timestamp_name));
			}	
		});	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(100, 100, 1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		btnBackToMenu = new JButton("Back to Menu");
		btnNext = new JButton("Next");
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(201)
					.addComponent(btnBackToMenu)
					.addGap(364)
					.addComponent(btnNext, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(213))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(837, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnBackToMenu)
						.addComponent(btnNext))
					.addGap(108))
		);
		frame.getContentPane().setLayout(groupLayout);
		
		int x=50, y=50, width=500, height=60; //choose whatever you want
		group = new ButtonGroup();
        jRadioButton = new JRadioButton[3];
        
        jRadioButton[0] = new JRadioButton("TIMESTAMP_NAME AS 'YEAR'");
        jRadioButton[0].setBounds(x, y, width, height);
        group.add(jRadioButton[0]);
        frame.getContentPane().add(jRadioButton[0]);
        y += 40;
        
        jRadioButton[1] = new JRadioButton("TIMESTAMP_NAME AS 'MONTH'");
        jRadioButton[1].setBounds(x, y, width, height);
        group.add(jRadioButton[1]);
        frame.getContentPane().add(jRadioButton[1]);
        y += 40;
        
        jRadioButton[2] = new JRadioButton("TIMESTAMP_NAME AS 'DAY'");
        jRadioButton[2].setBounds(x, y, width, height);
        group.add(jRadioButton[2]);
        frame.getContentPane().add(jRadioButton[2]);
	}
}
