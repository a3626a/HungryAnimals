package oortcloud.hungryanimals.entities.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ProviderAgeable implements ICapabilitySerializable<INBT> {

	@CapabilityInject(ICapabilityAgeable.class)
	public static final Capability<ICapabilityAgeable> CAP = null;
	
	private ICapabilityAgeable instance;
		
	public ProviderAgeable(MobEntity entity) {
		instance = new CapabilityAgeable(entity);
	}
	
	public ProviderAgeable(EntityAgeable entity) {
		instance = new CapabilityAgeableSub(entity);
	}
	
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing) {
		return capability == CAP;
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		return capability == CAP ? CAP.<T> cast(this.instance) : null;
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
