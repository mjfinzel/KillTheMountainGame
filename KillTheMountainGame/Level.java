package Game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Level {
	Tile[][] tileMap;
	Tile[][] hardMap;
	Tile[] baseTiles;	//for initalizing maps
	int width=30;
	int height=30;
	String name;
	Random levelGenerationRandom;
	int seed;
	int minNumberOfMountains;
	int maxNumberOfMountains;
	int mountainMaxHeight;
	int mountainMinHeight;
	int minNumberOfCanyons;
	int maxNumberOfCanyons;
	int canyonMaxDepth;//the maximum depth of canyons in the level
	int canyonMinDepth;//the minimum depth of canyons in the level

	//ratio of size between one layer and the layer before it
	int mountainMaxSteepness=0;
	int mountainMinSteepness=0;
	int canyonMaxSteepness;
	int canyonMinSteepness;
	LevelMap map;

	boolean drawingLevel = false;

	ArrayList<ArrayList<Tile>> lakes = new ArrayList<ArrayList<Tile>>();


	public Level(String Name){

		//Pre-determined map generation for specified zones or debugging purposes.
		name = Name;
		name = "Test";
		tileMap = new Tile[width][height];
		if(name.equals("Test")){
			seed = 136;
			width = 200;
			height = 200;
			minNumberOfMountains = 2;
			maxNumberOfMountains = 10;
			mountainMaxHeight = 5;
			mountainMinHeight = 1;
			mountainMaxSteepness = 8;//each layer is half the size of the one it's built on
			mountainMinSteepness = 4;//each layer is one quarter of the size of what it's built on
		}

		//generateDoors();
		//update all the tiles on the map to show their proper artwork
		//updateTileMapArt();
		else if(name.equals("Overworld"))	//player hub; Links to each of the dungeons' starting locations and serves as safe point.
		{
			seed = -1;
			width = 50;
			height = 50;
		}
		else if(name.equals("Boss Room A"))	//zone where player encounters the first boss
		{
			seed = -2;
			width = 20;
			height = 10;
		}
		else if(name.equals("Boss Room B"))	//zone where player encounters the second boss
		{
			seed = -3;
			width = 20;
			height = 10;
		}
		else if(name.equals("Boss Room C")) //zone where player encounters the third boss
		{
			seed = -4;
			width = 15;
			height = 40;
		}
		else if(name.equals("Safe Zone")) 	//randomly occuring safe zone within dungeons where the player character can rest.
		{
			seed = -5;
			width = 40;
			height = 25;
		}
		
		generateMap();

	}
	/*
	 * calls the updateArt method on every tile in the tilemap
	 * 
	 * @return none
	 */
	public void updateTileMapArt(){
		for(int x = 0; x<width; x++){
			for(int y = 0; y<height; y++){
				if(tileMap[x][y]!=null){
					tileMap[x][y].updateArt();
				}
			}
		}

	}
	/*
	 * Fills in the tileMap array
	 */
	public void generateMap(){
		tileMap = new Tile[width][height];
		levelGenerationRandom = new Random(seed);
		//initialize the tile map
		for(int x = 0; x<width;x++){
			for(int y = 0; y<height; y++){

				//tileMap[x][y]=new Tile(x,y,6,0);//everything is grass
				tileMap[x][y]=new Tile(x,y,1,0);//everything is water

			}
		}		

		if(seed < 0)
		{
			buildRoom(-1 - seed);	//translate seed to a key for room ID's, build them
			tileMap = hardMap;
		}

		//create islands
		int islandCount = randomNumber(2,15);
		for(int i = 0; i<islandCount; i++){
			generateLakeOrIsland(randomNumber(320,2550), false);
		}
		//create lakes
		int lakeCount = randomNumber(2,3);
		for(int i = 0; i<lakeCount; i++){
			generateLakeOrIsland(randomNumber(120,500), true);
		}
		int numberOfMountains = randomNumber(minNumberOfMountains,maxNumberOfMountains);
		//create mountains

		int mountainHeight = randomNumber(mountainMinHeight,mountainMaxHeight);
		int steepness = randomNumber(mountainMinSteepness,mountainMaxSteepness);
		int mountainBaseSize = 500*mountainHeight*steepness;
		for(int f = 1; f<=mountainHeight;f++){
			for(int k = 0; k<numberOfMountains;k++){
				//create a plateau at the elevation determined by f
				generatePlateauOrCanyon(f,mountainBaseSize/(f*steepness),true);
			}
			//create the plateau edges
			setElevationEdges(f+1);
			//remove all weird outcrops that we don't have textures for
			removeAnySectionsOfCliffThatAreConnectedToCliffButTooSmallToWalkOn(f+1);
		}





		//create the map
		map = new LevelMap(tileMap);
	}
	/*
	 * used to generate an area on the map which has a higher or lower elevation that the elevation it is created on
	 * 
	 * @param buildElevation - the elevation to build this terrain feature on
	 * @param size - the number of tiles this feature should be made of (not always correct because
	 * the method skips some tiles if it is necessary to prevent an infinite loop where it can't find
	 * any valid places to add to at the desired elevation
	 * @param isPlateau - true if this feature should be raising elevation compared to build elevation,
	 * false if it should be lowering elevation compated to the build elevation.
	 */
	public void generatePlateauOrCanyon(int buildElevation, int size, boolean isPlateau){
		int elevationChange = 0;
		int count = 0;
		if(isPlateau){
			elevationChange = 1;
		}
		else{
			elevationChange = -1;
		}
		ArrayList<Tile> tilesAdded = new ArrayList<Tile>();
		//find a random tile where the tileMap is not water
		Point randomlyChosenTile = new Point(randomNumber(0,width-1),randomNumber(0,height-1));
		int timesTried = 0;
		int maxTries = 999;
		while(timesTried<maxTries&&tileMap[randomlyChosenTile.x][randomlyChosenTile.y].elevation!=buildElevation||tileMap[randomlyChosenTile.x][randomlyChosenTile.y].tileID!=0){//could create an infinite loop if the build elevation does not yet exist
			randomlyChosenTile = new Point(randomNumber(0,width-1),randomNumber(0,height-1));
			timesTried++;
		}
		if(timesTried<maxTries){
			tileMap[randomlyChosenTile.x][randomlyChosenTile.y] = new Tile(randomlyChosenTile.x,randomlyChosenTile.y,0,buildElevation+elevationChange);
			tileMap[randomlyChosenTile.x][randomlyChosenTile.y].oldElevation=buildElevation;
			tilesAdded.add(tileMap[randomlyChosenTile.x][randomlyChosenTile.y]);
			count++;
		}
		while(count<size&&tilesAdded.size()>0){//for the desired amount of tiles
			//pick a random tile which has already been created for this terrain feature
			Tile randTile = tilesAdded.get(randomNumber(0,tilesAdded.size()-1));
			randomlyChosenTile = new Point(randTile.xpos/32,randTile.ypos/32);

			//get all the adjacent grass tiles that are at the same elevation as buildElevation
			ArrayList<Tile> adjacentTiles = tilesAdjacentToPosition(randomlyChosenTile.x,randomlyChosenTile.y,0,true,buildElevation);

			//make sure there are adjacent tiles that can be modified
			if(adjacentTiles.size()>0){
				randTile = adjacentTiles.get(randomNumber(0,adjacentTiles.size()-1));
				//pick a random one of these tiles
				randomlyChosenTile = new Point(randTile.xpos/32,randTile.ypos/32);
				//make sure randTile is not a cliff tile
				ArrayList<Tile> tilesAdjacentToRandTile = getTilesAdjacentToPosition(new Point(randTile.xpos/32,randTile.ypos/32),true);
				boolean temp = true;
				//				for(int f = 0; f<tilesAdjacentToRandTile.size();f++){
				//					if(tilesAdjacentToRandTile.get(f).elevation<buildElevation){
				//						temp = false;
				//					}
				//				}
				if(temp&&tileMap[randomlyChosenTile.x][randomlyChosenTile.y].tileID==0&&tileMap[randomlyChosenTile.x][randomlyChosenTile.y].elevation==buildElevation){
					if(buildElevation!=2||true){
						//set the tile at this position to be a placeholder tile
						tileMap[randomlyChosenTile.x][randomlyChosenTile.y]=new Tile(randomlyChosenTile.x,randomlyChosenTile.y,0,buildElevation+elevationChange);
						tileMap[randomlyChosenTile.x][randomlyChosenTile.y].oldElevation=buildElevation;
						tilesAdded.add(tileMap[randomlyChosenTile.x][randomlyChosenTile.y]);
						count++;
					}
					else{
						//tileMap[randomlyChosenTile.x][randomlyChosenTile.y].flagged=true;
						tilesAdded.add(tileMap[randomlyChosenTile.x][randomlyChosenTile.y]);
					}
				}
				else{
					System.out.println("failed to pick a grass tile");
				}
			}
			if(randomNumber(1,100)==1){//failsafe to prevent infinite loops
				count++;
			}
		}

	}
	public void generateHouse(String id, int xpos, int ypos, int elev){
		int w=0;
		int h=0;
		int roofHeight=0;
		int floors=0;
		if(id.equalsIgnoreCase("Noob Hut")){
			w = randomNumber(5,20);
			roofHeight = randomNumber(1,4)*3;
			floors = randomNumber(1,2);
			h = (2*floors)+roofHeight;
		}
		int startX = xpos;
		int startY = ypos;
		int endX = xpos+w;
		int endY = ypos+h;
		//create the roof
		for(int x = startX; x<=endX;x++){
			for(int y = startY; y<=endY;y++){
				//top part of the roof
				if(y<(startY+(roofHeight/3))){
					tileMap[x][y]= new Tile(x,y,4,elev);
				}
			}
		}
	}
	/*
	 * Generates some doors in the level so that you can get from one level to the next.
	 * 
	 * @return none.
	 */
	public void generateDoors(){
		if(name.equals("Test")){
			ArrayList<Point> acceptableTilesToOverwrite = new ArrayList<Point>();
			acceptableTilesToOverwrite.add(new Point(6,0));
			int w = 3;
			int h = 3;
			ArrayList<Tile[][]> possibleAreas = findAreasFullOfTileTypes(w,h,acceptableTilesToOverwrite);
			Tile[][] randomlyPickedSpot = possibleAreas.get(randomNumber(0,possibleAreas.size()));
		}
	}
	/*
	 * Finds all the areas in the level which consist completely of the specified tile types.
	 * 
	 * @param w - the width of the area to search for.
	 * @param h - the height of the area to search for.
	 * @param tileIDs - An arraylist of all the tiles that are acceptable in the area desired.
	 */
	public ArrayList<Tile[][]> findAreasFullOfTileTypes(int w, int h, ArrayList<Point> tileIDs){
		ArrayList<Tile[][]> areasFound = new ArrayList<Tile[][]>();
		//loop through all the tiles in the level
		for(int x = 0; x<width-w; x++){
			for(int y = 0; y<height-h;y++){
				//check an area where this x,y is the top left corner and width and height are what was passed
				boolean isCorrect = true;
				Tile[][] subArea = new Tile[w][h];
				for(int i = x; i<x+w;i++){
					for(int j = y; j<y+h; j++){
						boolean oneWasRight = false;
						//loop through all the desired tile types
						for(int k = 0; k<tileIDs.size();k++){
							if((tileMap[w][j].artX==tileIDs.get(k).x&&tileMap[w][j].artY==tileIDs.get(k).y)){
								oneWasRight = true;
							}
						}
						subArea[i-w][j-h] = tileMap[w][j];
						if(!oneWasRight){
							isCorrect=false;
						}
					}
				}
				if(isCorrect){
					areasFound.add(subArea);
				}
			}
		}
		return areasFound;
	}
	/*
	 * makes cliff tiles on the edges of a change in elevation
	 * 
	 * @param elev - the elevation to act upon
	 */
	public void setElevationEdges(int elev){
		//loop through all tiles
		for(int x = 0; x<width; x++){
			for(int y = 0; y<height; y++){
				//if this tile is at the specified elevation
				if(tileMap[x][y].elevation==elev){
					//create a list of the tiles that are adjacent to this
					ArrayList<Tile> adjacentTiles = getTilesAdjacentToPosition(new Point(x,y),true);
					//if this tile is at an edge
					//check for any adjacent tiles that are not at the same elevation as this tile
					boolean isEdge = false;
					for(int i = 0; i<adjacentTiles.size();i++){
						if(adjacentTiles.get(i).elevation!=elev){
							isEdge = true;
						}
					}
					//if the tile is an edge tile make it be cliff
					if(isEdge){
						int oldElev = tileMap[x][y].oldElevation;
						tileMap[x][y]= new Tile(x,y,3,elev);
						tileMap[x][y].oldElevation=oldElev;
					}
				}
			}
		}
	}
	/*
	 * Recursively removes any sections of a plateau or canyon which are too small to walk on
	 * 
	 * @param elev- the elevation to perform this method upon
	 */
	public void removeAnySectionsOfCliffThatAreConnectedToCliffButTooSmallToWalkOn(int elev){
		//find all the cliff tiles which have cliff on only one side
		for(int x = 0; x<width;x++){
			for(int y = 0; y<height; y++){
				if(tileMap[x][y].tileID==3&&tileMap[x][y].elevation==elev){
					removeOutcrops(new Point(x,y),elev);
				}
			}
		}
	}
	/*replaces any cliff tile passed (which is connected to only one other cliff tile) with a grass tile
	 * 
	 * @param position - the position of the tile to check
	 * @param elev - the elevation to check for adjacent cliff tiles
	 */
	public void removeOutcrops(Point position, int elev){
		//get the tiles which are adjacent to this one
		ArrayList<Tile> adjacentTiles = getTilesAdjacentToPosition(new Point(position.x,position.y),false);
		//determine how many of the adjacent tiles are cliff
		int count = 0;//the number of adjacent cliff tiles detected
		ArrayList<Tile> cliffTiles = new ArrayList<Tile>();
		for(int i = 0; i<adjacentTiles.size();i++){
			if(adjacentTiles.get(i).tileID==3&&adjacentTiles.get(i).elevation==elev){
				//cliffTiles.add(adjacentTiles.get(i));
				count++;
			}
		}
		Tile thisTile = tileMap[position.x][position.y];
		if(getNorthTile(thisTile)!=null&&getSouthTile(thisTile)!=null&&getWestTile(thisTile)!=null&&getEastTile(thisTile)!=null){
			//check if top and bottom are the same elevation but left and right are not
			if(getNorthTile(thisTile).elevation==thisTile.elevation&&getSouthTile(thisTile).elevation==thisTile.elevation){
				if(getWestTile(thisTile).elevation!=thisTile.elevation&&getEastTile(thisTile).elevation!=thisTile.elevation&&getWestTile(thisTile).elevation==getEastTile(thisTile).elevation){
					if(getNorthTile(thisTile).tileID==3&&getSouthTile(thisTile).tileID==3){
						count = 1;
						cliffTiles.add(getNorthTile(thisTile));
						cliffTiles.add(getSouthTile(thisTile));
					}
				}
			}
			//check if left and right are the same elevation but top and bottom are not
			if(getNorthTile(thisTile).elevation!=thisTile.elevation&&getSouthTile(thisTile).elevation!=thisTile.elevation&&getNorthTile(thisTile).elevation==getSouthTile(thisTile).elevation){
				if(getWestTile(thisTile).elevation==thisTile.elevation&&getEastTile(thisTile).elevation==thisTile.elevation){
					if(getWestTile(thisTile).tileID==3&&getEastTile(thisTile).tileID==3){
						count = 1;
						cliffTiles.add(getWestTile(thisTile));
						cliffTiles.add(getEastTile(thisTile));
					}
				}
			}
		}
		if(position.x!=0&&position.y!=0&&position.x!=width-1&&position.y!=height-1){
			if(count==1){//there is only one cliff tile adjacent to this

				int oldElev = tileMap[position.x][position.y].oldElevation;
				//set this section to be grass
				tileMap[position.x][position.y]=new Tile(position.x,position.y,0,oldElev);
				//tileMap[position.x][position.y].flagged=true;
				//call this method on the connected cliff sections
				for(int i = 0; i<cliffTiles.size();i++){
					removeOutcrops(new Point(cliffTiles.get(i).xpos/32,cliffTiles.get(i).ypos/32),elev);
				}
			}
		}
	}
	public Tile getNorthTile(Tile tile){
		if((tile.ypos/32)-1>0){
			return tileMap[(tile.xpos/32)][(tile.ypos/32)-1];
		}
		return null;
	}
	public Tile getNorthEastTile(Tile tile){
		if((tile.ypos/32)-1>=0&&(tile.xpos/32)+1<width){
			return tileMap[(tile.xpos/32)+1][(tile.ypos/32)-1];
		}
		return null;
	}
	public Tile getSouthTile(Tile tile){
		if((tile.ypos/32)+1<height){
			return tileMap[(tile.xpos/32)][(tile.ypos/32)+1];
		}
		return null;
	}
	public Tile getSouthEastTile(Tile tile){
		if((tile.ypos/32)+1<height&&(tile.xpos/32)+1<width){
			return tileMap[(tile.xpos/32)+1][(tile.ypos/32)+1];
		}
		return null;
	}
	public Tile getWestTile(Tile tile){
		if((tile.xpos/32)-1>0){
			return tileMap[(tile.xpos/32)-1][(tile.ypos/32)];
		}
		return null;
	}
	public Tile getNorthWestTile(Tile tile){
		if((tile.ypos/32)-1>=0&&(tile.xpos/32)-1>=0){
			return tileMap[(tile.xpos/32)-1][(tile.ypos/32)-1];
		}
		return null;
	}
	public Tile getEastTile(Tile tile){
		if((tile.xpos/32)+1<width){
			return tileMap[(tile.xpos/32)+1][(tile.ypos/32)];
		}
		return null;
	}
	public Tile getSouthWestTile(Tile tile){
		if((tile.ypos/32)+1<height&&(tile.xpos/32)-1>=0){
			return tileMap[(tile.xpos/32)-1][(tile.ypos/32)+1];
		}
		return null;
	}
	/* Get/Set methods*/
	public int getSeed()
	{
		return this.seed;
	}
	public void setSeed(int s)
	{
		this.seed = s;
	}
	public String getLevelName()
	{
		return this.name;
	}
	public void setLevelName(String str)
	{
		this.name = str;
	}
	/*
	 * gets all of the tiles that are adjacent to a tile
	 * 
	 * @param position - the position of the tile to check for adjacency
	 * @param includeCorners - true if corners should be added to the list of adjacent tiles, false if not
	 * 
	 * @return An arraylist of all the tiles adjacent to the specified tile position
	 */
	public ArrayList<Tile> getTilesAdjacentToPosition(Point position,boolean includeCorners){
		ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();
		for(int x = position.x-1; x<=position.x+1;x++){
			for(int y = position.y-1; y<=position.y+1;y++){
				if(x>=0&&y>=0&&x<width&&y<height){
					if(includeCorners){
						if(!(x==position.x&&y==position.y)){
							adjacentTiles.add(tileMap[x][y]);
						}
					}
					else{
						//if(!(x==position.x-1&&y==position.y-1)&&!(x==position.x+1&&y==position.y-1)&&!(x==position.x-1&&y==position.y+1)&&!(x==position.x+1&&y==position.y+1)){
						if(x==position.x||y==position.y){	
							if(!(x==position.x&&y==position.y)){
								adjacentTiles.add(tileMap[x][y]);
							}
						}
						//}
					}
				}
			}
		}
		//		//flag all adjacent tiles for testing purposes
		//		for(int i = 0; i<adjacentTiles.size();i++){
		//			adjacentTiles.get(i).flagged=true;
		//		}
		return adjacentTiles;
	}
	/*
	 * Generates a group of grass or water tiles on the tilemap
	 * 
	 * @param lakeSize - the number of tiles in this formation (can end up being smaller than specified if
	 * it is necessary to prevent an infinite loop of trying to find a valid location to add a tile to the 
	 * feature when there are none.
	 * @param isLake - true if creating a lake, false if creating an island
	 */
	public void generateLakeOrIsland(int lakeSize, boolean isLake){
		int createdTileID;
		int replacedTileID;
		int elevChange = 0;
		if(isLake){//decrease in elevation
			elevChange = -1;
			createdTileID=1;//water
			replacedTileID =0;//grass
		}
		else{//increase in elevation
			elevChange= 1;
			createdTileID = 0;//grass
			replacedTileID = 1;//water
		}
		//add randomly placed water
		Point lakePosition = new Point(randomNumber(0,width-1),randomNumber(0,height-1));
		ArrayList<Tile> lakeEdges = new ArrayList<Tile>();
		lakeEdges.add(tileMap[lakePosition.x][lakePosition.y]);

		int oldElevation = tileMap[lakePosition.x][lakePosition.y].elevation;
		tileMap[lakePosition.x][lakePosition.y]=new Tile(lakePosition.x,lakePosition.y,createdTileID,oldElevation+elevChange);
		//tileMap[lakePosition.x][lakePosition.y].flagged=true;
		int count = 0;
		//generate water
		while(count<lakeSize){
			//pick a random side of this tile
			boolean changed = false;
			while(!changed){
				Tile randTile = lakeEdges.get(randomNumber(0,lakeEdges.size()-1));
				Point randEdge = new Point(randTile.xpos/32,randTile.ypos/32);
				lakePosition = new Point(randEdge.x,randEdge.y);
				int side = randomNumber(1,4);
				//top
				if(side==1&&lakePosition.y-1>=0){
					lakePosition.y-=1;
					changed = true;
				}
				//bottom
				if(side==2&&lakePosition.y+1<height){
					lakePosition.y+=1;
					changed = true;
				}
				//left
				if(side==3&&lakePosition.x-1>=0){
					lakePosition.x-=1;
					changed = true;
				}
				//right
				if(side==4&&lakePosition.x+1<width){
					lakePosition.x+=1;
					changed = true;
				}
			}
			//System.out.println("lake edge: "+lakePosition.x+","+lakePosition.y);
			//if the tile chosen is not already a water tile and is at the correct elevation
			if(!(tileMap[lakePosition.x][lakePosition.y].tileID==createdTileID)&&tileMap[lakePosition.x][lakePosition.y].elevation==oldElevation){
				tileMap[lakePosition.x][lakePosition.y]=new Tile(lakePosition.x,lakePosition.y,createdTileID,oldElevation+elevChange);//random bits of water

				count++;
				//get a list of all the water tiles which are adjacent to this one
				ArrayList<Tile> adjacentTiles = tilesAdjacentToPosition(lakePosition.x,lakePosition.y,createdTileID,false,0);
				//if there is a non water tile adjacent to this
				if(adjacentTiles.size()!=4){
					lakeEdges.add(tileMap[lakePosition.x][lakePosition.y]);
				}
				//remove any water tiles from the list of edge tiles which are no longer on an edge
				for(int i = 0; i<adjacentTiles.size();i++){
					//if(tilesAdjacentToPosition(adjacentTiles.get(i).xpos/32,adjacentTiles.get(i).ypos/32,3,0).size()==4){
					if(adjacentTiles.size()==4){
						lakeEdges.remove(adjacentTiles.get(i));
					}
				}
			}
			else{
				if(randomNumber(1,10)==1){
					count++;
				}
			}
		}
		//generate shore
		for(int i = 0; i<lakeEdges.size();i++){

			//list of all the adjacent grass tiles to this water tile
			ArrayList<Tile> adjacentNonWaterTiles = tilesAdjacentToPosition(lakeEdges.get(i).xpos/32,lakeEdges.get(i).ypos/32,replacedTileID,false,0);

			//make all adjacent grass into dirt
			for(int j = 0; j<adjacentNonWaterTiles.size();j++){
				int x = adjacentNonWaterTiles.get(j).xpos/32;
				int y = adjacentNonWaterTiles.get(j).ypos/32;
				tileMap[x][y].flagged=true;

				tileMap[x][y]= new Tile(x,y,2,oldElevation);
			}
		}


	}
	/*
	 * generates a very specific room with little to no randomization
	 */
	private void buildRoom(int ID)
	{
		hardMap = new Tile[width][height];
		switch(ID)
		{
		//Overworld
		case 0: 
			//overworld = new Tile[width][height];
			for(int x = 0; x<width;x++){
				for(int y = 0; y<height; y++){
					hardMap[x][y]=new Tile(x,y,7,2); //base tile is heavy dirt
				}
			}
			for(int x = 0; x < width; x++)			//wall in with stone wall
			{
				hardMap[x][0] = new Tile(x,0,1,0);
				hardMap[x][height - 1] = new Tile(x, height - 1, 1, 0);
			}
			for(int y = 0; y < height - 1; y++)
			{
				hardMap[0][y] = new Tile(0,y,1,1);
				hardMap[width - 1][y] = new Tile(width - 1,y,1,1);
			}
			break;

			//boss room a
		case 1:
			//bossA = new Tile[width][height];
			for(int x = 0; x<width;x++){
				for(int y = 0; y<height; y++){
					hardMap[x][y]=new Tile(x,y,6,0); //base tile grass
				}
			}
			addTallGrass(hardMap);
			for(int x = 0; x < width; x++)			//wall in with tree cover
			{
				hardMap[x][0] = new Tile(x,0,8,1);
				hardMap[x][height - 1] = new Tile(x, height - 1, 8, 1);
			}
			for(int y = 0; y < height - 1; y++)
			{
				hardMap[0][y] = new Tile(0,y,8,2);
				hardMap[width - 1][y] = new Tile(width - 1,y,8,2);
			}

			break;

			//boss room b
		case 2:
			//bossB = new Tile[width][height];
			for(int x = 0; x<width;x++){
				for(int y = 0; y<height; y++){
					hardMap[x][y]=new Tile(x,y,0,1); //base tile is heavy dirt
				}
			}
			for(int x = 0; x < width; x++)			//wall in with stone wall
			{
				hardMap[x][0] = new Tile(x,0,1,0);
				hardMap[x][height - 1] = new Tile(x, height - 1, 1, 0);
			}
			for(int y = 0; y < height - 1; y++)
			{
				hardMap[0][y] = new Tile(0,y,1,1);
				hardMap[width - 1][y] = new Tile(width - 1,y,1,1);
			}
			break;

			//boss room c
		case 3:
			//bossC = new Tile[width][height];
			for(int x = 0; x<width;x++){
				for(int y = 0; y<height; y++){
					hardMap[x][y]=new Tile(x,y,0,1); //base tile is heavy dirt
				}
			}
			for(int x = 0; x < width; x++)
			{
				hardMap[x][0] = new Tile(x,0,1,0);
				hardMap[x][height - 1] = new Tile(x, height - 1, 1, 0);
			}
			for(int y = 0; y < height - 1; y++)
			{
				hardMap[0][y] = new Tile(0,y,1,1);
				hardMap[width - 1][y] = new Tile(width - 1,y,1,1);
			}
			break;

			//safe zone
		case 4:
			//safeRoom = new Tile[width][height];
			for(int x = 0; x<width;x++){
				for(int y = 0; y<height; y++){
					hardMap[x][y]=new Tile(x,y,0,1); //base tile is heavy dirt
				}
			}
			for(int x = 0; x < width; x++)
			{
				hardMap[x][0] = new Tile(x,0,1,0);
				hardMap[x][height - 1] = new Tile(x, height - 1, 1, 0);
			}
			for(int y = 0; y < height - 1; y++)
			{
				hardMap[0][y] = new Tile(0,y,1,1);
				hardMap[width - 1][y] = new Tile(width - 1,y,1,1);
			}
			break;		
		}
	}
	/*
	 * adds randomly generated tall grass to the tileMap
	 */
	private Tile[][] addTallGrass(Tile[][] map)
	{
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				if(map[x][y].getArtX() == 6 && map[x][y].getArtY() == 0)
					if(levelGenerationRandom.nextFloat() < 0.1)
						map[x][y].setArtY(1);
		return map;
	}



	/*
	 * Used to determine how many of the specified tile type is adjacent to a position
	 * 
	 * @param xpos - the x position of the tile to check for adjacent tiles
	 * @param ypos - the y position of the tile to check for adjacent tiles
	 * @param xID - the artX of the tile to be checked for
	 * @param yID - the artY of the tile to be checked for
	 * 
	 * @return The number of tiles of the type specified that are adjacent to this
	 */
	public ArrayList<Tile> tilesAdjacentToPosition(int xpos, int ypos, int id, boolean careAboutElevation, int desiredElevation){
		ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();
		//top
		if(ypos-1>=0){
			if(tileMap[xpos][ypos-1].tileID==id){
				if(careAboutElevation == false||tileMap[xpos][ypos-1].elevation==desiredElevation){
					adjacentTiles.add(tileMap[xpos][ypos-1]);
				}
			}
		}
		//bottom
		if(ypos+1<height){
			if(tileMap[xpos][ypos+1].tileID==id){
				if(careAboutElevation == false||tileMap[xpos][ypos+1].elevation==desiredElevation){
					adjacentTiles.add(tileMap[xpos][ypos+1]);
				}
			}
		}
		//left
		if(xpos-1>=0){
			if(tileMap[xpos-1][ypos].tileID==id){
				if(careAboutElevation == false||tileMap[xpos-1][ypos].elevation==desiredElevation){
					adjacentTiles.add(tileMap[xpos-1][ypos]);
				}
			}
		}
		//right
		if(xpos+1<width){
			if(tileMap[xpos+1][ypos].tileID==id){
				if(careAboutElevation == false||tileMap[xpos+1][ypos].elevation==desiredElevation){
					adjacentTiles.add(tileMap[xpos+1][ypos]);
				}
			}
		}

		return adjacentTiles;
	}
	/*
	 * generates a random number in the specified range
	 * 
	 * @param min - the lowest number possible
	 * @param max - the highest number possible
	 * 
	 * @return a random number in the range
	 */
	public int randomNumber(int min, int max){
		if(max==min){
			return max;
		}
		int randNum = levelGenerationRandom.nextInt((max-min)+1) + min;
		return randNum;
	}
	/*
	 * updates various objects in the level
	 */
	public void update(){
		GamePanel.player.update();
	}
	/*
	 * Draws the level.
	 * 
	 * @param g - the graphics2D object to use for drawing to the GamePanel.
	 * 
	 * @return none.
	 */
	public void Draw(Graphics2D g){
		int viewDistanceX = ((ApplicationUI.windowWidth/32)/2)+2;
		int viewDistanceY = ((ApplicationUI.windowHeight/32)/2)+2;
		drawingLevel = true;
		for(int x = (int)(GamePanel.player.xpos/32)-(viewDistanceX); x<(int)(GamePanel.player.xpos/32)+(viewDistanceX);x++){
			for(int y = (int)(GamePanel.player.ypos/32)-(viewDistanceY); y<(int)(GamePanel.player.ypos/32)+(viewDistanceY);y++){
				if(x>=0&&y>=0&&x<width&&y<height)
					tileMap[x][y].Draw(g);
			}
		}
		GamePanel.player.Draw(g);
		drawingLevel = false;
		//update the player however many times it should have updated but couldn't because of the program 
		//being in the process of drawing.
		for(int i = 0; i<GamePanel.player.updatesInQue;i++){
			GamePanel.player.update();
		}
		GamePanel.player.updatesInQue=0;

	}
}
