package temporalGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import dbResource.DatabaseConnection;
import dbResource.Temporal_Join;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class DiffEnterPKFrame {

	private JFrame frame;
	JButton btnNext = new JButton(),btnMenu = new JButton(),btnBack = new JButton();
	String[][] diffTables;
	Map<String,String> pkVal;
	Map<String,String> entered_val = new HashMap<String,String>();
	String ref_FK = null;
	JTextField[] textFields;
	/**
	 * Create the application.
	 */
	public DiffEnterPKFrame(String[][] sel, Map<String,String> pk) {
		diffTables = sel;
		pkVal = pk;
		Temporal_Join temp_join = new Temporal_Join();
		temp_join.initiate_join(diffTables);
		textFields = new JTextField[pkVal.size()+1];
		initialize(temp_join.type);
		
		btnMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TemporalMenuFrame menu = new TemporalMenuFrame();
			}
		});	
		
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				int i=0;
				for(Map.Entry<String,String> entry:pkVal.entrySet()){
					entered_val.put(entry.getKey(), textFields[i].getText());
					i++;
				}
				ref_FK = textFields[i].getText();
				@SuppressWarnings("unused")
				ResultFrame next = new ResultFrame(DatabaseConnection.getInstance().Difference_Operator(temp_join.join_info, entered_val, ref_FK));
			}
		});
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				OverlapsTablesFrame back = new OverlapsTablesFrame("difference");
			}
		});	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String type) {
		frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(100, 100, 1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		if(type.equals("temporal")) {
			btnNext = new JButton("Next");
			btnMenu = new JButton("Menu");
			
			JLabel lblEnterTheFollowing = new JLabel("Enter the following values to perform the 'DIFFERENCE' operator:");
			
			GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
			groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.TRAILING)
					.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap(375, Short.MAX_VALUE)
						.addComponent(btnMenu, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
						.addGap(376)
						.addComponent(btnNext, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
						.addGap(20))
					.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
						.addGap(43)
						.addComponent(lblEnterTheFollowing)
						.addContainerGap(524, Short.MAX_VALUE))
			);
			groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
						.addGap(26)
						.addComponent(lblEnterTheFollowing)
						.addGap(844)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnNext)
							.addComponent(btnMenu))
						.addContainerGap(60, Short.MAX_VALUE))
			);
			frame.getContentPane().setLayout(groupLayout);
			
			int i=0, x=50, y=50, width=500, height=60; //choose whatever you want
			for(Map.Entry<String,String> entry:pkVal.entrySet()) {
				JLabel lblCol = new JLabel(diffTables[0][0]+"."+entry.getKey()+":");
				lblCol.setBounds(x, y, width, height);
				frame.getContentPane().add(lblCol);
				y+=40;
				textFields[i] = new JTextField();
				textFields[i].setColumns(10);
	            textFields[i].setBounds(x, y, width, height);
	            
	            frame.getContentPane().add(textFields[i]);
	            y+=40;
	            i++;
			}
			JLabel lblCol = new JLabel(diffTables[1][0]+"."+diffTables[1][1]+":");
			lblCol.setBounds(x, y, width, height);
			frame.getContentPane().add(lblCol);
			y+=40;
			textFields[i] = new JTextField();
			textFields[i].setColumns(10);
	        textFields[i].setBounds(x, y, width, height);
	        frame.getContentPane().add(textFields[i]);
		}
		
		else if(type.equals("normal")) {
			JLabel lblCannotPerformoverlaps = new JLabel("Cannot perform 'DIFFERENCE' operator since both of the referencing and the referenced tables are not temporal!!");
			
			btnBack = new JButton("Back");
			GroupLayout groupLayout_1 = new GroupLayout(frame.getContentPane());
			groupLayout_1.setHorizontalGroup(
				groupLayout_1.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout_1.createSequentialGroup()
						.addContainerGap(142, Short.MAX_VALUE)
						.addGroup(groupLayout_1.createParallelGroup(Alignment.LEADING)
							.addGroup(Alignment.TRAILING, groupLayout_1.createSequentialGroup()
								.addComponent(lblCannotPerformoverlaps)
								.addGap(92))
							.addGroup(Alignment.TRAILING, groupLayout_1.createSequentialGroup()
								.addComponent(btnBack)
								.addGap(58))))
			);
			groupLayout_1.setVerticalGroup(
				groupLayout_1.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout_1.createSequentialGroup()
						.addGap(441)
						.addComponent(lblCannotPerformoverlaps)
						.addPreferredGap(ComponentPlacement.RELATED, 421, Short.MAX_VALUE)
						.addComponent(btnBack)
						.addGap(68))
			);
			frame.getContentPane().setLayout(groupLayout_1);
		}
	}
}
