package client;

import proto.PlayerProtos.Player;
import proto.TcpPacketProtos.TcpPacket.*;
import proto.TcpPacketProtos.TcpPacket;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Graphics;
import java.io.IOException;
import java.io.File;
import java.net.MalformedURLException;
import javax.imageio.ImageIO;
import java.net.URL;

public class GameGUI{
  JFrame mainFrame = new JFrame("RJ45.io");
  JPanel chatPanel = new JPanel();
  JPanel statPanel = new JPanel();
  JPanel homePanel = new JPanel(){
    @Override
    protected void paintComponent(Graphics g) {
      try{
        URL imgURL = new File( "./resources/homeBG.jpg" ).toURI().toURL();
        Image homeBG = ImageIO.read(imgURL);
        super.paintComponent(g);
        g.drawImage(homeBG, 0, 0, null);
      }catch(IOException e){
        e.printStackTrace();
      }
    }
  };
  JPanel optionPanel = new JPanel(){
    @Override
    protected void paintComponent(Graphics g) {
      try{
        URL imgURL = new File( "./resources/homeBG.jpg" ).toURI().toURL();
        Image homeBG = ImageIO.read(imgURL);
        super.paintComponent(g);
        g.drawImage(homeBG, 0, 0, null);
      }catch(IOException e){
        e.printStackTrace();
      }
    }
  };
  JPanel joinLobbyPanel = new JPanel(){
    @Override
    protected void paintComponent(Graphics g) {
      try{
        URL imgURL = new File( "./resources/homeBG.jpg" ).toURI().toURL();
        Image homeBG = ImageIO.read(imgURL);
        super.paintComponent(g);
        g.drawImage(homeBG, 0, 0, null);
      }catch(IOException e){
        e.printStackTrace();
      }
    }
  } ;
  JScrollPane chatScroll;
  GamePanel gamePanel;
  JLabel scoreLabel = new JLabel("Score: 0");
  JTextField messageGetter = new JTextField();
  JTextArea messageReceiver = new JTextArea(100,100);
  JScrollPane messageReceiverScroll = new JScrollPane();
  GameClient gameProper;
  JLabel lobbyIDLabel;

  public GameGUI(GameClient gameProper){
     this.gameProper = gameProper;
     setUpMainFrame();
     setUpHomePanel();
     setUpStatPanel();
     setUpOptionPanel();
     setUpChatPanel();
     setUpGamePanel();
     setUpJoinLobbyPanel();

     mainFrame.getContentPane().add(homePanel);
  }

