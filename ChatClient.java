package client;

import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import proto.PlayerProtos.Player;
import proto.TcpPacketProtos.TcpPacket.*;
import proto.TcpPacketProtos.TcpPacket;

public class ChatClient implements Runnable{
	private static Socket server = null;
	private static DataOutputStream out = null;
	private static DataInputStream in = null;
	private static BufferedReader inputLine = null;
	private static boolean isClosed = false;
	private static String inGameName;


  public ChatClient(Socket server, String inGameName){
    this.server = server;
		this.inGameName =inGameName;
  }

	public void run(){
		try{
			while(true){
				InputStream inFromServer = server.getInputStream();
				while(inFromServer.available()==0){}

				byte []response = new byte[inFromServer.available()];
				inFromServer.read(response);

				TcpPacket reply = TcpPacket.parseFrom(response);
				if(reply.getType() == PacketType.CONNECT){
					ConnectPacket received = ConnectPacket.parseFrom(response);
					System.out.println(received.getPlayer().getName() + " joined the lobby.");
				}else if(reply.getType() == PacketType.CHAT){
					ChatPacket received = ChatPacket.parseFrom(response);
					System.out.println(received.getPlayer().getName()+ ": "+ received.getMessage());
				}else if(reply.getType() == PacketType.DISCONNECT){
					DisconnectPacket received = DisconnectPacket.parseFrom(response);
					System.out.println(received.getPlayer().getName() + " left the lobby.");
				}else{
					System.out.println("ERROR");
				}
			}
	 }catch(SocketTimeoutException s){
		 System.out.println("Socket timed out!");
	 }catch(IOException e){
		 e.printStackTrace();
		 System.out.println("Input/Output Error!");
	 }
	}
}
