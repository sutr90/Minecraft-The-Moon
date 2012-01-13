package net.minecraft.src;

public class BlockMoonCore extends Block {

	private int face, side1, side2, bottom;

	protected BlockMoonCore(int var1, int var2) {
		super(var1, var2, Material.wood);
		face = ModLoader.addOverride("/terrain.png", "/moon/sc1.png");
		side1 = ModLoader.addOverride("/terrain.png", "/moon/sc2.png");
		side2 = ModLoader.addOverride("/terrain.png", "/moon/sc3.png");
		bottom = ModLoader.addOverride("/terrain.png", "/moon/sc4.png");
	}

	@Override
	public int getBlockTextureFromSide(int var1) {
		if (var1 == 0) {
			return bottom;
		} else if (var1 == 1) {
			return face;
		}
		if (var1 == 2 || var1 == 3) {
			return side2;
		} else {
			return side1;
		}
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2,
			int var3, int var4) {
		return null;
	}

	// public void onEntityCollidedWithBlock(World var1, int var2, int var3,
	// int var4, Entity var5) {
	// if (var5.ridingEntity == null && var5.riddenByEntity == null) {
	// if (var5 instanceof SAPIEntityPlayerSP) {
	// SAPIEntityPlayerSP var6 = (SAPIEntityPlayerSP) var5;
	// var6.portal = this.getDimNumber();
	// }
	// if (mod_MoonDimension.travel != 1) {
	// mod_MoonDimension.travel = 1;
	// }
	//
	// mod_MoonDimension.coreX = var2;
	// mod_MoonDimension.coreY = var3;
	// mod_MoonDimension.coreZ = var4;
	//
	// var5.setInPortal();
	// }
	//
	// }

	@Override
	public boolean blockActivated(World var1, int var2, int var3, int var4,
			EntityPlayer var5) {
		if (var1.isBlockGettingPowered(var2, var3, var4)) {
			if (var5 instanceof SAPIEntityPlayerSP) {
				SAPIEntityPlayerSP var6 = (SAPIEntityPlayerSP) var5;
				var6.portal = this.getDimNumber();
			}
			if (mod_MoonDimension.travel != 1) {
				mod_MoonDimension.travel = 1;
			}

			mod_MoonDimension.coreX = var2;
			mod_MoonDimension.coreY = var3;
			mod_MoonDimension.coreZ = var4;
			DimensionBase.usePortal(getDimNumber());
		}

		return super.blockActivated(var1, var2, var3, var4, var5);
	}

	public int getDimNumber() {
		return mod_MoonDimension.dimNumber;
	}
}
