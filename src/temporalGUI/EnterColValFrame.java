package temporalGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import dbResource.DatabaseConnection;

import javax.swing.JButton;

public class EnterColValFrame {

	private JFrame frame;
	Map<String,String> pk = new HashMap<String,String>();
	String selTable,selCol,selColType,choice;
	private JTextField textField1;
	private JButton btnNext, btnBackToMenu;

	/**
	 * Create the application.
	 */
	public EnterColValFrame(Map<String,String> val,String table,String col,String cType,String c) {
		pk = val;
		selCol = col;
		selColType = cType;
		selTable = table;
		choice = c;
		initialize();
		
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				
				String colVal = textField1.getText();
				switch(choice) {
				case "next":
					@SuppressWarnings("unused") 
			 		ResultFrame n = new ResultFrame(DatabaseConnection.getInstance().getNext(pk, selTable, selCol, colVal));
			 		break;
				case "previous":
					@SuppressWarnings("unused") 
			 		ResultFrame p = new ResultFrame(DatabaseConnection.getInstance().getPrevious(pk, selTable, selCol, colVal));
			 		break;
				case "evolution":
					@SuppressWarnings("unused") 
			 		ResultFrame ev = new ResultFrame(DatabaseConnection.getInstance().getEvolution(pk, selTable, selCol, colVal));
			 		break;
				case "timestamps":
					@SuppressWarnings("unused") 
			 		ResultFrame t = new ResultFrame(DatabaseConnection.getInstance().getTimestamps(pk, selTable, selCol, colVal));
			 		break;
				case "this_timestampname":
					@SuppressWarnings("unused") 
			 		OptTimestampNameFrame tp = new OptTimestampNameFrame(pk,selTable,selCol,colVal);
			 		break;
				default: break;
					
				}
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
		
		textField1 = new JTextField();
		textField1.setColumns(10);
		
		JLabel lblFrom = new JLabel("Enter the '"+selCol+"' ('"+selColType+"'):");
		
		btnNext = new JButton("Next");
		btnBackToMenu = new JButton("Back to menu");

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(lblFrom)
					.addGap(37)
					.addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(576))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(80)
					.addComponent(btnBackToMenu)
					.addPreferredGap(ComponentPlacement.RELATED, 667, Short.MAX_VALUE)
					.addComponent(btnNext)
					.addGap(68))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(84)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFrom)
						.addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(777)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnBackToMenu)
						.addComponent(btnNext))
					.addGap(87))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
