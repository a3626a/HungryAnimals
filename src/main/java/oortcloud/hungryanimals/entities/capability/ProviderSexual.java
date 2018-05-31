package oortcloud.hungryanimals.entities.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ProviderSexual implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ICapabilitySexual.class)
	public static final Capability<ICapabilitySexual> CAP = null;
	
	private ICapabilitySexual instance;
		
	public ProviderSexual(EntityLiving entity) {
		instance = new CapabilitySexual(entity);
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
