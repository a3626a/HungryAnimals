package oortcloud.hungryanimals.tileentities;

import java.util.ArrayList;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.configuration.util.HashProbabilityItemStack;
import oortcloud.hungryanimals.core.network.PacketTileEntityClient;
import oortcloud.hungryanimals.energy.PowerNetwork;
import oortcloud.hungryanimals.recipes.RecipeThresher;

public class TileEntityThresher extends TileEntityPowerTransporter implements IInventory, ISidedInventory {

	private ItemStack[] inventory = new ItemStack[getSizeInventory()];

	private double powerUsage = 0.5;

	private int leftAttempt;
	private int threshTime;
	private int totalthreshTime = 2 * 20;

	private boolean needSync = true;

	private static double powerCapacity = PowerNetwork.powerUnit * 3;

	public TileEntityThresher() {
		super();
		super.powerCapacity = TileEntityThresher.powerCapacity;
		leftAttempt = 0;
	}

	@Override
	public void update() {
		super.update();
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {

			if (needSync) {
				worldObj.markBlockForUpdate(pos);
				markDirty();
				needSync = false;
			}

			if (getStackInSlot(0) != null) {
				if (leftAttempt > 0) {
					ArrayList<HashProbabilityItemStack> output = RecipeThresher.getRecipe(getStackInSlot(0));
					if (output != null && this.getPowerNetwork().getPowerStored() > powerUsage) {
						this.getPowerNetwork().consumeEnergy(powerUsage);
						this.threshTime += 1;

						if (this.threshTime >= this.totalthreshTime) {
							this.threshTime = 0;

							for (HashProbabilityItemStack i : output) {
								if (this.worldObj.rand.nextDouble() < i.prob) {

									float f = this.worldObj.rand.nextFloat() * 0.8F + 0.1F;
									float f1 = this.worldObj.rand.nextFloat() * 0.8F + 0.1F;
									float f2 = this.worldObj.rand.nextFloat() * 0.8F + 0.1F;

									EntityItem entityitem = new EntityItem(this.worldObj, (double) ((float) this.pos.getX() + f), (double) ((float) this.pos.getY() + f1), (double) ((float) this.pos.getZ() + f2), i.item.copy());

									float f3 = 0.05F;
									entityitem.motionX = (double) ((float) this.worldObj.rand.nextGaussian() * f3);
									entityitem.motionY = (double) ((float) this.worldObj.rand.nextGaussian() * f3 + 0.2F);
									entityitem.motionZ = (double) ((float) this.worldObj.rand.nextGaussian() * f3);
									this.worldObj.spawnEntityInWorld(entityitem);
								}
							}
							this.leftAttempt--;
							if (this.leftAttempt == 0) {
								decrStackSize(0, 1);
								if (getStackInSlot(0) != null)
									leftAttempt = 4;
							}
						}
					}
				} else {
					leftAttempt = 4;
				}
			}
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inventory[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (this.inventory[index] != null) {
			needSync = true;

			ItemStack itemstack;

			if (this.inventory[index].stackSize <= count) {
				itemstack = this.inventory[index];
				this.inventory[index] = null;
				this.markDirty();
				return itemstack;
			} else {
				itemstack = this.inventory[index].splitStack(count);

				if (this.inventory[index].stackSize == 0) {
					this.inventory[index] = null;
				}

				this.markDirty();
				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		if (this.inventory[index] != null) {
			ItemStack itemstack = this.inventory[index];
			this.inventory[index] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		needSync = true;

		this.inventory[index] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return RecipeThresher.getRecipe(stack) != null;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		needSync = true;

		for (int i = 0; i < this.inventory.length; ++i) {
			this.inventory[i] = null;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("leftAttempt", leftAttempt);
		writeSyncableDataToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.leftAttempt = compound.getInteger("leftAttempt");
		readSyncableDataFromNBT(compound);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound compound = new NBTTagCompound();
		writeSyncableDataToNBT(compound);
		return new S35PacketUpdateTileEntity(getPos(), getBlockMetadata(), compound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound compound = pkt.getNbtCompound();
		readSyncableDataFromNBT(compound);
	}

	private void writeSyncableDataToNBT(NBTTagCompound compound) {
		for (int i = 0; i < getSizeInventory(); i++) {
			NBTTagCompound tag = new NBTTagCompound();
			ItemStack item = getStackInSlot(i);
			if (item != null) {
				item.writeToNBT(tag);
				compound.setTag("items" + i, tag);
			}
		}
	}

	private void readSyncableDataFromNBT(NBTTagCompound compound) {

		for (int i = 0; i < getSizeInventory(); i++) {
			if (compound.hasKey("items" + i)) {
				NBTTagCompound tag = (NBTTagCompound) compound.getTag("items" + i);
				setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(tag));
			} else {
				setInventorySlotContents(i, null);
			}
		}
	}

	public boolean canTakeOut() {
		return leftAttempt == 4;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] { 0 };
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}

	@Override
	public BlockPos[] getConnectedBlocks() {
		return new BlockPos[] { pos.up(), pos.down() };
	}
}
