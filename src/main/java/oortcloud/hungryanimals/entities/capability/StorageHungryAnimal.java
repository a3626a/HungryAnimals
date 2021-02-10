package oortcloud.hungryanimals.entities.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageHungryAnimal implements IStorage<ICapabilityHungryAnimal> {

	@Override
	public INBT writeNBT(Capability<ICapabilityHungryAnimal> capability, ICapabilityHungryAnimal instance, Direction side) {
		CompoundNBT tag = new CompoundNBT();
		tag.setDouble("nutrient", instance.getNutrient());
		tag.setDouble("stomach", instance.getStomach());
		tag.setDouble("weight", instance.getWeight());
		tag.setDouble("excretion", instance.getExcretion());
		return tag;
	}

	@Override
	public void readNBT(Capability<ICapabilityHungryAnimal> capability, ICapabilityHungryAnimal instance, Direction side, INBT nbt) {
		CompoundNBT tag = (CompoundNBT) nbt;
		instance.setNutrient(tag.getDouble("nutrient"));
		instance.setStomach(tag.getDouble("stomach"));
		if (tag.contains("weight"))
			instance.setWeight(tag.getDouble("weight"));
		instance.setExcretion(tag.getDouble("excretion"));
	}

}
