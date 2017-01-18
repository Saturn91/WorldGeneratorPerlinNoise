package pathfinding;

import java.awt.Point;
import java.util.ArrayList;

public class River_PathFinding {
	private ArrayList<Point> path;
	private ArrayList<Point> openPositions;
	private ArrayList<Point> closedPositions;
	private final Point up = new Point(0, 1);
	private final Point right = new Point(1, 0);
	private final Point down = new Point(-1, 0);
	private final Point left = new Point(-1, 0);
	
	/**
	 * returns the positions which are now river positions
	 * @param startPosition
	 * @return a list of all positions in the map which are now riverPositions or null (if not possible)
	 */
	public Point[] generateARiver(int[][] heightMapInt, int mapWidth, int mapHeight, Point startPosition, float waterHeight){
		Point[] riverBed = null;
		
		path = new ArrayList<>();
		openPositions = new ArrayList<>();
		closedPositions = new ArrayList<>();
		
		Point cursor = startPosition;
		Point checkPoint = new Point();
		int getStartHeight = heightMapInt[cursor.x][cursor.y];
		float[] directionsDownWeight = new float[4];
		boolean[] possibleDirection = new boolean[4];
		int[] lengthCounter = new int[4];
		for(int i = 0; i < 4; i++){
			checkPoint.x = cursor.x;
			checkPoint.y = cursor.y;
			while(heightMapInt[checkPoint.x][checkPoint.y] == getStartHeight &&
					checkPoint.x < mapWidth && checkPoint.y < mapHeight &&
					checkPoint.x >= 0 && checkPoint.y >= 0){
				possibleDirection[i] = true;
				checkPoint.x += getDirection(i).x;
				checkPoint.y += getDirection(i).y;
				lengthCounter[i]++;
			} 
			if(heightMapInt[checkPoint.x][checkPoint.y] > getStartHeight){
				possibleDirection[i] = false;
			}
		}
		int length = 0;
		int posibilities = 0;
		int lastPosibleIndex = -1;
		for(int i = 0; i < 4; i ++){
			if(possibleDirection[i]){
				posibilities ++;
				length += lengthCounter[i];
				lastPosibleIndex = i;
			}			
		}
		
		if(posibilities > 1){
			for(int i = 0; i < 4; i ++){
				if(possibleDirection[i]){
					directionsDownWeight[i] = (float) (length - lengthCounter[i])/(float) (length);
				}
			}
		}else{
			directionsDownWeight[getNextDirection(lastPosibleIndex, true)] = 0.1f;
			directionsDownWeight[getNextDirection(lastPosibleIndex, false)] = 0.1f;
			directionsDownWeight[lastPosibleIndex] = 0.9f;
			
		}		
		
		
		
		
		return riverBed;
	}
	
	private Point getDirection(int direction){
		switch(direction){
			case 0: return up;
			case 1: return right;
			case 2: return down;
			case 3: return left;
			default: return null;
		}
	}
	
	private int getNextDirection(int direction, boolean counterclockwhise){
		if(counterclockwhise){
			switch(direction){
				case 0: return 3;
				case 1: return 0;
				case 2: return 1;
				case 3: return 2;
				default: return -1;
			}
		}else{
			switch(direction){
				case 0: return 1;
				case 1: return 2;
				case 2: return 3;
				case 3: return 0;
				default: return -1;
			}
		}
		
	}
}
