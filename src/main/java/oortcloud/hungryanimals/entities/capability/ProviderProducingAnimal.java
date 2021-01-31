package oortcloud.hungryanimals.entities.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import oortcloud.hungryanimals.entities.production.Productions;

public class ProviderProducingAnimal implements ICapabilitySerializable<INBT> {

	@CapabilityInject(ICapabilityProducingAnimal.class)
	public static final Capability<ICapabilityProducingAnimal> CAP = null;
	
	private ICapabilityProducingAnimal instance;
	
	public ProviderProducingAnimal(MobEntity entity) {
		instance = new CapabilityProducingAnimal(entity, Productions.getInstance().apply(entity));
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
