package client;

import javax.swing.JPanel;
//import javax.swing.JLabel;
//import javax.swing.ImageIcon;
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

public class GamePanel extends JPanel implements KeyListener, MouseMotionListener{
	//private Game game;
	ArrayList<Point> listOfDots;
    ArrayList<Point> foods;
    Image OSC;
    PointerInfo a = MouseInfo.getPointerInfo();
    int size = 10;
    int speed = 10;
    Random r;

	public GamePanel(){
		this.setBackground(Color.blue);
    	this.setPreferredSize(new Dimension(900,700));
    	this.addKeyListener(this);
		this.addMouseMotionListener(this);

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
        Dimension d = getSize();
        checkOffscreenImage();
        Graphics offG = OSC.getGraphics();
        offG.setColor(Color.white);
        offG.fillRect(0, 0, d.width, d.height);
        paintOffscreen(OSC.getGraphics());
        g.drawImage(OSC, 0, 0, null);
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
        g.setColor(Color.BLACK);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(9 + (float) size / 10));
        for(int i = 1; i < listOfDots.size(); i++){
            first = listOfDots.get(i);
            g2.drawLine(first.x, first.y, last.x, last.y);
            last = new Point(first);
        }
        g2.setColor(Color.red);
        for(int i = 0; i < foods.size(); i++){
            g2.fillOval(foods.get(i).x, foods.get(i).y, 10, 10);
        }
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
		while(true){
			try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(foods.size() < 100)
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
                    i.remove();
                    size++;
                }
            }
            repaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent e){}

	@Override
	public void keyReleased(KeyEvent e){}

	@Override
	public void keyTyped(KeyEvent e){}

	/*@Override
	public void mousePressed(MouseEvent e){}

	@Override
	public void mouseReleased(MouseEvent e){}

	@Override
	public void mouseClicked(MouseEvent e){}

	@Override
	public void mouseEntered(MouseEvent e){}

	@Override
	public void mouseExited(MouseEvent e){}*/

	@Override
	public void mouseDragged(MouseEvent e){

	}

	@Override
	public void mouseMoved(MouseEvent e){
		//rj45.setLocation(e.getX() - 16, e.getY() + 40);
	}
}
