package oortcloud.hungryanimals.entities.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageAgeable implements IStorage<ICapabilityAgeable> {

	@Override
	@Nullable
	public NBTBase writeNBT(Capability<ICapabilityAgeable> capability, ICapabilityAgeable instance, EnumFacing side) {
		NBTTagCompound tag = new NBTTagCompound();
		
		if (instance instanceof CapabilityAgeableSub) {
			// TODO BAD OOP
			return tag;
		}
		
		tag.setInteger("age", instance.getAge());
		return tag;
	}

	@Override
	public void readNBT(Capability<ICapabilityAgeable> capability, ICapabilityAgeable instance, EnumFacing side, NBTBase nbt) {
		if (instance instanceof CapabilityAgeableSub) {
			return;
		}
		
		NBTTagCompound tag = (NBTTagCompound) nbt;
		instance.setAge(tag.getInteger("age"));
	}

}
