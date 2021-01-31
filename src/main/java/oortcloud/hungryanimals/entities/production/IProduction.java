package oortcloud.hungryanimals.entities.production;

import net.minecraft.nbt.INBT;

public interface IProduction {
	public INBT writeNBT();
	public void readNBT(INBT nbt);
	public String getName();
}
