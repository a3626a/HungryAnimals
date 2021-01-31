package oortcloud.hungryanimals.entities.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ProviderTamableAnimal implements ICapabilitySerializable<INBT> {

	@CapabilityInject(ICapabilityTamableAnimal.class)
	public static final Capability<ICapabilityTamableAnimal> CAP = null;

	// Allocating Defuault Instance Here, is it important?
	protected ICapabilityTamableAnimal instance;

	public ProviderTamableAnimal(MobEntity entity) {
		instance = new CapabilityTamableAnimal(entity);
	}

	public ProviderTamableAnimal(AbstractHorse entity) {
		instance = new CapabilityTamableHorse(entity);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing) {
		return capability == CAP;
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		return capability == CAP ? CAP.<T>cast(this.instance) : null;
	}

	@Override
	public INBT serializeNBT() {
		return CAP.getStorage().writeNBT(CAP, this.instance, null);
	}

	@Override
	public void deserializeNBT(INBT nbt) {
		CAP.getStorage().readNBT(CAP, this.instance, null, nbt);
	}

}
