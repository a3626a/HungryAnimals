package oortcloud.hungryanimals.tileentities;

import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.PacketTileEntityClient;
import oortcloud.hungryanimals.energy.EnergyNetwork;
import oortcloud.hungryanimals.recipes.RecipeBlender;
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
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntityBlender extends TileEntityEnergyTransporter implements IInventory, ISidedInventory {

	private ItemStack[] inventory = new ItemStack[getSizeInventory()];

	private double energyUsage = 0.5;

	private int currentInputSlot1 = 0;
	private int currentInputSlot2 = 1;
	private int currentOutputSlot = 2;
	private ItemStack currentOutput;
	private boolean isInventoryChanged = true;
	private boolean needSync = true;
	private boolean canWork = false;

	private int blendTime;
	private int totalBlendTime = 5 * 20;

	private static double energyCapacity = EnergyNetwork.energyUnit * 5;

	public TileEntityBlender() {
		super(energyCapacity);
	}
	
	@Override
	public void update() {
		super.update();

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {

		}

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {

			if (needSync) {
				PacketTileEntityClient msg = new PacketTileEntityClient(5, worldObj.provider.getDimensionId(), pos);
				msg.setItemStackArray(inventory);
				HungryAnimals.simpleChannel.sendToAll(msg);
				needSync=false;
			}
			
			if (isInventoryChanged) {
				isInventoryChanged = false;

				int itemsNext = 0;
				ItemStack[] items = new ItemStack[getSizeInventory()];
				int[] itemsSlot = new int[getSizeInventory()];

				int emptySlot = -1;

				for (int i = 0; i < getSizeInventory(); i++) {
					items[itemsNext] = getStackInSlot(i);
					if (items[itemsNext] != null) {
						itemsSlot[itemsNext++] = i;
					} else {
						if (emptySlot == -1)
							emptySlot = i;
					}
				}

				canWork = false;

				boolean breakFlag = false;
				for (int i = 0; i < itemsNext - 1; i++) {
					if (breakFlag)
						break;
					for (int j = i + 1; j < itemsNext; j++) {
						ItemStack output = RecipeBlender.getRecipe(items[i], items[j]);
						if (output != null) {
							if (itemsNext >= 3) {
								for (int k = 0; k < itemsNext; k++) {
									if (k != i && k != j) {
										ItemStack targetOutput = items[k];
										if (output.getItem() == targetOutput.getItem() && output.getItemDamage() == targetOutput.getItemDamage()) {
											if (targetOutput.stackSize + output.stackSize <= targetOutput.getMaxStackSize()) {
												canWork = true;
												currentInputSlot1 = itemsSlot[i];
												currentInputSlot2 = itemsSlot[j];
												currentOutputSlot = itemsSlot[k];
												currentOutput = output;
												breakFlag = true;
												break;
											}
										}
									}
								}
							}
							if (breakFlag)
								break;
							if (itemsNext < getSizeInventory()) {
								canWork = true;
								currentInputSlot1 = itemsSlot[i];
								currentInputSlot2 = itemsSlot[j];
								currentOutputSlot = emptySlot;
								currentOutput = output;
								breakFlag = true;
								break;
							}
						}
					}
				}
			}
		} else {
			if (canWork) {
				ItemStack output = getStackInSlot(currentOutputSlot);
				if (output.stackSize + currentOutput.stackSize < output.getMaxStackSize()) {
					canWork = true;
				} else {
					canWork = false;
				}
			}
		}

		if (canWork) {

			if (this.getNetwork().getEnergy() > energyUsage) {
				this.getNetwork().consumeEnergy(energyUsage);
				this.blendTime += 1;

				if (this.blendTime >= this.totalBlendTime) {
					this.blendTime = 0;

					decrStackSize(currentInputSlot1, 1);
					decrStackSize(currentInputSlot2, 1);
					ItemStack output = getStackInSlot(currentOutputSlot);
					if (output != null) {
						output.stackSize += currentOutput.stackSize;
					} else {
						setInventorySlotContents(currentOutputSlot, currentOutput.copy());
					}
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
		return 4;
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
				isInventoryChanged = true;
				this.markDirty();
				return itemstack;
			} else {
				itemstack = this.inventory[index].splitStack(count);

				if (this.inventory[index].stackSize == 0) {
					this.inventory[index] = null;
					isInventoryChanged = true;
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
		isInventoryChanged = true;
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
		isInventoryChanged = true;
		needSync = true;
		
		for (int i = 0; i < this.inventory.length; ++i) {
			this.inventory[i] = null;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

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

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (side==EnumFacing.UP)
			return new int [] {0,1};
		if (side==EnumFacing.DOWN)
			return new int [] {3,2};
		return new int [] {side.getHorizontalIndex()};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}

}
