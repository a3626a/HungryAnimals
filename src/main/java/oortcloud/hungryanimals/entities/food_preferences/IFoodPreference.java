package oortcloud.hungryanimals.entities.food_preferences;

import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;

public interface IFoodPreference<T> extends IFoodPreferenceSimple<T> {

	public boolean canEat(ICapabilityHungryAnimal cap, T food);
	public double getNutrient(T food);
	public double getStomach(T food);
	
	/**
	 * this method is for optimization.
	 * 
	 * @return true if should eat something, false to skip search.
	 */
	public boolean shouldEat(ICapabilityHungryAnimal cap);
	
}
