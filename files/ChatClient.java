import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChatClient implements Runnable{
	private static Socket socket = null;
	private static DataOutputStream out = null;
	private static DataInputStream in = null;

	private static BufferedReader inputLine = null;
	private static boolean isClosed = false;

	public void run(){
	    String serverReply;
	    try{
	      while((serverReply = in.readUTF()) != null) {
          System.out.println(serverReply);
	        if (serverReply.startsWith("/quit"))	break;
	      }
	      
	      isClosed = true;
	    }catch(IOException e){
	      e.printStackTrace();
	    }
	}

	public static void main(String[] args){
		try{
      socket = new Socket("localhost", 8888);
			inputLine = new BufferedReader(new InputStreamReader(System.in));
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
    }catch(SocketTimeoutException s){
      System.out.println("Socket timed out!");
    }catch(IOException e){
      e.printStackTrace();
      System.out.println("Input/Output Error!");
    }

		try{
    	new Thread(new ChatClient()).start();
    	while(!isClosed)	out.writeUTF(inputLine.readLine());

      out.close();
      in.close();
      socket.close();
    }catch(IOException e){
      e.printStackTrace();
 	  }
	}
}