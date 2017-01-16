package com.saturn91.perlinNoise;

import java.util.Random;

/**
 * This Class is based on the Internetside:
 * http://devmag.org.za/2009/04/25/perlin-noise/
 * @author M.Geissbberger
 *
 */
public class PerlinNoise {
	private float[][] smoothNoiseOutput;
	
	private int width;
	private int height;
	
	public PerlinNoise(int width, int height) {
		this.width = width;
		this.height = height;
	}
	/**
	 * This Methode generates a float[width][height] with values 0 and 1
	 * @param width -> of map
	 * @param height -> of map
	 * @param seed -> to get same results again
	 * @return a white noise value 0 or 1 on each coordinate
	 */
	public float[][] generateWhiteNoise(long seed){
		Random random = new Random(seed); //Seed to 0 for testing
		float[][] noise = new float[width][height];

		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				noise[x][y] = (float)random.nextDouble() % 1;
			}
		}

		return noise;
	}
	
	/**
	 * 
	 * @param baseNoise
	 * @param octave
	 * @return
	 */
	public float[][][] GenerateSmoothNoise(float[][] baseNoise, int octaveCount){
		
		float[][][] smoothNoiseOutput = new float[octaveCount][width][height];
		
		for(int i = 0; i < octaveCount; i++){
			int samplePeriod = (int) Math.pow(2, i); // calculates 2 ^ octave
			float sampleFrequency = 1.0f / samplePeriod;

			for (int x = 0; x < width; x++)
			{
				//calculate the horizontal sampling indices
				int sample_x0 = (x / samplePeriod) * samplePeriod;
				int sample_x1 = (sample_x0 + samplePeriod) % width; //wrap around
				float horizontal_blend = (x - sample_x0) * sampleFrequency;

				for (int y = 0; y < height; y++)
				{
					//calculate the vertical sampling indices
					int sample_j0 = (y / samplePeriod) * samplePeriod;
					int sample_j1 = (sample_j0 + samplePeriod) % height; //wrap around
					float vertical_blend = (y - sample_j0) * sampleFrequency;

					//blend the top two corners
					float top = Interpolate(baseNoise[sample_x0][sample_j0],
							baseNoise[sample_x1][sample_j0], horizontal_blend);

					//blend the bottom two corners
					float bottom = Interpolate(baseNoise[sample_x0][sample_j1],
							baseNoise[sample_x1][sample_j1], horizontal_blend);

					//final blend
					try {
						smoothNoiseOutput[i][x][y] = Interpolate(top, bottom, vertical_blend);
					} catch (Exception e) {
						System.out.println("x:" + x + "y:" + y + ":" + top + ":" + bottom + ":" + vertical_blend);
						e.printStackTrace();
						System.exit(0);
					}

				}
			}
		}

		return smoothNoiseOutput;
	}

	/**
	 * Linear Interpolation between value x0 and x1
	 * @param x0
	 * @param x1
	 * @param alpha
	 * @return
	 */	
	private float Interpolate(float x0, float x1, float alpha)
	{
		return x0 * (1 - alpha) + alpha * x1;
	}


	/**
	 * Pernal Noise Generator
	 * @param baseNoise
	 * @param octaveCount
	 * @return
	 */
	public float[][] GeneratePerlinNoise(float[][] baseNoise, int octaveCount)
	{

		float[][][] smoothNoise = GenerateSmoothNoise(baseNoise, octaveCount);

		float persistance = 0.5f;

		float[][] perlinNoise = new float[width][height];
		float amplitude = 1.0f;
		float totalAmplitude = 0.0f;

		//blend noise together
		for (int octave = octaveCount - 1; octave >= 0; octave--)
		{
			amplitude *= persistance;
			totalAmplitude += amplitude;

			for (int x = 0; x < width; x++)
			{
				for (int y = 0; y < height; y++)
				{
					perlinNoise[x][y] += smoothNoise[octave][x][y] * amplitude;
				}
			}
		}

		//normalisation
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				perlinNoise[x][y] /= totalAmplitude;
			}
		}

		return perlinNoise;
	}
}
