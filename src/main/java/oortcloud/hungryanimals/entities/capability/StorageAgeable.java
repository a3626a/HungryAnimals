package oortcloud.hungryanimals.entities.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageAgeable implements IStorage<ICapabilityAgeable> {

	@Override
	@Nullable
	public INBT writeNBT(Capability<ICapabilityAgeable> capability, ICapabilityAgeable instance, Direction side) {
		CompoundNBT tag = new CompoundNBT();
		
		if (instance instanceof CapabilityAgeableSub) {
			// TODO BAD OOP
			return tag;
		}
		
		tag.setInteger("age", instance.getAge());
		return tag;
	}

	@Override
	public void readNBT(Capability<ICapabilityAgeable> capability, ICapabilityAgeable instance, Direction side, INBT nbt) {
		if (instance instanceof CapabilityAgeableSub) {
			return;
		}
		
		CompoundNBT tag = (CompoundNBT) nbt;
		instance.setAge(tag.getInteger("age"));
	}

}
