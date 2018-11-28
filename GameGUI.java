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

import java.util.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseListener;
import javax.swing.Timer;
import java.awt.event.ActionEvent;

public class GameGUI extends JPanel implements ActionListener{

  JFrame mainFrame = new JFrame("RJ45.io");
  JPanel chatPanel = new JPanel();
  JPanel gamePanel = new JPanel();
  GameClient game;

  private final int B_WIDTH = 800; //board width temporary lang
  private final int B_HEIGHT = 800; // height
  private final int DOT_SIZE = 10; //food
  private final int ALL_DOTS = 6400; //total food
  private final int RAND_POS = 79; //position
  private final int DELAY = 140;
  private final int ALL_C = 30; //number foods
  private final int ALL_M = 30;
  private final int ALL_S = 30;

  private final int x[] = new int[ALL_DOTS];  //dots coordinate
  private final int y[] = new int[ALL_DOTS];

  private int dots;
  private int apple_x[] = new int[ALL_C]; //letterc
  private int apple_y[] = new int[ALL_C]; //letterc

  private int mango_x[] = new int[ALL_M]; //letterm
  private int mango_y[] = new int[ALL_M]; //letterm

  private int grapes_x[] = new int[ALL_S]; //letters
  private int grapes_y[] = new int[ALL_S]; //letters

  private boolean leftDirection = false;  //position
  private boolean rightDirection = true;
  private boolean upDirection = false;
  private boolean downDirection = false;
  private boolean inGame = true;

  private Timer timer;  //movement
  private Image ball;
  private Image apple; //c
  private Image mango; //m
  private Image grapes; //s
  private Image head; //rj45
  private Image body; //wire

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


    gamePanel.setBackground(Color.black);
    gamePanel.setPreferredSize(new Dimension(900,700));
    gamePanel.addKeyListener(new TAdapter());
    gamePanel.setFocusable(true);
    loadImages();
    initGame();


