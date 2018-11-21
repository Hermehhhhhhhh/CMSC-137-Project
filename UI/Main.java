import java.util.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.awt.event.ActionEvent;

public class Main extends JPanel implements ActionListener{
	private final int B_WIDTH = 500; //board width temporary lang
	private final int B_HEIGHT = 500; // height
	private final int DOT_SIZE = 10; //food
	private final int ALL_DOTS = 2500; //total food
	private final int RAND_POS = 49; //position
	private final int DELAY = 140;
	//foods
	private final int ALL_C = 30;
	private final int ALL_M = 30;
	private final int ALL_S = 30;



	private final int x[] = new int[ALL_DOTS];
	private final int y[] = new int[ALL_DOTS];

	private int dots;
	private int apple_x[] = new int[ALL_C]; //letterc
	private int apple_y[] = new int[ALL_C]; //letterc

	private int mango_x[] = new int[ALL_M]; //letterm
	private int mango_y[] = new int[ALL_M]; //letterm

	private int grapes_x[] = new int[ALL_S]; //letters
	private int grapes_y[] = new int[ALL_S]; //letters

	private boolean leftDirection = false;
	private boolean rightDirection = true;
	private boolean upDirection = false;
	private boolean downDirection = false;
	private boolean inGame = true;

	private Timer timer;
	private Image ball;
	private Image apple; //c
	private Image mango; //m
	private Image grapes; //s
	private Image head; //rj45
	private Image body; //wire

	public Main(){
		addKeyListener(new TAdapter());
		setBackground(Color.black);
		setFocusable(true);
		setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
		loadImages();
		initGame();
	}

	public void loadImages(){
		ImageIcon wirepic = new ImageIcon(getClass().getResource("images/wire.png"));
		ball = wirepic.getImage();

		ImageIcon applepic = new ImageIcon(getClass().getResource("images/food.png"));
		apple = applepic.getImage();

		ImageIcon grapespic = new ImageIcon(getClass().getResource("images/food3.png"));
		grapes = grapespic.getImage();

		ImageIcon mangopic = new ImageIcon(getClass().getResource("images/food2.png"));
		mango = mangopic.getImage();

		ImageIcon wormhead = new ImageIcon(getClass().getResource("images/rj45.png"));
		head = wormhead.getImage();
	}

	private void initGame(){
		dots = 3;

		for(int z=0; z<dots; z++){
			x[z] = 50-z*10;
			y[z] = 50;
		}

		locateApple();
		locateMango();
		locateGrapes();

		timer = new Timer(DELAY, this);
		timer.start();
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		doDrawing(g);
	}

	private void doDrawing(Graphics g){
		if(inGame){
			
				for(int p=0; p<ALL_C; p++){	
					g.drawImage(apple, apple_x[p], apple_y[p], this);
				}

				for(int p=0; p<ALL_C; p++){	
					g.drawImage(mango, mango_x[p], mango_y[p], this);
				}

				for(int p=0; p<ALL_C; p++){	
					g.drawImage(grapes, grapes_x[p], grapes_y[p], this);
				}
						
				for(int z=0; z<dots; z++){
					if(z==0){
						g.drawImage(head, x[z], y[z],  this);
					}else{
						g.drawImage(ball, x[z], y[z], this);
					}
				}
				Toolkit.getDefaultToolkit().sync();
			
		}else{
			gameOver(g);
		}
	
	}

	private void gameOver(Graphics g){
		String msg="game over";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = getFontMetrics(small);

		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(msg, (B_WIDTH - metr.stringWidth(msg))/2, B_HEIGHT/2);
	}

	private void checkApple(){
		for(int p=0; p<ALL_C; p++){
			if ((x[0] == apple_x[p]) && (y[0] == apple_y[p])){
				dots++;
				locateApple();
			}

		}

	}

	private void checkMango(){
		for(int p=0; p<ALL_C; p++){
			if ((x[0] == mango_x[p]) && (y[0] == mango_y[p])){
				dots++;
				locateMango();
			}

		}

	}

	private void checkGrapes(){
		for(int p=0; p<ALL_C; p++){
			if ((x[0] == grapes_x[p]) && (y[0] == grapes_y[p])){
				dots++;
				locateGrapes();
			}

		}

	}

	private void move(){
		for(int z=dots; z>0; z--){
			x[z] = x[z-1];
			y[z] = y[z-1];
		}

		if(leftDirection){
			x[0] -= DOT_SIZE;
		}

		if(rightDirection){
			x[0] += DOT_SIZE;
		}

		if(upDirection){
			y[0] -= DOT_SIZE;
		}

		if(downDirection){
			y[0] += DOT_SIZE;
		}
	}

	private void checkCollision(){
		for(int z=dots; z>0; z--){
			if((z>4) && (x[0] == x[z]) && (y[0] == y[z])){
				inGame = false;
			}
		}

		if(y[0] >= B_HEIGHT){
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

	private void locateApple(){
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
	public void actionPerformed(ActionEvent e){
		if(inGame){
			checkApple();
			checkMango();
			checkGrapes();
			checkCollision();
			move();
		}

		repaint();
	}

	private class TAdapter extends KeyAdapter{
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