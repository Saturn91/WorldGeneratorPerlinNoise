package com.saturn91.perlinNoise;

public class MapGenerator {
	
	private PerlinNoise noise;
	private float[][] map;
	private int octave = 5;
	private float[] heightLayers;	//this Array stores the number and height of the different hight-Layers
	
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
			return landLayers;
		}else{
			System.err.println("define heightLayers!");
			return null;
		}
		
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
