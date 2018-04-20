package temporalGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import dbResource.DatabaseConnection;

public class OverlapsTablesFrame {

	private JFrame frame;
	private JButton btnNext;
	private JRadioButton[] jRadioButton;
	ButtonGroup group;
	ArrayList<String[][]> displayTables;
	String[][] selJoinTables;
	private JButton btnMenu;
	String choice;
	/**
	 * Create the application.
	 */
	
	public OverlapsTablesFrame(String c) {
		choice = c;
		displayTables = DatabaseConnection.getInstance().getPosTemporalJoinTables();
		initialize();
		
		//Next button action
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				//Get selected tables 
				
				for(int i=displayTables.size()-1;i>=0;i--) {
					boolean isSelected = jRadioButton[i].isSelected();
					if(isSelected) {
						selJoinTables = displayTables.get(i);
						break;
					}
				}
				if(choice.equals("overlaps")) {
					@SuppressWarnings("unused")
					OverlapsColumnsFrame colJoin = new OverlapsColumnsFrame(selJoinTables);
				}
				else if(choice.equals("difference")) {
					@SuppressWarnings("unused")
					DiffEnterPKFrame diff = new DiffEnterPKFrame(selJoinTables,DatabaseConnection.getInstance().getPrimaryKey(selJoinTables[0][0]));
				}
				
			}
		});	
		
		btnMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TemporalMenuFrame menu = new TemporalMenuFrame();
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
		
		JLabel lblNewLabel = new JLabel("Select the pair of tables you want to perform '"+choice+"' operator: ");
		btnNext = new JButton("Next");
		btnMenu = new JButton("Menu");
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(55)
					.addComponent(lblNewLabel)
					.addContainerGap(656, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(259)
					.addComponent(btnMenu)
					.addPreferredGap(ComponentPlacement.RELATED, 542, Short.MAX_VALUE)
					.addComponent(btnNext)
					.addGap(61))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(35)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED, 831, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNext)
						.addComponent(btnMenu))
					.addGap(38))
		);
		frame.getContentPane().setLayout(groupLayout);

		int x=50, y=50, width=900, height=60; //choose whatever you want
		group = new ButtonGroup();
        jRadioButton = new JRadioButton[displayTables.size()];
        for(int i=0; i<displayTables.size(); i++, y+=40) {
        	String pair = "REFERENCING TABLE: '" + displayTables.get(i)[0][0] + 
        			"' AND REFERENCED TABLE: '" + displayTables.get(i)[1][0] + "'";
            jRadioButton[i] = new JRadioButton(pair);
            jRadioButton[i].setBounds(x, y, width, height);
            group.add(jRadioButton[i]);
            frame.getContentPane().add(jRadioButton[i]);
        }
	}
}
