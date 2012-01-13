package net.minecraft.src;

public class ItemSonicScrewdriver extends Item {

	protected ItemSonicScrewdriver(int i) {
		super(i);
		setMaxStackSize(1);
		setMaxDamage(mod_MoonDimension.screwUses);
	}

	public boolean canHarvestBlock(Block block) {
		return false;
	}

//	@Override
//	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
//		System.out.println("ufon");
//		world.entityJoinedWorld(new EntityMoonUfo(world, entityplayer));
//		return itemstack;
//	}
}