  public void setUpHomePanel(){
    JTextField inGameName = new JTextField();
    JTextField password = new JTextField();
    JLabel ign = new JLabel("In-Game-Name: ");
    JLabel pw = new JLabel("Password: ");
    JButton enter = new JButton("ENTER");

    ign.setBounds(375, 265, 150, 20);
    inGameName.setBounds(500,250,300,50);
    pw.setBounds(405, 340, 150, 20);
    password.setBounds(500,325,300,50);
    enter.setBounds(500,400,300,50);
    enter.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        gameProper.createPlayer(inGameName.getText(), password.getText());
        inGameName.setText("");
        password.setText("");
        changePanel(homePanel, optionPanel);
      }
    } );
    homePanel.add(ign);
    homePanel.add(inGameName);
    homePanel.add(pw);
    homePanel.add(password);
    homePanel.add(enter);
    homePanel.setLayout(null);
  }

  public void setUpOptionPanel(){
    JButton createLobbyButton = new JButton("CREATE LOBBY");
    JButton joinLobbyButton = new JButton("JOIN LOBBY");
    JButton exitButton = new JButton("EXIT");

    createLobbyButton.setBounds(450,250,300,50);
    joinLobbyButton.setBounds(450,350,300,50);
    exitButton.setBounds(450,450,300,50);

    joinLobbyButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        changePanel(optionPanel, joinLobbyPanel);
      }
    } );

    createLobbyButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        gameProper.createLobby();
        lobbyIDLabel.setText("Lobby ID: "+ gameProper.getLobbyId());
        gameProper.connectToLobby();
        gameProper.startChat();
        putChatGamePanel(optionPanel);
      }
    } );
    exitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        gameProper.disconnectToServer();
        changePanel(optionPanel, homePanel);
      }
    } );

    optionPanel.add(createLobbyButton);
    optionPanel.add(joinLobbyButton);
    optionPanel.add(exitButton);
    optionPanel.setLayout(null);
  }

  public void setUpJoinLobbyPanel(){
    JTextField lobbyID = new JTextField();
    JLabel lid = new JLabel("Lobby ID: ");
    JLabel errorNotification = new JLabel("");
    JButton enter = new JButton("ENTER");
    JButton back = new JButton("BACK");

    lid.setBounds(375, 265, 150, 20);
    lobbyID.setBounds(500,250,300,50);
    errorNotification.setBounds(500, 315, 350, 20);
    errorNotification.setForeground(Color.RED);
    back.setBounds(500,350,150,50);
    enter.setBounds(650,350,150,50);

    enter.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String response = gameProper.connectToLobby(lobbyID.getText());
        if(response.equals("Connected")){
          gameProper.startChat();
          lobbyIDLabel.setText("Lobby ID: "+ gameProper.getLobbyId());
          putChatGamePanel(joinLobbyPanel);
        }else{
          errorNotification.setText(response);
          mainFrame.repaint();
        }
      }
    } );

    back.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        lobbyID.setText("");
        errorNotification.setText("");
        changePanel(joinLobbyPanel, optionPanel);
      }
    } );

    joinLobbyPanel.add(lobbyID);
    joinLobbyPanel.add(lid);
    joinLobbyPanel.add(enter);
    joinLobbyPanel.add(back);
    joinLobbyPanel.add(errorNotification);
    joinLobbyPanel.setLayout(null);
  }

  public void setUpStatPanel(){
    lobbyIDLabel = new JLabel("Lobby ID: ");
    statPanel.setPreferredSize(new Dimension(200,200));

    scoreLabel.setBounds(50,15,150,20);
    lobbyIDLabel.setBounds(50,35,150,20);

    statPanel.add(scoreLabel);
    statPanel.add(lobbyIDLabel);
    statPanel.setLayout(null);
  }

  public void setUpChatPanel(){
    messageGetter.addKeyListener(new KeyListener(){
      @Override
      public void keyPressed(KeyEvent e) {
        // System.out.println("asddadasd");
      }

      public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
          gameProper.sendMessage(messageGetter.getText());
          messageReceiver.setCaretPosition(messageReceiver.getDocument().getLength());
          messageGetter.setText("");
        }
      }
      public void keyTyped(KeyEvent e) {

      }
    });

    chatPanel.setLayout(new BorderLayout());
    chatPanel.setBackground(Color.gray);
    chatPanel.setPreferredSize(new Dimension(300,700));
    messageReceiver.setEditable(false);
    chatScroll = new JScrollPane(messageReceiver);

    chatPanel.add(messageGetter, BorderLayout.PAGE_END);
    chatPanel.add(chatScroll, BorderLayout.CENTER);
    chatPanel.add(statPanel, BorderLayout.NORTH);
  }

  public void receiveMessages(byte[] response){
    try{
      TcpPacket reply = TcpPacket.parseFrom(response);
      if(reply.getType() == PacketType.CONNECT){
        ConnectPacket received = ConnectPacket.parseFrom(response);
        messageReceiver.append(received.getPlayer().getName() + " joined the lobby.\n");
      }else if(reply.getType() == PacketType.CHAT){
        ChatPacket received = ChatPacket.parseFrom(response);
        // System.out.println(received.getPlayer().getName()+ ": "+ received.getMessage() + "\n");
        messageReceiver.append(received.getPlayer().getName()+ ": "+ received.getMessage()+ "\n");
      }else if(reply.getType() == PacketType.DISCONNECT){
        DisconnectPacket received = DisconnectPacket.parseFrom(response);
        messageReceiver.append(received.getPlayer().getName() + " left the lobby.\n");
      }else{
        messageReceiver.append("ERROR!");
      }
      messageReceiver.update(messageReceiver.getGraphics());
    }catch(IOException e){
 		 e.printStackTrace();
 		 System.out.println("Input/Output Error!");
 	 }

  }

  public void setUpMainFrame(){
    mainFrame.setLayout(new BorderLayout());
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setSize(1200,700);
    mainFrame.setVisible(true);
  }

  public void changePanel(JPanel toBeRemoved, JPanel toBeAdd){
    mainFrame.remove(toBeRemoved);
    mainFrame.getContentPane().add(toBeAdd);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  public void putChatGamePanel(JPanel toBeRemoved){
    mainFrame.remove(toBeRemoved);
    mainFrame.getContentPane().add(chatPanel, BorderLayout.WEST);
    mainFrame.getContentPane().add(gamePanel, BorderLayout.CENTER);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  public void setUpGamePanel(){
    gamePanel = new GamePanel(this);
  }

  public void updateStatPanel(){
    scoreLabel.setText("Score: " +String.valueOf(gamePanel.getScore()));
  }

}
