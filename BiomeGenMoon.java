package net.minecraft.src;

public class BiomeGenMoon extends BiomeGenBase {

	public BiomeGenMoon() {
		spawnableMonsterList.clear();
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableCreatureList.add(new SpawnListEntry(EntityMoonUfo.class, 30));
		
	}

	public int getSkyColorByTemp(float f) {
		return 0x000000;
	}
}
