package net.minecraft.src;

public class EntityMoonBuilder extends EntityLiving {
	private int grX, grY, grZ;
	private int dockX, dockY, dockZ;
	private int destX, destY, destZ;
	private int retX, retY, retZ;
	private int pathDirection;
	private int brake, shakeSteps, buildSteps, index;
	private Point3D curLoc, nextLoc;
	private boolean part1, part2, part3, shake;
	private TileEntityChest chest;
	private TileEntitySign sign;

	private boolean refuel, build, prepared;

	private int fuel, lastStatus, heading;

	private int glass, aluIng, sCore, cobble;

	private Loc oldLoc;
	/*
	 * 0 - pøíprava plochy 1 - stavba rakety
	 */
	private int status;

	public EntityMoonBuilder(World var1) {
		super(var1);
		grX = 9;// 7
		grZ = 9;// 8
		grY = 11;// 10
		noClip = true;
		texture = "/moon/RoboBuilder.png";
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
		this.yOffset = this.height / 2.0F;
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;

		buildSteps = grX * grZ;
		pathDirection = 1;
		brake = 0;
		shakeSteps = 1;
		index = -1;
		curLoc = new Point3D(0, 0, 0);
		nextLoc = new Point3D(0, 0, 0);

		status = 4;
		prepared = build = shake = part1 = part2 = part3 = false;
		refuel = true;
		fuel = 0;
		heading = 0;

		glass = 4;
		aluIng = 120;
		sCore = 1;
		cobble = grX * grZ;
	}

	@Override
	public boolean attackEntityFrom(Entity var1, int var2) {
		health -= 2;
		return true;
	}

	public EntityMoonBuilder(World var1, int x, int y, int z, EntityPlayer ep) {
		this(var1);

		destX = dockX = x + 1;
		destY = dockY = y;
		destZ = dockZ = z;

		oldLoc = new Loc(dockX, y, dockZ);

		this.setPosition(dockX + 0.5, dockY + 0.1, dockZ + 0.5);
		chest = (TileEntityChest) worldObj.getBlockTileEntity(dockX - 1, dockY,
				dockZ);

		var1.setBlockAndMetadataWithNotify(x - 1, y, z, Block.signWall.blockID,
				4);
	}

	@Override
	public void onUpdate() {
		chest = (TileEntityChest) worldObj.getBlockTileEntity(dockX - 1, dockY,
				dockZ);
		if (chest == null) {
			setEntityDead();
			ModLoader.getMinecraftInstance().thePlayer
					.addChatMessage("Error 404.5 dock not found! Shutting down.");
		}

		worldObj.spawnParticle("smoke", posX - 0.40625, posY + 0.1, posZ - 0.2,
				0, -0.1, 0);
		worldObj.spawnParticle("smoke", posX - 0.40625, posY + 0.1, posZ + 0.2,
				0, -0.1, 0);
		worldObj.spawnParticle("smoke", posX + 0.40625, posY + 0.1, posZ - 0.2,
				0, -0.1, 0);
		worldObj.spawnParticle("smoke", posX + 0.40625, posY + 0.1, posZ + 0.2,
				0, -0.1, 0);

		if (health < 0) {
			setEntityDead();
			updateSing("Status:€Builder Bot€destroyed");
		}
		rotationYaw = heading;

		if (status == 0) {
			prepareLaunchPad();
		} else if (status == 1) {
			build();
		} else if (status == 2) {
			if (isInDock() && !refuel) {
				brake = 0;
				status = 3;
				destX = retX;
				destY = retY;
				destZ = retZ;
				refuel = true;
				return;
			}
			goToLocation();

		} else if (status == 3) {
			waitForRefuel();
		} else if (status == 4) {
			waitForMaterial();
		} else {
			updateSing("Status:€Builder Bot€Idle");
			return;
		}

	}

	private void updateSing(String str) {
		sign = (TileEntitySign) worldObj.getBlockTileEntity(dockX - 2, dockY,
				dockZ);
		if (sign == null)
			worldObj.setBlockAndMetadataWithNotify(dockX - 2, dockY, dockZ,
					Block.signWall.blockID, 4);

		sign = (TileEntitySign) worldObj.getBlockTileEntity(dockX - 2, dockY,
				dockZ);

		String[] strExplode = str.split("€");

		for (int i = 0; i < 4; i++) {
			sign.signText[i] = "";
		}

		for (int i = 0; i < strExplode.length; i++) {
			sign.signText[i] = strExplode[i];
		}
	}

	@Override
	public void setEntityDead() {
		worldObj.setBlockMetadataWithNotify(dockX - 1, dockY, dockZ, 0);
		super.setEntityDead();
	}

