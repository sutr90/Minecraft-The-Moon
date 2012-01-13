package net.minecraft.src;

import java.util.Random;

public class ChunkProviderMoon implements IChunkProvider {

	private MapGenBase caveGen;
	private Random moonRand;
	private World worldObj;
	private int param = 512;
	private float f = 3;
	private int fill = Block.stone.blockID,
			top = mod_MoonDimension.mRegolithID;

	/**
	 * Block coordniates. Start of chunk.
	 */
	private int chunkX, chunkZ;

	public ChunkProviderMoon(World worldObj, long randomSeed) {
		caveGen = new MapGenCaves();
		this.worldObj = worldObj;
		moonRand = new Random(randomSeed);
		MoonPerlin.initGenerator(randomSeed);
	}

	@Override
	public boolean chunkExists(int i, int j) {
		return true;
	}

	public Chunk provideChunk(int i, int j) {
		moonRand.setSeed(i * 341873128712L + j * 132897987541L);
		byte[] abyte0 = new byte[32768];
		Chunk chunk = new Chunk(worldObj, abyte0, i, j);
		generateTerrain(i, j, abyte0);
		caveGen.generate(this, worldObj, i, j, abyte0);
		chunk.generateSkylightMap();
		return chunk;
	}

	@Override
	public Chunk prepareChunk(int i, int j) {
		return provideChunk(i, j);
	}

	@Override
	public void populate(IChunkProvider ichunkprovider, int i, int j) {
		// TODO Pøidat featury - ruda, láva, voda...
		int chunkStartX = i * 16;
		int chunkStartZ = j * 16;
		int tmp, x, y, z;

		for (tmp = 0; tmp < 5; ++tmp) {
			x = chunkStartX + this.moonRand.nextInt(16);
			y = this.moonRand.nextInt(30);
			z = chunkStartZ + this.moonRand.nextInt(16);
			(new WorldGenMinable(Block.oreIron.blockID, 4)).generate(
					this.worldObj, this.moonRand, x, y, z);
		}

		for (tmp = 0; tmp < 2; ++tmp) {
			x = chunkStartX + this.moonRand.nextInt(16);
			y = this.moonRand.nextInt(30);
			z = chunkStartZ + this.moonRand.nextInt(16);
			(new WorldGenMinable(Block.oreGold.blockID, 4)).generate(
					this.worldObj, this.moonRand, x, y, z);
		}

		for (tmp = 0; tmp < 2; ++tmp) {
			x = chunkStartX + this.moonRand.nextInt(16);
			y = this.moonRand.nextInt(32);
			z = chunkStartZ + this.moonRand.nextInt(16);
			(new WorldGenMinable(mod_MoonDimension.mCheeseID, 8)).generate(
					this.worldObj, this.moonRand, x, y, z);
		}

		for (tmp = 0; tmp < 20; ++tmp) {
			x = i + this.moonRand.nextInt(16);
			y = this.moonRand.nextInt(128);
			z = j + this.moonRand.nextInt(16);
			(new WorldGenMinable(mod_MoonDimension.mRegolithID, 32)).generate(
					this.worldObj, this.moonRand, x, y, z);
		}
	}

