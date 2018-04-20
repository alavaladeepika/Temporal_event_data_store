package temporalGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import dbResource.Temporal_Join;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class OverlapsEnterPKFrame {

	private JFrame frame;
	JButton btnNext,btnMenu;
	String[][] overlapTables;
	ArrayList<String> sel_col;
	Map<String,String> pkVal;
	Map<String,String> entered_val = new HashMap<String,String>();
	JTextField[] textFields;
	/**
	 * Create the application.
	 */
	public OverlapsEnterPKFrame(String[][] sel, ArrayList<String> col, Map<String,String> pk) {
		overlapTables = sel;
		sel_col = col;
		pkVal = pk;
		Temporal_Join temp_join = new Temporal_Join();
		temp_join.initiate_join(overlapTables);
		initialize();
		
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
					entered_val.put(overlapTables[0][0]+"."+entry.getKey(), textFields[i].getText());
					i++;
				}
				entered_val.put(overlapTables[1][0]+"."+overlapTables[1][1], textFields[i].getText());
				@SuppressWarnings("unused")
				ResultFrame next = new ResultFrame(temp_join.perform_overlaps(sel_col,entered_val));
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
		
		btnNext = new JButton("Next");
		btnMenu = new JButton("Menu");
		
		JLabel lblEnterTheFollowing = new JLabel("Enter the following values to perform the 'OVERLAPS' operator:");
		
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
		textFields = new JTextField[pkVal.size()+1];
		for(Map.Entry<String,String> entry:pkVal.entrySet()) {
			JLabel lblCol = new JLabel(overlapTables[0][0]+"."+entry.getKey()+":");
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
		JLabel lblCol = new JLabel(overlapTables[1][0]+"."+overlapTables[1][1]+":");
		lblCol.setBounds(x, y, width, height);
		frame.getContentPane().add(lblCol);
		y+=40;
		textFields[i] = new JTextField();
		textFields[i].setColumns(10);
        textFields[i].setBounds(x, y, width, height);
        frame.getContentPane().add(textFields[i]);
	}

}
