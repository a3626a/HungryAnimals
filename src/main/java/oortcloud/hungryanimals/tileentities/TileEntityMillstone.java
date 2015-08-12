package oortcloud.hungryanimals.tileentities;

import java.util.ArrayList;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.configuration.util.ProbItemStack;
import oortcloud.hungryanimals.core.network.PacketTileEntityClient;
import oortcloud.hungryanimals.core.network.PacketTileEntityServer;
import oortcloud.hungryanimals.recipes.RecipeMillstone;
import oortcloud.hungryanimals.recipes.RecipeThresher;

public class TileEntityMillstone extends TileEntityEnergyTransporter implements IInventory {

	private ItemStack[] inventory = new ItemStack[getSizeInventory()];

	private int grindTime;
	private int totalgrindTime = 5 * 20;

	private double energyUsage = 0.5;

	private boolean needSync = true;

	private static double maxLiquidAmount = 1000;
	public double liquidAmount = 0;

	public TileEntityMillstone() {
	}

	@Override
	public void update() {
		super.update();

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {

			if (needSync) {
				PacketTileEntityClient msg = new PacketTileEntityClient(4, worldObj.provider.getDimensionId(), pos);
				msg.setItemStackArray(inventory);
				HungryAnimals.simpleChannel.sendToAll(msg);
				needSync = false;
			}

			ItemStack item = getStackInSlot(0);
			if (item != null) {
				if (this.getNetwork().getEnergy() > energyUsage) {
					
					double amount = RecipeMillstone.getRecipe(item);
					if (amount > 0) {
						this.getNetwork().consumeEnergy(energyUsage);
						this.grindTime += 1;

						if (this.grindTime >= this.totalgrindTime) {
							this.grindTime = 0;

							decrStackSize(0, 1);
							this.liquidAmount = Math.min(this.liquidAmount + amount, this.maxLiquidAmount);

							PacketTileEntityClient msg = new PacketTileEntityClient(3, this.worldObj.provider.getDimensionId(), this.pos);
							msg.setDouble(this.liquidAmount);
							HungryAnimals.simpleChannel.sendToAll(msg);
						}
					}
				}

			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setDouble("liquidAmount", this.liquidAmount);

		for (int i = 0; i < getSizeInventory(); i++) {
			NBTTagCompound tag = new NBTTagCompound();
			ItemStack item = getStackInSlot(i);
			if (item != null) {
				item.writeToNBT(tag);
				compound.setTag("items" + i, tag);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		if (compound.hasKey("liquidAmount")) {
			this.liquidAmount = compound.getDouble("liquidAmount");
		}

		for (int i = 0; i < getSizeInventory(); i++) {
			if (compound.hasKey("items" + i)) {
				NBTTagCompound tag = (NBTTagCompound) compound.getTag("items" + i);
				setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(tag));
			}
		}
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound compound = pkt.getNbtCompound();
		liquidAmount = compound.getDouble("liquidAmount");

		for (int i = 0; i < getSizeInventory(); i++) {
			if (compound.hasKey("items" + i)) {
				NBTTagCompound tag = (NBTTagCompound) compound.getTag("items" + i);
				setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(tag));
			}
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setDouble("liquidAmount", liquidAmount);

		for (int i = 0; i < getSizeInventory(); i++) {
			NBTTagCompound tag = new NBTTagCompound();
			ItemStack item = getStackInSlot(i);
			if (item != null) {
				item.writeToNBT(tag);
				compound.setTag("items" + i, tag);
			}
		}
		return new S35PacketUpdateTileEntity(getPos(), getBlockMetadata(), compound);
	}

	public double getHeight() {
		return this.liquidAmount / this.maxLiquidAmount;
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
		return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
				(double) this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
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
}
