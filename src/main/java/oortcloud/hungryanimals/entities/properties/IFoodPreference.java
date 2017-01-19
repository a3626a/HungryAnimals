package oortcloud.hungryanimals.entities.properties;

import java.util.Iterator;

import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;

public interface IFoodPreference<T> {

	public boolean canEat(T food);
	public double getHunger(T food);
	
	/**
	 * this method is for optimization.
	 * 
	 * @return true if should eat something, false to skip search.
	 */
	public boolean shouldEat();
	
}
