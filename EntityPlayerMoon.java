package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class EntityPlayerMoon extends SAPIEntityPlayerSP {

	public int moonAir, maxAir;

	public EntityPlayerMoon(Minecraft minecraft, World world, Session session,
			int i) {
		super(minecraft, world, session, i);
		maxAir = 100;
		moonAir = maxAir;
	}

	@Override
	public void moveEntityWithHeading(float f, float f1) {
		float f2 = 0.91F;
		if (onGround) {
			f2 = 0.5460001F;
			int i = worldObj.getBlockId(MathHelper.floor_double(posX),
					MathHelper.floor_double(boundingBox.minY) - 1,
					MathHelper.floor_double(posZ));
			if (i > 0) {
				f2 = Block.blocksList[i].slipperiness * 0.91F;
			}
		}
		float f3 = 0.1627714F / (f2 * f2 * f2);
		moveFlying(f, f1, onGround ? 0.1F * f3 : 0.02F);
		f2 = 0.91F;
		if (onGround) {
			f2 = 0.5460001F;
			int j = worldObj.getBlockId(MathHelper.floor_double(posX),
					MathHelper.floor_double(boundingBox.minY) - 1,
					MathHelper.floor_double(posZ));
			if (j > 0) {
				f2 = Block.blocksList[j].slipperiness * 0.91F;
			}
		}
		if (isOnLadder()) {
			float f4 = 0.15F;
			if (motionX < (double) (-f4)) {
				motionX = -f4;
			}
			if (motionX > (double) f4) {
				motionX = f4;
			}
			if (motionZ < (double) (-f4)) {
				motionZ = -f4;
			}
			if (motionZ > (double) f4) {
				motionZ = f4;
			}
			fallDistance = 0.0F;
			if (motionY < -0.14999999999999999D) {
				motionY = -0.14999999999999999D;
			}
			if (isSneaking() && motionY < 0.0D) {
				motionY = 0.0D;
			}
		}
		moveEntity(motionX, motionY, motionZ);
		if (isCollidedHorizontally && isOnLadder()) {
			motionY = 0.20000000000000001D;
		}
		motionY -= 0.021D; // 0.08
		motionY *= 0.98000001907348633D;
		motionX *= f2;
		motionZ *= f2;

		field_705_Q = field_704_R;
		double d2 = posX - prevPosX;
		double d3 = posZ - prevPosZ;
		float f5 = MathHelper.sqrt_double(d2 * d2 + d3 * d3) * 4F;
		if (f5 > 1.0F) {
			f5 = 1.0F;
		}
		field_704_R += (f5 - field_704_R) * 0.4F;
		field_703_S += field_704_R;
	}

	protected void fall(float f) {
		if (f >= 2.0F) {
			addStat(StatList.distanceFallenStat,
					(int) Math.round((double) f * 100D));
		}
		int i = (int) Math.ceil(f - 6F);
		if (i > 0) {
			attackEntityFrom(null, i);
			int j = worldObj.getBlockId(
					MathHelper.floor_double(posX),
					MathHelper.floor_double(posY - 0.20000000298023224D
							- (double) yOffset), MathHelper.floor_double(posZ));
			if (j > 0) {
				StepSound stepsound = Block.blocksList[j].stepSound;
				worldObj.playSoundAtEntity(this, stepsound.func_1145_d(),
						stepsound.getVolume() * 0.5F,
						stepsound.getPitch() * 0.75F);
			}
		}
	}

	@Override
	protected void jump() {
		super.jump();
		if (isSneaking()) {
			motionY = 0.22D;
		} else {
			motionY = 0.41999998688697815D;
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (mod_MoonDimension.inTent(worldObj, MathHelper.floor_double(posX),
				MathHelper.floor_double(posY), MathHelper.floor_double(posZ))) {
			moonAir = maxAir;
		}

		ItemStack boots = inventory.armorInventory[0];
		ItemStack legs = inventory.armorInventory[1];
		ItemStack chest = inventory.armorInventory[2];
		ItemStack helm = inventory.armorInventory[3];
		if (!(boots == null || legs == null || chest == null || helm == null)) {
			if (boots.itemID == mod_MoonDimension.mBoots.shiftedIndex
					&& legs.itemID == mod_MoonDimension.mPants.shiftedIndex
					&& chest.itemID == mod_MoonDimension.mJacket.shiftedIndex
					&& helm.itemID == mod_MoonDimension.mHelm.shiftedIndex) {
				moonAir = maxAir;
			}
		}

		moonAir--;
		air = (int) ((300.0 / maxAir) * moonAir);

		if (moonAir == -5) {
			moonAir = 0;
			for (int i = 0; i < 8; i++)
				attackEntityFrom(null, 2);
		}
	}

	@Override
	public void onEntityDeath() {
		mod_MoonDimension
				.changeMoonTexture(0, ModLoader.getMinecraftInstance());

		worldObj.setBlock(mod_MoonDimension.coreX, mod_MoonDimension.coreY,
				mod_MoonDimension.coreZ, 0);
	}
}
