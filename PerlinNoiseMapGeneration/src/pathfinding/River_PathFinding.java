package pathfinding;

import java.awt.Point;
import java.util.ArrayList;

public class River_PathFinding {
	private ArrayList<Point> path;
	private ArrayList<Point> openPositions = new ArrayList<>();
	
	/**
	 * returns the positions which are now river positions
	 * @param startPosition
	 * @return a list of all positions in the map which are now riverPositions or null (if not possible)
	 */
	public Point[] generateARiver(float[][] heightMap, int mapWidth, int mapHeight, Point startPosition, float waterHeight){
		Point cursor = startPosition;
		path = new ArrayList<>();
		//search until you are in water or there's no other deeper field under cursor
		Point[] checkPositions = new Point[4];
		Point lastPosition = null;
		for(int i = 0; i < 4; i++){
			checkPositions[i] = new Point();
		}
		while(heightMap[cursor.x][cursor.y] > waterHeight){
			path.add(cursor);
			//check positions around Cursor for deepest
			if(cursor.x < mapWidth -1 && cursor.y < mapHeight -1 && cursor.x > 1 && cursor.y > 1){
				checkPositions[0].setLocation(cursor.x-1, cursor.y);	//links
				checkPositions[1].setLocation(cursor.x, cursor.y+1);	//oben
				checkPositions[2].setLocation(cursor.x+1, cursor.y);	//rechts
				checkPositions[3].setLocation(cursor.x, cursor.y-1);	//unten
			}
			int forbiddenIndex = -1;
			if(lastPosition != null){
				for(int i = 0; i < 4; i ++){
					if(checkPositions[i].x == lastPosition.x && checkPositions[i].y == lastPosition.y){
						forbiddenIndex = i;
					}
				}
			}
			
			
			
			int smallestIndex = -1;
			int smallestPoint = 0;
			
			for(int i = 0; i < 4; i++){
				if(i != forbiddenIndex){
					if(heightMap[checkPositions[i].x][checkPositions[i].y] < heightMap[checkPositions[smallestPoint].x][checkPositions[smallestPoint].y]){
						smallestPoint = i;
					}
					if(heightMap[checkPositions[i].x][checkPositions[i].y] < heightMap[cursor.x][cursor.y]){
						smallestIndex = i;
					}
				}				
			}
			
			if(smallestIndex != -1){
				cursor = checkPositions[smallestIndex];
				lastPosition = cursor;
			}else{
				//cursor = checkPositions[smallestPoint];
				break;
			}
			
			if(path.size() > 500){
				break;
			}
			
		}
		Point[] riverBed = null;
		if(path.size() > 0){
			riverBed = new Point[path.size()];
			for(int i = 0; i < path.size(); i++){
				riverBed[i] = path.get(i);
			}
			System.out.println("generated River with lenght: " + path.size());
		}else{
			return null;
		}
		
		return riverBed;
	}
}
