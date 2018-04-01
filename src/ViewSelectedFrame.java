import java.sql.SQLException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewSelectedFrame {

	private JFrame frame;
	java.sql.ResultSet selRows;
	Map<String,String> colNames;
	JTable tb;
	JButton btnDone;

	/**
	 * Create the application.
	 */
	public ViewSelectedFrame(java.sql.ResultSet resultSet) {
		selRows = resultSet;
		colNames = CRUD_TablesFrame.select_col_Names;
		initialize();
		
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				//Get selected tables 
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
		
		tb = new JTable();
		DefaultTableModel dtm = new DefaultTableModel(0,0);
		
		String header[] = new String[colNames.size()];
		int i=0;
		for(Map.Entry<String,String> entry:colNames.entrySet()){
			header[i] = entry.getKey();
			i++;
		}
		
		dtm.setColumnIdentifiers(header);
		tb.setModel(dtm);
		
		try {
			while(selRows.next()) {
				Object[] data = new Object[header.length];
				for(int j=0;j<header.length;j++) {
					data[j] = selRows.getString(header[j]);
				}
				dtm.addRow(data);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		frame.getContentPane().add(new JScrollPane(tb));
		
		btnDone = new JButton("Done");
		frame.getContentPane().add(btnDone, BorderLayout.SOUTH);
	}

}
