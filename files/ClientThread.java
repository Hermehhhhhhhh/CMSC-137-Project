import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ClientThread extends Thread {
	private final ArrayList<ClientThread> clients;
	private Socket socket;
	private String clientName = null;
  private DataInputStream in = null;
  private DataOutputStream out = null;

	public ClientThread(Socket socket, ArrayList<ClientThread> clients){
		this.socket = socket;
		this.clients = clients;
	}

	public void run(){
		ArrayList<ClientThread> clients = this.clients;
    
		try{
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());

			out.writeUTF("Please enter your name: ");
	        clientName = in.readUTF().trim();
	        out.writeUTF("Welcome "+clientName+". To leave enter /quit in a new line.");
		}catch(IOException e){
			e.printStackTrace();
		}

    while(true){
      try{
        String line = in.readUTF();
        if (line.startsWith("/quit")) break;
        System.out.println(line);

        for (int i = 0; i < clients.size(); i++) {
          clients.get(i).out.writeUTF("<" + clientName + "> " + line);
        }
        
      }catch(IOException e){
        e.printStackTrace();
      }
    }

    try{
  		clients.clear();    
      in.close();
      out.close();
      socket.close();
    }catch(IOException e){
      e.printStackTrace();
    }
	}
}