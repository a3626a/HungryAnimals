package oortcloud.hungryanimals.entities.capability;

import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageTamableAnimal  implements IStorage<ICapabilityTamableAnimal> {

	@Override
	public INBT writeNBT(Capability<ICapabilityTamableAnimal> capability, ICapabilityTamableAnimal instance, Direction side) {
		return new DoubleNBT(instance.getTaming());
	}

	@Override
	public void readNBT(Capability<ICapabilityTamableAnimal> capability, ICapabilityTamableAnimal instance, Direction side, INBT nbt) {
		instance.setTaming(((DoubleNBT)nbt).getDouble());
	}

}
