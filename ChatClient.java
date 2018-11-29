package client;

import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import proto.PlayerProtos.Player;
import proto.TcpPacketProtos.TcpPacket.*;
import proto.TcpPacketProtos.TcpPacket;
import client.GameGUI;

public class ChatClient implements Runnable{
	private static Socket server = null;
	private static DataOutputStream out = null;
	private static DataInputStream in = null;
	private static BufferedReader inputLine = null;
	private static boolean isClosed = false;
	private static String inGameName;
	private static GameGUI gameUI;

  public ChatClient(Socket server, String inGameName, GameGUI gameUI){
    this.server = server;
		this.gameUI = gameUI;
		this.inGameName =inGameName;
  }

	public void run(){
		try{
			while(true){
				InputStream inFromServer = server.getInputStream();
				while(inFromServer.available()==0){}

				byte []response = new byte[inFromServer.available()];
				inFromServer.read(response);

				this.gameUI.receiveMessages(response);
			}
	 }catch(SocketTimeoutException s){
		 System.out.println("Socket timed out!");
	 }catch(IOException e){
		 e.printStackTrace();
		 System.out.println("Input/Output Error!");
	 }
	}
}
