package temporalGUI;

import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import dbResource.DatabaseConnection;
import javax.swing.JLabel;

public class DateScaleFrame {

	private JFrame frame;
	ButtonGroup group;
	String selTable = null;
	JRadioButton[] jRadioButton;
	JButton btnBackToMenu, btnNext;
	JLabel resultLabel;

	/**
	 * Create the application.
	 */
	public DateScaleFrame() {
		initialize();
		
		btnBackToMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TemporalMenuFrame m = new TemporalMenuFrame();
			}	
		});

		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				resultLabel.setText(DatabaseConnection.getInstance().getColumn_Value(pk, selTable, selCol, date));
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
		
		btnBackToMenu = new JButton("Back to Menu");
		btnNext = new JButton("Next");
		
		JLabel lblColumn = new JLabel("'"+selCol+"': ");
		
		resultLabel = new JLabel("");
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(246)
					.addComponent(lblColumn)
					.addPreferredGap(ComponentPlacement.RELATED, 132, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(resultLabel)
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(btnNext, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnBackToMenu, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGap(437))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(653)
					.addComponent(btnNext)
					.addGap(82)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblColumn)
						.addComponent(resultLabel))
					.addPreferredGap(ComponentPlacement.RELATED, 113, Short.MAX_VALUE)
					.addComponent(btnBackToMenu)
					.addGap(57))
		);
		frame.getContentPane().setLayout(groupLayout);
		
		int x=50, y=50, width=200, height=60; //choose whatever you want
		group = new ButtonGroup();
        jRadioButton = new JRadioButton[3];
        
        jRadioButton[0] = new JRadioButton("Scale of one 'YEAR'");
        jRadioButton[0].setBounds(x, y, width, height);
        group.add(jRadioButton[0]);
        frame.getContentPane().add(jRadioButton[0]);
        y += 40;
        
        jRadioButton[1] = new JRadioButton("Scale of one 'MONTH'");
        jRadioButton[1].setBounds(x, y, width, height);
        group.add(jRadioButton[1]);
        frame.getContentPane().add(jRadioButton[1]);
        y += 40;
        
        jRadioButton[2] = new JRadioButton("Scale of one 'DAY'");
        jRadioButton[2].setBounds(x, y, width, height);
        group.add(jRadioButton[2]);
        frame.getContentPane().add(jRadioButton[2]);
	}
}
