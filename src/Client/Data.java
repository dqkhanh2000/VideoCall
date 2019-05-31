package Client;

import java.awt.Frame;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JOptionPane;

public class Data {
	private Socket socketControl;
	private DataOutputStream dos;
	private WaitRoom waitroom;
	Login login;
	DiaglogCall dialogCall;
	DiaglogRecive diaglogRecive;
	public boolean hasCall = false;
	VideoChatFrame clientGUI;
	
	public WaitRoom getWaitroom() {
		return waitroom;
	}

	public void setWaitroom(WaitRoom waitroom) {
		this.waitroom = waitroom;
	}
	public Data(String ip) {
		try {
			socketControl = new Socket(ip, 5567);
			dos = new DataOutputStream(socketControl.getOutputStream());
			clientThread(socketControl.getInputStream(), socketControl.getOutputStream()).start();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Please check your connection or check IP server!", "Can't connect to sever", 0);
		}
	}
	public Data(String ip, Login login) {
		try {
			socketControl = new Socket(ip, 5567);
			dos = new DataOutputStream(socketControl.getOutputStream());
			clientThread(socketControl.getInputStream(), socketControl.getOutputStream()).start();
			this.login = login;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Please check your connection or check IP server!", "Can't connect to sever", 0);
		}
	}
	
	public void sendData(String data) {
		try {
			dos.writeUTF(data);
		} catch (Exception e) {}
	}
	
	public void setcallDialog(DiaglogCall dialog,String name) {
		this.dialogCall = dialog;
		dialogCall.lblStatus.setText("Calling "+name);
	}
	@SuppressWarnings("unchecked")
	public Thread clientThread(InputStream is, OutputStream os) {
		return new Thread(() -> {
			DataInputStream dis;
			ObjectInputStream ois;
			try {
				dis = new DataInputStream(is);
				ois = new ObjectInputStream(socketControl.getInputStream());			
				while(true) {
					String recive = dis.readUTF();
					
					if(recive.equals("result user false")) JOptionPane.showMessageDialog(null, "Username was used");
					
					else if(recive.startsWith("result signup")) {
						if(recive.equals("result signup true")) JOptionPane.showMessageDialog(null, "Sign up done!");
						else JOptionPane.showMessageDialog(null, "Sign up flase!\nTry again");
					}
					else if(recive.startsWith("result login")) {
						String result = String.copyValueOf(recive.toCharArray(), recive.lastIndexOf(" ")+1, recive.length()- recive.lastIndexOf(" ")-1);
						if(result.equals("true")) {
							for (Frame frame : Login.getFrames()) {
								frame.dispose();
							}
							login.connectSucccess();
							dos.writeUTF("Get list user");
						}
						else {
							JOptionPane.showMessageDialog(null, "Username or password is not match");
						}
					}
					else if(recive.equals("Refresh list user")){
						Vector<String[]> data = (Vector<String[]>) ois.readObject();
						waitroom.setListUser(data);
					}
					else if(recive.startsWith("result load chat")) {
						String user1 = recive.substring(recive.lastIndexOf(" ")+1, recive.indexOf("|"));
						String user2 = recive.substring(recive.lastIndexOf("|")+1, recive.length());
						Vector<String[]> data = (Vector<String[]>) ois.readObject();
						waitroom.setDataChat(data, user1, user2);
					}
					else if(recive.startsWith("Call from ")) {
						diaglogRecive = new DiaglogRecive(waitroom.dataSocket);
						diaglogRecive.lblStatus.setText(recive);
						diaglogRecive.playSound.play(Data.class.getResource("/sound/call.wav"));	
					}
					else if(recive.equals("accept")) {
						dialogCall.dispose();
						clientGUI = new VideoChatFrame(this);
						clientGUI.startCall();
						hasCall =true;
					}
					else if(recive.startsWith("ip")) {
						String ip = recive.substring(3, recive.length());
						clientGUI = new VideoChatFrame(this);
						clientGUI.reciveCall(ip);
						diaglogRecive.playSound.pause();
					}
					else if(recive.equals("end call")) {
						try {
							diaglogRecive.playSound.pause();
							diaglogRecive.lblStatus.setText("Call end");
							Thread.sleep(1000);
							diaglogRecive.dispose();
						} catch (Exception e) {}
						
						try {
							clientGUI.endCall();
						} catch (Exception e) {}
					}
					else if(recive.equals("rejected")) {
						dialogCall.lblStatus.setText("Rejected calls");
						Thread.sleep(1000);
						dialogCall.dispose();
					}
					
				}
			} catch (Exception e) {JOptionPane.showMessageDialog(null, "Please check your connect or try later!", "Disconnect with server", 0);}
		});
	}
}
