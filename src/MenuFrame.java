import java.awt.EventQueue;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import java.awt.BorderLayout;

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
				tFrame = new CRUD_TablesFrame("insert"); 
			}
		});
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tFrame = new CRUD_TablesFrame("select"); 
			}
		});
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tFrame = new CRUD_TablesFrame("update"); 
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tFrame = new CRUD_TablesFrame("delete"); 
			}
		});
		btnTemporalOperators.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(438, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnDelete)
								.addComponent(btnSelect)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addComponent(btnInsert)
									.addComponent(btnUpdate)))
							.addGap(442))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(btnTemporalOperators)
							.addGap(367))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(199)
					.addComponent(btnInsert)
					.addGap(65)
					.addComponent(btnSelect)
					.addGap(77)
					.addComponent(btnDelete)
					.addPreferredGap(ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
					.addComponent(btnUpdate)
					.addGap(104)
					.addComponent(btnTemporalOperators)
					.addGap(49))
		);
		frame.getContentPane().setLayout(groupLayout);
		frame.setVisible(true);
	}
}
