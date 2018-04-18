package temporalGUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import initGUI.MenuFrame;

public class ResultFrame {

	private JFrame frame;
	ArrayList<Map<String,String>> result;
	JTable tb;
	private JButton btnDone;

	/**
	 * Create the application.
	 */
	public ResultFrame(ArrayList<Map<String,String>> r) {
		result = r;
		initialize();
		
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
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
		String header[];

		if(result.size()>0) {
			header = new String[result.get(0).size()];
			int j=0;
			for(Map.Entry<String,String> entry:result.get(0).entrySet()) {
				header[j] = entry.getKey();
				j++;
			}
	
			dtm.setColumnIdentifiers(header);
			tb.setModel(dtm);
				
			for(int i=0;i<result.size();i++) {
				Object[] data = new Object[header.length];
				for(int k=0;k<header.length;k++) {
					data[k] = result.get(i).get(header[k]);
				}
				dtm.addRow(data);
			}
			frame.getContentPane().add(new JScrollPane(tb));
		}
		else {
			JLabel label = new JLabel("No data found!!");
			frame.getContentPane().add(label, BorderLayout.NORTH);
		}
		
		btnDone = new JButton("Done");
		frame.getContentPane().add(btnDone, BorderLayout.SOUTH);
	}

}
