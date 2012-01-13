package net.minecraft.src;

public class BlockMoonBlockAlu extends Block {

	protected BlockMoonBlockAlu(int var1) {
		super(var1, 0, Material.iron);
	}

	@Override
	public int colorMultiplier(IBlockAccess var1, int var2, int var3, int var4) {
		int meta = var1.getBlockMetadata(var2, var3, var4);

		if (meta == 0)
			return 0xe0e0ff;
		else
			return 0xff0000;
	}

	@Override
	public int getRenderColor(int var1) {
		return 0xe0e0ff;
	}
}
