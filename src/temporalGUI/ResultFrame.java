package temporalGUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.ResultSetMetaData;

public class ResultFrame {

	private JFrame frame;
	java.sql.ResultSet result=null;
	JTable tb;
	private JButton btnDone;

	/**
	 * Create the application.
	 */
	public ResultFrame(java.sql.ResultSet r) {
		result = r;
		initialize();
		
		btnDone.addActionListener(new ActionListener() {
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
		frame.setBounds(100, 100, 1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tb = new JTable();
		DefaultTableModel dtm = new DefaultTableModel(0,0);
		ResultSetMetaData col = null;
		String header[];
		try {
			col = (ResultSetMetaData) result.getMetaData();
			header = new String[col.getColumnCount()];
			for(int i=0;i<col.getColumnCount();i++) {
				header[i] = col.getColumnName(i);
			}
			dtm.setColumnIdentifiers(header);
			tb.setModel(dtm);
			
			while(result.next()) {
				Object[] data = new Object[header.length];
				for(int j=0;j<header.length;j++) {
					data[j] = result.getString(header[j]);
				}
				dtm.addRow(data);
			}
		} 
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 		
		frame.getContentPane().add(new JScrollPane(tb));
		
		btnDone = new JButton("Done");
		frame.getContentPane().add(btnDone, BorderLayout.SOUTH);
	}

}
