package oortcloud.hungryanimals.entities.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import oortcloud.hungryanimals.entities.production.Productions;

public class ProviderProducingAnimal implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ICapabilityProducingAnimal.class)
	public static final Capability<ICapabilityProducingAnimal> CAP = null;
	
	private ICapabilityProducingAnimal instance;
	
	public ProviderProducingAnimal(EntityAnimal entity) {
		instance = new CapabilityProducingAnimal(entity, Productions.getInstance().apply(entity));
	}
	
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CAP;
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CAP ? CAP.<T> cast(this.instance) : null;
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
