package initGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import dbResource.DatabaseConnection;

public class CRUD_TablesFrame {

	private JFrame frame;
	ArrayList<String> displayTables;
	String choice;
	JButton btnNext;
	JButton btnMenu;
	ButtonGroup group;
	String selTable = null;
	JRadioButton[] jRadioButton;
	
	public static Map<String,String> select_col_Names;
	
	/**
	 * Create the application.
	 */
	public CRUD_TablesFrame(String c) {
		choice = c;
		displayTables = WelcomeFrame.transactTables;
		
		initialize();
		
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				//Get selected tables 
				
				for(int i=displayTables.size()-1;i>=0;i--) {
					boolean isSelected = jRadioButton[i].isSelected();
					if(isSelected) {
						selTable = (displayTables.get(i));
						break;
					}
				}
				 switch(choice) {
				 	case "insert": 
				 		@SuppressWarnings("unused") InsertFrame i = new InsertFrame(selTable,DatabaseConnection.getInstance().getInsert_UpdateColumns(selTable)); 
				 		break;
				 	case "delete":
				 		@SuppressWarnings("unused") CRUD_ColumnsFrame d= new CRUD_ColumnsFrame(selTable,choice,DatabaseConnection.getInstance().getSelect_DeleteColumns(selTable));
				 		break;
				 	case "update":
				 		@SuppressWarnings("unused") CRUD_ColumnsFrame u = new CRUD_ColumnsFrame(selTable,choice,DatabaseConnection.getInstance().getInsert_UpdateColumns(selTable));
				 		break;
				 	case "select":
				 		select_col_Names = DatabaseConnection.getInstance().getSelect_DeleteColumns(selTable);
				 		@SuppressWarnings("unused") CRUD_ColumnsFrame s = new CRUD_ColumnsFrame(selTable,choice,select_col_Names);
				 		break;
				 	default:
				 		
						break;
				 }
			}
		});	
		
		btnMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				MenuFrame m = new MenuFrame();
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
		JLabel lblNewLabel = new JLabel("Select the table to perform '" + choice +"' :");
		btnNext = new JButton("Next");
		
		btnMenu = new JButton("Menu");
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(55)
					.addComponent(lblNewLabel)
					.addContainerGap(654, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(387)
					.addComponent(btnMenu, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(413)
					.addComponent(btnNext)
					.addGap(61))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(35)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED, 603, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNext)
						.addComponent(btnMenu, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(38))
		);
		frame.getContentPane().setLayout(groupLayout);
		
		
		int x=50, y=50, width=200, height=60; //choose whatever you want
		group = new ButtonGroup();
        jRadioButton = new JRadioButton[displayTables.size()];
        for(int i=0; i<displayTables.size(); i++, y+=40) {
            jRadioButton[i] = new JRadioButton(displayTables.get(i));
            jRadioButton[i].setBounds(x, y, width, height);
            group.add(jRadioButton[i]);
            frame.getContentPane().add(jRadioButton[i]);
        }
	}
}
