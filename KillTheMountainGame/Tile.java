package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Tile {
	int xpos;
	int ypos;
	int artX;
	int artY;
	int tileID = 0;
	int elevation;
	int oldElevation;
	boolean flagged = false;
	Color flagColor = new Color(200,0,0,100);
	int collisionType = 0;//0 means no collision, 1 means collision
	Rectangle collisionBox;

	public Tile(int x, int y, int id, int elev){
		xpos = x*32;
		ypos = y*32;
		elevation = elev;
		oldElevation = elev;
		tileID = id;
		collisionBox = new Rectangle(x,y,32,32);
		if(tileID==-1){
			collisionType = 0;
			artX = 4;
			artY = 0;
		}
		if(tileID==0){//grass
			artX = 6;
			artY = 0;
			collisionType = 0;
		}
		if(tileID==1){//water
			artX = 3;
			artY = 0;
			collisionType = 1;
		}

		if(tileID==2){//sand
			artX = 7;
			artY = 0;
			collisionType = 0;
		}
		if(tileID==3){//Plateau wall
			artX=6;
			artY=2;
			collisionType = 1;
		}
		if(tileID==4){//roof facing away from the player
			artX = 1;
			artY = 9;
		}
		if(tileID==5){//roof facing towards the player
			artX = 1;
			artY = 10;
		}
		if(tileID==6){
			
		}
	}


	public int getXpos()
	{
		return this.xpos;
	}
	public int getYpos()
	{
		return this.ypos;
	}
	public void setXpos(int x)
	{
		this.xpos = x;
	}
	public void setYpos(int y)
	{
		this.ypos = y;
	}
	public int getArtX()
	{
		return this.artX;
	}
	public int getArtY()
	{
		return this.artY;
	}
	public void setArtX(int x)
	{
		this.artX = x;
	}
	public void setArtY(int y)
	{
		this.artY = y;
	}
	public boolean isFlagged()
	{
		return this.flagged;
	}
	public void setFlagged(boolean bool)
	{
		this.flagged = bool;
	}


	//	public Tile(int x, int y, int xID, int yID){
	//		xpos = x*32;
	//		ypos = y*32;
	//		artX = xID;
	//		artY = yID;
	//		collisionBox = new Rectangle(xpos,ypos,32,32);
	//		
	//
	//	}
	/*
	 * Updates the artwork of the tile to make it match up with surrounding tiles
	 */
	public void updateArt(){
		Tile northernTile = getNorthTile();
		Tile northEasternTile = getNorthEastTile();
		Tile easternTile = getEastTile();
		Tile southEasternTile = getSouthEastTile();
		Tile southernTile = getSouthTile();
		Tile southWesternTile = getSouthWestTile();
		Tile westernTile = getWestTile();
		Tile northWesternTile = getNorthWestTile();
		if(tileID==3){//plateau wall
			//check if this is the top edge
			if(northernTile!=null&&northernTile.elevation<elevation){	
				//if(southernTile!=null&&southernTile.elevation==elevation){					
				if(westernTile!=null&&westernTile.elevation==elevation){
					if(easternTile!=null&&easternTile.elevation==elevation){
						//if(northWesternTile!=null&&northWesternTile.elevation==elevation){	
						//if(southWesternTile!=null&&southWesternTile.elevation==elevation){	
							//if(northEasternTile!=null&&northEasternTile.elevation==elevation){	
							//if(southEasternTile!=null&&southEasternTile.elevation==elevation){	
								artX=9;
								artY=3;
							//}
							//}
						//}
						//}
					}
				}
				//}
			}
			//check if this is the bottom edge
			//if(northernTile!=null&&northernTile.elevation==elevation){	
			if(southernTile!=null&&southernTile.elevation<elevation){					
				if(westernTile!=null&&westernTile.elevation==elevation){
					if(easternTile!=null&&easternTile.elevation==elevation){
						//if(northWesternTile!=null&&northWesternTile.elevation==elevation){	
							//if(southWesternTile!=null&&southWesternTile.elevation==elevation){	
							//if(northEasternTile!=null&&northEasternTile.elevation==elevation){	
								//if(southEasternTile!=null&&southEasternTile.elevation==elevation){	
								artX=9;
								artY=5;
								//}
							//}
							//}
						//}
					}
				}
			}
			//}
			//check if this is the left edge
			if(northernTile!=null&&northernTile.elevation==elevation){	
				if(southernTile!=null&&southernTile.elevation==elevation){					
					if(westernTile!=null&&westernTile.elevation<elevation){
						//if(easternTile!=null&&easternTile.elevation==elevation){
						//if(northWesternTile!=null&&northWesternTile.elevation==elevation){	
						//if(southWesternTile!=null&&southWesternTile.elevation==elevation){	
						//if(northEasternTile!=null&&northEasternTile.elevation==elevation){	
						//if(southEasternTile!=null&&southEasternTile.elevation==elevation){	
						artX=9;
						artY=1;
						//}
						//}
						//}
						//}
						//}
					}
				}
			}
			//check if this is the right edge
			if(northernTile!=null&&northernTile.elevation==elevation){	
				if(southernTile!=null&&southernTile.elevation==elevation){					
					//if(westernTile!=null&&westernTile.elevation==elevation){
					if(easternTile!=null&&easternTile.elevation<elevation){
						//if(northWesternTile!=null&&northWesternTile.elevation==elevation){	
						//if(southWesternTile!=null&&southWesternTile.elevation==elevation){	
						//if(northEasternTile!=null&&northEasternTile.elevation==elevation){	
						//if(southEasternTile!=null&&southEasternTile.elevation==elevation){	
						artX=9;
						artY=9;
						//}
						//}
						//}
						//}
					}
					//}
				}
			}
			//check if this is the top right corner with lower elevation right and above
			if(northernTile!=null&&northernTile.elevation<elevation){	
				if(southernTile!=null&&southernTile.elevation==elevation){					
					if(westernTile!=null&&westernTile.elevation==elevation){
						if(easternTile!=null&&easternTile.elevation<elevation){
							//if(northWesternTile!=null&&northWesternTile.elevation==elevation){	
							//if(southWesternTile!=null&&southWesternTile.elevation==elevation){	
							//if(northEasternTile!=null&&northEasternTile.elevation==elevation){	
							//if(southEasternTile!=null&&southEasternTile.elevation==elevation){	
							artX=9;
							artY=8;
							//}
							//}
							//}
							//}
						}
					}
				}
			}
			//check if this is the top left corner with lower elevation left and above
			if(northernTile!=null&&northernTile.elevation<elevation){	
				if(southernTile!=null&&southernTile.elevation==elevation){					
					if(westernTile!=null&&westernTile.elevation<elevation){
						if(easternTile!=null&&easternTile.elevation==elevation){
							//if(northWesternTile!=null&&northWesternTile.elevation==elevation){	
							//if(southWesternTile!=null&&southWesternTile.elevation==elevation){	
							//if(northEasternTile!=null&&northEasternTile.elevation==elevation){	
							//if(southEasternTile!=null&&southEasternTile.elevation==elevation){	
							artX=9;
							artY=0;
							//}
							//}
							//}
							//}
						}
					}
				}
			}
			//check if this is the bottom left corner with lower elevation left and below
			if(northernTile!=null&&northernTile.elevation==elevation){	
				if(southernTile!=null&&southernTile.elevation<elevation){					
					if(westernTile!=null&&westernTile.elevation<elevation){
						if(easternTile!=null&&easternTile.elevation==elevation){
							//if(northWesternTile!=null&&northWesternTile.elevation==elevation){	
							//if(southWesternTile!=null&&southWesternTile.elevation==elevation){	
							//if(northEasternTile!=null&&northEasternTile.elevation==elevation){	
							//if(southEasternTile!=null&&southEasternTile.elevation==elevation){	
							artX=9;
							artY=2;
							//}
							//}
							//}
							//}
						}
					}
				}
			}
			//check if this is the bottom right corner with lower elevation right and below
			if(northernTile!=null&&northernTile.elevation==elevation){	
				if(southernTile!=null&&southernTile.elevation<elevation){					
					if(westernTile!=null&&westernTile.elevation==elevation){
						if(easternTile!=null&&easternTile.elevation<elevation){
							//if(northWesternTile!=null&&northWesternTile.elevation==elevation){	
							//if(southWesternTile!=null&&southWesternTile.elevation==elevation){	
							//if(northEasternTile!=null&&northEasternTile.elevation==elevation){	
							//if(southEasternTile!=null&&southEasternTile.elevation==elevation){	
							artX=9;
							artY=10;
							//}
							//}
							//}
							//}
						}
					}
				}
			}
			//check if this is the inverted top left corner
			if(northernTile!=null&&northernTile.elevation>=elevation){	
				if(southernTile!=null&&southernTile.elevation==elevation){					
					if(westernTile!=null&&westernTile.elevation>=elevation){
						if(easternTile!=null&&easternTile.elevation==elevation){
							if(northWesternTile!=null&&northWesternTile.elevation>=elevation){	
								if(southWesternTile!=null&&southWesternTile.elevation>=elevation){	
									//if(northEasternTile!=null&&northEasternTile.elevation==elevation){	
									if(southEasternTile!=null&&southEasternTile.elevation<elevation){	
										artX=9;
										artY=11;
									}
									//}
								}
							}
						}
					}
				}
			}
			//check if this is the inverted top right corner
			if(northernTile!=null&&northernTile.elevation>=elevation){	
				if(southernTile!=null&&southernTile.elevation==elevation){					
					if(westernTile!=null&&westernTile.elevation==elevation){
						if(easternTile!=null&&easternTile.elevation>=elevation){
							//if(northWesternTile!=null&&northWesternTile.elevation==elevation){	
							if(southWesternTile!=null&&southWesternTile.elevation<elevation){	
								if(northEasternTile!=null&&northEasternTile.elevation>=elevation){	
									if(southEasternTile!=null&&southEasternTile.elevation>=elevation){	
										artX=9;
										artY=6;
									}
								}
							}
							//}
						}
					}
				}
			}
			//check if this is the inverted bottom left corner
			if(northernTile!=null&&northernTile.elevation==elevation){	
				//if(southernTile!=null&&southernTile.elevation==elevation){					
					//if(westernTile!=null&&westernTile.elevation==elevation){
						if(easternTile!=null&&easternTile.elevation==elevation){
							if(northWesternTile!=null&&northWesternTile.elevation>=elevation){	
								//if(southWesternTile!=null&&southWesternTile.elevation==elevation){	
								if(northEasternTile!=null&&northEasternTile.elevation<elevation){	
									if(southEasternTile!=null&&southEasternTile.elevation>=elevation){	
										artX=9;
										artY=12;
									}
								}
								//}
							}
						}
					//}
				//}
			}
			//check if this is the inverted bottom right corner
			if(northernTile!=null&&northernTile.elevation==elevation){	
				if(southernTile!=null&&southernTile.elevation>=elevation){					
					if(westernTile!=null&&westernTile.elevation==elevation){
						if(easternTile!=null&&easternTile.elevation>=elevation){
							if(northWesternTile!=null&&northWesternTile.elevation<elevation){	
								if(southWesternTile!=null&&southWesternTile.elevation>=elevation){	
									if(northEasternTile!=null&&northEasternTile.elevation>=elevation){	
										if(southEasternTile!=null&&southEasternTile.elevation>=elevation){	
											artX=9;
											artY=7;
										}
									}
								}
							}
						}
					}
				}
			}
			//check if this is a lone rock
			if(northernTile!=null&&northernTile.elevation<elevation){	
				if(southernTile!=null&&southernTile.elevation<elevation){					
					if(westernTile!=null&&westernTile.elevation<elevation){
						if(easternTile!=null&&easternTile.elevation<elevation){
							//if(northWesternTile!=null&&northWesternTile.elevation==elevation){	
							//if(southWesternTile!=null&&southWesternTile.elevation==elevation){	
							//if(northEasternTile!=null&&northEasternTile.elevation<elevation){	
							//if(southEasternTile!=null&&southEasternTile.elevation==elevation){	
							artX=9;
							artY=13;
							//}
							//}
							//}
							//}
						}
					}
				}
			}

		}
	}
	public Tile getNorthTile(){
		if((ypos/32)-1>0){
			return GamePanel.levels.get(GamePanel.currentLevel).tileMap[(xpos/32)][(ypos/32)-1];
		}
		return null;
	}
	public Tile getNorthEastTile(){
		if((ypos/32)-1>=0&&(xpos/32)+1<GamePanel.levels.get(GamePanel.currentLevel).width){
			return GamePanel.levels.get(GamePanel.currentLevel).tileMap[(xpos/32)+1][(ypos/32)-1];
		}
		return null;
	}
	public Tile getSouthTile(){
		if((ypos/32)+1<GamePanel.levels.get(GamePanel.currentLevel).height){
			return GamePanel.levels.get(GamePanel.currentLevel).tileMap[(xpos/32)][(ypos/32)+1];
		}
		return null;
	}
	public Tile getSouthEastTile(){
		if((ypos/32)+1<GamePanel.levels.get(GamePanel.currentLevel).height&&(xpos/32)+1<GamePanel.levels.get(GamePanel.currentLevel).width){
			return GamePanel.levels.get(GamePanel.currentLevel).tileMap[(xpos/32)+1][(ypos/32)+1];
		}
		return null;
	}
	public Tile getWestTile(){
		if((xpos/32)-1>0){
			return GamePanel.levels.get(GamePanel.currentLevel).tileMap[(xpos/32)-1][(ypos/32)];
		}
		return null;
	}
	public Tile getNorthWestTile(){
		if((ypos/32)-1>=0&&(xpos/32)-1>=0){
			return GamePanel.levels.get(GamePanel.currentLevel).tileMap[(xpos/32)-1][(ypos/32)-1];
		}
		return null;
	}
	public Tile getEastTile(){
		if((xpos/32)+1<GamePanel.levels.get(GamePanel.currentLevel).width){
			return GamePanel.levels.get(GamePanel.currentLevel).tileMap[(xpos/32)+1][(ypos/32)];
		}
		return null;
	}
	public Tile getSouthWestTile(){
		if((ypos/32)+1<GamePanel.levels.get(GamePanel.currentLevel).height&&(xpos/32)-1>=0){
			return GamePanel.levels.get(GamePanel.currentLevel).tileMap[(xpos/32)-1][(ypos/32)+1];
		}
		return null;
	}
	public void Draw(Graphics2D g){
		//updateArt();
		g.drawImage(GamePanel.tiles[artX][artY],((ApplicationUI.windowWidth/2)-16)+xpos-(int)GamePanel.player.xpos,((ApplicationUI.windowHeight/2)-16)+ypos-(int)GamePanel.player.ypos,32,32,null);
		
		if(flagged){
			g.setColor(flagColor);
			g.fillRect(((ApplicationUI.windowWidth/2)-16)+xpos-(int)GamePanel.player.xpos,((ApplicationUI.windowHeight/2)-16)+ypos-(int)GamePanel.player.ypos, collisionBox.width, collisionBox.height);
			//flagged = false;
		}
		//Font font = new Font("Iwona Heavy",Font.BOLD,10);
		//g.setFont(font);
		//g.setColor(Color.WHITE);
		//g.drawString(elevation+"", ((ApplicationUI.windowWidth/2)-16)+xpos-(int)GamePanel.player.xpos+10,((ApplicationUI.windowHeight/2)-16)+ypos-(int)GamePanel.player.ypos+10);
		//g.setColor(Color.pink);
		//g.drawString(oldElevation+"", ((ApplicationUI.windowWidth/2)-16)+xpos-(int)GamePanel.player.xpos+10,((ApplicationUI.windowHeight/2)-16)+ypos-(int)GamePanel.player.ypos+25);
	}
}
