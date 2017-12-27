package oortcloud.hungryanimals.entities.capability;

import net.minecraft.entity.EntityLiving;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;

public class CapabilityHungryAnimal implements ICapabilityHungryAnimal {

	private double excretion;
	private double stomach; 
	private double nutrient;
	private double weight; 
	
	private EntityLiving entity;

	public CapabilityHungryAnimal(EntityLiving entity) {
		this.entity = entity;
		setStomach(0.0);
		setNutrient(0.0);
		setWeight(entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_weight_normal).getAttributeValue());
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
		if (stomach < 0) {
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
	
	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	public double addWeight(double weight) {
		double oldWeight = getWeight();
		setWeight(getWeight() + weight);
		return oldWeight;
	}

	@Override
	public double setWeight(double weight) {
		double oldWeight = this.weight;
		if (weight > getMaxWeight()) {
			this.weight = getMaxWeight();
		} else if (weight < 0) {
			this.weight = 0;
		} else {
			this.weight = weight;
		}
		return oldWeight;
	}
	
	@Override
	public double getNormalWeight() {
		return entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_weight_normal).getAttributeValue();
	}
	
	@Override
	public double getMaxWeight() {
		return entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_weight_normal).getAttributeValue()*2;
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
