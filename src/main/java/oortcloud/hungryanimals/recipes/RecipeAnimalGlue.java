package oortcloud.hungryanimals.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnull;

public class RecipeAnimalGlue {

	private static List<RecipeAnimalGlueEntry> recipe;

	public static class RecipeAnimalGlueEntry {
		public Ingredient ingredient;
		public int count;
		public RecipeAnimalGlueEntry(Ingredient ing, int count) {
			this.ingredient = ing;
			this.count = count;
		}
	}
	
	public static List<RecipeAnimalGlueEntry> getRecipeList() {
		return recipe;
	}
	
	public static void init() {
		recipe = new ArrayList<RecipeAnimalGlueEntry>();
	}

	public static void addRecipe(@Nonnull Ingredient input, int output) {
		recipe.add(new RecipeAnimalGlueEntry(input, output));
	}

	public static int getRecipe(ItemStack item) {
		for (RecipeAnimalGlueEntry i : recipe) {
			if (i.ingredient.apply(item)) {
				return i.count;
			}
		}
		return 0;
	}
	
}
