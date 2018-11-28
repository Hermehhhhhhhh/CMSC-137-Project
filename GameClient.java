import proto.TcpPacketProtos.TcpPacket.*;
import proto.TcpPacketProtos.TcpPacket;
import proto.PlayerProtos.Player;
import java.util.Scanner;
import java.net.*;
import java.io.*;
import client.ChatClient;
import client.GameGUI;

public class GameClient{
  private static Scanner sc = new Scanner(System.in);
  private static String inGameName;
  private static String password;
  private static Player player;
  private static Socket server;
  private static final String server_ip = "202.92.144.45";
  private static final int server_port = 80;
  private static DataOutputStream out = null;
  private static DataInputStream in = null;
  private static BufferedReader inputLine = null;
  private static boolean isClosed = false;
  private static boolean inLobby = false;
  private static String lobbyId;
  public static int option;


  public static void main(String args[]){

    GameGUI gameUI = new GameGUI();


    // // connect to server
    // connectToServer();
    //
    // // Create a GameClient
    // createPlayer();
    //
    // while(isClosed == false){
    //   if(inLobby == false){
    //     getOption();
    //     if(option == 1){
    //       createLobby();
    //       connectToLobby();
    //       if(inLobby == true){
    //         startChat();
    //       }
    //     }else if(option == 2){
    //       System.out.print("Lobby ID: ");
    //       lobbyId = sc.nextLine();
    //       connectToLobby();
    //       if(inLobby == true){
    //         startChat();
    //       }
    //     }else{
    //       disconnectToServer();
    //       break;
    //     }
    //   }
    // }
  }

  public static void startChat(){
    new Thread(new ChatClient(server, inGameName)).start();
      while(inLobby == true){
        try{
         inputLine = new BufferedReader(new InputStreamReader(System.in));
    		 ChatPacket send = ChatPacket.newBuilder().setType(PacketType.CHAT).setMessage(inputLine.readLine()).build();
    		 OutputStream outToServer = server.getOutputStream();
    		 outToServer.write(send.toByteArray());
         if(send.getMessage().trim().equals("exit")){
           leaveLobby();
         }
    	 }catch(SocketTimeoutException s){
    		 System.out.println("Socket timed out!");
    	 }catch(IOException e){
    		 e.printStackTrace();
    		 System.out.println("Input/Output Error!");
    	 }
      }
  }

  public static void getOption(){
    System.out.println("[1] Create Lobby");
    System.out.println("[2] Join Lobby");
    System.out.println("[3] Exit");
    System.out.print("> ");
    option = sc.nextInt();
    sc.nextLine();
  }

  public static void createPlayer(){
    // Get Username and Password
    System.out.print("In-Game-Name: ");
    inGameName = sc.nextLine();

    System.out.print("Password: ");
    password = sc.nextLine();

    player = Player.newBuilder().setName(inGameName).build();
  }

  public static void createLobby(){
  		try{
  			CreateLobbyPacket request = CreateLobbyPacket.newBuilder().setMaxPlayers(5).setType(PacketType.CREATE_LOBBY).build();

  			OutputStream outToServer = server.getOutputStream();
  			outToServer.write(request.toByteArray());

  			InputStream inFromServer = server.getInputStream();
  			while(inFromServer.available()==0){}

  			byte []response = new byte[inFromServer.available()];
    		inFromServer.read(response);
  			CreateLobbyPacket packet = CreateLobbyPacket.parseFrom(response);
        lobbyId = packet.getLobbyId();
  			System.out.println("Lobby ID = " + packet.getLobbyId());

  		}catch(SocketTimeoutException s){
  			System.out.println("Socket timed out!");
  		}catch(IOException e){
  			e.printStackTrace();
  			System.out.println("Input/Output Error!");
  		}

      inLobby = true;
	}

  public static void connectToLobby(){
    try{
      ConnectPacket join = ConnectPacket.newBuilder().setType(PacketType.CONNECT).setPlayer(player).setLobbyId(lobbyId).build();

      OutputStream outToServer = server.getOutputStream();
      outToServer.write(join.toByteArray());

      InputStream inFromServer = server.getInputStream();
      while(inFromServer.available()==0){}

      byte []response = new byte[inFromServer.available()];
      inFromServer.read(response);

      TcpPacket connectRequest = TcpPacket.parseFrom(response);
      if(connectRequest.getType() == PacketType.CONNECT){
        ConnectPacket connectPacket = ConnectPacket.parseFrom(response);
        System.out.println("Connected to lobby " + connectPacket.getLobbyId());
        inLobby = true;
      }else if(connectRequest.getType() == PacketType.ERR_LDNE){
        ErrLdnePacket lobbyDNE = ErrLdnePacket.parseFrom(response);
        System.out.println("Lobby " + lobbyId + " does not exist.");
      }else{
        ErrLfullPacket lobbyFull = ErrLfullPacket.parseFrom(response);
        System.out.println("Lobby " + lobbyId + " is full.");
      }

    }catch(SocketTimeoutException s){
      System.out.println("Socket timed out!");
    }catch(IOException e){
      e.printStackTrace();
      System.out.println("Input/Output Error!");
    }

  }

  public static void disconnectToServer(){
    try{
      isClosed = true;
      server.close();
    }catch(IOException e){
      e.printStackTrace();
      System.out.println("Input/Output Error!");
    }

  }

  public static void connectToServer(){
  		try{
  			server = new Socket(server_ip, server_port);
  		}catch(SocketTimeoutException s){
  			System.out.println("Socket timed out!");
  		}catch(IOException e){
  			e.printStackTrace();
  			System.out.println("Input/Output Error!");
  		}
  }

  public static void leaveLobby(){
      try{
        DisconnectPacket dis = DisconnectPacket.newBuilder().setPlayer(player).setType(PacketType.DISCONNECT).build();
        OutputStream outToServer = server.getOutputStream();
        outToServer.write(dis.toByteArray());
        inLobby = false;
        sc.nextLine();
      }catch(IOException e){
        e.printStackTrace();
      }
  }

}