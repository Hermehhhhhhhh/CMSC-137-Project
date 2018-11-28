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

public class GameGUI{

  JFrame mainFrame = new JFrame("RJ45.io");
  JPanel chatPanel = new JPanel();
  JPanel gamePanel = new JPanel();
  GameClient game;

  public GameGUI(){
       setUpMainFrame();
       setUpChatPanel();
       setUpGamePanel();
  }

  public void setUpChatPanel(){
    JTextField messageGetter = new JTextField();
    JTextArea messageReceiver = new JTextArea(100,100);
    JScrollPane messageReceiverScroll = new JScrollPane();

    messageGetter.setPreferredSize(new Dimension(200,20));
    chatPanel.setLayout(new BorderLayout());
    chatPanel.setBackground(Color.gray);
    chatPanel.setPreferredSize(new Dimension(300,700));
    chatPanel.add(messageGetter, BorderLayout.PAGE_END);
    chatPanel.add(messageReceiver, BorderLayout.CENTER);
    chatPanel.add(messageReceiverScroll);
    mainFrame.getContentPane().add(chatPanel, BorderLayout.LINE_START);
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
