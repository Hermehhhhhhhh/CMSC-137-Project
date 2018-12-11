package client;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.event.KeyListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.lang.Exception;
import java.util.ArrayList;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.util.Random;
import java.util.Iterator;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.io.*;
import javax.imageio.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.util.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements KeyListener, MouseMotionListener{
	//private Game game;
	ArrayList<Point> listOfDots;
	ArrayList<Point> foods;
	Image OSC;
	GameGUI mainGUI;


	Image img = Toolkit.getDefaultToolkit().getImage("images/c.png");
	Image img1 = Toolkit.getDefaultToolkit().getImage("images/m.png");
	Image img2 = Toolkit.getDefaultToolkit().getImage("images/food3.png");
	Image img3 = Toolkit.getDefaultToolkit().getImage("images/food1.png");

	Image img4 = Toolkit.getDefaultToolkit().getImage("images/bricks.png");
  Image img5 = Toolkit.getDefaultToolkit().getImage("images/galaxy.png");

	Boolean inGame = true;

	int playerscore = 0;

	PointerInfo a = MouseInfo.getPointerInfo();
	int size = 10;
	int speed = 10;
	Random r;

	public GamePanel(GameGUI mainGUI){
		this.mainGUI = mainGUI;
		this.setBackground(Color.RED);
    this.setPreferredSize(new Dimension(900,700));
    this.addKeyListener(this);
		this.addMouseMotionListener(this);
    this.setLayout(null);
		listOfDots = new ArrayList<Point>();
    foods = new ArrayList<Point>();

    r = new Random();
    listOfDots.add(new Point(100, 100));

		Thread gameThread = new Thread(){
			@Override
			public void run(){
				GameLoop();
			}
		};
		gameThread.start();
	}

	/*@Override*/
	public void paintComponent(Graphics g) {
        if(inGame){
            Dimension d = getSize();
            drawScore(g);
            checkOffscreenImage();
            Graphics offG = OSC.getGraphics();
            offG.setColor(Color.BLUE);
            offG.fillRect(0, 0, d.width, d.height);
            paintOffscreen(OSC.getGraphics());
            g.drawImage(OSC, 0, 0, null);
        }else{
            gameOver(g);
        }
    }

    public void drawScore(Graphics g){
        String msg="Score" + playerscore;
        Font small = new Font("Helvetica", Font.BOLD, 15);
        FontMetrics metr = getFontMetrics(small);
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, 0, 0);
    }

    private void checkOffscreenImage() {
        Dimension d = getSize();
        if (OSC == null || OSC.getWidth(null) != d.width
                || OSC.getHeight(null) != d.height) {
            OSC = createImage(d.width, d.height);
        }
    }

    public void paintOffscreen(Graphics g) {
        g.clearRect(0, 0, 900, 900);
        Point first = new Point();
        Point last = listOfDots.get(0);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(9 + (float) size / 10));
        for(int i = 1; i < listOfDots.size(); i++){
            first = listOfDots.get(i);
            if(i==listOfDots.size()-1){
                g2.setColor(Color.WHITE);
                //g2.drawImage(img5, last.x, last.y, null);
                g2.drawLine(first.x, first.y, last.x, last.y);

            }else{
                g2.setColor(Color.BLUE);
                g2.drawLine(first.x, first.y, last.x, last.y);
            }
            last = new Point(first);
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for(int i = 0; i < foods.size(); i++){
            r = new Random();
            int foodpic = r.nextInt(4) + 1;

            if (foodpic == 1) g2d.drawImage(img, foods.get(i).x, foods.get(i).y, null);
            if (foodpic == 2) g2d.drawImage(img1, foods.get(i).x, foods.get(i).y, null);
            if (foodpic == 3) g2d.drawImage(img2, foods.get(i).x, foods.get(i).y, null);
            if (foodpic == 4) g2d.drawImage(img3, foods.get(i).x, foods.get(i).y, null);
        }

        Graphics2D g2g = (Graphics2D) g;
        g2g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

	public Point calcCoor(Point last, Point mouse){
        double degree = 0;
        if(last.x < mouse.x && last.y < mouse.y){
            degree = 360 - Math.toDegrees(Math.atan((double) (mouse.y - last.y) / (mouse.x - last.x)));
        }else if(last.x > mouse.x && last.y > mouse.y){
            degree = 180 - Math.toDegrees(Math.atan((double) (last.y - mouse.y) / (last.x - mouse.x)));
        }else if(last.y > mouse.y && last.x < mouse.x){
            degree = Math.toDegrees(Math.atan((double) (last.y - mouse.y) / (mouse.x - last.x)));
        }else if(last.y < mouse.y && last.x > mouse.x){
            degree = 180 + Math.toDegrees(Math.atan((double) (mouse.y - last.y) / (last.x - mouse.x)));
        }
        Point p = new Point((int)
                (last.x + Math.cos(Math.toRadians(degree)) * speed), (int)
                (last.y - Math.sin(Math.toRadians(degree)) * speed));
        return p;
    }

	private void GameLoop(){
		while(inGame){
			try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkCollision();
            if(foods.size() < 200)
                foods.add(new Point(r.nextInt(900), r.nextInt(900)));
            a = MouseInfo.getPointerInfo();
            Point p = a.getLocation();
            Point last = listOfDots.get(listOfDots.size() - 1);
            Point n = new Point();

            if(last.distance(p) > 5){
                n = calcCoor(last, p);
                listOfDots.add(n);
                if(listOfDots.size() >= size){
                    for(int i = 0; i < listOfDots.size() - size; i++){
                        listOfDots.remove(i);
                    }
                }
                // System.out.println(n);
            }
            Iterator<Point> i = foods.iterator();
            while(i.hasNext()){
                Point food = i.next();
                if(food.distance(n) < 20){
                    playerscore = playerscore +10;
										this.mainGUI.updateStatPanel();
                    i.remove();
                    size++;
                }

                if(playerscore==3000){
                    inGame = false;
                    repaint();
                    break;
                }
            }
            repaint();
		}
	}

  private void checkCollision(){ //collision of sides palang
      Point first = new Point();
      first = listOfDots.get(listOfDots.size()-1);
      if(first.x < 2){
          inGame = false;
          repaint();
      }
      if(first.x > 899){
          inGame = false;
          repaint();
      }
      if(first.y < 21){
          inGame = false;
          repaint();
      }
      if(first.y > 699){
          inGame = false;
          repaint();
      }


  }

  public void gameOver(Graphics g){

      // JFrame popout = new JFrame("Game Over!");
      // popout.setPreferredSize(new Dimension(300, 100));
      // popout.setBackground(Color.BLACK);
      // popout.setVisible(true);

      // JButton b1 = new JButton("EXIT");
      // b1.setBounds(50, 50, 50, 50);
      // b1.addActionListener(new ActionListener() {
      //     public void actionPerformed(ActionEvent e)
      //     {
      //         System.exit(0);
      //     }
      // });

      // popout.add(b1);
      // popout.pack();

      String msg="GAME OVER";
      String msg1="SCORE: " + playerscore;

      Font small = new Font("Helvetica", Font.BOLD, 20);
      FontMetrics metr = getFontMetrics(small);

      g.setColor(Color.white);
      g.setFont(small);
      g.drawString(msg, (900 - metr.stringWidth(msg))/2, 700/2);
      g.drawString(msg1, (900 - metr.stringWidth(msg1))/2, (700/2)+30);


  }

	public int getScore(){
		return(playerscore);
	}

	public int getSpeed(){
		return(speed);
	}


	@Override
	public void keyPressed(KeyEvent e){}

	@Override
	public void keyReleased(KeyEvent e){}

	@Override
	public void keyTyped(KeyEvent e){}

	@Override
	public void mouseDragged(MouseEvent e){

	}

	@Override
	public void mouseMoved(MouseEvent e){
		//rj45.setLocation(e.getX() - 16, e.getY() + 40);
	}
}
