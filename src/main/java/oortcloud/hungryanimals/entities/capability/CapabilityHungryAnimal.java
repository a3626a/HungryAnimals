package oortcloud.hungryanimals.entities.capability;

import net.minecraft.entity.EntityLiving;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;

public class CapabilityHungryAnimal implements ICapabilityHungryAnimal {

	private double hunger;
	private double excretion;
	private EntityLiving entity;

	public CapabilityHungryAnimal(EntityLiving entity) {
		this.entity = entity;
		setHunger(entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_max).getAttributeValue()/2.0);
		setExcretion(-2);
	}
	
	@Override
	public double getHunger() {
		return hunger;
	}

	@Override
	public double addHunger(double hunger) {
		double oldHunger =getHunger();
		setHunger(getHunger()+hunger);
		if (hunger < 0) {
			addExcretion(-hunger * entity.getAttributeMap().getAttributeInstance(ModAttributes.excretion_factor).getAttributeValue());
		}
		return oldHunger;
	}

	@Override
	public double setHunger(double hunger) {
		double oldHunger = this.hunger;
		if (hunger > getMaxHunger()) {
			this.hunger = getMaxHunger();
		} else if (hunger < 0) {
			this.hunger = 0;
		} else {
			this.hunger = hunger;
		}
		return oldHunger;
	}

	@Override
	public double getMaxHunger() {
		return entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_max).getAttributeValue();
	}

	@Override
	public double getExcretion() {
		return excretion;
	}

	@Override
	public double addExcretion(double excretion) {
		return setExcretion(getExcretion()+excretion);
	}

	@Override
	public double setExcretion(double excretion) {
		double oldExcretion = this.excretion;
		this.excretion = excretion;
		return oldExcretion;
	}

}
