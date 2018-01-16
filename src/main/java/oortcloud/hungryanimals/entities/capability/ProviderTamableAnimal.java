package oortcloud.hungryanimals.entities.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ProviderTamableAnimal implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ICapabilityTamableAnimal.class)
	public static final Capability<ICapabilityTamableAnimal> CAP = null;

	// Allocating Defuault Instance Here, is it important?
	protected ICapabilityTamableAnimal instance;

	public ProviderTamableAnimal(EntityAnimal entity) {
		instance = new CapabilityTamableAnimal(entity);
	}

	public ProviderTamableAnimal(AbstractHorse entity) {
		instance = new CapabilityTamableHorse(entity);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CAP;
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CAP ? CAP.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return CAP.getStorage().writeNBT(CAP, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		CAP.getStorage().readNBT(CAP, this.instance, null, nbt);
	}

}