	private boolean isInDock() {
		if (MathHelper.floor_double(posY) != dockY)
			return false;
		if (MathHelper.floor_double(posZ) != dockZ)
			return false;
		if (MathHelper.floor_double(posX) != dockX)
			return false;
		return true;

	}

	private void waitForRefuel() {
		brake++;
		if (brake < 5)
			return;

		if (brake == 5) {
			updateSing("Status:€Need more fuel");
			// System.out.println("cekam palivo");
			refuel();
			if (fuel > getDistToRefuel()) {
				status = 2;
			}
		}

		if (brake < 15)
			return;

		brake = 0;
	}

	private void waitForMaterial() {
		brake++;
		if (brake < 5)
			return;

		if (brake == 5) {
			if (cobble <= 0)
				status = 0;

			if (glass <= 0 && aluIng <= 0 && sCore <= 0) {
				status = 1;
			}

			if (prepared)
				updateSing("Material needed:€Glass: " + glass + "€Alu Ingots: "
						+ aluIng + "€Rocket core: " + sCore);
			else
				updateSing("Material needed:€Cobblestone: " + cobble);
			// System.out.println("cekam material");
			getMaterial();
		}

		if (brake < 15)
			return;

		brake = 0;
	}

	private void refuel() {
		if (chest == null) {
			setEntityDead();
			return;
		}
		int coalID = Item.coal.shiftedIndex;
		int chestSize = chest.getSizeInventory();
		ItemStack tmpStck;
		fuel = 0;
		for (int i = 0; i < chestSize; i++) {
			tmpStck = chest.getStackInSlot(i);
			if (tmpStck == null)
				continue;

			if (tmpStck.itemID == coalID) {
				fuel += 2 * tmpStck.stackSize;
				chest.decrStackSize(i, tmpStck.stackSize);
			}
		}

	}

	private void getMaterial() {
		if (chest == null) {
			setEntityDead();
			return;
		}
		int glassID = Block.glass.blockID;
		int aluIngID = mod_MoonDimension.mIngotAluID + 256;
		int sCoreID = mod_MoonDimension.mSCoreID;
		int chestSize = chest.getSizeInventory();
		ItemStack tmpStck;

		for (int i = 0; i < chestSize; i++) {
			tmpStck = chest.getStackInSlot(i);
			if (tmpStck == null)
				continue;
			if (tmpStck.itemID == glassID) {
				if (glass > 0) {
					if (tmpStck.stackSize >= glass) {
						chest.decrStackSize(i, glass);
						glass = 0;
					} else {
						glass -= tmpStck.stackSize;
						chest.decrStackSize(i, tmpStck.stackSize);
					}
				}
			}

			if (tmpStck.itemID == aluIngID) {
				if (aluIng > 0) {
					if (tmpStck.stackSize >= aluIng) {
						chest.decrStackSize(i, aluIng);
						aluIng = 0;
					} else {
						aluIng -= tmpStck.stackSize;
						chest.decrStackSize(i, tmpStck.stackSize);
					}
				}
			}

			if (tmpStck.itemID == sCoreID) {
				if (sCore > 0) {
					if (tmpStck.stackSize >= sCore) {
						chest.decrStackSize(i, sCore);
						sCore = 0;
					} else {
						sCore -= tmpStck.stackSize;
						chest.decrStackSize(i, tmpStck.stackSize);
					}
				}
			}

			if (tmpStck.itemID == Block.cobblestone.blockID) {
				if (sCore > 0) {
					if (tmpStck.stackSize >= cobble) {
						chest.decrStackSize(i, cobble);
						cobble = 0;
					} else {
						cobble -= tmpStck.stackSize;
						chest.decrStackSize(i, tmpStck.stackSize);
					}
				}
			}
		}
	}

	private void getCurPosition(int ind) {
		curLoc.z = ind / grX;
		if (curLoc.z % 2 == 0)
			curLoc.x = ind % grX;
		else
			curLoc.x = grX - ind % grX - 1;
	}

	private void getNextPosition(int ind) {
		ind += pathDirection;
		nextLoc.y = curLoc.y;
		if (ind == buildSteps) {
			ind--;
			nextLoc.y = curLoc.y + 1;
		}

		if (ind == -1) {
			ind++;
			nextLoc.y = curLoc.y + 1;
		}

		nextLoc.z = ind / grX;
		if (nextLoc.z % 2 == 0)
			nextLoc.x = ind % grX;
		else
			nextLoc.x = grX - ind % grX - 1;
	}

