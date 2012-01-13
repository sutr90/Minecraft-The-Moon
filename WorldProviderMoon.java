package net.minecraft.src;

public class WorldProviderMoon extends WorldProvider {
	public WorldProviderMoon() {
	}

	public void registerWorldChunkManager() {
		BiomeGenBase biomeMoon = (mod_MoonDimension.moonBiome).setColor(
				0x000000).setBiomeName("Moon");
		worldChunkMgr = new WorldChunkManagerMoon(biomeMoon, 0.5D, 0.0D);
		worldType = mod_MoonDimension.dimNumber;
	}

	public IChunkProvider getChunkProvider() {
		return new ChunkProviderMoon(worldObj, worldObj.getRandomSeed());
	}

	public float getCloudHeight() {
		return -5F;
	}

	public float calculateCelestialAngle(long l, float f) {
		return 0.5f;
	}

	// Barva mlhy
	public Vec3D func_4096_a(float f, float f1) {
		return Vec3D.createVector(0D, 0D, 0D);
	}

	public boolean canCoordinateBeSpawn(int i, int j) {
		int k = worldObj.getFirstUncoveredBlock(i, j);
		if (k == 0) {
			return false;
		} else {
			return Block.blocksList[k].blockMaterial.getIsSolid();
		}
	}

	@Override
	public boolean canRespawnHere() {
		return false;
	}

	@Override
	protected void generateLightBrightnessTable() {
		float f = 0.1F;
		for (int i = 0; i <= 15; i++) {
			float f1 = 1.0F - (float) i / 15F;
			lightBrightnessTable[i] = ((1.0F - f1) / (f1 * 3F + 1.0F))
					* (1.0F - f) + f;
		}

	}

}
