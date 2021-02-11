package oortcloud.hungryanimals.entities.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ProviderAgeable implements ICapabilitySerializable<INBT> {

	@CapabilityInject(ICapabilityAgeable.class)
	public static final Capability<ICapabilityAgeable> CAP = null;
	
	private ICapabilityAgeable instance;
		
	public ProviderAgeable(MobEntity entity) {
		instance = new CapabilityAgeable(entity);
	}
	
	public ProviderAgeable(AgeableEntity entity) {
		instance = new CapabilityAgeableSub(entity);
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
