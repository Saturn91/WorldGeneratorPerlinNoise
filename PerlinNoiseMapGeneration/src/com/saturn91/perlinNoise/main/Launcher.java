package com.saturn91.perlinNoise.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.saturn91.perlinNoise.MapGenerator;
import com.saturn91.perlinNoise.PerlinNoise;

public class Launcher {
	
	public static void main(String[] args){
		int width = 1000;
		int height = 1000;
		
		for(int i = 0; i < 2; i++){
			int seed = (int) (Math.random()*Integer.MAX_VALUE);
			generateMap(width, height, 9, seed);	
		}
			
	}
	
	private static void generateMap(int width, int height, int octave, long seed){
		long startTime = System.currentTimeMillis();
		MapGenerator generator = new MapGenerator();
		
		generator.setOctave(octave);
		generator.defineHeightLayersNum(7);
		generator.defineHeightLayer(0, 0.00f);
		generator.defineHeightLayer(1, 0.43f);
		generator.defineHeightLayer(2, 0.45f);
		generator.defineHeightLayer(3, 0.55f);
		generator.defineHeightLayer(4, 0.65f);
		generator.defineHeightLayer(5, 0.70f);
		generator.defineHeightLayer(6, 0.80f);
		int[][] map = generator.getRandomMap(seed, width, height);
		
		System.out.println("generated Map in: " + ((float) (System.currentTimeMillis()-startTime)/1000) + "s");
		startTime = System.currentTimeMillis();
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		
		//debug
		int countRiverTiles = 0;
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				int r = 0;
				int g = 0;
				int b = 0;
				
				if(map[x][y] == -1){
					countRiverTiles++;
					r = 255;
					g = 0;
					b = 255;
				}
				
				if(map[x][y] == 0){
					r = 0;
					g = 0;
					b = 255;
				}
				
				if(map[x][y] == 1){
					r = 80;
					g = 80;
					b = 255;
				}
				
				if(map[x][y] == 2){
					r = 80;
					g = 200;
					b = 80;
				}
				
				if(map[x][y] == 3){
					r = 70;
					g = 180;
					b = 70;
				}
				
				if(map[x][y] == 4){
					r = 60;
					g = 160;
					b = 60;
				}
				
				if(map[x][y] == 5){
					r = 120;
					g = 120;
					b = 120;
				}
				
				if(map[x][y] == 6){
					r = 255;
					g = 255;
					b = 255;
				}
				
				
				int col = (r << 16) | (g << 8) | b;
				bufferedImage.setRGB(x, y, col);
			}
			
			
		}
		
		System.out.println("generated " + countRiverTiles + " riverTiles");
		
		try {
		    File outputfile = new File("map" +  seed + ".png");
		    ImageIO.write(bufferedImage, "png", outputfile);
		    System.out.println("painted Map in: " + ((float) (System.currentTimeMillis()-startTime)/1000) + "s");
		    System.out.println("savedMap"+ seed);
		} catch (IOException e) {
		    System.err.println("Failed to paint map!");
		}		
	}
}
