package temporalGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import dbResource.DatabaseConnection;

public class EnterPKFrame {

	private JFrame frame;
	String choice = null;
	JTextField[] textFields;
	private JButton btnBackToMenu, btnNext;
	Map<String, String> pk = new HashMap<String,String>();
	String selTable=null,selCol=null,selColType=null;
	Map<String, String> pkVal = new HashMap<String,String>();

	/**
	 * Create the application.
	 */
	public EnterPKFrame(String table,Map<String,String> col,String c) {
		choice = c;
		selTable = table;
		for(Map.Entry<String,String> entry:col.entrySet()){
			selColType = entry.getValue();
			selCol = entry.getKey();
			break;
		}
		pk = DatabaseConnection.getInstance().getPrimaryKey(table);
		initialize();
		
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				//Get selected tables 
				
				int i = 0;
				for(Map.Entry<String,String> entry:pk.entrySet()){
					pkVal.put(entry.getKey(), textFields[i].getText());
					i++;
				}
				
				switch(choice) {
				 	case "first":
				 		@SuppressWarnings("unused") 
				 		ResultFrame f = new ResultFrame(DatabaseConnection.getInstance().getFirst(pkVal, selTable, selCol));
				 		break;
				 	case "last":
				 		@SuppressWarnings("unused") 
				 		ResultFrame l = new ResultFrame(DatabaseConnection.getInstance().getLast(pkVal, selTable, selCol));
				 		break;
				 	case "next":
				 		@SuppressWarnings("unused")
				 		EnterColValFrame ecn = new EnterColValFrame(pkVal,selCol,selColType,choice);
				 		break;
				 	case "previous":
				 		@SuppressWarnings("unused") 
				 		EnterColValFrame ecp = new EnterColValFrame(pkVal,selCol,selColType,choice);
				 		break;
				 	case "previous_scale":
				 		@SuppressWarnings("unused") 
				 		ResultFrame p = new ResultFrame(DatabaseConnection.getInstance().getLast(pkVal, selTable, selCol));
				 		break;
				 	case "next_scale":
				 		@SuppressWarnings("unused")
				 		ResultFrame n = new ResultFrame(DatabaseConnection.getInstance().getLast(pkVal, selTable, selCol));
				 		break;
				 	case "evolution_history":
				 		@SuppressWarnings("unused") 
				 		ResultFrame eh = new ResultFrame(DatabaseConnection.getInstance().getLast(pkVal, selTable, selCol));
				 		break;
				 	case "evolution":
				 		@SuppressWarnings("unused") 
				 		EnterColValFrame ece = new EnterColValFrame(pkVal,selCol,selColType,choice);
				 		break;
				 	case "first_evolution":
				 		@SuppressWarnings("unused") 
				 		ResultFrame fe = new ResultFrame(DatabaseConnection.getInstance().getLast(pkVal, selTable, selCol));
				 		break;
				 	case "last_evolution":
				 		@SuppressWarnings("unused") 
				 		ResultFrame le = new ResultFrame(DatabaseConnection.getInstance().getLast(pkVal, selTable, selCol));
				 		break;
				 	case "evolution_V1-V2":
				 		@SuppressWarnings("unused")
				 		EnterTwoValColFrame ec = new EnterTwoValColFrame(pkVal,selCol,selColType,choice);
				 		break;
				 	case "timestamps":
				 		@SuppressWarnings("unused") 
				 		EnterColValFrame ect = new EnterColValFrame(pkVal,selCol,selColType,choice);
				 		break;
				 	case "this_value":
				 		@SuppressWarnings("unused") 
				 		EnterDateFrame tv = new EnterDateFrame(pkVal,selCol);
				 		break;
				 	case "this_timestampname":
				 		//@SuppressWarnings("unused") 
				 		break;
				 	default:
				 		break;
				}
			
			}	
		});	
		
		btnBackToMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TemporalMenuFrame m = new TemporalMenuFrame();
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
		
		JLabel lblEnterThePk = new JLabel("Enter the PK value(s) to perform the operation '"+choice+"'on '"+selTable+"':");
		
		btnBackToMenu = new JButton("Back to menu");
		btnNext = new JButton("Next");
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblEnterThePk)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(76)
					.addComponent(btnBackToMenu)
					.addPreferredGap(ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
					.addComponent(btnNext, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE)
					.addGap(50))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblEnterThePk)
					.addPreferredGap(ComponentPlacement.RELATED, 214, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnBackToMenu)
						.addComponent(btnNext))
					.addGap(25))
		);
		frame.getContentPane().setLayout(groupLayout);
		
		int i=0, x=50, y=50, width=500, height=60; //choose whatever you want
		textFields = new JTextField[pk.size()];
		for(Map.Entry<String,String> entry:pk.entrySet()) {
			
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
