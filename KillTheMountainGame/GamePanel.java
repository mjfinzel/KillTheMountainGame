package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel{
	private static final long serialVersionUID = 496501089018037548L;
	public static BufferedImage[][] tiles = FileIO.loadSpriteSheet("/Textures/overlappedTiles.png", 32, 32);
	public static BufferedImage playerImage = FileIO.loadImage("/Textures/Player.png");
	public static ArrayList<Level> levels = new ArrayList<Level>();
	public static MenuButton button;
	public static int currentLevel = 0;
	public static Player player = new Player(200,200);
	public static boolean showMap = false;
	public static boolean godmode = false;
	public GamePanel(){
		button = new MenuButton(60,40,"",ApplicationUI.windowWidth - 60 - 30, 30);
		Level testLevel = new Level("Test");
		levels.add(testLevel);
		testLevel.updateTileMapArt();
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Draw((Graphics2D)g);
	}
	public static void setCurrentLevel(Level lvl){
		for(int i = 0; i<levels.size();i++){
			if(levels.get(i)==lvl){
				currentLevel = i;
			}
		}
	}
	public void Draw(Graphics2D g){
		levels.get(currentLevel).Draw(g);
		//button.Draw(g);
		if(showMap){
			levels.get(currentLevel).map.Draw(g);
		}
		if(godmode){
			Font font = new Font("Iwona Heavy",Font.BOLD,18);
			g.setFont(font);
			g.setColor(Color.WHITE);
			g.drawString("Godmode: "+godmode,5,ApplicationUI.windowHeight-80);
			g.drawString("Seed: "+levels.get(currentLevel).seed,5,ApplicationUI.windowHeight-50);
		}
	}
}
