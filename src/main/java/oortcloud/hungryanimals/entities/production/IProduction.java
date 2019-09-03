package oortcloud.hungryanimals.entities.production;

import net.minecraft.nbt.NBTBase;

public interface IProduction {
	public NBTBase writeNBT();
	public void readNBT(NBTBase nbt);
	public String getName();
}
