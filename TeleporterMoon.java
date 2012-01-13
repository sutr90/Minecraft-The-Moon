package net.minecraft.src;

import java.util.Random;

public class TeleporterMoon extends Teleporter {

	private int cheese = mod_MoonDimension.mCheeseID;
	private int portal = mod_MoonDimension.mPortID;
	private int core = mod_MoonDimension.m;

	private Random random = new Random();

	public void placeInPortal(World var1, Entity var2) {
		if (!this.placeInExistingPortal(var1, var2)) {
			this.createPortal(var1, var2);
			this.placeInExistingPortal(var1, var2);
		}
		mod_MoonDimension.travel = -1;
	}

	// find portal
	public boolean placeInExistingPortal(World var1, Entity var2) {
		if (mod_MoonDimension.travel == 0)
			return findPortal(var1, var2);
		else
			return findRocket(var1, var2);
	}

	// create portal
	public boolean createPortal(World var1, Entity var2) {
		if (mod_MoonDimension.travel == 0)
			return createPortalDo(var1, var2);
		else
			return createRocket(var1, var2);
	}

	private boolean findPortal(World var1, Entity var2) {
		short scanLimit = 128;
		double distToPortalFinal = -1.0D;
		int closePortX = 0;
		int closePortY = 0;
		int closePortZ = 0;
		int plX = MathHelper.floor_double(var2.posX);
		int plZ = MathHelper.floor_double(var2.posZ);

		double pointY;
		for (int forX = plX - scanLimit; forX <= plX + scanLimit; ++forX) {
			double pointX = (double) forX + 0.5D - var2.posX;

			for (int forZ = plZ - scanLimit; forZ <= plZ + scanLimit; ++forZ) {
				double pointZ = (double) forZ + 0.5D - var2.posZ;

				for (int forY = 127; forY >= 0; --forY) {
					if (var1.getBlockId(forX, forY, forZ) == portal) {
						while (var1.getBlockId(forX, forY - 1, forZ) == portal) {
							--forY;
						}

						pointY = (double) forY + 0.5D - var2.posY;
						double distToPortal = pointX * pointX + pointY * pointY
								+ pointZ * pointZ;
						if (distToPortalFinal < 0.0D
								|| distToPortal < distToPortalFinal) {
							distToPortalFinal = distToPortal;
							closePortX = forX;
							closePortY = forY;
							closePortZ = forZ;
						}
					}
				}
			}
		}
		// nastaví entitu doprostøed portálu
		// pokud je nìjaký nalezen
		if (distToPortalFinal >= 0.0D) {
			double var22 = (double) closePortX + 0.5D;
			double var16 = (double) closePortY + 0.5D;
			pointY = (double) closePortZ + 0.5D;
			if (var1.getBlockId(closePortX - 1, closePortY, closePortZ) == portal) {
				var22 -= 0.5D;
			}

			if (var1.getBlockId(closePortX + 1, closePortY, closePortZ) == portal) {
				var22 += 0.5D;
			}

			if (var1.getBlockId(closePortX, closePortY, closePortZ - 1) == portal) {
				pointY -= 0.5D;
			}

			if (var1.getBlockId(closePortX, closePortY, closePortZ + 1) == portal) {
				pointY += 0.5D;
			}

			var2.setLocationAndAngles(var22, var16, pointY, var2.rotationYaw,
					0.0F);
			var2.motionX = var2.motionY = var2.motionZ = 0.0D;
			return true;
		} else {
			return false;
		}
	}