    mainFrame.getContentPane().add(gamePanel, BorderLayout.LINE_END);
  }

  public void setUpMainFrame(){
    mainFrame.setLayout(new BorderLayout());
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setSize(1200,700);
    mainFrame.setVisible(true);
  }

  //FUNCTIONS NA GINAWA KO!!!!!

  public void loadImages(){
    ImageIcon wirepic = new ImageIcon(getClass().getResource("images/wire.png"));
    ball = wirepic.getImage(); //body ng wire
    ImageIcon applepic = new ImageIcon(getClass().getResource("images/food.png"));
    apple = applepic.getImage(); //letter C food
    ImageIcon grapespic = new ImageIcon(getClass().getResource("images/food3.png"));
    grapes = grapespic.getImage(); //letter M food
    ImageIcon mangopic = new ImageIcon(getClass().getResource("images/food2.png"));
    mango = mangopic.getImage(); //letter S food
    ImageIcon wormhead = new ImageIcon(getClass().getResource("images/rj45.png"));
    head = wormhead.getImage();
  }

  private void initGame(){
    dots = 3; //initial size ng wire- 3 score
    for(int z=0; z<dots; z++){ //coordinate nung wire at start
      x[z] = 50-z*10;
      y[z] = 50;
    }

    locateApple();  //locte foods
    locateMango();
    locateGrapes();

    timer = new Timer(DELAY, this); //start timer
    timer.start();
  }

  @Override
  protected void paintComponent(Graphics g){ //override paint component func
    super.paintComponent(g);
    doDrawing(g);
  }


  private void doDrawing(Graphics g){ //paint graphics
    if(inGame){
        for(int p=0; p<ALL_C; p++){ //draw all letter C inmap
          g.drawImage(apple, apple_x[p], apple_y[p], this);
        }
        for(int p=0; p<ALL_C; p++){ //draw all letter M
          g.drawImage(mango, mango_x[p], mango_y[p], this);
        }
        for(int p=0; p<ALL_C; p++){ //draw all letter S
          g.drawImage(grapes, grapes_x[p], grapes_y[p], this);
        }

        for(int z=0; z<dots; z++){ //draw the moving wire
          if(z==0){
            g.drawImage(head, x[z], y[z],  this);
          }else{
            g.drawImage(ball, x[z], y[z], this);
          }
        }
        Toolkit.getDefaultToolkit().sync();
    }else{  //if gameover na
      gameOver(g);
    }
  }

  private void gameOver(Graphics g){ //game over
    String msg="game over";
    Font small = new Font("Helvetica", Font.BOLD, 14);
    FontMetrics metr = getFontMetrics(small);

    g.setColor(Color.white);
    g.setFont(small);
    g.drawString(msg, (B_WIDTH - metr.stringWidth(msg))/2, B_HEIGHT/2);
  }

  private void checkApple(){ //check if nakakuha ng letter c
    for(int p=0; p<ALL_C; p++){
      if ((x[0] == apple_x[p]) && (y[0] == apple_y[p])){
        dots++;
        locateApple();
      }
    }
  }

  private void checkMango(){ //check if nakakuha ng letter m
    for(int p=0; p<ALL_C; p++){
      if ((x[0] == mango_x[p]) && (y[0] == mango_y[p])){
        dots++;
        locateMango();
      }
    }
  }

  private void checkGrapes(){ //check if nakakuha ng letter s
    for(int p=0; p<ALL_C; p++){
      if ((x[0] == grapes_x[p]) && (y[0] == grapes_y[p])){
        dots++;
        locateGrapes();
      }
    }
  }

  private void move(){  //movement by keys palang, not yet mouse
    for(int z=dots; z>0; z--){
      x[z] = x[z-1];
      y[z] = y[z-1];
    }

    if(leftDirection){  //nabawasan ng isa sa left kasi gummalaw
      x[0] -= DOT_SIZE;
    }

    if(rightDirection){ //same here
      x[0] += DOT_SIZE;
    }

    if(upDirection){
      y[0] -= DOT_SIZE;
    }

    if(downDirection){
      y[0] += DOT_SIZE;
    }
  }

  private void checkCollision(){ //check if nagbump
    for(int z=dots; z>0; z--){ //nagbump sa sides?
      if((z>4) && (x[0] == x[z]) && (y[0] == y[z])){
        inGame = false;
      }
    }

    if(y[0] >= B_HEIGHT){ //nagbump sa self?
      inGame = false;
    }

    if(y[0] < 0){
      inGame= false;
    }

    if(x[0] >= B_WIDTH){
      inGame = false;
    }

    if(x[0] < 0){
      inGame= false;
    }

    if(!inGame){
      timer.stop();
    }
  }


  private void locateApple(){ //lagay ng new foods
    for(int p=0; p<ALL_C; p++){
      int r = (int)(Math.random()*RAND_POS);
      apple_x[p]= r*DOT_SIZE;
    
      r = (int)(Math.random()*RAND_POS);
      apple_y[p]=r*DOT_SIZE;
    }
  }
  private void locateMango(){
    for(int p=0; p<ALL_C; p++){
      int r = (int)(Math.random()*RAND_POS);
      mango_x[p]= r*DOT_SIZE;
    
      r = (int)(Math.random()*RAND_POS);
      mango_y[p]=r*DOT_SIZE;
    }
  }
  private void locateGrapes(){
    for(int p=0; p<ALL_C; p++){
      int r = (int)(Math.random()*RAND_POS);
      grapes_x[p]= r*DOT_SIZE;
    
      r = (int)(Math.random()*RAND_POS);
      grapes_y[p]=r*DOT_SIZE;
    }
  }

  @Override
  public void actionPerformed(ActionEvent e){ //override func
    if(inGame){
      checkApple();
      checkMango();
      checkGrapes();
      checkCollision();
      move();
    }
    repaint();
  }


  private class TAdapter extends KeyAdapter{ //direction of head
    @Override
    public void  keyPressed(KeyEvent e){
      int key = e.getKeyCode();
      if (key==KeyEvent.VK_LEFT && !rightDirection){
        leftDirection=true;
        upDirection=false;
        downDirection=false;
      }
      if (key==KeyEvent.VK_RIGHT && !leftDirection){
        rightDirection=true;
        upDirection=false;
        downDirection=false;
      }
      if (key==KeyEvent.VK_UP && !downDirection){
        upDirection=true;
        rightDirection=false;
        leftDirection=false;
      }
      if (key==KeyEvent.VK_DOWN && !upDirection){
        downDirection=true;
        rightDirection=false;
        leftDirection=false;
      }

    }
  }


}
