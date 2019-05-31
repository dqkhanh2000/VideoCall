package Client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import javax.swing.ImageIcon;

import java.util.Vector;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.LineBorder;

public class WaitRoom extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vector<User> listUser;
	private JPanel contentPane;
	private JPanel anotherUserPanel, chatBox;
	public JScrollPane scrollPane, scrollPane_1;
	public JLabel lblYourName, lblChatWith,lblYourAvatar;
	public JTextField txtMessage;
	public User curentUser;
	public int lastLocation = 0;
	public String YourName = "", targetName = "", action="";
	public JLabel lblNewmess;
	public Data dataSocket;
	
	public WaitRoom() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		listUser = new Vector<>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 276, 443);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);;
		
		lblYourAvatar = new JLabel();
		lblYourAvatar.setBounds(34, 11, 64, 64);
		contentPane.add(lblYourAvatar);
		
		lblYourName = new JLabel("");
		lblYourName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblYourName.setHorizontalAlignment(SwingConstants.CENTER);
		lblYourName.setBounds(108, 40, 150, 15);
		contentPane.add(lblYourName);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(8, 99, 250, 305);
		contentPane.add(scrollPane);
		
		anotherUserPanel = new JPanel();
		anotherUserPanel.setLayout(null);
		anotherUserPanel.setPreferredSize(new Dimension(230, 600));
		scrollPane.setViewportView(anotherUserPanel);
	}
	
	public void createChatBox() {
		this.setSize(773, 443);
		JPanel panelChat = new JPanel();
		panelChat.setBounds(270, 11, 481, 393);
		contentPane.add(panelChat);
		panelChat.setLayout(null);
		
		txtMessage = new JTextField();
		txtMessage.setBounds(10, 345, 368, 37);
		panelChat.add(txtMessage);
		txtMessage.setColumns(10);
		
		JButton btnSend = new JButton(new ImageIcon(WaitRoom.class.getResource("/img/send.png")));
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!txtMessage.getText().trim().isEmpty()) {
					String data = "send chat|"+YourName+"|"+targetName+"*%%"+txtMessage.getText();
					dataSocket.sendData(data);
					txtMessage.setText("");
				}
			}
		});
		btnSend.setBounds(439, 345, 42, 37);
		panelChat.add(btnSend);
		
		JButton button = new JButton(new ImageIcon(WaitRoom.class.getResource("/img/video-call.png")));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
					if(!targetName.equals("") && !dataSocket.hasCall) {
						if(curentUser.isOnline()) {
							dataSocket.sendData("Call_"+targetName);
							dataSocket.setcallDialog(new DiaglogCall(dataSocket), targetName);
						}
						else JOptionPane.showMessageDialog(null, targetName+" not online");
					}
			}
		});
		button.setBounds(388, 345, 41, 37);
		panelChat.add(button);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 24, 465, 310);
		panelChat.add(scrollPane_1);
		
		lblChatWith = new JLabel();
		lblChatWith.setBounds(10, 7, 187, 14);
		panelChat.add(lblChatWith);		
		
		lblNewmess = new JLabel("");
		lblNewmess.setForeground(new Color(255, 0, 0));
		lblNewmess.setBounds(270, 7, 205, 14);
		panelChat.add(lblNewmess);		
	}
	
	public  void setListUser(Vector<String[]> data) {
		int x = 0,y =0;
		if(lblYourName.getText().equals("")) {
			for (String[] row : data) {
				if(row[0].endsWith("(you)")) {
					YourName = row[0].substring(0,  row[0].indexOf('(')).trim();
					lblYourName.setText(row[0]);
					lblYourAvatar.setIcon(new ImageIcon(WaitRoom.class.getResource(row[2]+".png")));
					continue;
				}
				JPanel UserPanel = new JPanel();
				UserPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
				if(x==0) y=0;
				else y= x * 82 + 10;
				UserPanel.setBounds(0, y, 230, 82);
				anotherUserPanel.add(UserPanel);
				UserPanel.setLayout(null);
				
				JLabel lblUsericon = new JLabel();
				lblUsericon.setBounds(10, 11, 64, 64);
				UserPanel.add(lblUsericon);
				lblUsericon.setToolTipText(row[2]);
				lblUsericon.setIcon(new ImageIcon(WaitRoom.class.getResource(row[2]+".png")));
				
				JLabel lblUserName = new JLabel();
				lblUserName.setFont(new Font("Tahoma", Font.BOLD, 11));
				lblUserName.setBounds(89, 18, 131, 14);
				UserPanel.add(lblUserName);
				lblUserName.setText(row[0]);
				
				JLabel lblStatus = new JLabel();
				lblStatus.setBounds(111, 54, 16, 16);
				UserPanel.add(lblStatus);
				boolean ol = false;
				if(row[1].equals("1")) {
					ol = true;
					lblStatus.setIcon(new ImageIcon(WaitRoom.class.getResource("/img/online.png")));
				}
				else lblStatus.setIcon(new ImageIcon(WaitRoom.class.getResource("/img/offline.png")));
				
				JButton btnMess = new JButton(new ImageIcon(WaitRoom.class.getResource("/img/messenger.png")));
				btnMess.setBounds(171, 43, 32, 32);
				btnMess.setToolTipText("Open chat");
				UserPanel.add(btnMess);
				x++;
				User user = new User(lblUsericon, lblUserName, lblStatus, btnMess, this);
				user.setOnline(ol);
				listUser.add(user);
			}
			anotherUserPanel.revalidate();
			anotherUserPanel.repaint();
		}
		else {
			for (String[] strings : data) {
				for (User user : listUser) {
					if(strings[0].equals(user.getLblUserName().getText()) || strings[0].equals(user.getLblUserName().getText()+" (you)")) {
						if(strings[1].equals("1")) user.getLblStatus().setIcon(new ImageIcon(WaitRoom.class.getResource("/img/online.png")));
						else user.getLblStatus().setIcon(new ImageIcon(WaitRoom.class.getResource("/img/offline.png")));
					}
				}
			}
		}
	}

	public void setDataChat(Vector<String[]> data, String user1, String user2) {
		String name = lblNewmess.getText().substring(lblNewmess.getText().lastIndexOf(" ")+1, lblNewmess.getText().length());
		if(user1.equals(YourName) && curentUser.getLblUserName().getText().equals(user2)) {
			loadchat(curentUser, data);	
			if(name.equals(user2)) lblNewmess.setText("");
		}
		else if(user2.equals(YourName) && curentUser.getLblUserName().getText().equals(user1)) {
			loadchat(curentUser, data);	
			lblNewmess.setText("");
		}
		else if(user2.equals(YourName) && curentUser.getLblUserName().getText()!=user1) {
			lblNewmess.setText("You have new message from "+user1);
			new PlaySound().play(WaitRoom.class.getResource("/sound/mess.wav"));
		}
	}
	
	public void setDataSocket(Data data) {
		this.dataSocket = data;
	}

	public void loadchat(User user, Vector<String[]> data) {
		
		if(targetName.equals(user.getLblUserName().getText())) {
			lastLocation = 0;
			JPanel tagetChat = null;
			chatBox = new JPanel();
			chatBox.setLayout(null);
			scrollPane_1.setViewportView(null);
			for (String[] strings : data) {
				if(lastLocation==0) lastLocation=10;
				else lastLocation+=60;
				if(strings[0].equals(user.getLblUserName().getText())) {
					tagetChat = new JPanel();
					
					tagetChat.setBounds(10, lastLocation, 426, 40);
					chatBox.add(tagetChat);
					tagetChat.setLayout(null);
					JLabel lblIcon = new JLabel(new ImageIcon(WaitRoom.class.getResource(user.getLblUsericon().getToolTipText()+"-32.png")));
					lblIcon.setBounds(7, 7, 32, 32);
					tagetChat.add(lblIcon);
					
					JLabel lblText = new JLabel(strings[2]);
					lblText.setBorder(new LineBorder(new Color(0, 255, 204), 1, true));
					lblText.setBounds(47, 7, 370, 33);
					tagetChat.add(lblText);
					lblText.setToolTipText(strings[3]);
				}
				else {
					tagetChat = new JPanel();
					JLabel lblYourchat = new JLabel(strings[2]);
					lblYourchat.setBorder(new LineBorder(new Color(51, 153, 255), 1, true));
					lblYourchat.setBackground(new Color(0, 255, 204));
					lblYourchat.setHorizontalAlignment(SwingConstants.RIGHT);
				
					lblYourchat.setBounds(77, lastLocation, 360, 35);
					chatBox.add(lblYourchat);
					lblYourchat.setToolTipText(strings[3]);
				}
				
			}
			chatBox.setPreferredSize(new Dimension(440, lastLocation+100));
			scrollPane_1.setViewportView(chatBox);
			scrollPane_1.getVerticalScrollBar().setValue(scrollPane_1.getVerticalScrollBar().getMaximum());
			chatBox.revalidate();
			chatBox.repaint();
			
		}
		scrollPane_1.getVerticalScrollBar().setValue(scrollPane_1.getVerticalScrollBar().getMaximum());
		revalidate();
		repaint();
		
	}
	
}
class User implements ActionListener{
	private JLabel lblUsericon, lblUserName, lblStatus;
	private JButton btnMess;
	private WaitRoom waitroom;
	private boolean online = false;
	public User(JLabel lblUsericon,JLabel lblUserName, JLabel lblStatus, JButton btnMess, WaitRoom waitroom) {
		this.lblUsericon = lblUsericon;
		this.lblUserName = lblUserName;
		this.lblStatus = lblStatus;
		this.btnMess = btnMess;
		this.btnMess.addActionListener(this);
		this.waitroom = waitroom;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public JLabel getLblUsericon() {
		return lblUsericon;
	}

	public JLabel getLblUserName() {
		return lblUserName;
	}

	public JLabel getLblStatus() {
		return lblStatus;
	}

	public JButton getBtnMess() {
		return btnMess;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(waitroom.txtMessage == null) waitroom.createChatBox();
		waitroom.lastLocation = 0;
		waitroom.curentUser = this;
		waitroom.action="change user";
		waitroom.targetName = this.getLblUserName().getText();
		waitroom.lblChatWith.setText("Chat with: "+this.getLblUserName().getText());
		waitroom.dataSocket.sendData("load chat|"+waitroom.YourName+"|"+this.lblUserName.getText());
	}
	
}