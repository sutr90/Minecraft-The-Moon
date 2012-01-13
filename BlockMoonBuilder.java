package net.minecraft.src;

public class BlockMoonBuilder extends BlockChest {
	private int i, j, k;
	private int grX, grY, grZ;
	private int side1, face, side2;

	protected BlockMoonBuilder(int var1) {
		super(var1);
		i = j = k = 0;
		grX = grZ = 9;
		grY = 11;
		face = ModLoader.addOverride("/terrain.png", "/moon/bb0.png");
		side1 = ModLoader.addOverride("/terrain.png", "/moon/bb1.png");
		side2 = ModLoader.addOverride("/terrain.png", "/moon/bb2.png");
	}

	@Override
	public void onBlockPlaced(World var1, int var2, int var3, int var4, int var5) {
		var1.setBlockMetadataWithNotify(var2, var3, var4, 0);
	}

	@Override
	public boolean blockActivated(World var1, int var2, int var3, int var4,
			EntityPlayer var5) {

		// int dest, temp, lowest;
		// lowest = checkSpot(1, 1, var1, var2, var3, var4);
		// dest = 0;
		//
		// temp = checkSpot(1, -1, var1, var2, var3, var4);
		// if (temp < lowest) {
		// dest = 1;
		// lowest = temp;
		// }
		//
		// temp = checkSpot(-1, 1, var1, var2, var3, var4);
		// if (temp < lowest) {
		// dest = 2;
		// lowest = temp;
		// }
		//
		// temp = checkSpot(-1, -1, var1, var2, var3, var4);
		// if (temp < lowest) {
		// dest = 3;
		// lowest = temp;
		// }
		//
		// if (lowest > (grX * grZ * grY * grY)) {
		// var5.addChatMessage("Cannot build here");
		// return false;
		// }

		if (var1.getBlockMetadata(var2, var3, var4) != 0) {
			return super.blockActivated(var1, var2, var3, var4, var5);
		}

		int slot = var5.inventory.currentItem;
		ItemStack is = var5.inventory.mainInventory[slot];
		if (is == null || is.itemID != mod_MoonDimension.mScrewer.shiftedIndex)
			super.blockActivated(var1, var2, var3, var4, var5);
		else {
			if (!(is.getItemDamage() < mod_MoonDimension.screwUses))
				return false;

			is.damageItem(1, var5);

			if (var1.getBlockId(var2, var3, var4) != 0) {
				var5.addChatMessage("Cannot deploy Builder Bot.");
				return false;
			}

			EntityMoonBuilder builder = new EntityMoonBuilder(var1, var2, var3,
					var4, var5);
			var1.entityJoinedWorld(builder);
			var1.setBlockMetadata(var2, var3, var4, 1);
		}

		return true;
	}

	private int checkSpot(int dX, int dZ, World worldObj, int dockX, int dockY,
			int dockZ) {
		int score = 0, top, distSq;
		for (int x = 1; x <= 7; x += 2) {
			for (int z = 1; z <= 7; z += 2) {
				top = worldObj.findTopSolidBlock(dockX + (x * dX), dockZ
						+ (z * dZ));

				top = dockY - 1 - top;
				distSq = top * top;
				if (distSq > 100)
					score += 10000;
				else
					score += distSq;
			}
		}
		return score;
	}

	@Override
	public int getBlockTexture(IBlockAccess var1, int var2, int var3, int var4,
			int var5) {
		return this.getBlockTextureFromSide(var5);
	}

	@Override
	public int getBlockTextureFromSide(int var1) {
		if (var1 == 1) {
			return face;
		}
		if (var1 == 2 || var1 == 3) {
			return side2;
		}
		return side1;
	}
}
