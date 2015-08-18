package oortcloud.hungryanimals.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.PacketTileEntityClient;
import oortcloud.hungryanimals.fluids.ModFluids;
import oortcloud.hungryanimals.recipes.RecipeMillstone;

public class TileEntityMillstone extends TileEntityPowerTransporter implements IInventory, IFluidHandler, ISidedInventory {

	private ItemStack[] inventory = new ItemStack[getSizeInventory()];

	private int grindTime;
	private int totalgrindTime = 5 * 20;

	private double powerUsage = 0.5;

	private boolean needSync = true;

	private FluidTank fluidTank;

	private static int fluidCapacity = 1000;

	public TileEntityMillstone() {
		fluidTank = new FluidTank(fluidCapacity);
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
				if (this.getPowerNetwork().getPowerStored() > powerUsage) {

					int amount = RecipeMillstone.getRecipe(item);
					if (amount > 0) {
						this.getPowerNetwork().consumeEnergy(powerUsage);
						this.grindTime += 1;

						if (this.grindTime >= this.totalgrindTime) {
							this.grindTime = 0;

							decrStackSize(0, 1);
							fluidTank.fill(new FluidStack(ModFluids.seedoil, amount), true);

							PacketTileEntityClient msg = new PacketTileEntityClient(3, this.worldObj.provider.getDimensionId(), this.pos);
							msg.setInt(fluidTank.getFluidAmount());
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

		fluidTank.writeToNBT(compound);

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

		fluidTank.readFromNBT(compound);

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

		fluidTank.readFromNBT(compound);

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

		fluidTank.writeToNBT(compound);

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
		return fluidTank.getFluidAmount() / (double) fluidTank.getCapacity();
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
		return RecipeMillstone.getRecipe(stack)>0;
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
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		if (resource.getFluid() == ModFluids.seedoil)
			return fluidTank.fill(resource, doFill);
		else 
			return 0;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		if (resource.getFluid() == ModFluids.seedoil)
			return fluidTank.drain(resource.amount, doDrain);
		else {
			return null;
		}
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		return fluidTank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		return fluidTank.getFluidAmount() < fluidTank.getCapacity() && ModFluids.seedoil == fluid;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		return fluidTank.getFluidAmount() > 0 && ModFluids.seedoil == fluid;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		return new FluidTankInfo[] { fluidTank.getInfo() };
	}

	public FluidTank getFluidTank() {
		return fluidTank;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] {0};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}
}
