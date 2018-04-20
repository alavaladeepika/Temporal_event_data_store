package initGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;

import dbResource.DatabaseConnection;

public class DeleteFrame {

	private JFrame frame;
	Map<String,String> colNames;
	String selTable;
	JButton btnNext;
	JButton btnMenu;
	JTextField[] textFields;
	Map<String,String> modCol;

	

	/**
	 * Create the application.
	 */
	public DeleteFrame(String table,Map<String,String> sel) {
		selTable = table;
		colNames = sel;
		initialize();
		
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				//Get selected tables 
				int i = 0;
				modCol = new HashMap<String,String>();
				for(Map.Entry<String,String> entry:colNames.entrySet()){
					modCol.put(entry.getKey(), textFields[i].getText());
					i++;
				}
				DatabaseConnection.getInstance().deleteRow(modCol,selTable);
				@SuppressWarnings("unused")
				MenuFrame m = new MenuFrame();
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
		
		JLabel lblEnterValuesTo = new JLabel("Enter values to 'delete' :");
		
		btnNext = new JButton("Next");
		
		btnMenu = new JButton("Menu");
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(26)
					.addComponent(lblEnterValuesTo)
					.addContainerGap(807, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(519)
					.addComponent(btnMenu, GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
					.addGap(269)
					.addComponent(btnNext)
					.addGap(73))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(36)
					.addComponent(lblEnterValuesTo)
					.addGap(788)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnMenu, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnNext))
					.addGap(106))
		);
		frame.getContentPane().setLayout(groupLayout);
		
		int i=0, x=50, y=50, width=500, height=60; //choose whatever you want
		textFields = new JTextField[colNames.size()];
		for(Map.Entry<String,String> entry:colNames.entrySet()) {
			
			JLabel lblCol = new JLabel(entry.getKey()+"("+entry.getValue()+") :");
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
	}
}
