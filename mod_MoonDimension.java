package net.minecraft.src;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class mod_MoonDimension extends BaseMod implements IInterceptBlockSet {
	private static int moonID = -1;
	private static int earthID = -1;
	private static HashMap<String, Integer> map;
	public static int travel = -1;

	public static BlockMoonPortal mPort;
	public static Block mLamp;
	public static Block mTent;
	public static Block mSuperCloth;
	public static Block mCheese;
	public static Block mRobot;
	public static Block mRegolith;
	public static Block mBauxit;
	public static Block mSCore;
	public static Block mBAlu;

	public static int mLampID;
	public static int mClothID;
	public static int mTentID;
	public static int mCheeseID;
	public static int mPortID;
	public static int mRobotID;
	public static int mRegolithID;
	public static int mBauxitID;
	public static int mSCoreID;
	public static int mBAluID;

	public static int mJacketID;
	public static int mBootsID;
	public static int mHelmID;
	public static int mPantsID;
	public static int mScrewerID;
	public static int mCheeseSliceID;
	public static int mIngotAluID;

	public static int screwUses = 30;
	/*
	 * GhostoftheChatroom
	 */

	/*
	 * This tells the game that we haves new armor item with the name
	 * emeraldHelm, which has an ID of 3000 and a damage amount of 1. 0 =
	 * leather, 1 = chain mail and gold, 2 = iron, 3 = diamond. 0=helmet,
	 * 1=chestplate, 2=legs, 3=boots.
	 */
	public static Item mHelm;
	public static Item mJacket;
	public static Item mPants;
	public static Item mBoots;
	public static Item mScrewer;
	public static Item mCheeseSlice;
	public static Item mIngotAlu;

	public static int dimNumber = 2;

	public static BiomeGenBase moonBiome;
	private static World world;
	public static int coreX, coreY, coreZ;
	public static boolean destroyed = false;

	public mod_MoonDimension() {
		new DimensionMoon().name = "Moon dimension";

		ModLoader.SetInGameHook(this, true, false);

		SAPI.interceptAdd(this);

		moonBiome = new BiomeGenMoon();
		try {
			ModLoader.setPrivateValue(BiomeGenBase.class, moonBiome, "w",
					Boolean.valueOf(false));
		} catch (Exception e) {
			System.out
					.println("Forgot to update reflection. Trying MCP name for disabling rain.");
			try {
				ModLoader.setPrivateValue(BiomeGenBase.class, moonBiome,
						"enableRain", Boolean.valueOf(false));
			} catch (Exception e2) {
			}
		}
	}

	@Override
	public void ModsLoaded() {
		mLampID = ID("lamp");
		mClothID = ID("cloth");
		mTentID = ID("tent");
		mCheeseID = ID("cheese");
		mPortID = ID("port");
		mRobotID = ID("robot");
		mRegolithID = ID("regolith");
		mBauxitID = ID("bauxit");
		mSCoreID = ID("score");
		mBAluID = ID("alublock");

		mJacketID = ID("jacket");
		mBootsID = ID("boots");
		mHelmID = ID("helm");
		mPantsID = ID("pants");
		mScrewerID = ID("screwer");
		mCheeseSliceID = ID("slice");
		mIngotAluID = ID("ingotAlu");

		initMoon();
	}

	private static int ID(String s) {
		try {
			String x;
			Properties p = new Properties();

			String loca = (new StringBuilder())
					.append(Minecraft.getMinecraftDir()).append("/moon.ini")
					.toString();

			p.load(new FileInputStream(loca));
			x = p.getProperty(s);
			return Integer.parseInt(x);
		} catch (Exception e) {
			System.out.println(e);
		}

		return -10;
	}

	private static void destroyShip(World world) {
		System.out.println("nicim");

		coreX = coreX - 4;
		coreY = coreY - 6;
		coreZ = coreZ - 4;
		for (int k = 0; k < 11; k++) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					world.setBlock(i + coreX, k + coreY, j + coreZ, 0);
				}
			}
		}

	}

	@Override
	public boolean OnTickInGame(Minecraft game) {
		// if (game.thePlayer.inWater) {
		// game.thePlayer.air = game.thePlayer.maxAir;
		// }

		if (game.thePlayer.timeInPortal > 0.999999F
				&& game.thePlayer.timeInPortal < 1.0F && travel == 1
				&& !destroyed) {

			System.out.println(game.thePlayer.timeInPortal);
			destroyShip(game.theWorld);
			destroyed = true;
		}

		if (game.thePlayer.timeInPortal < 0.5F)
			destroyed = false;

		DrawOverlay(game, "/gui/icons.png");

		if ((world != game.theWorld)
				|| ((game.thePlayer != null) && ((game.thePlayer.isDead) || (game.thePlayer.health <= 0)))) {
			world = game.theWorld;
			return true;
		}
		if (world == null)
			return true;

		if ((!world.multiplayerWorld) && (game.thePlayer != null)) {
			if (world.worldProvider.worldType == dimNumber
					&& game.thePlayer instanceof EntityPlayerMoon)
				return true;

			if (world.worldProvider.worldType != dimNumber
					&& !(game.thePlayer instanceof EntityPlayerMoon))
				return true;

			List<?> playerList = game.theWorld.playerEntities;
			int dimension = world.worldProvider.worldType;
			NBTTagCompound tag = new NBTTagCompound();
			game.thePlayer.writeToNBT(tag);
			game.thePlayer.setEntityDead();
			for (int i = 0; i < playerList.size(); i++) {
				if (playerList.get(i).equals(game.thePlayer))
					playerList.remove(i);
			}
			game.thePlayer = null;
			EntityPlayerSP player = changePlayerEntity(dimension, game);
			player.movementInput = new MovementInputFromOptions(
					game.gameSettings);
			player.readFromNBT(tag);
			player.dimension = dimension;
			game.thePlayer = player;
			game.changeWorld(game.theWorld, " ", player);
			game.theWorld.playerEntities = playerList;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	public void AddRenderer(Map map) {
		map.put(EntityMoonUfo.class,
				new RenderMoonUfo(new ModelMoonUfo(), 0.5F));
		map.put(EntityMoonBuilder.class, new RenderBuilderBot(
				new ModelBuilderBot(), 0.5F));
	}

	@SuppressWarnings("unchecked")
	private static void initMoon() {
		ToolBase.Pickaxe.mineMaterials.add(Material.rock);
		ToolBase.Shovel.mineBlocks.add(new BlockHarvestPower(mBauxitID, 60.0F));

		/*
		 * Entities
		 */
		ModLoader.RegisterEntityID(EntityMoonUfo.class, "MoonUfo",
				ModLoader.getUniqueEntityId());

		ModLoader.RegisterEntityID(EntityMoonBuilder.class, "Builder",
				ModLoader.getUniqueEntityId());

		/*
		 * Blocks
		 */
		mPort = (BlockMoonPortal) (new BlockMoonPortal(mPortID, 205))
				.setHardness(-1.0F).setLightValue(1.0F).setResistance(6.0F)
				.setBlockName("moonPort");

		mLamp = (new BlockMoonLamp(mLampID, 0)).setHardness(0.6F)
				.setResistance(6.0F).setLightValue(1.0F)
				.setBlockName("lampBlock");

		mTent = (new BlockMoonTent(mTentID, 0)).setHardness(0.6F)
				.setResistance(6.0F).setBlockName("moonTent");

		mSuperCloth = (new BlockReinforcedCloth(mClothID, 0)).setHardness(-1F)
				.setResistance(6.0F).setBlockName("moonReinforcedCloth");

		mCheese = (new BlockMoonCheese(mCheeseID, 0)).setHardness(0.6F)
				.setResistance(6.0F).setBlockName("moonCheese");

		mRobot = (new BlockMoonBuilder(mRobotID)).setHardness(0.6F)
				.setResistance(6.0F).setBlockName("buildRobot");

		mRegolith = new BlockMoonSoil(mRegolithID, 0).setHardness(0.5F)
				.setBlockName("regolith");

		mBauxit = new BlockMoonBauxit(mBauxitID, 0).setHardness(3.0F)
				.setResistance(5.0F).setBlockName("bauxit");

		mSCore = new BlockMoonCore(mSCoreID, 0).setHardness(1F).setBlockName(
				"score");

		mBAlu = new BlockMoonBlockAlu(mBAluID).setBlockUnbreakable()
				.setBlockName("alublock");
		/*
		 * Items
		 */
		mHelm = (new ItemArmor(mHelmID, 0, ModLoader.AddArmor("moonSuit"), 0)
				.setItemName("moonHelm"));

		mJacket = (new ItemArmor(mJacketID, 0, ModLoader.AddArmor("moonSuit"),
				1).setItemName("moonChest"));

		mPants = (new ItemArmor(mPantsID, 0, ModLoader.AddArmor("moonSuit"), 2)
				.setItemName("moonPants"));

		mBoots = (new ItemArmor(mBootsID, 0, ModLoader.AddArmor("moonSuit"), 3)
				.setItemName("moonBoots"));

		mScrewer = (new ItemSonicScrewdriver(mScrewerID))
				.setItemName("screwer");

		mCheeseSlice = (new ItemFood(mCheeseSliceID, 1, false))
				.setItemName("cheeseSlice");

		mIngotAlu = new Item(mIngotAluID).setItemName("ingotAlu");
		/*
		 * Registering
		 */
		ModLoader.RegisterBlock(mPort);
		ModLoader.RegisterBlock(mLamp);
		ModLoader.RegisterBlock(mTent);
		ModLoader.RegisterBlock(mSuperCloth);
		ModLoader.RegisterBlock(mCheese);
		ModLoader.RegisterBlock(mRobot);
		ModLoader.RegisterBlock(mRegolith);
		ModLoader.RegisterBlock(mBauxit);
		ModLoader.RegisterBlock(mSCore);
		ModLoader.RegisterBlock(mBAlu);

		/*
		 * Icons
		 */
		mHelm.iconIndex = ModLoader.addOverride("/gui/items.png",
				"/moon/helmIco.png");
		mJacket.iconIndex = ModLoader.addOverride("/gui/items.png",
				"/moon/jacketIco.png");
		mBoots.iconIndex = ModLoader.addOverride("/gui/items.png",
				"/moon/bootsIco.png");
		mPants.iconIndex = ModLoader.addOverride("/gui/items.png",
				"/moon/pantsIco.png");
		mScrewer.iconIndex = ModLoader.addOverride("/gui/items.png",
				"/moon/screwer.png");
		mSuperCloth.blockIndexInTexture = ModLoader.addOverride("/terrain.png",
				"/moon/clothForce.png");
		mTent.blockIndexInTexture = ModLoader.addOverride("/terrain.png",
				"/moon/clothBlock.png");
		mCheese.blockIndexInTexture = ModLoader.addOverride("/terrain.png",
				"/moon/cheeseBlock.png");
		mLamp.blockIndexInTexture = ModLoader.addOverride("/terrain.png",
				"/moon/lamp.png");
		mCheeseSlice.iconIndex = ModLoader.addOverride("/gui/items.png",
				"/moon/cheeseSlice.png");
		mRegolith.blockIndexInTexture = ModLoader.addOverride("/terrain.png",
				"/moon/regolith.png");
		mBauxit.blockIndexInTexture = ModLoader.addOverride("/terrain.png",
				"/moon/bauxit.png");
		mIngotAlu.iconIndex = ModLoader.addOverride("/gui/items.png",
				"/moon/ingotAlu.png");

		mRobot.blockIndexInTexture = 48;
		mBAlu.blockIndexInTexture = Block.blockSteel.blockIndexInTexture;

		/*
		 * Names
		 */
		ModLoader.AddName(mLamp, "Lamp");
		ModLoader.AddName(mHelm, "Moon Helmet");
		ModLoader.AddName(mBoots, "Moon Sneakers");
		ModLoader.AddName(mJacket, "Moon Jacket");
		ModLoader.AddName(mPants, "Moon Pants");
		ModLoader.AddName(mTent, "MoonTent Block");
		ModLoader.AddName(mSuperCloth, "Reinforced Cloth Block");
		ModLoader.AddName(mScrewer, "Sonic Screwdriver");
		ModLoader.AddName(mCheese, "Cheese");
		ModLoader.AddName(mPort, "Moon Portal Field");
		ModLoader.AddName(mRobot, "Robot Builder");
		ModLoader.AddName(mCheeseSlice, "Slice of Cheese");
		ModLoader.AddName(mRegolith, "Regolith");
		ModLoader.AddName(mBauxit, "Bauxit");
		ModLoader.AddName(mIngotAlu, "Alu Ingot");
		ModLoader.AddName(mSCore, "Rocket Core");
		ModLoader.AddName(mBAlu, "Alu Block");
		/*
		 * Recipes
		 */
		ModLoader.AddRecipe(new ItemStack(mLamp, 6),
				new Object[] { " G ", "GRI", " I ", Character.valueOf('I'),
						Item.ingotIron, Character.valueOf('R'), Item.redstone,
						Character.valueOf('G'), Block.glass });

		ModLoader.AddRecipe(new ItemStack(mHelm, 1),
				new Object[] { "CCC", "CGC", Character.valueOf('C'),
						mSuperCloth, Character.valueOf('G'), Block.glass });

		ModLoader.AddRecipe(new ItemStack(mBoots, 1),
				new Object[] { "   ", "I I", "C C", Character.valueOf('C'),
						mSuperCloth, Character.valueOf('I'), Item.ingotIron });

		ModLoader.AddRecipe(new ItemStack(mPants, 1),
				new Object[] { "CIC", "C C", "I I", Character.valueOf('C'),
						mSuperCloth, Character.valueOf('I'), Item.ingotIron });

		ModLoader.AddRecipe(new ItemStack(mJacket, 1),
				new Object[] { "CIC", "CCC", "CIC", Character.valueOf('C'),
						mSuperCloth, Character.valueOf('I'), Item.ingotIron });

		ModLoader.AddRecipe(new ItemStack(mTent, 1),
				new Object[] { "   ", " C ", "CDC", Character.valueOf('C'),
						mSuperCloth, Character.valueOf('D'), Item.doorWood });

		ModLoader.AddRecipe(new ItemStack(mSuperCloth, 8), new Object[] {
				"CCC", "CIC", "CCC", Character.valueOf('C'), Block.cloth,
				Character.valueOf('I'), Item.ingotGold });

		ModLoader
				.AddRecipe(new ItemStack(mScrewer, 1), new Object[] { "  R",
						" I ", "I  ", Character.valueOf('R'), Item.redstone,
						Character.valueOf('I'), Item.ingotIron });

		ModLoader.AddRecipe(new ItemStack(mScrewer, 1),
				new Object[] { "   ", " S ", " R ", Character.valueOf('R'),
						Item.redstone, Character.valueOf('S'),
						new ItemStack(mScrewer, 1, screwUses) });

		ModLoader.AddRecipe(new ItemStack(mCheese, 2), new Object[] { "MMM",
				"MMM", "MMM", Character.valueOf('M'), Item.bucketMilk });

		ModLoader.AddRecipe(new ItemStack(mCheese, 1), new Object[] { "CC",
				"CC", Character.valueOf('C'), mCheeseSlice });

		ModLoader.AddRecipe(
				new ItemStack(mRobot, 1),
				new Object[] { "AAA", "WIR", "AAA", Character.valueOf('A'),
						mIngotAlu, Character.valueOf('W'), Item.bucketWater,
						Character.valueOf('I'), Block.blockSteel,
						Character.valueOf('R'), Item.redstone });

		ModLoader.AddRecipe(new ItemStack(Block.tnt, 2),
				new Object[] { "RDR", "DRD", "RDR", Character.valueOf('R'),
						mRegolith, Character.valueOf('D'), Item.gunpowder });

		ModLoader.AddSmelting(mBauxitID, new ItemStack(mIngotAlu, 1));

		ModLoader.AddRecipe(
				new ItemStack(mSCore, 1),
				new Object[] { " Y ", "ARA", " A ", Character.valueOf('A'),
						mIngotAlu, Character.valueOf('Y'),
						new ItemStack(Item.dyePowder, 1, 11),
						Character.valueOf('R'), Item.redstone });
	}

	private EntityPlayerSP changePlayerEntity(int type, Minecraft game) {
		changeMoonTexture(type, game);
		if (type == dimNumber) {
			return new EntityPlayerMoon(game, game.theWorld, game.session, type);
		}
		return new EntityPlayerSP(game, game.theWorld, game.session, type);
	}

	@SuppressWarnings("unchecked")
	public static void changeMoonTexture(int type, Minecraft game) {
		if (earthID == -1)
			earthID = game.renderEngine.getTexture("/moon/earth.png");

		map = null;
		try {
			map = (HashMap<String, Integer>) ModLoader.getPrivateValue(
					RenderEngine.class, game.renderEngine, "textureMap");

			if (moonID == -1)
				moonID = (Integer) map.get("/terrain/moon.png");

			if (type == dimNumber)
				map.put("/terrain/moon.png", earthID);
			else
				map.put("/terrain/moon.png", moonID);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String Version() {
		return "v1.0";
	}

	@Override
	public boolean canIntercept(World world, Loc loc, int blockID) {
		if (blockID == mSCoreID)
			return true;

		if (world.worldProvider.worldType == dimNumber) {
			if (blockID == Block.reed.blockID)
				return true;
			if (blockID == Block.pumpkinLantern.blockID)
				return true;
			if (blockID == Block.waterStill.blockID)
				return true;
			if (blockID == Block.waterMoving.blockID)
				return true;
			if (blockID == Block.torchWood.blockID)
				return true;
			if (blockID == Block.stoneOvenActive.blockID)
				return true;
			if (blockID == Block.stoneOvenIdle.blockID)
				return true;
			if (blockID == Block.crops.blockID)
				return true;
			if (blockID == Block.sapling.blockID)
				return true;
			if (blockID == Block.fire.blockID)
				return true;
			if (blockID == Block.leaves.blockID)
				return true;
			if (blockID == Block.tallGrass.blockID)
				return true;
			if (blockID == Block.grass.blockID)
				return true;
			if (blockID == Block.cactus.blockID)
				return true;
			if (blockID == Block.deadBush.blockID)
				return true;
			if (blockID == Block.plantRed.blockID)
				return true;
			if (blockID == Block.plantYellow.blockID)
				return true;
			if (blockID == Block.bed.blockID)
				return true;
			if (blockID == Block.mobSpawner.blockID)
				return true;
			if (blockID == Block.mushroomBrown.blockID)
				return true;
			if (blockID == Block.mushroomRed.blockID)
				return true;
		}
		return false;
	}

	@Override
	public int intercept(World world, Loc loc, int blockID) {
		if (blockID == Block.bed.blockID)
			return 0;

		if (blockID == mSCoreID) {
			Random rand = new Random();
			EntityPlayer pl = ModLoader.getMinecraftInstance().thePlayer;
			if (pl.dimension != dimNumber)
				pl.addChatMessage(earthTalk.get(rand.nextInt(earthTalk.size())));
			else
				pl.addChatMessage(spaceTalk.get(rand.nextInt(spaceTalk.size())));

			if (rand.nextInt(5) == 0)
				pl.addChatMessage("HEYOO!! Let BuilderBot handle it!");
		}

		if (inTent(world, loc.x(), loc.y(), loc.z()))
			return blockID;
		return 0;
	}

	private void DrawOverlay(Minecraft mc, String texture) {
		if (mc.thePlayer.dimension != dimNumber)
			return;
		if (inTent(mc.theWorld, MathHelper.floor_double(mc.thePlayer.posX),
				MathHelper.floor_double(mc.thePlayer.posY),
				MathHelper.floor_double(mc.thePlayer.posZ)))
			return;
		ScaledResolution scaledresolution = new ScaledResolution(
				mc.gameSettings, mc.displayWidth, mc.displayHeight);
		int k = scaledresolution.getScaledWidth();
		int l = scaledresolution.getScaledHeight();
		GL11.glBindTexture(3553 /* GL_TEXTURE_2D */,
				mc.renderEngine.getTexture(texture));
		int k2 = (int) Math
				.ceil(((double) (mc.thePlayer.air - 2) * 10D) / 300D);
		int l3 = (int) Math.ceil(((double) mc.thePlayer.air * 10D) / 300D) - k2;
		for (int l5 = 0; l5 < k2 + l3; l5++) {
			if (l5 < k2) {
				drawTexturedModalRect((k / 2 - 91) + l5 * 8, l - 32 - 9, 16,
						18, 9, 9);
			} else {
				drawTexturedModalRect((k / 2 - 91) + l5 * 8, l - 32 - 9, 25,
						18, 9, 9);
			}
		}
	}

	private void drawTexturedModalRect(int i, int j, int k, int l, int i1,
			int j1) {
		int zLevel = -30;
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(i + 0, j + j1, zLevel, (float) (k + 0) * f,
				(float) (l + j1) * f1);
		tessellator.addVertexWithUV(i + i1, j + j1, zLevel, (float) (k + i1)
				* f, (float) (l + j1) * f1);
		tessellator.addVertexWithUV(i + i1, j + 0, zLevel,
				(float) (k + i1) * f, (float) (l + 0) * f1);
		tessellator.addVertexWithUV(i + 0, j + 0, zLevel, (float) (k + 0) * f,
				(float) (l + 0) * f1);
		tessellator.draw();
	}

	public static boolean inTent(World wd, int x, int y, int z) {
		int sides = 0;
		boolean ceil = false;
		for (int i = -5; i < 128; i++) {
			if (wd.getBlockId(x, y + i, z) == mClothID
					|| wd.getBlockId(x, y + i, z) == mBAluID) {
				ceil = true;
				break;
			}
		}
		if (!ceil)
			return false;

		for (int i = -32; i <= 0; i++) {
			if (wd.getBlockId(x + i, y, z) == mClothID
					|| wd.getBlockId(x + i, y, z) == mBAluID) {
				sides++;
				break;
			}
		}

		for (int i = -32; i <= 0; i++) {
			if (wd.getBlockId(x - i, y, z) == mClothID
					|| wd.getBlockId(x - i, y, z) == mBAluID) {
				sides++;
				break;
			}
		}

		for (int i = -32; i <= 0; i++) {
			if (wd.getBlockId(x, y, z + i) == mClothID
					|| wd.getBlockId(x, y, z + i) == mBAluID) {
				sides++;
				break;
			}
		}

		for (int i = -32; i <= 0; i++) {
			if (wd.getBlockId(x, y, z - i) == mClothID
					|| wd.getBlockId(x, y, z - i) == mBAluID) {
				sides++;
				break;
			}
		}

		if (sides >= 3)
			return true;
		else
			return false;
	}

	@Override
	public void GenerateSurface(World var1, Random var2, int var3, int var4) {
		int i;
		int var14, var15, var16;
		for (i = 0; i < 20; ++i) {
			var14 = var3 + var1.rand.nextInt(16);
			var15 = var1.rand.nextInt(128);
			var16 = var4 + var1.rand.nextInt(16);
			(new WorldGenMinable(mBauxitID, 10)).generate(var1, var1.rand,
					var14, var15, var16);
		}
	}

	public static int m = ID("score");
	public static int n = -1, c = ID("alublock"), g = Block.glass.blockID;

	public static int[][][] rocketScheme = new int[][][] {
			new int[][] { new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, n, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, n, 0, 0, 0, 0, 0, n, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, n, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, },
			new int[][] { new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, n, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, n, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, n, n, 0, 0, 0, n, n, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, n, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, n, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, },
			new int[][] { new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, n, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, n, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, c, c, c, 0, 0, 0 },
					new int[] { 0, n, n, c, 0, c, n, n, 0 },
					new int[] { 0, 0, 0, c, c, c, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, n, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, n, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, },// 37
			new int[][] { new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, n, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, c, c, c, 0, 0, 0 },
					new int[] { 0, 0, c, 0, 0, 0, c, 0, 0 },
					new int[] { 0, n, c, 0, 0, 0, c, n, 0 },
					new int[] { 0, 0, c, 0, 0, 0, c, 0, 0 },
					new int[] { 0, 0, 0, c, c, c, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, n, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, },
			new int[][] { new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, n, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, c, c, c, 0, 0, 0 },
					new int[] { 0, 0, c, 0, 0, 0, c, 0, 0 },
					new int[] { 0, n, c, 0, 0, 0, c, n, 0 },
					new int[] { 0, 0, c, 0, 0, 0, c, 0, 0 },
					new int[] { 0, 0, 0, c, c, c, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, n, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, },// 32
			new int[][] { new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, c, c, c, 0, 0, 0 },
					new int[] { 0, 0, c, c, c, 0, c, 0, 0 },
					new int[] { 0, 0, c, c, c, c, c, 0, 0 },
					new int[] { 0, 0, c, c, c, c, c, 0, 0 },
					new int[] { 0, 0, 0, c, c, c, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, },
			new int[][] {
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, c, g, c, 0, 0, 0 },
					new int[] { 0, 0, c, 0, 0, 0, c, 0, 0 },
					new int[] { 0, 0, g, 0, m, 0, g, 0, 0 },// m doprostred
					new int[] { 0, 0, c, 0, 0, 0, c, 0, 0 },
					new int[] { 0, 0, 0, c, g, c, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, },// 28
			new int[][] { new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, c, c, c, 0, 0, 0 },
					new int[] { 0, 0, c, 0, 0, 0, c, 0, 0 },
					new int[] { 0, 0, c, 0, 0, 0, c, 0, 0 },
					new int[] { 0, 0, c, 0, 0, 0, c, 0, 0 },
					new int[] { 0, 0, 0, c, c, c, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, },
			new int[][] { new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, c, c, c, 0, 0, 0 },
					new int[] { 0, 0, 0, c, c, c, 0, 0, 0 },
					new int[] { 0, 0, 0, c, c, c, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, },
			new int[][] { new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, c, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, },
			new int[][] { new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, c, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, } };// 23
	private static List<String> earthTalk = new ArrayList<String>();
	private static List<String> spaceTalk = new ArrayList<String>();
	static {

		earthTalk.add("Space space wanna go to space.");
		earthTalk.add("Ba! Ba. Ba! Space! Ba ba baaa!");
		earthTalk.add("Play it cool. Here come space cops.");
		earthTalk.add("Help me space cops. Space cops help!.");
		earthTalk.add("Space space wanna go to space.");

		spaceTalk.add("Space? Space!");
		spaceTalk.add("I'm in space.");
		spaceTalk.add("SPAAACE!");
		spaceTalk.add("So much space. Need to see it all!");
	}
}