	@Override
	public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
		return true;
	}

	@Override
	public boolean unload100OldestChunks() {
		return false;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public String makeString() {
		return "MoonRandomLevelSource";
	}

	public void generateTerrain(int i, int j, byte abyte0[]) {
		int surface, surfaceDust, xx, zz;

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {

				xx = x + (16 * i);
				zz = z + (16 * j);

				surface = (int) Math.abs(MoonPerlin.Noise(f * xx / param, f
						* zz / param, 0) * 15) + 60;
				surfaceDust = surface - (2 + moonRand.nextInt(5));

				for (int y = 127; y >= 0; y--) {
					int l1 = getIndex(x, y, z);

					if (y <= 0 + moonRand.nextInt(3)) {
						abyte0[l1] = (byte) Block.bedrock.blockID;
						continue;
					}
					if (y <= surfaceDust) {
						abyte0[l1] = (byte) fill;
						continue;
					}
					if (y <= surface) {
						abyte0[l1] = (byte) top;
						continue;
					}
				}
			}
		}
		computeCraters(i, j, abyte0);
	}

	/**
	 * Když jsou obì souøadnice chunku sudý, nachází se na nìm støed. Ze
	 * souøadnic støedu spoèítám náhodný èíslo <-1;1>. Náhodný èíslo použiju na
	 * propoèet hloubky a prùmìru kráteru a posun støedu. Potom vytvoøim
	 * krátery.
	 * 
	 * když jsou obì souø. sudý. jsem na støedu brnkaèka.
	 * 
	 * když je sudý x a z lichý poèítám: z+1,x; z-1,x;z+1,x+2; z+2,x-1; z+1,x-2;
	 * z-1,x-2;
	 * 
	 * když je sudý z a x lichý poèítám: z,x+1; z,x-1;z+2,x+1; z-1,x+2; z-2,x+1;
	 * z-2,x-1;
	 * 
	 * když lichý obì poèítám: z+1,x+1;z+1,x-1;z-1,x+1;z-1;x-1
	 */
	private void computeCraters(int x, int z, byte[] chunkArr) {
		// sudý x a z
		boolean evenX, evenZ;
		evenX = x % 2 == 0 ? true : false;
		evenZ = z % 2 == 0 ? true : false;
		chunkX = x * 16;
		chunkZ = z * 16;

		if (evenX && evenZ) {
			createCrater(x, z, chunkArr);
		} else if (evenX) {
			createCrater(x, z + 1, chunkArr);
			createCrater(x, z - 1, chunkArr);
			createCrater(x + 2, z + 1, chunkArr);
			createCrater(x + 2, z - 1, chunkArr);
			createCrater(x - 2, z + 1, chunkArr);
			createCrater(x - 2, z - 1, chunkArr);
		} else if (evenZ) {
			createCrater(x + 1, z, chunkArr);
			createCrater(x - 1, z, chunkArr);
			createCrater(x + 1, z + 2, chunkArr);
			createCrater(x - 1, z + 2, chunkArr);
			createCrater(x + 1, z + 2, chunkArr);
			createCrater(x - 1, z + 2, chunkArr);
		} else {
			createCrater(x + 1, z + 1, chunkArr);
			createCrater(x - 1, z + 1, chunkArr);
			createCrater(x + 1, z - 1, chunkArr);
			createCrater(x - 1, z - 1, chunkArr);
		}

	}

	private void createCrater(int chnkX, int chnkZ, byte[] chunkArr) {

		double centerRand = randFromPoint(chnkX, chnkZ);
		int maxCenterDelta = 6;
		int centerX, centerZ, radius;

		centerX = chnkX * 16 + 8 + (int) (maxCenterDelta * centerRand);
		centerZ = chnkZ * 16 + 8 + (int) (maxCenterDelta * centerRand);
		radius = (int) ((centerRand + 1) * 8) + 8;

		int distance, sphereY = 0, index = 0;
		boolean inSphere;
		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {
				distance = -1 * centerX * centerX + 2 * (x + chunkX) * centerX
						- centerZ * centerZ + 2 * centerZ * (z + chunkZ)
						+ radius * radius - (x + chunkX) * (x + chunkX)
						- (z + chunkZ) * (z + chunkZ);

				if (distance > 0) {
					sphereY = (int) (Math.sqrt(distance) / 2.5);
				} else {
					continue;
				}

				inSphere = false;

				for (int y = 90; y > 0; y--) {
					index = getIndex(x, y, z);

					if (sphereY == 0) {
						break;
					}

					if (inSphere) {
						chunkArr[index] = 0;
						sphereY--;
						continue;
					}

					if (chunkArr[index] == 0) {
						continue;
					} else {
						y++;
						inSphere = true;
					}
				}

				if (moonRand.nextDouble() < 0.8 && inSphere)
					chunkArr[index] = (byte) top;

			}
		}

	}

	private int getIndex(int x, int y, int z) {
		return (x * 16 + z) * 128 + y;
	}

	/**
	 * Returns pseudo random value for given point.
	 */
	private double randFromPoint(int x, int z) {
		int n;
		n = x + z * 57;
		n = (n << 13) ^ n;
		return (1.0 - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0);
	}

}
