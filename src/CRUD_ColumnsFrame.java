import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

public class CRUD_ColumnsFrame {

	private JFrame frame;
	Map<String,String> colNames;
	String choice = null;
	private JButton btnNext;
	JCheckBox[] jCheckBox;
	String selTable;
	private JButton btnMenu;

	/**
	 * Create the application.
	 */
	public CRUD_ColumnsFrame(String table,String c,Map<String,String> t) {
		choice = c;
		selTable = table;
		colNames = t;
		initialize();
		
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				//Get selected tables 
				int i=0;
				Map<String,String> selCols = new HashMap<String,String>();
				for(Map.Entry<String,String> entry:colNames.entrySet()) {
					boolean isSelected = jCheckBox[i].isSelected();
					if(isSelected) {
						selCols.put(entry.getKey(),entry.getValue());
					}
					i++;
				}
				switch(choice) {
			 	case "delete":
			 		@SuppressWarnings("unused") DeleteFrame d= new DeleteFrame(selTable,selCols);
			 		break;
			 	case "update":
			 		@SuppressWarnings("unused") UpdateFrame u = new UpdateFrame(selTable,selCols);
			 		break;
			 	case "select":
			 		@SuppressWarnings("unused") SelectFrame s = new SelectFrame(selTable,selCols);
			 		break;
			 	default:
			 		
					break;
			 }
			}
		});	
		
		btnMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
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
		
		btnNext = new JButton("Next");
		
		JLabel lblSelectTheColumns = new JLabel("Select the columns through which you want to '" + choice + "' from '" + selTable + "' :");
		
		btnMenu = new JButton("Menu");
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSelectTheColumns)
					.addContainerGap(434, Short.MAX_VALUE))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(221)
					.addComponent(btnMenu, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 607, Short.MAX_VALUE)
					.addComponent(btnNext)
					.addGap(23))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(23)
					.addComponent(lblSelectTheColumns)
					.addGap(880)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnMenu, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnNext))
					.addGap(57))
		);
		frame.getContentPane().setLayout(groupLayout);
		
		int i=0, x=50, y=50, width=500, height=60; //choose whatever you want
		jCheckBox = new JCheckBox[colNames.size()];
		for(Map.Entry<String,String> entry:colNames.entrySet()) {
	        jCheckBox[i] = new JCheckBox(entry.getKey());
            jCheckBox[i].setBounds(x, y, width, height);
            frame.getContentPane().add(jCheckBox[i]);
            i++;
            y+=40;
		}
	}
}
