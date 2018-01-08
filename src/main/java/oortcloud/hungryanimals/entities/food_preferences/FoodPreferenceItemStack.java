package oortcloud.hungryanimals.entities.food_preferences;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.event.EntityEventHandler.Pair;
import oortcloud.hungryanimals.utils.HashItemType;

public class FoodPreferenceItemStack implements IFoodPreference<ItemStack> {

	// Left : nutrient, Right : stomach
	private Map<HashItemType, Pair<Double, Double>> map = new HashMap<HashItemType, Pair<Double, Double>>();
	
	public FoodPreferenceItemStack(Map<HashItemType, Pair<Double, Double>> map) {
		this.map = map;
	}
	
	@Override
	public double getNutrient(ItemStack food) {
		HashItemType key;

		if (this.map.containsKey(key = new HashItemType(food.getItem()))) {
			return this.map.get(key).left;
		} else if (this.map.containsKey(key = new HashItemType(food.getItem(), food.getItemDamage()))) {
			return this.map.get(key).left;
		} else {
			return 0;
		}
	}

	@Override
	public double getStomach(ItemStack food) {
		HashItemType key;

		if (this.map.containsKey(key = new HashItemType(food.getItem()))) {
			return this.map.get(key).right;
		} else if (this.map.containsKey(key = new HashItemType(food.getItem(), food.getItemDamage()))) {
			return this.map.get(key).right;
		} else {
			return 0;
		}
	}
	

	@Override
	public boolean canEat(ICapabilityHungryAnimal cap, ItemStack food) {
		double stomach = getStomach(food);
		return stomach > 0 && shouldEat(cap);
	}

	@Override
	public boolean shouldEat(ICapabilityHungryAnimal cap) {
		return cap.getStomach() < cap.getMaxStomach();
	}

	@Override
	public String toString() {
		return map.toString();
	}
	
}
