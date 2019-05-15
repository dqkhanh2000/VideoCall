package Client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import com.github.sarxos.webcam.Webcam;

import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.JList;
import javax.swing.JComboBox;

public class ClientGUI extends JFrame {

	private JPanel contentPane;
	private Socket socketVideo, socketAudio, socketControl;
	private Webcam webcam;
	private JLabel lblMyCam, lblServerCam;
	private AudioFormat format;
	private DataOutputStream dos;
	JComboBox comboBox;
	String name;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI frame = new ClientGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientGUI() {
		name = JOptionPane.showInputDialog(null, "Your name?");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 546, 302);
		getContentPane().setLayout(null);
		
		lblServerCam = new JLabel("");
		lblServerCam.setBounds(10, 11, 320, 240);
		getContentPane().add(lblServerCam);
		
		lblMyCam = new JLabel("");
		lblMyCam.setBounds(340, 11, 176, 144);
		getContentPane().add(lblMyCam);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setForeground(new Color(230, 230, 250));
		btnStop.setBackground(new Color(255, 0, 0));
		btnStop.setBounds(349, 227, 68, 23);
		getContentPane().add(btnStop);
		
		JButton btnCall = new JButton("Call");
		btnCall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String target = (String) comboBox.getSelectedItem();
					if(target != null )dos.writeUTF("Call_"+target);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnCall.setBackground(new Color(50, 205, 50));
		btnCall.setBounds(427, 227, 89, 23);
		getContentPane().add(btnCall);
		
		comboBox = new JComboBox();
		comboBox.setBounds(350, 167, 166, 26);
		getContentPane().add(comboBox);
		
		Thread threadReciveAudio = reciveAudio();
		Thread threadSendAudio = sendAudio();
		Thread threadReciveVideo = reciveAudio();
		Thread threadSendVideo = sendVideo();
		
		btnStop.addActionListener(new ActionListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					threadReciveAudio.stop();
					threadSendAudio.stop();
					threadReciveVideo.stop();
					threadSendVideo.stop();
				} catch (Exception e) {}
			}
		});
		
//		webcam = Webcam.getDefault();
//		webcam.setViewSize(new Dimension(320, 240));
//		webcam.open();
		
		//format = new AudioFormat(44100, 16, 2, true, true);
		
		try {
			socketControl = new Socket("127.0.0.1", 5567);
			dos = new DataOutputStream(socketControl.getOutputStream());
			dos.writeUTF(name);
			socketAudio = new Socket("127.0.0.1", 5568);
			socketVideo = new Socket("127.0.0.1", 5569);
			dos.writeUTF("Get list user");
			clientThread(socketControl).start();
		} catch (Exception e) {}
	}
	
	public Thread clientThread(Socket socket) {
		return new Thread(() -> {
			DataInputStream dis;
			DataOutputStream dos;
			ObjectInputStream ois;
			ObjectOutputStream oos;
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
		
				dos.writeUTF("Get list user");
				while(true) {
					String recive = dis.readUTF();
					System.out.println(recive);
					if(recive.equals("Refresh list user")){
						comboBox.removeAllItems();
						Vector<String> listuser = (Vector<String>) new ObjectInputStream(socketControl.getInputStream()).readObject();
						for (String string : listuser) {
							if(string.equals(name)) continue;
							comboBox.addItem(string);
						}
					}
				}
			} catch (Exception e) {e.printStackTrace();}
		});
	}
	
	public BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
	
	public Thread sendVideo() {
		return new Thread(() ->{
			ObjectOutputStream oos = null;
			try {
				oos = new ObjectOutputStream(socketVideo.getOutputStream());
			} catch (IOException e1) {}
			while(true) {
				try {
				ImageIcon imgIcon = new ImageIcon(resize(webcam.getImage(), lblMyCam.getHeight(), lblServerCam.getWidth()));
				oos.writeObject(imgIcon);
				lblMyCam.setIcon(imgIcon);
					Thread.sleep(40);
				} catch (Exception e) {}
			}
		});
	}

	public Thread sendAudio() {
		return new Thread(() -> {
			DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
			TargetDataLine targetdata;
			try {
				targetdata = (TargetDataLine) AudioSystem.getLine(targetInfo);
				targetdata.open(format);
				targetdata.start();
				byte[] data = new byte[4096];
				targetdata.read(data, 0, data.length);
				int i =0;
				DataOutputStream dos = new DataOutputStream(socketAudio.getOutputStream());
				while(true) {
					dos.write(targetdata.read(data, 0, data.length));
				}
			} catch (Exception e) {e.printStackTrace();}
		});
	}
	
	public Thread reciveAudio() {
		return new Thread(() -> {
			DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);
			try {
				SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
				sourceLine.open(format);
				sourceLine.start();
				byte[] targetData = new byte[4096];
				DataInputStream dis = new DataInputStream(socketAudio.getInputStream());
				int i = 0;
				while(true) {
					dis.read(targetData);
					sourceLine.write(targetData, 0, targetData.length);
				}
			}catch(Exception e) {e.printStackTrace();}
		});
	}
	
	public Thread reciveVideo() {
		return new Thread(() ->{
			try {
				ObjectInputStream is = new ObjectInputStream(socketVideo.getInputStream());
				while(true) {
					lblServerCam.setIcon((ImageIcon) is.readObject());
				}
			} catch (Exception e) {}
		});
	}
}