	private boolean createPortalDo(World var1, Entity var2) {
		byte var3 = 16;
		double var4 = -1.0D;
		int var6 = MathHelper.floor_double(var2.posX);
		int var7 = MathHelper.floor_double(var2.posY);
		int var8 = MathHelper.floor_double(var2.posZ);
		int var9 = var6;
		int var10 = var7;
		int var11 = var8;
		int var12 = 0;
		int var13 = this.random.nextInt(4);

		int var14;
		double var15;
		int var17;
		double var18;
		int var21;
		int var20;
		int var23;
		int var22;
		int var25;
		int var24;
		int var27;
		int var26;
		int var28;
		double var34;
		double var32;
		for (var14 = var6 - var3; var14 <= var6 + var3; ++var14) {
			var15 = (double) var14 + 0.5D - var2.posX;

			for (var17 = var8 - var3; var17 <= var8 + var3; ++var17) {
				var18 = (double) var17 + 0.5D - var2.posZ;

				label271: for (var20 = 127; var20 >= 0; --var20) {
					if (var1.isAirBlock(var14, var20, var17)) {
						while (var20 > 0
								&& var1.isAirBlock(var14, var20 - 1, var17)) {
							--var20;
						}

						for (var21 = var13; var21 < var13 + 4; ++var21) {
							var22 = var21 % 2;
							var23 = 1 - var22;
							if (var21 % 4 >= 2) {
								var22 = -var22;
								var23 = -var23;
							}

							for (var24 = 0; var24 < 3; ++var24) {
								for (var25 = 0; var25 < 4; ++var25) {
									for (var26 = -1; var26 < 4; ++var26) {
										var27 = var14 + (var25 - 1) * var22
												+ var24 * var23;
										var28 = var20 + var26;
										int var29 = var17 + (var25 - 1) * var23
												- var24 * var22;
										if (var26 < 0
												&& !var1.getBlockMaterial(
														var27, var28, var29)
														.isSolid()
												|| var26 >= 0
												&& !var1.isAirBlock(var27,
														var28, var29)) {
											continue label271;
										}
									}
								}
							}

							var32 = (double) var20 + 0.5D - var2.posY;
							var34 = var15 * var15 + var32 * var32 + var18
									* var18;
							if (var4 < 0.0D || var34 < var4) {
								var4 = var34;
								var9 = var14;
								var10 = var20;
								var11 = var17;
								var12 = var21 % 4;
							}
						}
					}
				}
			}
		}

		if (var4 < 0.0D) {
			for (var14 = var6 - var3; var14 <= var6 + var3; ++var14) {
				var15 = (double) var14 + 0.5D - var2.posX;

				for (var17 = var8 - var3; var17 <= var8 + var3; ++var17) {
					var18 = (double) var17 + 0.5D - var2.posZ;

					label219: for (var20 = 127; var20 >= 0; --var20) {
						if (var1.isAirBlock(var14, var20, var17)) {
							while (var1.isAirBlock(var14, var20 - 1, var17)) {
								--var20;
							}

							for (var21 = var13; var21 < var13 + 2; ++var21) {
								var22 = var21 % 2;
								var23 = 1 - var22;

								for (var24 = 0; var24 < 4; ++var24) {
									for (var25 = -1; var25 < 4; ++var25) {
										var26 = var14 + (var24 - 1) * var22;
										var27 = var20 + var25;
										var28 = var17 + (var24 - 1) * var23;
										if (var25 < 0
												&& !var1.getBlockMaterial(
														var26, var27, var28)
														.isSolid()
												|| var25 >= 0
												&& !var1.isAirBlock(var26,
														var27, var28)) {
											continue label219;
										}
									}
								}

								var32 = (double) var20 + 0.5D - var2.posY;
								var34 = var15 * var15 + var32 * var32 + var18
										* var18;
								if (var4 < 0.0D || var34 < var4) {
									var4 = var34;
									var9 = var14;
									var10 = var20;
									var11 = var17;
									var12 = var21 % 2;
								}
							}
						}
					}
				}
			}
		}

		int var30 = var9;
		int var16 = var10;
		var17 = var11;
		int var31 = var12 % 2;
		int var19 = 1 - var31;
		if (var12 % 4 >= 2) {
			var31 = -var31;
			var19 = -var19;
		}

		boolean var33;
		if (var4 < 0.0D) {
			if (var10 < 70) {
				var10 = 70;
			}

			if (var10 > 118) {
				var10 = 118;
			}

			var16 = var10;

			for (var20 = -1; var20 <= 1; ++var20) {
				for (var21 = 1; var21 < 3; ++var21) {
					for (var22 = -1; var22 < 3; ++var22) {
						var23 = var30 + (var21 - 1) * var31 + var20 * var19;
						var24 = var16 + var22;
						var25 = var17 + (var21 - 1) * var19 - var20 * var31;
						var33 = var22 < 0;
						var1.setBlockWithNotify(var23, var24, var25,
								var33 ? cheese : 0);
					}
				}
			}
		}

		for (var20 = 0; var20 < 4; ++var20) {
			var1.editingBlocks = true;

			for (var21 = 0; var21 < 4; ++var21) {
				for (var22 = -1; var22 < 4; ++var22) {
					var23 = var30 + (var21 - 1) * var31;
					var24 = var16 + var22;
					var25 = var17 + (var21 - 1) * var19;
					var33 = var21 == 0 || var21 == 3 || var22 == -1
							|| var22 == 3;
					var1.setBlockWithNotify(var23, var24, var25, var33 ? cheese
							: portal);
				}
			}

			var1.editingBlocks = false;

			for (var21 = 0; var21 < 4; ++var21) {
				for (var22 = -1; var22 < 4; ++var22) {
					var23 = var30 + (var21 - 1) * var31;
					var24 = var16 + var22;
					var25 = var17 + (var21 - 1) * var19;
					var1.notifyBlocksOfNeighborChange(var23, var24, var25,
							var1.getBlockId(var23, var24, var25));
				}
			}
		}

		return true;
	}

