package oortcloud.hungryanimals.entities.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import oortcloud.hungryanimals.entities.capability.ICapabilitySexual.Sex;

public class StorageSexual implements IStorage<ICapabilitySexual> {

	@Override
	@Nullable
	public INBT writeNBT(Capability<ICapabilitySexual> capability, ICapabilitySexual instance, Direction side) {
		CompoundNBT tag = new CompoundNBT();
		tag.setString("sex", instance.getSex().name());
		return tag;
	}

	@Override
	public void readNBT(Capability<ICapabilitySexual> capability, ICapabilitySexual instance, Direction side, INBT nbt) {
		try {
			CompoundNBT tag = (CompoundNBT) nbt;
			String str = tag.getString("sex");
			instance.setSex(Sex.valueOf(str));
		} catch (IllegalArgumentException e) {
			
		}
	}

}
