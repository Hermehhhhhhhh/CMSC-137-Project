package client;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import proto.PlayerProtos.Player;
import proto.TcpPacketProtos.TcpPacket.*;
import proto.TcpPacketProtos.TcpPacket;
import java.net.*;
import java.io.*;

public class GameGUI{
  JFrame mainFrame = new JFrame("RJ45.io");
  JPanel chatPanel = new JPanel();
  JPanel gamePanel = new JPanel();
  JTextField messageGetter = new JTextField();
  JTextArea messageReceiver = new JTextArea(100,100);
  JScrollPane messageReceiverScroll = new JScrollPane();
  GameClient gameProper;

  public GameGUI(GameClient gameProper){
       this.gameProper = gameProper;
       setUpMainFrame();
       setUpChatPanel();
       setUpGamePanel();
  }

  public void setUpChatPanel(){
    System.out.println("asddadasd");
    messageGetter.addKeyListener(new KeyListener(){
      @Override
      public void keyPressed(KeyEvent e) {
        // System.out.println("asddadasd");
      }

      public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
          gameProper.sendMessage(messageGetter.getText());
          messageGetter.setText("");
        }
      }
      public void keyTyped(KeyEvent e) {

      }
    });

    chatPanel.setLayout(new BorderLayout());
    chatPanel.setBackground(Color.gray);
    chatPanel.setPreferredSize(new Dimension(300,700));
    chatPanel.add(messageGetter, BorderLayout.PAGE_END);
    chatPanel.add(messageReceiver, BorderLayout.CENTER);
    // chatPanel.add(messageReceiverScroll);
    mainFrame.getContentPane().add(chatPanel, BorderLayout.LINE_START);
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

  public void setUpGamePanel(){
    gamePanel.setBackground(Color.blue);
    gamePanel.setPreferredSize(new Dimension(900,700));
    mainFrame.getContentPane().add(gamePanel, BorderLayout.LINE_END);
  }

  public void setUpMainFrame(){
    mainFrame.setLayout(new BorderLayout());
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setSize(1200,700);
    mainFrame.setVisible(true);
  }


}