	private boolean findRocket(World var1, Entity var2) {
		short scanLimit = 128;
		double distToPortalFinal = -1.0D;
		int closePortX = 0;
		int closePortY = 0;
		int closePortZ = 0;
		int plX = MathHelper.floor_double(var2.posX);
		int plZ = MathHelper.floor_double(var2.posZ);

		double pointY;
		for (int forX = plX - scanLimit; forX <= plX + scanLimit; ++forX) {
			double pointX = (double) forX + 0.5D - var2.posX;

			for (int forZ = plZ - scanLimit; forZ <= plZ + scanLimit; ++forZ) {
				double pointZ = (double) forZ + 0.5D - var2.posZ;

				for (int forY = 127; forY >= 0; --forY) {
					if (var1.getBlockId(forX, forY, forZ) == core) {
						while (var1.getBlockId(forX, forY - 1, forZ) == core) {
							--forY;
						}

						pointY = (double) forY + 0.5D - var2.posY;
						double distToPortal = pointX * pointX + pointY * pointY
								+ pointZ * pointZ;
						if (distToPortalFinal < 0.0D
								|| distToPortal < distToPortalFinal) {
							distToPortalFinal = distToPortal;
							closePortX = forX;
							closePortY = forY;
							closePortZ = forZ;
						}
					}
				}
			}
		}

		// nastaví entitu doprostøed portálu
		// pokud je nìjaký nalezen
		if (distToPortalFinal >= 0.0D) {
			double var22 = (double) closePortX - 0.5D;
			double var16 = (double) closePortZ + 0.5D;
			pointY = (double) closePortY;

			var2.setLocationAndAngles(var22, pointY, var16, var2.rotationYaw,
					0.0F);
			var2.motionX = var2.motionY = var2.motionZ = 0.0D;
			return true;
		} else {
			return false;
		}

	}

	private boolean createRocket(World var1, Entity var2) {
		int plX = MathHelper.floor_double(var2.posX) + 20;
		int plZ = MathHelper.floor_double(var2.posZ) + 20;
		int plY;

		int tempScore = Integer.MAX_VALUE, tmpX = plX, tmpZ = plZ;

		for (int x = plX - 70; x < plX + 70; x += 5) {
			for (int z = plZ - 70; z < plZ + 70; z += 5) {
				plY = var1.findTopSolidBlock(x, z);
				int tmpSc = checkSpot(var1, x, plY, z);
				if (tmpSc < tempScore) {
					tempScore = tmpSc;
					tmpX = x;
					tmpZ = z;
				}
			}
		}

		plX = tmpX;
		plZ = tmpZ;
		plY = var1.findTopSolidBlock(plX, plZ);

		for (int x = plX - 4; x < plX + 5; x++) {
			for (int z = plZ - 4; z < plZ + 5; z++) {
				var1.setBlockWithNotify(x, plY, z, Block.cobblestone.blockID);
			}
		}

		int block;
		for (int k = 0; k < 11; k++) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {

					block = mod_MoonDimension.rocketScheme[k][j][i];

					if (block < -1)
						continue;
					if (block == -1) {
						var1.setBlockAndMetadata(i + plX - 4, k + plY + 1, j
								+ plZ - 4, mod_MoonDimension.mBAluID, 1);
						var1.markBlockNeedsUpdate(i + plX - 4, k + plY + 1, j
								+ plZ - 4);
					} else {
						var1.setBlock(i + plX - 4, k + plY + 1, j + plZ - 4,
								block);
					}

					if (block == mod_MoonDimension.mSCoreID) {
						mod_MoonDimension.coreX = i + plX - 4;
						mod_MoonDimension.coreY = k + plY + 1;
						mod_MoonDimension.coreZ = j + plZ - 4;
					}
				}
			}
		}

		return true;
	}

	private int checkSpot(World worldObj, int dockX, int dockY, int dockZ) {
		int score = 0, top, distSq;
		for (int x = dockX - 4; x < dockX + 5; x += 2) {
			for (int z = dockZ - 4; z < dockZ + 5; z += 2) {
				top = worldObj.findTopSolidBlock(dockX + x, dockZ + z);

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
}
