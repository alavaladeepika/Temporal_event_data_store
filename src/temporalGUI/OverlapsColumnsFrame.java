package temporalGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.LayoutStyle.ComponentPlacement;

import dbResource.DatabaseConnection;
import dbResource.Temporal_Join;

public class OverlapsColumnsFrame {

	private JFrame frame;
	private JButton btnNext = new JButton();
	private JCheckBox[] jCheckBox;
	String[][] joinTables;
	Map<String,String> pk;
	ArrayList<String> join_col, sel_join_col;
	private JButton btnMenu = new JButton();
	private JButton btnBack = new JButton();
	/**
	 * Create the application.
	 */
	public OverlapsColumnsFrame(String[][] sel) {
		joinTables = sel;
		pk = DatabaseConnection.getInstance().getPrimaryKey(joinTables[0][0]);
		Temporal_Join temp_join = new Temporal_Join();
		temp_join.initiate_join(joinTables);
		join_col = temp_join.getJoinColumns();
		initialize(temp_join.type);
		
		//Next button action
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				//Get selected tables 
				sel_join_col = new ArrayList<String>();
				for(int i=0;i<join_col.size();i++) {
					boolean isSelected = jCheckBox[i].isSelected();
					if(isSelected) {
						sel_join_col.add(join_col.get(i));
					}
				}
				@SuppressWarnings("unused")
				OverlapsEnterPKFrame r = new OverlapsEnterPKFrame(joinTables,sel_join_col,pk);
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
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				OverlapsTablesFrame back = new OverlapsTablesFrame("overlaps");
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
			JLabel lblNewLabel = new JLabel("Select the columns you want to see after 'OVERLAP' operator:");
			btnNext = new JButton("Next");
			
			btnMenu = new JButton("Menu");
			
			GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
			groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.TRAILING)
					.addGroup(groupLayout.createSequentialGroup()
						.addGap(55)
						.addComponent(lblNewLabel)
						.addContainerGap(625, Short.MAX_VALUE))
					.addGroup(groupLayout.createSequentialGroup()
						.addGap(295)
						.addComponent(btnMenu)
						.addPreferredGap(ComponentPlacement.RELATED, 464, Short.MAX_VALUE)
						.addComponent(btnNext)
						.addGap(61))
			);
			groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
						.addGap(35)
						.addComponent(lblNewLabel)
						.addPreferredGap(ComponentPlacement.RELATED, 857, Short.MAX_VALUE)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnNext)
							.addComponent(btnMenu))
						.addGap(38))
			);
			frame.getContentPane().setLayout(groupLayout);
	
			int x=50, y=50, width=800, height=60; //choose whatever you want
	        jCheckBox = new JCheckBox[join_col.size()];
			for(int i=0;i<join_col.size();i++, y+=40) {
	            jCheckBox[i] = new JCheckBox(join_col.get(i));
	            jCheckBox[i].setBounds(x, y, width, height);
	            frame.getContentPane().add(jCheckBox[i]);
	        }
		}
		
		else if(type.equals("normal")) {
			JLabel lblCannotPerformoverlaps = new JLabel("Cannot perform 'OVERLAPS' operator since both of the referencing and the referenced tables are not temporal!!");
			
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
