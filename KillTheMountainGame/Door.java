package Game;


import java.awt.Point;

public class Door {
	int xposA;
	int xposB;
	Point spawnPointA;
	Point spawnPointB;
	int yposA;
	int yposB;
	
	Level levelB;
	
	public Door(int x, int y, Level destination, int destX, int destY){
		xposA = x;
		yposA = y;
		levelB = destination;
		spawnPointB = new Point(xposB,yposB);
	}
	public void enterDoor(){
		GamePanel.setCurrentLevel(levelB);
	}
}
