package Client;

//import java.awt.BorderLayout;
//import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class SignUp extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField pwdPass;
	private JPasswordField pwdCfpass;
	String imgpath;
	Data data;

	/**
	 * Create the frame.
	 */
	public SignUp() {
		String ip = JOptionPane.showInputDialog(null, "Server IP?", "127.0.0.1");
		data = new Data(ip);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 348, 273);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(10, 11, 86, 14);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 36, 86, 14);
		contentPane.add(lblPassword);
		
		JLabel lblConfirmPassword = new JLabel("Confirm password");
		lblConfirmPassword.setBounds(10, 61, 106, 14);
		contentPane.add(lblConfirmPassword);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(126, 11, 196, 20);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		txtUsername.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				data.sendData("Check user "+txtUsername.getText());
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {}
		});
		
		
		pwdPass = new JPasswordField();
		pwdPass.setBounds(126, 36, 196, 20);
		contentPane.add(pwdPass);
		
		pwdCfpass = new JPasswordField();
		pwdCfpass.setBounds(126, 61, 196, 20);
		contentPane.add(pwdCfpass);
		
		JLabel lblAvatar = new JLabel("Avatar");
		lblAvatar.setBounds(47, 98, 86, 14);
		contentPane.add(lblAvatar);
		
		JLabel lblImage = new JLabel("");
		lblImage.setBounds(155, 98, 64, 64);
		contentPane.add(lblImage);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		JButton btnChoose = new JButton("Choose");
		btnChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new File("src/img"));
					chooser.setDialogTitle("Choose avatar");
					chooser.setFileFilter(new FileNameExtensionFilter("Image file", "png"));
					chooser.showOpenDialog(null);
					File file = chooser.getSelectedFile();
					lblImage.setIcon(new ImageIcon(file.getAbsolutePath()));
					String name = file.getName();
					imgpath = "/img/"+name.substring(0, name.lastIndexOf("."));
				} catch (Exception e) {}
			}
			
		});
		btnChoose.setBounds(47, 126, 81, 14);
		contentPane.add(btnChoose);
		
		JButton btnSignUp = new JButton("Sign up");
		btnSignUp.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				try {
					String user = txtUsername.getText();
					String pass = pwdPass.getText();
					String cfpass = pwdCfpass.getText();
					if(user.isEmpty() || pass.isEmpty() || cfpass.isEmpty()) JOptionPane.showMessageDialog(null, "All field must not empty");
					else
						if(!pass.equals(cfpass)) JOptionPane.showMessageDialog(null, "Password is not match");
						else {
							data.sendData("signup "+user+"|"+pass+" "+imgpath);
						}
				} catch (Exception e) {}
			}
		});
		btnSignUp.setBounds(94, 173, 125, 35);
		contentPane.add(btnSignUp);
		setVisible(true);
	}
}
