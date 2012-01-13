package net.minecraft.src;

public class DimensionMoon extends DimensionBase {

	public DimensionMoon() {
		super(mod_MoonDimension.dimNumber, WorldProviderMoon.class, TeleporterMoon.class);
	}

}
