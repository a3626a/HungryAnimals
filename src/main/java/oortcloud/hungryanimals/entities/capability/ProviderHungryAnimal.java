package oortcloud.hungryanimals.entities.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ProviderHungryAnimal implements ICapabilitySerializable<INBT> {

	@CapabilityInject(ICapabilityHungryAnimal.class)
	public static final Capability<ICapabilityHungryAnimal> CAP = null;
	
	private ICapabilityHungryAnimal instance;
	
	public ProviderHungryAnimal(MobEntity entity) {
		instance = new CapabilityHungryAnimal(entity);
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		return  capability == CAP ? LazyOptional.of(()->this.instance).cast() : LazyOptional.empty();
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
