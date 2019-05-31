package Client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DiaglogRecive extends JFrame {

	private static final long serialVersionUID = 1L;
	public JPanel contentPane;
	public JButton btnAccept;
	public JLabel lblStatus;
	public PlaySound playSound;

	/**
	 * Create the frame.
	 */
	public DiaglogRecive(Data data) {
		playSound = new PlaySound();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 224, 198);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnEndcall = new JButton(new ImageIcon(WaitRoom.class.getResource("/img/call-end.png")));
		btnEndcall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = lblStatus.getText().substring(lblStatus.getText().lastIndexOf(" ")+1, lblStatus.getText().length());
				data.sendData("call rejected "+name);
				dispose();
				playSound.pause();
			}
		});
		btnEndcall.setBounds(55, 112, 39, 36);
		contentPane.add(btnEndcall);
		
		btnAccept = new JButton(new ImageIcon(DiaglogRecive.class.getResource("/img/phone-call.png")));
		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = lblStatus.getText().substring(lblStatus.getText().lastIndexOf(" ")+1, lblStatus.getText().length());
				data.sendData("call accepted "+name);
				dispose();
				playSound.pause();
				
			}
		});
		btnAccept.setBounds(109, 112, 39, 36);
		contentPane.add(btnAccept);
		
		lblStatus = new JLabel("status");
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setBounds(10, 32, 188, 36);
		contentPane.add(lblStatus);
		
		setVisible(true);
	}

}
