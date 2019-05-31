package Server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Server {
	static Vector<Client> listClient;
	private static ServerSocket serverSocket;
	static ObjectInputStream ois;
	private static String URL = "jdbc:mysql://127.0.0.1:3306/chat?useUnicode=true&characterEncoding=UTF-8";
	private static Connection con;
	public static void main(String[] args) {
		try {
			connectDB();
			listClient = new Vector<>();
			serverSocket = new ServerSocket(5567);
			System.out.println("Server is ready");
			while(true) {
				Socket socket = serverSocket.accept();
				String ip = socket.getInetAddress().getHostAddress();
				new serverThread(socket, ip).start();
			}
		} catch (Exception e) {
			System.err.println("A port is used by another program");
		}
	}
	public static void connectDB() {
		try {
			con = DriverManager.getConnection(URL, "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static Vector<String[]> getDataDB(String query) {
		Vector<String[]> data = new Vector<>();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			ResultSetMetaData rsm = rs.getMetaData();
			int countcol = rsm.getColumnCount();
			while(rs.next()) {
				String[] row = new String[countcol];
				for(int i = 0; i < countcol; i++) {
					row[i] = rs.getString(i+1);
				}
				data.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
		
	}
	
	
	public static boolean checkUser(String user) {
		boolean rt = false;
		try {
			PreparedStatement pst = con.prepareStatement("SELECT Username from users where Username = ?");
			pst.setString(1, user);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) rt = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rt;
	}
	public static String checkLogin(String username, String password) {
		String name = null;
		try {
			PreparedStatement pst = con.prepareStatement("SELECT Username from users where Username = ? and Password = ?");
			pst.setString(1, username);
			pst.setString(2, password);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) name = rs.getString(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}
 	public static boolean updateDB(String query) {
		boolean rt = false;
		try {
			Statement st = con.createStatement();
			int rowUpdated = st.executeUpdate(query);
			if(rowUpdated > 0) rt = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rt;
		
	}
 	public static void loadListUser(String name) {
 		Vector<String[]> data = Server.getDataDB("SELECT Username, Status, Image FROM users");
		for (String[] strings : data) {
			if(strings[0].equals(name)) {
				strings[0] = name + " (you)";
				break;
			}
		}
		for (Client clnt : Server.listClient) {
			try {
				clnt.getDos().writeUTF("Refresh list user");
				clnt.getOos().writeObject(data);
			} catch (IOException e) {}
			
		}
 	}
 	public static void refreshListUser() {
 		Vector<String[]> data = Server.getDataDB("SELECT Username, Status FROM users");
		for (Client clnt : Server.listClient) {
			try {
				clnt.getDos().writeUTF("Refresh list user");
				clnt.getOos().writeObject(data);
			} catch (IOException e) {}
			
		}
 	}
	
}

class serverThread extends Thread{
	private Socket socket;
	private String ip;
	DataInputStream dis;
	DataOutputStream dos;
	ObjectOutputStream oos;
	String name = null;
	
	public serverThread (Socket socket, String ip) {
		this.socket = socket;
		this.ip = ip;
	}
	
	@SuppressWarnings("deprecation")
	public void run() {
		{
			Client client = null;
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());	
				oos = new ObjectOutputStream(socket.getOutputStream());	
				String becaller ="";
				while(true) {
					String recive = dis.readUTF();
					
					if(recive.startsWith("Check user")) {
						String user = recive.substring(recive.lastIndexOf(" ")+1, recive.length());
						if( Server.checkUser(user)) dos.writeUTF("result user false");
						
					}
					else if(recive.startsWith("signup")) {
						String user = recive.substring(recive.indexOf(" ")+1, recive.indexOf("|"));
						String pass = recive.substring(recive.indexOf("|")+1, recive.lastIndexOf(" "));
						String img = recive.substring(recive.lastIndexOf(" ")+1, recive.length());
						
						if(Server.updateDB("INSERT INTO `users` (`Username`, `Password`, `Status`, `Image`) VALUES ('"+user+"', '"+pass+"', '0', '"+img+"')"))
							dos.writeUTF("result signup true");
						else
							dos.writeUTF("result signup false");
					}
					else if(recive.startsWith("login")) {
						String user = recive.substring(recive.indexOf(" ")+1, recive.lastIndexOf(" "));
						String pass = recive.substring(recive.lastIndexOf(" ")+1, recive.length());	
						
						if((name = Server.checkLogin(user, pass))!=null) {
							Server.updateDB("UPDATE users SET Status = 1 WHERE Username = '"+user+"'");
							System.out.println(name+" online!");
							client = new Client(ip, name, dis, dos, oos);
							Server.listClient.add(client);
							dos.writeUTF("result login true");;
						}
						else dos.writeUTF("result login false");
					}
					
					else if(recive.equals("Get list user")){
						Server.loadListUser(name);
					}
					
					else if(recive.startsWith("load chat")) {
						String user1 = recive.substring(recive.indexOf("|")+1, recive.lastIndexOf("|"));
						String user2 = recive.substring(recive.lastIndexOf("|")+1, recive.length());	
						loadChat(user1, user2, "load");
					}
					
					else if(recive.startsWith("send chat")) {
						String user1 = recive.substring(recive.indexOf("|")+1, recive.lastIndexOf("|"));
						String user2 = recive.substring(recive.lastIndexOf("|")+1, recive.indexOf("*%%"));
						String text = recive.substring(recive.indexOf("*%%")+3, recive.length());
						String query = "INSERT INTO `message` (`User1`, `User2`, `Content`, `Time`) VALUES ('"+user1+"', '"+user2+"', '"+text+"', CURRENT_TIMESTAMP)";
						Server.updateDB(query);
						loadChat(user1, user2, "send");
					}
					
					else if(recive.startsWith("Call_")) {
						becaller =  String.copyValueOf(recive.toCharArray(), 5, recive.length()-5);
						for (Client clnt : Server.listClient) {
							if(clnt.getName().equals(becaller)) {
								clnt.getDos().writeUTF("Call from "+name);
							}
						}
					}
					
					else if(recive.startsWith("call accepted")) {
						String caller = recive.substring(recive.lastIndexOf(" ")+1, recive.length());
						for (Client clnt : Server.listClient) {
							if(clnt.getName().equals(caller)) {
								clnt.getDos().writeUTF("accept");
								dos.writeUTF("ip:"+clnt.ip);
							}
						}
					}
					
					else if(recive.startsWith("call rejected")) {
						String caller = recive.substring(recive.lastIndexOf(" ")+1, recive.length());
						for (Client clnt : Server.listClient) {
							if(clnt.getName().equals(caller)) {
								clnt.getDos().writeUTF("rejected");
							}
						}
					}
					
					else if(recive.equals("end call")) {
						dos.writeUTF("end call");
						for (Client clnt : Server.listClient) {
							if(clnt.getName().equals(becaller)) {
								clnt.getDos().writeUTF("end call");
							}
						}
					}					
				}
			} catch (IOException e) {
				Server.listClient.remove(client);
				Server.updateDB("Update users set Status = 0 where Username = '"+name+"'");
				Server.refreshListUser();
				System.out.println(name +" exited!");
				this.stop();
			}
		}
	}
	
	public void loadChat(String user1, String user2, String status) {
		try {
			String query = "SELECT * FROM `message` where (User1 = '"+user1+"' and User2 = '"+user2+"') "
					+"or (User1 = '"+user2+"' and User2 = '"+user1+"') Order by Time";
			Vector<String[]> data = Server.getDataDB(query);
			dos.writeUTF("result load chat "+user1+"|"+user2);
			oos.writeObject(data);
			if(status.equals("send")) {
				for (Client clnt : Server.listClient) {
					if(clnt.getName().equals(user2)) {
						clnt.getDos().writeUTF("result load chat "+user1+"|"+user2);
						clnt.getOos().writeObject(data);
					}
				}
			}
		} catch (Exception e) {}
	}
}
class Client{
	public String name, ip;
	private DataInputStream dis;
	private DataOutputStream dos;
	private ObjectOutputStream oos;

	public Client(String ip, String name, DataInputStream dis, DataOutputStream dos, ObjectOutputStream oos) {
		this.ip = ip;
		this.dos = dos;
		this.dis = dis;
		this.oos = oos;
		this.name = name;
	}
	
	public DataInputStream getDis() {
		return dis;
	}
	
	public DataOutputStream getDos() {
		return dos;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public String getName() {
		return name;
	}
	
}