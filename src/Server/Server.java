package Server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
	static Vector<Client> listClient;
	private static ServerSocket serverSocketControl, serverSocketAudio, serverSocketVideo;
	static DataInputStream dis;
	public static void main(String[] args) {
		try {
			listClient = new Vector<>();
			serverSocketControl = new ServerSocket(5567);
			serverSocketAudio = new ServerSocket(5568);
			serverSocketVideo = new ServerSocket(5569);
			System.out.println("Server is ready");
			while(true) {
				Socket socketControl = serverSocketControl.accept();
				dis = new DataInputStream(socketControl.getInputStream());
				String name = dis.readUTF();
				System.out.println(name+" connected");
				Socket socketAudio = serverSocketAudio.accept();
				Socket socketVideo = serverSocketVideo.accept();
				listClient.add(new Client(socketControl, socketAudio, socketVideo, name));
				serverThread(socketControl.getInputStream(), socketControl.getOutputStream()).start();
			}
		} catch (Exception e) {
			System.err.println("A port is used by another program");
		}
	}
	public static Thread serverThread(InputStream is, OutputStream os) {
		return new Thread(() -> {
			DataInputStream dis;
			DataOutputStream dos;
			ObjectOutputStream oos;
			try {
				dis = new DataInputStream(is);
				dos = new DataOutputStream(os);
				oos = new ObjectOutputStream(os);
				while(true) {
					String recive = dis.readUTF();
					if(recive.equals("Get list user")){
						System.out.println(recive);
						
						//không gửi được tại đây
						dos.writeUTF("Refresh list user");
						
						
						
						Vector<String> data = new Vector<String>();
						for (Client client : listClient) {
							data.add(client.getName());
						}
						for (Client client : listClient) {
							new DataOutputStream(client.getSocketControl().getOutputStream()).writeUTF("Refresh list user");
							new ObjectOutputStream(client.getSocketControl().getOutputStream()).writeObject(data);
						}
					}
				}
			} catch (Exception e) {e.printStackTrace();}
		});
	}
}
class Client{
	private  Socket socketControl, socketAudio, socketVideo;
	public String name;
	public Client(Socket socketControl, Socket socketAudio, Socket socketVideo, String name) {
		this.socketControl = socketControl;
		this.socketAudio = socketAudio;
		this.socketVideo = socketVideo;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public Socket getSocketControl() {
		return socketControl;
	}
	public Socket getSocketAudio() {
		return socketAudio;
	}
	public Socket getSocketVideo() {
		return socketVideo;
	}
	
	
	
}
class Connect {
	private DataInputStream dis;
	private DataOutputStream dos;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public Connect(DataInputStream dis, DataOutputStream dos, ObjectInputStream ois, ObjectOutputStream oos) {
		this.dis = dis;
		this.dos = dos;
		this.ois = ois;
		this.oos = oos;
		video().start();
		audio().start();
	}
	
	public Thread audio() {
		return new Thread(() -> {
			byte[] data = new byte[4096];
			while(true) {
				try {
					dis.read(data);
					dos.write(data);
				} catch (Exception e) {}
			}
		});
	}
	public Thread video() {
		return new Thread(() -> {
			while(true) {
				try {
					oos.writeObject(ois.readObject());
				} catch (Exception e) {}
			}
		});
	}
}
