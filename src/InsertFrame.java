import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import java.util.*;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
public class InsertFrame {

	private JFrame frame;
	public static Map<String,String> colNames;
	JTextField[] textFields;
	JButton btnNext;
	Map<String,String> modCol;
	
	/**
	 * Create the application.
	 */
	public InsertFrame(String selTable,Map<String,String> c) {
		colNames = c;
		
		initialize();
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				//Get selected tables 
				int i = 0;
				modCol = new HashMap<String,String>();
				for(Map.Entry<String,String> entry:colNames.entrySet()){
					modCol.put(entry.getKey(), textFields[i].getText());
					i++;
				}
				DatabaseConnection.getInstance().insertRow(modCol,selTable);
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
		
		JLabel lblEnterValuesTo = new JLabel("Enter values to 'insert' :");
		
		btnNext = new JButton("Next");
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(26)
					.addComponent(lblEnterValuesTo)
					.addContainerGap(262, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(352, Short.MAX_VALUE)
					.addComponent(btnNext)
					.addGap(32))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(36)
					.addComponent(lblEnterValuesTo)
					.addPreferredGap(ComponentPlacement.RELATED, 196, Short.MAX_VALUE)
					.addComponent(btnNext)
					.addGap(28))
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
