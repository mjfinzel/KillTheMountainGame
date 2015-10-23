package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ApplicationUI extends JFrame{
	private static final long serialVersionUID = 1L;
	Controller ctrl;
	public static int windowWidth = 800;
	public static int windowHeight = 600;
	private int gameFPS = 60;
	private long beginTime;
	private long updatePeriod = 1000000000L/60;
	public ApplicationUI(){
		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());
		JPanel drawPanel = new GamePanel();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		drawPanel.setBackground(Color.BLACK);
		ctrl = new Controller();
		this.addKeyListener(ctrl);
		ctrl.setGamePanel(drawPanel);
		this.setFocusable(true);
		pane.add(drawPanel);
		setFullScreen(false);
		Thread gameThread = new Thread(){
			public void run(){
				gameLoop();
			}
		};
		gameThread.start();
	}
	/*
	 * Used to set whether the game should be displayed in full screen or windowed mode
	 * 
	 * @param fullscreen	a boolean used to decide if the game is full screen
	 */
	public void setFullScreen(boolean fullscreen){
		if(fullscreen==true){
			this.setExtendedState(Frame.MAXIMIZED_BOTH);  
			this.setUndecorated(true);  
		}
		else{
			this.setExtendedState(Frame.NORMAL);  
			this.setUndecorated(false);  
		}
	}
	public static void main(String[] args) {
		ApplicationUI f = new ApplicationUI();
		f.setSize(windowWidth,windowHeight);
		f.setVisible(true);
		
	}
	/*
	 * The main loop of the game. This will call all of the update methods and repaint the game at a fixed rate
	 */
	public void gameLoop(){
		while(true){
			windowWidth = getWidth();
			windowHeight = getHeight();
			beginTime=System.nanoTime();
			//check keys
			Controller.checkKeys();
			
			//repaint the graphics of the game
			//look into buffer strategy and active rendering
			repaint();
			//update
			GamePanel.levels.get(GamePanel.currentLevel).update();
			//the time taken to do everything with the frame in nanoseconds
			long timeTaken = System.nanoTime()-beginTime;
			long timeLeft = (updatePeriod - timeTaken) / 1000000L;; // In milliseconds
			if(timeTaken<updatePeriod){
				// If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
				if (timeLeft < 10){ 
					timeLeft = 10; //set a minimum
				}
				try {
					//Provides the necessary delay and also yields control so that other thread can do work.
					Thread.sleep(timeLeft);
				} catch (InterruptedException ex) { }
			}
		}
	}

}
