package Client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JLabel lblPassword;
	private JPasswordField passwordField;
	private Data data;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new Login();
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 342, 216);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(10, 48, 70, 14);
		contentPane.add(lblUsername);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(90, 45, 228, 20);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 94, 70, 14);
		contentPane.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(90, 91, 228, 20);
		contentPane.add(passwordField);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String user = txtUsername.getText();
				@SuppressWarnings("deprecation")
				String pass = passwordField.getText();
				String ip = JOptionPane.showInputDialog(null, "Server IP?", "127.0.0.1");
				setDataClass(ip);
				data.sendData("login "+user+" "+pass);
			}
		});
		btnLogin.setBounds(38, 150, 89, 23);
		contentPane.add(btnLogin);
		
		JButton btnSignUp = new JButton("Sign up");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new SignUp();
			}
		});
		btnSignUp.setBounds(205, 150, 89, 23);
		contentPane.add(btnSignUp);
		setVisible(true);
	}
	public void setDataClass(String ip) {
		data = new Data(ip, this);
	}
	public void connectSucccess() {
		WaitRoom frame = new WaitRoom();
		frame.setVisible(true);
		data.setWaitroom(frame);
		frame.setDataSocket(data);
	}
}
