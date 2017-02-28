package pathfinding;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class River_PathFinding {
	private ArrayList<Point> path;
	private ArrayList<Point> openPositions;
	private ArrayList<Point> closedPositions;
	private final Point up = new Point(0, 1);
	private final Point right = new Point(1, 0);
	private final Point down = new Point(-1, 0);
	private final Point left = new Point(-1, 0);
	private Random random;
	
	/**
	 * returns the positions which are now river positions
	 * @param startPosition
	 * @return a list of all positions in the map which are now riverPositions or null (if not possible)
	 */
	public int[][] generateARiver(long seed, int[][] heightMapInt, int mapWidth, int mapHeight, Point startPosition, float waterHeight){
		
		path = new ArrayList<>();
		openPositions = new ArrayList<>();
		closedPositions = new ArrayList<>();
		
		Point cursor = startPosition;
		Point checkPoint = new Point();
		
		//*******************Check crosswhise in which directions its possible to go down ***************************
		int getStartHeight = heightMapInt[cursor.x][cursor.y];
		float[] directionsDownWeight = new float[4];
		boolean[] possibleDirection = new boolean[4];
		int[] lengthCounter = new int[4];
		for(int i = 0; i < 4; i++){
			checkPoint.x = cursor.x;
			checkPoint.y = cursor.y;
			if(checkPoint.x < mapWidth && checkPoint.y < mapHeight &&
					checkPoint.x >= 0 && checkPoint.y >= 0){
				while(heightMapInt[checkPoint.x][checkPoint.y] == getStartHeight) {
					possibleDirection[i] = true;
					if(checkPoint.x + getDirection(i).x < mapWidth && checkPoint.y + getDirection(i).y < mapHeight && checkPoint.x + getDirection(i).x >= 0 && checkPoint.y + getDirection(i).y >= 0){
						checkPoint.x += getDirection(i).x;
						checkPoint.y += getDirection(i).y;
						lengthCounter[i]++;
					}else{
						break;
					}
					
				} 
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
		
		//***************Calculate the vector in which direction the slowest Layer is mostlikely to found********
		if(posibilities > 1){
			for(int i = 0; i < 4; i ++){
				if(possibleDirection[i]){
					directionsDownWeight[i] = (float) (length - lengthCounter[i])/(float) (length);
				}
			}
		}else{
			if(lastPosibleIndex != -1){
				directionsDownWeight[getNextDirection(lastPosibleIndex, true)] = 0.1f;
				directionsDownWeight[getNextDirection(lastPosibleIndex, false)] = 0.1f;
				directionsDownWeight[lastPosibleIndex] = 0.8f;
				return heightMapInt;
			}
		}	
		
		//****************Generate Riverbed***********************************************************************
		//1 take startPoint and go aslong in the weighted direction until you find a deeper point
		
		cursor = startPosition;
		random = new Random(seed);
		int lastPoint = -1;
		int counter = 0;
		while(heightMapInt[cursor.x][cursor.y] == getStartHeight && counter < 100){
			heightMapInt[cursor.x][cursor.y] = -1;
			int direction = getWeightedRandomDirection(directionsDownWeight, random, lastPoint);
			if(cursor.x + getDirection(direction).x < mapWidth && cursor.y + getDirection(direction).y < mapHeight && cursor.x >= 0 && cursor.y >= 0){
				cursor.x += getDirection(direction).x;
				cursor.y += getDirection(direction).y;
				lastPoint = getNextDirection(getNextDirection(direction, false), false);
			}else{
				break;
			}
			
		}		
		
		if(counter >= 100){
			System.err.println("Not finished!");
		}
		
		return heightMapInt;
	}
	
	/**
	 * returns a random direction, but its semi random, it depends on weights for the different values
	 * @param weights[4] the different weights for each direction
	 * @param weightSum all weights from weights[] added up
	 * @param random
	 * @param lastPoint -> last direction can't be choosen now
	 * @return direction 0: up, 1: right; 2: down, 3: left
	 */
	private int getWeightedRandomDirection(float[] weights, Random random, int lastPoint){
		float value = (float) random.nextDouble();
		float valueSum = 0;
		for(int i = 0; i < 4; i++){
			
			if(lastPoint != i){
				valueSum += weights[i];
			}
			
			if (value <= valueSum){
				return i;
			}
		}
		
		return 3;
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
