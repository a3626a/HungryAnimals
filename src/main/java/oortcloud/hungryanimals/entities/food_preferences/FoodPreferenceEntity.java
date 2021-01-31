package oortcloud.hungryanimals.entities.food_preferences;

import java.util.Set;

import net.minecraft.entity.MobEntity;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;

public class FoodPreferenceEntity implements IFoodPreferenceSimple<MobEntity> {
	
	private Set<Class<? extends MobEntity>> entities;
	
	public FoodPreferenceEntity(Set<Class<? extends MobEntity>> entities) {
		this.entities = entities;
	}
	
	public boolean canEat(ICapabilityHungryAnimal cap, MobEntity food) {
		return shouldEat(cap) && entities.contains(food.getClass());
	}
	
	/**
	 * this method is for optimization.
	 * 
	 * @return true if should eat something, false to skip search.
	 */
	public boolean shouldEat(ICapabilityHungryAnimal cap) {
		return cap.getStomach() == 0;
	};
	
}
