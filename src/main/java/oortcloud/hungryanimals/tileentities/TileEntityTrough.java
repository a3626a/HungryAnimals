package oortcloud.hungryanimals.tileentities;

import java.util.ArrayList;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToTrough;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.capability.TamingLevel;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceManager;

public class TileEntityTrough extends TileEntity implements ITickable {

	public ItemStack stack = ItemStack.EMPTY;

	private static int period = 20 * 5;
	private static double radius = 8;

	@SideOnly(Side.CLIENT)
	public float[][] random;

	public TileEntityTrough() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			random = new float[16][2];
			for (int i = 0; i < 16; i++) {
				random[i][0] = (float) (0.05 * Math.random());
				random[i][1] = (float) (0.05 * Math.random());
			}
		}
	}

	@Override
	public void update() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER && this.getWorld().getWorldTime() % TileEntityTrough.period == 0 && !this.stack.isEmpty()) {
			ArrayList<EntityAnimal> list = (ArrayList<EntityAnimal>) this.getWorld().getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(this.pos.add(-radius, -radius, -radius), this.pos.add(radius + 1, radius + 1, radius + 1)));
			for (EntityAnimal i : list) {
				if (i.hasCapability(ProviderHungryAnimal.CAP, null) && i.hasCapability(ProviderTamableAnimal.CAP, null)) {
					if (i.getCapability(ProviderTamableAnimal.CAP, null).getTamingLevel() == TamingLevel.TAMED && FoodPreferenceManager.getInstance().REGISTRY_ITEM.get(i.getClass()).canEat(i.getCapability(ProviderHungryAnimal.CAP, null), this.stack)) {
						i.tasks.taskEntries.forEach(entry -> {
							if (entry.action instanceof EntityAIMoveToTrough) {
								((EntityAIMoveToTrough)entry.action).pos = this.pos;
							}
						});
					}
				}
				
			}
		}

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		writeSyncableDataToNBT(compound);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		readSyncableDataFromNBT(compound);
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound compound = new NBTTagCompound();
		writeSyncableDataToNBT(compound);
		return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), compound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound compound = pkt.getNbtCompound();
		readSyncableDataFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		writeSyncableDataToNBT(tag);
		return tag;
	}
	
	private void writeSyncableDataToNBT(NBTTagCompound compound) {
		if (!stack.isEmpty()) {
			NBTTagCompound tag = new NBTTagCompound();
			stack.writeToNBT(tag);
			compound.setTag("foodbox", tag);
		}
	}

	private void readSyncableDataFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("foodbox")) {
			NBTTagCompound tag = (NBTTagCompound) compound.getTag("foodbox");
			stack = new ItemStack(tag);
		} else {
			stack = ItemStack.EMPTY;
		}
	}

}
