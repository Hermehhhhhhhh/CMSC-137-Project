import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ChatServer{
	private static ServerSocket serverSocket;
	private static Socket clientSocket = null;
	private static ArrayList<ClientThread> clients = new ArrayList<ClientThread>();

	public static void main(String[] args){
		try{
			serverSocket = new ServerSocket(8888);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		while(true){
			try{
        System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
        clientSocket = serverSocket.accept();

        System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());
        clients.add(new ClientThread(clientSocket,clients));
        clients.get(clients.size()-1).start();
        
      }catch(SocketTimeoutException s){
        System.out.println("Socket timed out!");
        break;
      }catch(IOException e){
        e.printStackTrace();
        System.out.println("Input/Output Error!");
        break;
      }
		}
	}
}