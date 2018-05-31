package oortcloud.hungryanimals.entities.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import oortcloud.hungryanimals.entities.capability.ICapabilitySexual.Sex;

public class StorageSexual implements IStorage<ICapabilitySexual> {

	@Override
	@Nullable
	public NBTBase writeNBT(Capability<ICapabilitySexual> capability, ICapabilitySexual instance, EnumFacing side) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("sex", instance.getSex().name());
		return tag;
	}

	@Override
	public void readNBT(Capability<ICapabilitySexual> capability, ICapabilitySexual instance, EnumFacing side, NBTBase nbt) {
		try {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			String str = tag.getString("sex");
			instance.setSex(Sex.valueOf(str));
		} catch (IllegalArgumentException e) {
			
		}
	}

}
