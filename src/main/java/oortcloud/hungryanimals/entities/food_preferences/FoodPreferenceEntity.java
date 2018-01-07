package oortcloud.hungryanimals.entities.food_preferences;

import java.util.Set;

import net.minecraft.entity.EntityLiving;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;

public class FoodPreferenceEntity implements IFoodPreferenceSimple<EntityLiving> {
	
	private Set<Class<? extends EntityLiving>> entities;
	
	public FoodPreferenceEntity(Set<Class<? extends EntityLiving>> entities) {
		this.entities = entities;
	}
	
	public boolean canEat(ICapabilityHungryAnimal cap, EntityLiving food) {
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
