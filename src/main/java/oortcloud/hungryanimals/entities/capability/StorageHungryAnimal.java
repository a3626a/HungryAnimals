package oortcloud.hungryanimals.entities.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageHungryAnimal implements IStorage<ICapabilityHungryAnimal> {

	@Override
	public NBTBase writeNBT(Capability<ICapabilityHungryAnimal> capability, ICapabilityHungryAnimal instance, EnumFacing side) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setDouble("hunger", instance.getHunger());
		tag.setDouble("excretion", instance.getExcretion());
		return tag;
	}

	@Override
	public void readNBT(Capability<ICapabilityHungryAnimal> capability, ICapabilityHungryAnimal instance, EnumFacing side, NBTBase nbt) {
		instance.setHunger(((NBTTagCompound)nbt).getDouble("hunger"));
		instance.setExcretion(((NBTTagCompound)nbt).getDouble("excretion"));
	}

}
