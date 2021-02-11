package oortcloud.hungryanimals.entities.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ProviderTamableAnimal implements ICapabilitySerializable<INBT> {

	@CapabilityInject(ICapabilityTamableAnimal.class)
	public static final Capability<ICapabilityTamableAnimal> CAP = null;

	// Allocating Default Instance Here, is it important?
	protected ICapabilityTamableAnimal instance;

	public ProviderTamableAnimal(MobEntity entity) {
		instance = new CapabilityTamableAnimal(entity);
	}

	public ProviderTamableAnimal(AbstractHorseEntity entity) {
		instance = new CapabilityTamableHorse(entity);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		return capability == CAP ? LazyOptional.of(()->this.instance).cast() : LazyOptional.empty();
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
