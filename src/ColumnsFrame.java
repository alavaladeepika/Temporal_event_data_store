import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

public class ColumnsFrame {

	private JFrame frame;
	JRadioButton[] jRadioButton;
	JButton btnNext;
	Map<String,String> ColumnNames; 
	String tableName;
	Map<String,String> TempColumns = new HashMap<String,String>();

	/**
	 * Create the application.
	 */
	public ColumnsFrame(String table,Map<String,String> col) {
		ColumnNames = col;
		tableName = table;
		initialize();
		
		//Next button action
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				//Get selected tables 
				
				for(int i=0;i<ColumnNames.size();i++) {
					boolean isSelected = jRadioButton[i].isSelected();
					if(isSelected) {
						String colName = jRadioButton[i].getName();
						TempColumns.put(colName,ColumnNames.get(colName));
					}
				}
			}
		});	
	}
	
	public Map<String,String> getTempColumns(){
		return TempColumns;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblSelectTheColumns = new JLabel("Select the columns from '" + tableName + "' to temporalise : ");
		
		btnNext = new JButton("Next");
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSelectTheColumns)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(336, Short.MAX_VALUE)
					.addComponent(btnNext)
					.addGap(73))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(36)
					.addComponent(lblSelectTheColumns)
					.addPreferredGap(ComponentPlacement.RELATED, 212, Short.MAX_VALUE)
					.addComponent(btnNext)
					.addContainerGap())
		);
		frame.getContentPane().setLayout(groupLayout);
		
		int i=0, x=50, y=50, width=200, height=50; //choose whatever you want
		jRadioButton = new JRadioButton[ColumnNames.size()];
		for(Map.Entry<String,String> entry:ColumnNames.entrySet()) {
	        jRadioButton[i] = new JRadioButton(entry.getKey());
            jRadioButton[i].setBounds(x, y, width, height);
            frame.getContentPane().add(jRadioButton[i]);
            y+=30;
            i++;
		}
	}
}
