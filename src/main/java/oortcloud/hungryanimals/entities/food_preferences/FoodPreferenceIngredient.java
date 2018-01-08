package oortcloud.hungryanimals.entities.food_preferences;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;

public class FoodPreferenceIngredient implements IFoodPreference<ItemStack> {

	// Left : nutrient, Right : stomach
	private List<FoodPreferenceIngredientEntry> list;
	
	public static class FoodPreferenceIngredientEntry {
		public Ingredient ingredient;
		public double nutrient;
		public double stomach;
		public FoodPreferenceIngredientEntry(Ingredient ingredient, double nutrient, double stomach) {
			this.ingredient = ingredient;
			this.nutrient = nutrient;
			this.stomach = stomach;
		}
	}
	
	public FoodPreferenceIngredient(List<FoodPreferenceIngredientEntry> list) {
		this.list = list;
	}
	
	@Override
	public double getNutrient(ItemStack food) {
		for (FoodPreferenceIngredientEntry i : list) {
			if (i.ingredient.apply(food)) {
				return i.nutrient;
			}
		}
		return 0;
	}

	@Override
	public double getStomach(ItemStack food) {
		for (FoodPreferenceIngredientEntry i : list) {
			if (i.ingredient.apply(food)) {
				return i.stomach;
			}
		}
		return 0;
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
		return list.toString();
	}
	
}
