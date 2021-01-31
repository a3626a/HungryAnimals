package oortcloud.hungryanimals.tileentities;

import java.util.ArrayList;

import net.minecraft.entity.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToTrough;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.capability.TamingLevel;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferences;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreference;
import oortcloud.hungryanimals.utils.Tamings;

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
			ArrayList<MobEntity> list = (ArrayList<MobEntity>) this.getWorld().getEntitiesWithinAABB(MobEntity.class, new AxisAlignedBB(this.pos.add(-radius, -radius, -radius), this.pos.add(radius + 1, radius + 1, radius + 1)));
			for (MobEntity i : list) {
				if (i.hasCapability(ProviderHungryAnimal.CAP, null)) {
					ICapabilityHungryAnimal capHungry = i.getCapability(ProviderHungryAnimal.CAP, null);
					ICapabilityTamableAnimal capTaming = i.getCapability(ProviderTamableAnimal.CAP, null);
					IFoodPreference<ItemStack> pref = FoodPreferences.getInstance().REGISTRY_ITEM.get(i.getClass());
					if (Tamings.getLevel(capTaming) == TamingLevel.TAMED && pref.canEat(capHungry, this.stack)) {
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
	public CompoundNBT writeToNBT(CompoundNBT compound) {
		super.writeToNBT(compound);
		writeSyncableDataToNBT(compound);
		return compound;
	}

	@Override
	public void readFromNBT(CompoundNBT compound) {
		super.readFromNBT(compound);
		readSyncableDataFromNBT(compound);
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		CompoundNBT compound = new CompoundNBT();
		writeSyncableDataToNBT(compound);
		return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), compound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		CompoundNBT compound = pkt.getNbtCompound();
		readSyncableDataFromNBT(compound);
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT tag = super.getUpdateTag();
		writeSyncableDataToNBT(tag);
		return tag;
	}
	
	private void writeSyncableDataToNBT(CompoundNBT compound) {
		if (!stack.isEmpty()) {
			CompoundNBT tag = new CompoundNBT();
			stack.writeToNBT(tag);
			compound.setTag("foodbox", tag);
		}
	}

	private void readSyncableDataFromNBT(CompoundNBT compound) {
		if (compound.hasKey("foodbox")) {
			CompoundNBT tag = (CompoundNBT) compound.getTag("foodbox");
			stack = new ItemStack(tag);
		} else {
			stack = ItemStack.EMPTY;
		}
	}

}
