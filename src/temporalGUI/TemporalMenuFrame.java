package temporalGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JButton;

import initGUI.MenuFrame;

public class TemporalMenuFrame {

	private JFrame frame;
	private JButton btnFirst, btnLast, btnPrevious, btnNext, btnPreviousScale, 
					btnNextScale, btnEvolutionHistory, btnEvolution,
					btnFirstEvolution, btnLastEvolution, btnEvolutionvv, 
					btnTimestamps, btnThisValue, btnThisTimestampname, btnBack;

	/**
	 * Create the application.
	 */
	public TemporalMenuFrame() {
		initialize();
		
		btnFirst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TablesFrame f = new TablesFrame("first");
			}
		});
		
		btnLast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TablesFrame l = new TablesFrame("last");
			}
		});
		
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TablesFrame p = new TablesFrame("previous");
			}
		});
		
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TablesFrame n = new TablesFrame("next");
			}
		});
		
		btnPreviousScale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TablesFrame ps = new TablesFrame("previous_scale");
			}
		});
		
		btnNextScale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TablesFrame ns = new TablesFrame("next_scale");
			}
		});
		
		btnEvolutionHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TablesFrame eh = new TablesFrame("evolution_history");
			}
		});
		
		btnEvolution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TablesFrame ev = new TablesFrame("evolution");
			}
		});
		
		btnFirstEvolution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TablesFrame fe = new TablesFrame("first_evolution");
			}
		});
		
		btnLastEvolution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TablesFrame le = new TablesFrame("last_evolution");
			}
		});
		
		btnEvolutionvv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TablesFrame evv = new TablesFrame("evolution_V1-V2");
			}
		});
		btnTimestamps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TablesFrame t = new TablesFrame("timestamps");
			}
		}); 
		
		btnThisValue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TablesFrame tv = new TablesFrame("this_value");
			}
		}); 
		
		btnThisTimestampname.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame.setVisible(false);
				frame.dispose();
				@SuppressWarnings("unused")
				TablesFrame tt = new TablesFrame("this_timestampname");
			}
		});
		
		btnBack.addActionListener(new ActionListener() {
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
		
		JLabel lblSelectOneOf = new JLabel("Select one of the temporal operations:");
		
		btnFirst = new JButton("FIRST");
		btnLast = new JButton("LAST");
		btnPrevious = new JButton("PREVIOUS");
		btnNext = new JButton("NEXT");
		btnPreviousScale = new JButton("PREVIOUS SCALE");
		btnNextScale = new JButton("NEXT SCALE");
		btnEvolutionHistory = new JButton("EVOLUTION HISTORY");
		btnEvolution = new JButton("EVOLUTION");
		btnFirstEvolution = new JButton("FIRST EVOLUTION");
		btnLastEvolution = new JButton("LAST EVOLUTION");
		btnEvolutionvv = new JButton("EVOLUTION 'V1'-'V2'");
		btnTimestamps = new JButton("TIMESTAMPS");
		btnThisValue = new JButton("THIS VALUE");
		btnThisTimestampname = new JButton("THIS TIMESTAMP_NAME");
		btnBack = new JButton("BACK");

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(156)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnFirst, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
								.addComponent(btnLast, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnPrevious, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnNext, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnPreviousScale, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnNextScale, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnEvolutionHistory, GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
								.addComponent(btnEvolution, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnFirstEvolution, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnLastEvolution, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnEvolutionvv, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnTimestamps, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnThisValue, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
								.addComponent(btnThisTimestampname, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(22)
							.addComponent(lblSelectOneOf)))
					.addContainerGap(626, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(522, Short.MAX_VALUE)
					.addComponent(btnBack, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
					.addGap(314))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(23)
					.addComponent(lblSelectOneOf)
					.addGap(26)
					.addComponent(btnFirst, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnLast)
					.addGap(18)
					.addComponent(btnPrevious)
					.addGap(18)
					.addComponent(btnNext)
					.addGap(18)
					.addComponent(btnPreviousScale)
					.addGap(18)
					.addComponent(btnNextScale)
					.addGap(18)
					.addComponent(btnEvolutionHistory)
					.addGap(18)
					.addComponent(btnEvolution)
					.addGap(18)
					.addComponent(btnFirstEvolution)
					.addGap(18)
					.addComponent(btnLastEvolution)
					.addGap(18)
					.addComponent(btnEvolutionvv)
					.addGap(18)
					.addComponent(btnTimestamps)
					.addGap(18)
					.addComponent(btnThisValue)
					.addGap(18)
					.addComponent(btnThisTimestampname)
					.addGap(189)
					.addComponent(btnBack)
					.addContainerGap(138, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
