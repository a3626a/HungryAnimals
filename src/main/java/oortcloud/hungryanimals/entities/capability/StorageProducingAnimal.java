package oortcloud.hungryanimals.entities.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import oortcloud.hungryanimals.entities.production.IProduction;

public class StorageProducingAnimal implements IStorage<ICapabilityProducingAnimal> {

	// This cannot guarantee valid mappings if config is changed.
	// "name" should be indicated inside config to restore correct IProduction
	
	@Override
	@Nullable
	public NBTBase writeNBT(Capability<ICapabilityProducingAnimal> capability, ICapabilityProducingAnimal instance, EnumFacing side) {
		NBTTagCompound tag = new NBTTagCompound();
		for (IProduction i : instance.getProductions()) {
			tag.setTag(i.getName(), i.writeNBT());
		}
		return tag;
	}

	@Override
	public void readNBT(Capability<ICapabilityProducingAnimal> capability, ICapabilityProducingAnimal instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound)nbt;
		
		for (IProduction i : instance.getProductions()) {
			NBTBase inbt = tag.getTag(i.getName());
			i.readNBT(inbt);
		}
	}

}
