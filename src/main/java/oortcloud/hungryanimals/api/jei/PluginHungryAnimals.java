package oortcloud.hungryanimals.api.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.api.jei.animalglue.RecipeCategoryAnimalGlue;
import oortcloud.hungryanimals.api.jei.animalglue.RecipeHandlerAnimalGlue;
import oortcloud.hungryanimals.api.jei.shapeddistinctorerecipe.RecipeHandlerShapedDistinctOreRecipe;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue;

@JEIPlugin
public class PluginHungryAnimals implements IModPlugin {

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
		
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {
		
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void register(IModRegistry registry) {
		List<oortcloud.hungryanimals.api.jei.animalglue.RecipeAnimalGlue> recipes = new ArrayList<oortcloud.hungryanimals.api.jei.animalglue.RecipeAnimalGlue>();
		for (Entry<HashItemType, Integer> i : RecipeAnimalGlue.getRecipeList().entrySet()) {
			recipes.add(new oortcloud.hungryanimals.api.jei.animalglue.RecipeAnimalGlue(i));
		}
		registry.addRecipes(recipes);
		registry.addRecipeHandlers(new RecipeHandlerShapedDistinctOreRecipe(registry.getJeiHelpers()));
		registry.addRecipeHandlers(new RecipeHandlerAnimalGlue());
		registry.addRecipeCategories(new RecipeCategoryAnimalGlue(registry.getJeiHelpers().getGuiHelper()));
		registry.addDescription(new ItemStack(ModItems.animalGlue,1,0), "hungryanimals.jei.animalglue.description");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		
	}



}
