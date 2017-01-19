package oortcloud.hungryanimals.entities.capability;

import net.minecraft.entity.EntityLiving;

public class CapabilityTamablesAnimal implements ICapabilityTamableAnimal {

	private double taming;
	private EntityLiving entity;
	
	@Override
	public double getTaming() {
		return taming;
	}

	@Override
	public double setTaming(double taming) {
		double old = this.taming;
		this.taming = taming;
		return old;
	}

}
