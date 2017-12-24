package oortcloud.hungryanimals.entities.capability;

import net.minecraft.entity.EntityLiving;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;

public class CapabilityHungryAnimal implements ICapabilityHungryAnimal {

	private double excretion;
	private double stomach;
	private double nutrient;
	private EntityLiving entity;

	public CapabilityHungryAnimal(EntityLiving entity) {
		this.entity = entity;
		setStomach(0.0);
		setNutrient(0.0);
		// setHunger(entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_max).getAttributeValue()/2.0);
		setExcretion(0.0);
	}
	

	@Override
	public double getNutrient() {
		return nutrient;
	}

	@Override
	public double addNutrient(double nutrient) {
		double oldNutrient = getNutrient();
		setNutrient(getNutrient() + nutrient);
		return oldNutrient;
	}

	@Override
	public double setNutrient(double nutrient) {
		double oldNutrient = this.nutrient;
		if (nutrient < 0) {
			this.nutrient = 0;
		} else {
			this.nutrient = nutrient;
		}
		return oldNutrient;
	}

	@Override
	public double getStomach() {
		return stomach;
	}

	@Override
	public double addStomach(double stomach) {
		double oldStomach = getStomach();
		setStomach(getStomach() + stomach);
		if (stomach < 0) {
			addExcretion(-stomach * entity.getAttributeMap().getAttributeInstance(ModAttributes.excretion_factor).getAttributeValue());
		}
		return oldStomach;
	}

	@Override
	public double setStomach(double stomach) {
		double oldStomach = this.stomach;
		if (stomach > getMaxStomach()) {
			this.stomach = getMaxStomach();
		} else if (stomach < 0) {
			this.stomach = 0;
		} else {
			this.stomach = stomach;
		}
		return oldStomach;
	}

	@Override
	public double getMaxStomach() {
		return entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_stomach_max).getAttributeValue();
	}
	
	/*
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
	public double getMaxHunger() {
		return entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_max).getAttributeValue();
	}
    */

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
