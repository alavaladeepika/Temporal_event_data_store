import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;

public class ColumnsFrame {

	private JFrame frame;
	ArrayList<JCheckBox[]> jCheckBox = new ArrayList<JCheckBox[]>();
	JButton btnNext;
	Map<String,Map<String,String>> ColumnNames = new HashMap<String,Map<String,String>>(); 
	String tableName;
	
	Map<String, Map<String,String>> TempColumns = new HashMap<String,Map<String,String>>();

	/**
	 * Create the application.
	 */
	public ColumnsFrame(ArrayList<String> tables) {
		for(int i=0;i<tables.size();i++) {
			ColumnNames.put(tables.get(i),DatabaseConnection.getInstance().getColumns(tables.get(i)));
		}
		initialize();
		
		//Next button action
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				//Get selected tables 
				int i=0;
				for(Map.Entry<String,Map<String,String>> entryVal:ColumnNames.entrySet()) {
					int j=0;
					Map<String,String> temp = new HashMap<String,String>();
					for(Map.Entry<String,String> entry:entryVal.getValue().entrySet()) {
						boolean isSelected = jCheckBox.get(i)[j].isSelected();
						if(isSelected) {
							temp.put(entry.getKey(),entry.getValue());
						}
						j++;
					}
					TempColumns.put(entryVal.getKey(),temp);
					i++;
				}
				Static_Temporalize.Execute(TempColumns);
				MenuFrame.TempTables = TempColumns;
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
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(315, Short.MAX_VALUE)
					.addComponent(btnNext)
					.addGap(69))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(234, Short.MAX_VALUE)
					.addComponent(btnNext)
					.addGap(41))
		);
		frame.getContentPane().setLayout(groupLayout);
		
		int i=0, x=50, y=50, width=500, height=60; //choose whatever you want
		for(Map.Entry<String,Map<String,String>> entryVal:ColumnNames.entrySet()) {
			JLabel lblSelectTheColumns = new JLabel("Select the columns from '" + entryVal.getKey() + "' to temporalise : ");
			lblSelectTheColumns.setBounds(x, y, width, height);
			frame.getContentPane().add(lblSelectTheColumns);
			jCheckBox.add(i,new JCheckBox[entryVal.getValue().size()]);
			int j=0;
			for(Map.Entry<String,String> entry:entryVal.getValue().entrySet()) {
				y+=40;
		        jCheckBox.get(i)[j] = new JCheckBox(entry.getKey());
	            jCheckBox.get(i)[j].setBounds(x, y, width, height);
	            frame.getContentPane().add(jCheckBox.get(i)[j]);
	            j++;
			}
			y+=40;
			i++;
		}
	}
}
