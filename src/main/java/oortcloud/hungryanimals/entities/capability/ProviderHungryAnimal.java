package oortcloud.hungryanimals.entities.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ProviderHungryAnimal implements ICapabilitySerializable<NBTBase>	{

	@CapabilityInject(ICapabilityHungryAnimal.class)
	public static final Capability<ICapabilityHungryAnimal> CAP_HUNGRYANIMAL = null;
	
	private ICapabilityHungryAnimal instance = CAP_HUNGRYANIMAL.getDefaultInstance();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CAP_HUNGRYANIMAL;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return  capability == CAP_HUNGRYANIMAL ? CAP_HUNGRYANIMAL.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return CAP_HUNGRYANIMAL.getStorage().writeNBT(CAP_HUNGRYANIMAL, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		CAP_HUNGRYANIMAL.getStorage().readNBT(CAP_HUNGRYANIMAL, this.instance, null, nbt);
	}
	
}
