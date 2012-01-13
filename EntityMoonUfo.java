// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            EntityCreature, World, Block, BlockGrass, 
//            MathHelper, AxisAlignedBB, NBTTagCompound

public class EntityMoonUfo extends EntityAnimal {

	public EntityMoonUfo(World world) {
		super(world);
		texture = "/moon/u.png";
	}

	protected float getBlockPathWeight(int i, int j, int k) {
		return 0.5F - worldObj.getLightBrightness(i, j, k);
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
	}

	public boolean getCanSpawnHere() {
		super.getCanSpawnHere();
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(boundingBox.minY);
		int k = MathHelper.floor_double(posZ);
		boolean bbox = worldObj.checkIfAABBIsClear(boundingBox);
		boolean col = worldObj.getCollidingBoundingBoxes(this, boundingBox)
				.size() == 0;
		boolean liq = !worldObj.getIsAnyLiquid(boundingBox);
		boolean bpw = getBlockPathWeight(i, j, k) >= 0.0F;
		return bbox && col && liq && bpw;
	}

	protected String getLivingSound() {
		return null;
	}

	protected String getHurtSound() {
		return null;
	}

	protected String getDeathSound() {
		return null;
	}

	protected float getSoundVolume() {
		return 0.4F;
	}

	protected int getDropItemId() {
		Random rand = new Random();
		if (rand.nextInt(8) == 0)
			return Item.redstone.shiftedIndex;

		return 0;
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 4;
	}
}
