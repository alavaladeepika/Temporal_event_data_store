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

	/**
	 * Create the application.
	 */
	public MenuFrame() {
		initialize();
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
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(162)
							.addComponent(btnTemporalOperators))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(207)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnDelete)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addComponent(btnUpdate)
									.addComponent(btnSelect)
									.addComponent(btnInsert)))))
					.addContainerGap(643, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(101)
					.addComponent(btnInsert)
					.addGap(27)
					.addComponent(btnSelect)
					.addGap(30)
					.addComponent(btnUpdate)
					.addGap(30)
					.addComponent(btnDelete)
					.addGap(29)
					.addComponent(btnTemporalOperators)
					.addContainerGap(658, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
		frame.setVisible(true);
	}
}
