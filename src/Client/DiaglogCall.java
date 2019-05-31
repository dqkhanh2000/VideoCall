package Client;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class DiaglogCall extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	public JLabel lblStatus;

	/**
	 * Create the frame.
	 */
	public DiaglogCall(Data data) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 224, 198);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnEndcall = new JButton(new ImageIcon(WaitRoom.class.getResource("/img/call-end.png")));
		btnEndcall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				data.sendData("end call");
				dispose();
			}
		});
		btnEndcall.setBounds(85, 112, 39, 36);
		contentPane.add(btnEndcall);
		
		lblStatus = new JLabel("status");
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setBounds(10, 32, 188, 36);
		contentPane.add(lblStatus);
		setVisible(true);
	}

}
