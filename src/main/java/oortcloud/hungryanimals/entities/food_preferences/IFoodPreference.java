package oortcloud.hungryanimals.entities.food_preferences;

import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;

import javax.annotation.Nonnull;

public interface IFoodPreference<T> extends IFoodPreferenceSimple<T> {

	public boolean canEat(@Nonnull ICapabilityHungryAnimal cap, T food);
	public double getNutrient(T food);
	public double getStomach(T food);
	
	/**
	 * this method is for optimization.
	 * 
	 * @return true if should eat something, false to skip search.
	 */
	public boolean shouldEat(ICapabilityHungryAnimal cap);
	
}
