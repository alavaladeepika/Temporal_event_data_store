import java.util.*;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuFrame {

	private JFrame frame;
	private JButton btnInsert;
	private JButton btnSelect;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JButton btnTemporalOperators;
	public static Map<String,Map<String,String>> TempTables;
	CRUD_TablesFrame tFrame;

	/**
	 * Create the application.
	 */
	public MenuFrame() {
		initialize();
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				tFrame = new CRUD_TablesFrame("insert"); 
			}
		});
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				tFrame = new CRUD_TablesFrame("select"); 
			}
		});
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				tFrame = new CRUD_TablesFrame("update"); 
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				tFrame = new CRUD_TablesFrame("delete"); 
			}
		});
		btnTemporalOperators.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				//TODO : Add code
			}
		});
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		btnInsert = new JButton("INSERT");
	    btnUpdate = new JButton("UPDATE");
		btnDelete = new JButton("DELETE");
		btnTemporalOperators = new JButton("TEMPORAL OPERATORS");
		btnSelect = new JButton("SELECT");
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(420)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnInsert, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
						.addComponent(btnSelect, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
						.addComponent(btnUpdate, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
						.addComponent(btnTemporalOperators, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
						.addComponent(btnDelete, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE))
					.addGap(337))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(219)
					.addComponent(btnInsert)
					.addGap(84)
					.addComponent(btnSelect)
					.addGap(93)
					.addComponent(btnUpdate)
					.addGap(92)
					.addComponent(btnDelete)
					.addGap(99)
					.addComponent(btnTemporalOperators)
					.addContainerGap(288, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
		frame.setVisible(true);
	}
}
