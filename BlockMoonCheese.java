package net.minecraft.src;

import java.util.Random;

public class BlockMoonCheese extends Block {

	protected BlockMoonCheese(int var1, int var2) {
		super(var1, var2, Material.ground);
	}

	@Override
	public boolean blockActivated(World var1, int var2, int var3, int var4,
			EntityPlayer ep) {

		int slot = ep.inventory.currentItem;
		ItemStack is = ep.inventory.mainInventory[slot];
		if (is == null || is.itemID != mod_MoonDimension.mScrewer.shiftedIndex)
			return false;

		if (!(is.getItemDamage() < mod_MoonDimension.screwUses))
			return false;

		is.damageItem(1, ep);

		return mod_MoonDimension.mPort
				.tryToCreatePortal(var1, var2, var3, var4);
	}

	@Override
	public int idDropped(int var1, Random var2) {
		return mod_MoonDimension.mCheeseSlice.shiftedIndex;
	}

	@Override
	public int quantityDropped(Random var1) {
		return 4;
	}
}
