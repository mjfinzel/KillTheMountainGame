package Game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Player extends Entity{
	Inventory inventory = new Inventory();
	Point destination = new Point(0,0);
	double angleInRadians;
	double angleInDegrees;
	double movementSpeed = 3;
	Rectangle collisionBox;
	//Stats
	int level;
	int strength;
	int minStrength = 1;
	int maxStrength = 25;
	int maxHealth = 100;
	int currentHealth;
	double luck;
	boolean armorOn;
	boolean isAlive = true;
	int maxMana = 100;
	int minMana = 0;
	int currentMana = 0;
	double maxSpeed = 6;
	double minSpeed = 1;
	double magicResistance;
	double physicalResistance;
	double fireResistance;
	double iceResistance;
	int updatesInQue = 0;

	public Player(int x, int y){
		this.xpos = x;
		this.ypos = y;
		collisionBox = new Rectangle(x,y,32,32);
	}
	public void moveTowardsDestination(){

		if(!atDestination()){

			//find angle between current position and destination
			angleInDegrees = getAngleToDestination();
			angleInRadians = Math.toRadians(angleInDegrees);
			//move towards destination
			setPosition(xpos+(movementSpeed*Math.cos(angleInRadians)),ypos+(movementSpeed*Math.sin(angleInRadians)));


		}
	}
	public void setPosition(double x, double y){
		Rectangle collisionBoxAtNewXPosition = new Rectangle((int)x,(int)ypos,32,32);
		Rectangle collisionBoxAtNewYPosition = new Rectangle((int)xpos,(int)y,32,32);
		boolean collidedWithSomethingX = false;
		boolean collidedWithSomethingY = false;
		for(int x1 = (int)(xpos/32)-((int)movementSpeed+1);x1<(int)(xpos/32)+(int)movementSpeed+1;x1++){
			for(int y1 = (int)(ypos/32)-((int)movementSpeed+1);y1<(int)(ypos/32)+(int)movementSpeed+1;y1++){
				if(x1>=0&&x1<GamePanel.levels.get(GamePanel.currentLevel).width&&y1>=0&&y1<GamePanel.levels.get(GamePanel.currentLevel).height){
					Tile temp = GamePanel.levels.get(GamePanel.currentLevel).tileMap[x1][y1];
					if(collisionBoxAtNewXPosition.intersects(temp.collisionBox)&&temp.collisionType>=1){
						//GamePanel.levels.get(GamePanel.currentLevel).tileMap[x1][y1].flagged=true;
						collidedWithSomethingX = true;
					}
				}
			}
		}
		for(int x1 = (int)(xpos/32)-((int)movementSpeed+1);x1<(int)(xpos/32)+(int)movementSpeed+1;x1++){
			for(int y1 = (int)(ypos/32)-((int)movementSpeed+1);y1<(int)(ypos/32)+(int)movementSpeed+1;y1++){
				if(x1>=0&&x1<GamePanel.levels.get(GamePanel.currentLevel).width&&y1>=0&&y1<GamePanel.levels.get(GamePanel.currentLevel).height){
					Tile temp = GamePanel.levels.get(GamePanel.currentLevel).tileMap[x1][y1];
					if(collisionBoxAtNewYPosition.intersects(temp.collisionBox)&&temp.collisionType>=1){
						//GamePanel.levels.get(GamePanel.currentLevel).tileMap[x1][y1].flagged=true;
						collidedWithSomethingY = true;
					}
				}
			}
		}
		//if player is going to collide with the tile at their future position
		if(collidedWithSomethingX==false||GamePanel.godmode){
			xpos = x;
			collisionBox.x=(int)xpos;
		}
		if(collidedWithSomethingY==false||GamePanel.godmode){
			ypos = y;
			collisionBox.y=(int)ypos;
		}
	}
	/*
	 * check if the player is already at their destination
	 * 
	 * @return true if player is at their destination, false if not
	 */
	public boolean atDestination(){
		if(Math.abs(xpos-destination.x)<=movementSpeed&&Math.abs(ypos-destination.y)<=movementSpeed){
			setPosition(destination.x,destination.y);
			return true;
		}
		return false;
	}
	/*
	 * get angle between the player and the destination
	 * 
	 * @return the angle to move in
	 */
	public double getAngleToDestination() {
		double angle = (float) Math.toDegrees(Math.atan2((double)destination.y - ypos, (double)destination.x - xpos));
		if(angle < 0){
			angle += 360;
		}
		return angle;
	}

	public void update(){
		if(!GamePanel.levels.get(GamePanel.currentLevel).drawingLevel){
			moveTowardsDestination();
		}
		else{
			updatesInQue++;
		}

	}

	public void Draw(Graphics2D g){
		g.drawImage(GamePanel.playerImage,(ApplicationUI.windowWidth/2)-16,(ApplicationUI.windowHeight/2)-16,32,32,null);

	}
}
