package com.saturn91.perlinNoise;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import pathfinding.River_PathFinding;

public class MapGenerator {

	private PerlinNoise noise;
	private float[][] map;
	private int octave = 5;
	private float[] heightLayers;	//this Array stores the number and height of the different hight-Layers

	//River generation
	private final int selectionGridsize = 10;
	private final float riverStartHeight = 5f;

	/**
	 * returns a int[][] which divides the Land in discret hights
	 * @param seed
	 * @param width
	 * @param height
	 * @return discret hights of map
	 */
	public int[][] getRandomMap(long seed, int width, int height){
		if(heightLayers != null){
			noise = new PerlinNoise(width, height);
			map = noise.generateMap(octave, seed);
			
			
			
			int[][] landLayers = new int[width][height];
			for(int x = 0; x < width; x++){
				for(int y = 0; y < height; y++){
					for(int i = heightLayers.length-1; i >= 0; i--){
						if(map[x][y] > heightLayers[i]){
							landLayers[x][y] = i;
							break;
						}
					}
				}
			}
			
			//Debug!!!
			landLayers = generateRivers(landLayers, width, height, 50, seed);
			//\debug
			
			return landLayers;
		}else{
			System.err.println("define heightLayers!");
			return null;
		}

	}

	public int[][] generateRivers(int[][] heightMapInt, int mapWidth, int mapHeight, int numOfRivers, long seed){
		Random random = new Random(seed);

		//check Grid for possible Riverstartings
		ArrayList<Point> riverStartpositions = new ArrayList<>();
		for(int x = selectionGridsize; x < mapWidth; x +=selectionGridsize){
			for(int y = selectionGridsize; y < mapHeight; y +=selectionGridsize){
				if(heightMapInt[x][y] >= riverStartHeight && heightMapInt[x][y] <  (riverStartHeight + 1)){
					riverStartpositions.add(new Point(x, y));
					
				}
			}
		}
		
		System.out.println("found " + riverStartpositions.size() + " river startpositions");
		if(riverStartpositions.size() > 0){
			River_PathFinding riverPath = new River_PathFinding();
			for(int i = 0 ; i < numOfRivers; i++){
				int index = (int) (random.nextDouble()*riverStartpositions.size());
				heightMapInt = riverPath.generateARiver(seed, heightMapInt, mapWidth, mapHeight, riverStartpositions.get(index), heightLayers[1]);				
			}
		}else{
			System.out.println("generated 0 rivers!");
		}
		
		return heightMapInt;
	}


	/**
	 * defines how much layers the Perlinnoise loobs trough
	 */
	public void setOctave(int octave){
		this.octave = octave;
	}

	public void defineHeightLayersNum(int lenght){
		heightLayers = new float[lenght];
	}

	public void defineHeightLayer(int index, float height){
		if(height >= 0 && height < 1 && heightLayers != null && index < heightLayers.length && index >= 0){
			heightLayers[index] = height;
		}else{
			System.err.println("Not able to set " + index + " to " + height + " hight");
		}
	}
}
