import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

public class TablesFrame {

	private JFrame frame;
	private JButton btnNext;
	private JRadioButton[] jRadioButton;
	ArrayList<String> displayTables;
	Map<String, String> colNames = new HashMap<String,String>();
	Map<String,Map<String,String>> tempCol = new HashMap<String,Map<String,String>>();
	/**
	 * Create the application.
	 */
	public TablesFrame(ArrayList<String> tables) {
		displayTables = tables;
		initialize();
		
		//Next button action
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				//Get selected tables 
				
				for(int i=displayTables.size()-1;i>=0;i--) {
					boolean isSelected = jRadioButton[i].isSelected();
					if(isSelected) {
						colNames = DatabaseConnection.getInstance().getColumns(displayTables.get(i));
						ColumnsFrame c = new ColumnsFrame(displayTables.get(i),colNames);
						tempCol.put(displayTables.get(i),c.getTempColumns());
					}
				}
				Static_Temporalize.Execute(tempCol);
			}
		});	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("Select the tables you want to temporalise :");
		btnNext = new JButton("Next");
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(291, Short.MAX_VALUE)
					.addComponent(btnNext)
					.addGap(22))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(55)
					.addComponent(lblNewLabel)
					.addContainerGap(87, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGap(35)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED, 182, Short.MAX_VALUE)
					.addComponent(btnNext)
					.addGap(43))
		);
		frame.getContentPane().setLayout(groupLayout);

		int x=50, y=50, width=200, height=50; //choose whatever you want
        jRadioButton = new JRadioButton[displayTables.size()];
        for(int i=0; i<displayTables.size(); i++, y+=30) {
            jRadioButton[i] = new JRadioButton(displayTables.get(i));
            jRadioButton[i].setBounds(x, y, width, height);
            frame.getContentPane().add(jRadioButton[i]);
        }
	}
}
