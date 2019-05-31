package Client;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

import com.github.sarxos.webcam.Webcam;

import java.awt.Color;

public class VideoChatFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private ServerSocket serverAudio, SeverVideo;
	private Socket socketAudio, socketVideo;
	private Webcam webcam;
	private JLabel lblMyCam, lblServerCam;
	private AudioFormat format;
	String name, ip;
	Thread threadReciveVideo, threadReciveAudio;
	Thread threadSendVideo, threadSendAudio;
	
	
	public VideoChatFrame(Data data) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			// TODO: handle exception
		}
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 521, 302);
		getContentPane().setLayout(null);
		
		lblServerCam = new JLabel("");
		lblServerCam.setBounds(10, 11, 320, 240);
		getContentPane().add(lblServerCam);
		
		lblMyCam = new JLabel("");
		lblMyCam.setBounds(340, 11, 160, 120);
		getContentPane().add(lblMyCam);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setForeground(new Color(230, 230, 250));
		btnStop.setBackground(new Color(255, 0, 0));
		btnStop.setBounds(381, 179, 91, 38);
		getContentPane().add(btnStop);
		
		
		btnStop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				data.sendData("end call");
				endCall();
			}
		});
		
		
		
		try {
			webcam = Webcam.getDefault();
			if(webcam != null ) webcam.setViewSize(new Dimension(320, 240));
			format = new AudioFormat(8000.0f,8,1,true,false);
		} catch (Exception e) {}
		
		setVisible(true);
	}
	
	public Image resize(Image img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
	
	public void reciveCall(String ip) {
		try {
			socketVideo = new Socket(ip, 5568);
			threadSendVideo = sendVideo(socketVideo.getOutputStream());
			threadReciveVideo = reciveVideo(socketVideo.getInputStream());
			threadReciveVideo.start();
			threadSendVideo.start();
			
			socketAudio = new Socket(ip, 5569);
			threadSendAudio = sendAudio(new DataOutputStream(socketAudio.getOutputStream()));
			threadReciveAudio = reciveAudio(new DataInputStream(socketAudio.getInputStream()));
			threadReciveAudio.start();
			threadSendAudio.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void endCall() {
		try {
			threadReciveAudio.interrupt();
			threadSendAudio.interrupt();
			threadReciveVideo.interrupt();
			threadSendVideo.interrupt();
	
			serverAudio.close();
			SeverVideo.close();
		} catch (Exception e) {}
		
		try {
			socketVideo.close();
			socketAudio.close();
		} catch (Exception e) {}
		
		try {
			webcam.close();
		} catch (Exception e) {}
		
		this.dispose();
	}
	
	public void startCall() {
		try {
			SeverVideo = new ServerSocket(5568);
			socketVideo = SeverVideo.accept();
			threadSendVideo = sendVideo(socketVideo.getOutputStream());
			threadReciveVideo = reciveVideo(socketVideo.getInputStream());
			threadReciveVideo.start();
			threadSendVideo.start();
			
			serverAudio = new ServerSocket(5569);
			socketAudio = serverAudio.accept();
			threadSendAudio = sendAudio(new DataOutputStream(socketAudio.getOutputStream()));
			threadReciveAudio = reciveAudio(new DataInputStream(socketAudio.getInputStream()));
			threadReciveAudio.start();
			threadSendAudio.start();
		} catch (Exception e) {}
	}
	
	public Thread sendVideo(OutputStream os) {
		return new Thread(() ->{
			if(!webcam.isOpen()) webcam.open();
			ObjectOutputStream oos =null;
			try {
				oos = new ObjectOutputStream(os);
			} catch (IOException e1) {e1.printStackTrace();}
			while(true) {
				try {
				Image img = webcam.getImage();
				ImageIcon imgIcon = new ImageIcon(img);
				oos.writeObject(imgIcon);
//				lblMyCam.setIcon(imgIcon);
				lblMyCam.setIcon(new ImageIcon(img.getScaledInstance(lblServerCam.getWidth(), lblMyCam.getHeight(), Image.SCALE_DEFAULT)));
				Thread.sleep(50);
				} catch (Exception e) {}
			}
		});
	}

	public Thread reciveVideo(InputStream is) {
		return new Thread(() ->{
			ObjectInputStream ois =null;
			try {
				ois = new ObjectInputStream(is);
			} catch (IOException e1) {}
			try {
				while(true) {
					lblServerCam.setIcon((ImageIcon) ois.readObject());
				}
			} catch (Exception e) {}
		});
	}
	
	public Thread sendAudio(DataOutputStream dos) {
		return new Thread(() -> {
			try {
		        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		        TargetDataLine microphone = (TargetDataLine)AudioSystem.getLine(info);
		        microphone.open(format);
		        microphone.start();
		        int bytesRead = 0;
		        byte[] soundData = new byte[1];
		        while(bytesRead != -1)
		        {
		            bytesRead = microphone.read(soundData, 0, soundData.length);
		            if(bytesRead >= 0)
		            {
		                dos.write(soundData, 0, bytesRead);
		            }
		        }
			} catch (Exception e) {
				
			}
		});
	}
	
	public Thread reciveAudio(DataInputStream dis) {
		return new Thread(() -> {
			SourceDataLine inSpeaker = null;
			try {
			        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			        inSpeaker = (SourceDataLine) AudioSystem.getLine(info);
			        inSpeaker.open(format);
			        int bytesRead = 0;
			        byte[] inSound = new byte[1];
			        inSpeaker.start();
			        while(bytesRead != -1)
			        {
			            try{bytesRead = dis.read(inSound, 0, inSound.length);} catch (Exception e){}
			            if(bytesRead >= 0)
			            {
			                inSpeaker.write(inSound, 0, bytesRead);
			            }
			        }
			} catch (Exception e) {}
		});
	}
	
	
}
