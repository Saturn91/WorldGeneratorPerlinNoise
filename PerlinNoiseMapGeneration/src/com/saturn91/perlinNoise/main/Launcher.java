package com.saturn91.perlinNoise.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.saturn91.perlinNoise.PerlinNoise;

public class Launcher {
	private static float waterhight = 0.4f;
	
	public static void main(String[] args){
		int width = 7000;
		int height = 6000;
		int seed = (int) (Math.random()*Integer.MAX_VALUE);
		generateMap(width, height, seed);		
	}
	
	private static void generateMap(int width, int height, long seed){
		long startTime = System.currentTimeMillis();
		PerlinNoise noise = new PerlinNoise(width, height);
		float[][] baseNoise = noise.generateWhiteNoise(seed);
		float[][] perlinNoise = noise.GeneratePerlinNoise(baseNoise, 8);
		
		System.out.println("generated Map in: " + ((float) (System.currentTimeMillis()-startTime)/1000) + "s");
		startTime = System.currentTimeMillis();
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		
		int procent = 0;
		int oldpercent = -1;
		for(int x = 0; x < width; x++){
			procent = (int) ((float)x/ (float) width*100);
			if(oldpercent != procent){
				System.out.println("loaded: " + procent + "%");
				oldpercent = procent;
			}
			
			for(int y = 0; y < height; y++){
				int r = (int) (perlinNoise[x][y]*255.0f);
				int g = (int) (perlinNoise[x][y]*255.0f);
				int b = (int) (perlinNoise[x][y]*255.0f);
				if(perlinNoise[x][y] >= waterhight){
					r = 0;
					b = 0;
				}else{
					r = 0;
					g = 0;
				}
				
				int col = (r << 16) | (g << 8) | b;
				bufferedImage.setRGB(x, y, col);
			}
		}
		
		try {
		    File outputfile = new File("map" +  seed + "_" + waterhight + ".png");
		    ImageIO.write(bufferedImage, "png", outputfile);
		    System.out.println("painted Map in: " + ((float) (System.currentTimeMillis()-startTime)/1000) + "s");
		    System.out.println("savedMap"+ seed);
		} catch (IOException e) {
		    System.err.println("Failed to paint map!");
		}		
	}
}
