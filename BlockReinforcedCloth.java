package net.minecraft.src;

public class BlockReinforcedCloth extends Block {

	public BlockReinforcedCloth(int i, int j) {
		super(i, j, Material.cloth);
	}
	
	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		world.setBlockMetadata(i, j, k, 0);
	}

	@Override
	public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if (world.getBlockMetadata(i, j, k) == 1)
			setHardness(-1);
		else
			setHardness(1.1F);
	}

}
