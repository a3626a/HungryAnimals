package oortcloud.hungryanimals.entities.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageTamableAnimal  implements IStorage<ICapabilityTamableAnimal> {

	@Override
	public NBTBase writeNBT(Capability<ICapabilityTamableAnimal> capability, ICapabilityTamableAnimal instance, EnumFacing side) {
		return new NBTTagDouble(instance.getTaming());
	}

	@Override
	public void readNBT(Capability<ICapabilityTamableAnimal> capability, ICapabilityTamableAnimal instance, EnumFacing side, NBTBase nbt) {
		instance.setTaming(((NBTTagDouble)nbt).getDouble());
	}

}
