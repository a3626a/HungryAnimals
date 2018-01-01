package oortcloud.hungryanimals.entities.food_preferences;

import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;

public interface IFoodPreferenceSimple<T> {
	public boolean canEat(ICapabilityHungryAnimal cap, T food);

	/**
	 * this method is for optimization.
	 * 
	 * @return true if should eat something, false to skip search.
	 */
	public boolean shouldEat(ICapabilityHungryAnimal cap);
}
