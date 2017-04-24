package oortcloud.hungryanimals.api.jei.animalglue;

import java.util.Map.Entry;

import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;

public class RecipeAnimalGlue {

	public HashItemType input;
	public int num;
	
	public RecipeAnimalGlue(Entry<HashItemType, Integer> e) {
		this.input = e.getKey();
		this.num = e.getValue();
	}
	
}
