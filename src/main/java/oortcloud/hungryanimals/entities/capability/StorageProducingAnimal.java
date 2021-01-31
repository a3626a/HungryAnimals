package oortcloud.hungryanimals.entities.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import oortcloud.hungryanimals.entities.production.IProduction;

public class StorageProducingAnimal implements IStorage<ICapabilityProducingAnimal> {

	// This cannot guarantee valid mappings if config is changed.
	// "name" should be indicated inside config to restore correct IProduction
	
	@Override
	@Nullable
	public INBT writeNBT(Capability<ICapabilityProducingAnimal> capability, ICapabilityProducingAnimal instance, Direction side) {
		CompoundNBT tag = new CompoundNBT();
		for (IProduction i : instance.getProductions()) {
			tag.put(i.getName(), i.writeNBT());
		}
		return tag;
	}

	@Override
	public void readNBT(Capability<ICapabilityProducingAnimal> capability, ICapabilityProducingAnimal instance, Direction side, INBT nbt) {
		CompoundNBT tag = (CompoundNBT)nbt;
		
		for (IProduction i : instance.getProductions()) {
			INBT inbt = tag.get(i.getName());
			i.readNBT(inbt);
		}
	}
}
