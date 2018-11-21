import java.awt.*;
import javax.swing.*;

public class Snake extends JFrame {
	public Snake(){
		add(new Main());
		setResizable(false);
		pack();

		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run(){
				JFrame ex= new Snake();
				ex.setVisible(true);
			}
		});
	}
}