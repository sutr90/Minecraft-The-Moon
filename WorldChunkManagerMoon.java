package net.minecraft.src;

import java.util.Arrays;

public class WorldChunkManagerMoon extends WorldChunkManager {

	private BiomeGenBase biome;
	private double temp;
	private double humid;

	public WorldChunkManagerMoon(BiomeGenBase biom, double tem, double hum) {
		biome = biom;
		temp = tem;
		humid = hum;
	}

	public BiomeGenBase getBiomeGenAtChunkCoord(ChunkCoordIntPair chunkcoordintpair) {
		return biome;
	}

	public BiomeGenBase getBiomeGenAt(int i, int j) {
		return biome;
	}

	public double getTemperature(int i, int j) {
		return temp;
	}

	public double[] getTemperatures(double ad[], int i, int j, int k, int l) {
		if (ad == null || ad.length < k * l) {
			ad = new double[k * l];
		}
		Arrays.fill(ad, 0, k * l, temp);
		return ad;
	}

	public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase abiomegenbase[], int i, int j, int k,
			int l) {
		if (abiomegenbase == null || abiomegenbase.length < k * l) {
			abiomegenbase = new BiomeGenBase[k * l];
		}
		if (temperature == null || temperature.length < k * l) {
			temperature = new double[k * l];
			humidity = new double[k * l];
		}
		Arrays.fill(abiomegenbase, 0, k * l, biome);
		Arrays.fill(humidity, 0, k * l, humid);
		Arrays.fill(temperature, 0, k * l, temp);
		return abiomegenbase;
	}
}
