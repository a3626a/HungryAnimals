package oortcloud.hungryanimals.entities.capability;

public interface ICapabilityHungryAnimal {
	public double getNutrient();
	public double addNutrient(double nutrient);
	public double setNutrient(double nutrient);
	
	public double getStomach();
	public double addStomach(double stomach);
	public double setStomach(double stomach);
	public double getMaxStomach();
	
	public double getWeight();
	public double addWeight(double weight);
	public double setWeight(double weight);
	public double getStarvinglWeight();
	public double getNormalWeight();	
	public double getMaxWeight();	
	
	public double getExcretion();
	public double addExcretion(double excretion);
	public double setExcretion(double excretion);
}
