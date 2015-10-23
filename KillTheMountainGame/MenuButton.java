package Game;

import java.awt.Graphics2D;

public class MenuButton {

	int xPosition;
	int yPosition;
	boolean clicked = false;
	String text = "";
	int buttonWidth;
	int buttonHeight;
	
	public MenuButton(int buttonWidth, int buttonHeight, String text, int x, int y) {
		
		this.buttonWidth = buttonWidth;
		this.buttonHeight = buttonHeight;
		this.text = text;
		xPosition = x;
		yPosition = y;
		
	}
	
	public void Draw(Graphics2D g){
		g.drawImage(GamePanel.playerImage,(int)xPosition,(int)yPosition,buttonWidth,buttonHeight,null);

	}
	
}
