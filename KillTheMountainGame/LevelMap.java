package Game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class LevelMap {
	int xpos = 0;
	int ypos = 0;
	int pixelWidthPerTile = 4;
	BufferedImage mapImage;
	public LevelMap(Tile[][] map){
		mapImage = new BufferedImage( map.length*pixelWidthPerTile,map[0].length*pixelWidthPerTile, BufferedImage.TYPE_INT_ARGB);
		Graphics g = mapImage.getGraphics();
		for(int x = 0; x<map.length;x++){
			for(int y = 0; y<map[0].length;y++){
				g.drawImage(GamePanel.tiles[map[x][y].artX][map[x][y].artY], x*pixelWidthPerTile, y*pixelWidthPerTile, null);
			}
		}
		g.dispose();
	}
	public void Draw(Graphics g){
		g.drawImage(mapImage, xpos, ypos,mapImage.getWidth(), mapImage.getHeight(), null);
	}
}