	private void prepareLaunchPad() {
		int nextID, topY;
		brake++;
		if (brake < 5)
			return;

		if (brake == 5) {
			updateSing("Status:€Preparing€Launchpad");
			if (!part1) {
				getNextPosition(index);
				nextID = worldObj.getBlockId(dockX + nextLoc.x, dockY
						+ nextLoc.y, dockZ + nextLoc.z);
				topY = curLoc.y
						+ dockY
						- worldObj.findTopSolidBlock(dockX + nextLoc.x, dockZ
								+ nextLoc.z);
				// je volno pøed? tj. pøed je vzduch a nejsou žádné bloky nad
				if (nextID == 0 && topY >= 0) {
					// ANO
					index++;
					getCurPosition(index);
					part1 = true;
					move(dockX + curLoc.x + 0.5, dockY + curLoc.y, dockZ
							+ curLoc.z + 0.5);
					if (curLoc.y != 0)
						return;
				} else {
					// NE
					curLoc.y++;
					move(dockX + curLoc.x + 0.5, dockY + curLoc.y, dockZ
							+ curLoc.z + 0.5);
					return;
				}
			}

			if (!part2) {
				// je správná výška?
				if (curLoc.y != 0) {
					// NE
					nextID = worldObj.getBlockId(dockX + curLoc.x, dockY
							+ curLoc.y - 1, dockZ + curLoc.z);
					// je blok pod?
					if (nextID != 0) {
						// ANO
						worldObj.setBlockWithNotify(dockX + curLoc.x, dockY
								+ curLoc.y - 1, dockZ + curLoc.z, 0);
					} else {
						// NE
						curLoc.y--;
						move(dockX + curLoc.x + 0.5, dockY + curLoc.y, dockZ
								+ curLoc.z + 0.5);
					}
					return;
				} else {
					// ANO
					part2 = true;
				}
			}

			if (!part3) {
				nextID = worldObj.getBlockId(dockX + curLoc.x, dockY + curLoc.y
						- 1, dockZ + curLoc.z);

				// Material mat = nextID != 0 ?
				// Block.blocksList[nextID].blockMaterial
				// : Material.air;
				// Je podlaha?
				if (nextID != Block.cobblestone.blockID) {
					// NE
					shake = true;
				} else
					// ANO
					part3 = true;
			}
		}

		if (shake) {
			rotationPitch = (((shakeSteps % 2) * 2) - 1) * 4;
			shakeSteps++;
		}

		if (brake < 10)
			return;
		brake = 0;
		shakeSteps = 0;
		rotationPitch = 0;

		if (shake) {
			worldObj.setBlockWithNotify(dockX + curLoc.x, dockY + curLoc.y - 1,
					dockZ + curLoc.z, Block.cobblestone.blockID);
			shake = false;
		}

		if (part3) {
			part1 = part2 = part3 = false;
		}

		if (index == (buildSteps - 1)) {
			status = 2;
			index = 0;
			// nastavime cekani na material
			lastStatus = 4;
			destX = dockX;
			destY = dockY;
			destZ = dockZ;
			prepared = true;
		}
	}

	private void build() {
		brake++;

		if (brake < 10)
			return;

		if (brake == 10) {
			updateSing("Status:€Building");
			getCurPosition(index);
			move(dockX + curLoc.x + 0.5, dockY + curLoc.y + 1, dockZ + curLoc.z
					+ 0.5);

			index += pathDirection;
			if (mod_MoonDimension.rocketScheme[curLoc.y][curLoc.z][curLoc.x] == 0)
				brake = 21;
		}

		rotationPitch = (((shakeSteps % 2) * 2) - 1) * 4;
		shakeSteps++;

		if (brake < 20)
			return;

		rotationPitch = 0;
		shakeSteps = 0;
		brake = 0;

		int block = mod_MoonDimension.rocketScheme[curLoc.y][curLoc.z][curLoc.x];

		if (block == -1) {
			worldObj.setBlockAndMetadata(dockX + curLoc.x, dockY + curLoc.y,
					dockZ + curLoc.z, mod_MoonDimension.mBAluID, 1);
			worldObj.markBlockNeedsUpdate(dockX + curLoc.x, dockY + curLoc.y,
					dockZ + curLoc.z);
		} else {
			worldObj.setBlockWithNotify(dockX + curLoc.x, dockY + curLoc.y,
					dockZ + curLoc.z, block);
		}

		if (index == buildSteps || index == -1) {
			pathDirection *= -1;
			index += pathDirection;
			curLoc.y++;
		}

		if (curLoc.y == grY) {
			destX = dockX;
			destZ = dockZ;
			destY = dockY;
			status = 2;
			build = true;
			System.out.println("postaveno");
			return;
		}
	}

