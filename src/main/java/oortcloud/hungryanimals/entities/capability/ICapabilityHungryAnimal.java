package oortcloud.hungryanimals.entities.capability;

public interface ICapabilityHungryAnimal {
		
	public double getHunger();
	public double addHunger(double hunger);
	public double setHunger(double hunger);
	public double getMaxHunger();
	
	public double getExcretion();
	public double addExcretion(double excretion);
	public double setExcretion(double excretion);
	
}
