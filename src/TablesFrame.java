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
	ArrayList<String> selTables = new ArrayList<String>();
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
						selTables.add(displayTables.get(i));
					}
				}
				@SuppressWarnings("unused")
				ColumnsFrame c = new ColumnsFrame(selTables);
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
		
		JLabel lblNewLabel = new JLabel("Select the tables you want to temporalise :");
		btnNext = new JButton("Next");
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(55)
					.addComponent(lblNewLabel)
					.addContainerGap(637, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(873, Short.MAX_VALUE)
					.addComponent(btnNext)
					.addGap(61))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(35)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED, 887, Short.MAX_VALUE)
					.addComponent(btnNext)
					.addGap(38))
		);
		frame.getContentPane().setLayout(groupLayout);

		int x=50, y=50, width=200, height=60; //choose whatever you want
        jRadioButton = new JRadioButton[displayTables.size()];
        for(int i=0; i<displayTables.size(); i++, y+=40) {
            jRadioButton[i] = new JRadioButton(displayTables.get(i));
            jRadioButton[i].setBounds(x, y, width, height);
            frame.getContentPane().add(jRadioButton[i]);
        }
	}
}
