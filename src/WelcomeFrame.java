import java.awt.EventQueue;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JPasswordField;

public class WelcomeFrame {

	private JFrame frame;
	public static WelcomeFrame window;
	private JTextField username;
	private JPasswordField password;
	private JTextField schema;
	private JButton btnSubmit;
	private String user;
	private String pwd;
	private String schema_name;
	public static ArrayList<String> transactTables;
	public static boolean success = false;

	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new WelcomeFrame();
					window.frame.setVisible(true);
					window.frame.setTitle("Temporal Event Data Store");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WelcomeFrame() {
		initialize();
		
		//Submit button action
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				user = username.getText();
				pwd = String.valueOf(password.getPassword());
				schema_name = schema.getText();
				
				DatabaseConnection c = DatabaseConnection.getInstance(user,pwd,schema_name);
				if(success) {
					window.frame.setVisible(false);
					WelcomeFrame.transactTables = DatabaseConnection.getInstance(user,pwd,schema_name).getTables();
					TablesFrame t = new TablesFrame(transactTables);
					
				}
				
				else window.frame.setVisible(true);
				
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
		
		JLabel lblNewLabel = new JLabel("Welcome to Temporal Event Data Store");
		
		JLabel lblUsername = new JLabel("Username : ");
		
		username = new JTextField();
		username.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password : ");
		
		password = new JPasswordField();
		password.setColumns(10);
		
		JLabel lblSchemaName = new JLabel("Schema name : ");
		
		schema = new JTextField();
		schema.setColumns(10);
		
		btnSubmit = new JButton("SUBMIT");
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(33)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblUsername)
										.addComponent(lblPassword))
									.addGap(42)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(username, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblSchemaName)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(schema, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(127)
							.addComponent(btnSubmit))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(232)
							.addComponent(lblNewLabel)))
					.addContainerGap(491, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(27)
					.addComponent(lblNewLabel)
					.addGap(28)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUsername)
						.addComponent(username, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassword)
						.addComponent(password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSchemaName)
						.addComponent(schema, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 759, Short.MAX_VALUE)
					.addComponent(btnSubmit)
					.addGap(53))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
	
	public static void showAlertMessage(String s) {
		final JPanel panel = new JPanel();
		
		JOptionPane.showMessageDialog(panel, s,"ERROR", JOptionPane.ERROR_MESSAGE);
		success = false;
	}
}