	private void goToLocation() {
		brake++;

		if (brake < 5)
			return;
		if (brake == 5) {
			updateSing("Status:€Relocating");
			int topY;
			int dir;

			if (MathHelper.floor_double(posX) != destX) {
				dir = posX > destX ? -1 : 1;

				topY = worldObj.findTopSolidBlock(MathHelper.floor_double(posX)
						+ dir, MathHelper.floor_double(posZ));
				if ((int) posY < topY) {
					posY++;
					return;
				}
				move(posX + dir, posY, posZ);
				return;
			}

			if (MathHelper.floor_double(posZ) != destZ) {
				dir = posZ > destZ ? -1 : 1;

				topY = worldObj.findTopSolidBlock(
						MathHelper.floor_double(posX),
						MathHelper.floor_double(posZ) + dir);

				if ((int) posY < topY) {
					move(posX, posY + 1, posZ);
					return;
				}
				move(posX, posY, posZ + dir);
				return;
			}

			if (MathHelper.floor_double(posY) != destY) {
				dir = posY > destY ? -1 : 1;
				move(posX, posY + dir, posZ);
				return;
			}
			status = lastStatus;

			if (build) {
				status = 4000;
			}
		}
		if (brake == 15)
			brake = 0;
	}

	// Vrátí poèet krokù z aktuálního místa do místa doplnìní paliva s rezervou
	// 30% a na zpáteèní cestu, z aktuálního místa musim dojet na èerpaèku a
	// zase zpátky a mam rezervu pro nepøedpokládané stoupání
	private int getDistToRefuel() {
		double dist = Math.abs(posX - dockX) + Math.abs(posY - dockY)
				+ Math.abs(posZ - dockZ);
		dist = dist * 1.3;
		return (int) dist;
	}

	private void move(double x, double y, double z) {
		int delX, delZ;

		delX = (int) x - oldLoc.x();
		delZ = (int) z - oldLoc.z();
		if (!(delX == 0 && delZ == 0)) {
			if (delX == 0)
				if (delZ > 0)
					heading = 0;
				else
					heading = 180;
			else if (delX > 0)
				heading = 270;
			else
				heading = 90;
		}
		if (fuel <= getDistToRefuel() && status != 2) {
			lastStatus = status;
			status = 2;
			refuel = false;
			destX = dockX;
			destY = dockY;
			destZ = dockZ;

			index -= pathDirection;

			retX = MathHelper.floor_double(posX);
			retY = MathHelper.floor_double(posY);
			retZ = MathHelper.floor_double(posZ);

			return;
		}

		fuel--;
		setPosition(x, y, z);
		oldLoc = new Loc(x, y, z);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setInteger("status", status);
		var1.setInteger("destx", destX);
		var1.setInteger("desty", destY);
		var1.setInteger("destz", destZ);
		var1.setInteger("dockx", dockX);
		var1.setInteger("docky", dockY);
		var1.setInteger("dockz", dockZ);
		var1.setInteger("index", index);
		var1.setInteger("posx", dockX + curLoc.x);
		var1.setInteger("posy", dockY + curLoc.y);
		var1.setInteger("posz", dockZ + curLoc.z);

		var1.setInteger("fuel", fuel);
		var1.setInteger("heading", heading);
		var1.setInteger("pathDir", pathDirection);
		var1.setInteger("laststatus", lastStatus);
		var1.setBoolean("shake", shake);
		var1.setBoolean("part1", part1);
		var1.setBoolean("part2", part2);
		var1.setBoolean("part3", part3);
		var1.setBoolean("build", build);
		var1.setBoolean("refuel", refuel);
		var1.setInteger("glass", glass);
		var1.setInteger("aluing", aluIng);
		var1.setInteger("score", sCore);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		status = var1.getInteger("status");
		destX = var1.getInteger("destx");
		destY = var1.getInteger("desty");
		destZ = var1.getInteger("destz");
		dockX = var1.getInteger("dockx");
		dockY = var1.getInteger("docky");
		dockZ = var1.getInteger("dockz");
		fuel = var1.getInteger("fuel");
		index = var1.getInteger("index");
		pathDirection = var1.getInteger("pathDir");
		lastStatus = var1.getInteger("laststatus");
		shake = var1.getBoolean("shake");
		part1 = var1.getBoolean("part1");
		part2 = var1.getBoolean("part2");
		part3 = var1.getBoolean("part3");
		build = var1.getBoolean("build");
		refuel = var1.getBoolean("refuel");
		heading = var1.getInteger("heading");
		oldLoc = new Loc(dockX, 0, dockZ);

		glass = var1.getInteger("glass");
		aluIng = var1.getInteger("aluing");
		sCore = var1.getInteger("score");

		int px, py, pz;

		px = var1.getInteger("posx");
		py = var1.getInteger("posy");
		pz = var1.getInteger("posz");

		setPosition(px + 0.5, py, pz + 0.5);
	}

	class Point3D {
		public int x;
		public int y;
		public int z;

		public Point3D(int xp, int yp, int zp) {
			x = xp;
			y = yp;
			z = zp;
		}

		@Override
		public String toString() {
			return "" + x + ", " + y + ", " + z;
		}
	}
}
