package oortcloud.hungryanimals.api.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.api.jei.animalglue.RecipeCategoryAnimalGlue;
import oortcloud.hungryanimals.api.jei.animalglue.RecipeWrapperAnimalGlue;
import oortcloud.hungryanimals.api.jei.shapeddistinctorerecipe.RecipeWrapperShapedDistinctOreRecipe;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue.RecipeAnimalGlueEntry;
import oortcloud.hungryanimals.recipes.ShapedDistinctOreRecipe;

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
		registry.addRecipeCategories(new RecipeCategoryAnimalGlue(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void register(IModRegistry registry) {
		registry.addRecipes(RecipeAnimalGlue.getRecipeList(), RecipeCategoryAnimalGlue.UID);
		registry.handleRecipes(RecipeAnimalGlueEntry.class, (recipe) -> {
			return new RecipeWrapperAnimalGlue(registry.getJeiHelpers(), recipe);
		}, RecipeCategoryAnimalGlue.UID);
		registry.handleRecipes(ShapedDistinctOreRecipe.class, (recipe) -> {
			return new RecipeWrapperShapedDistinctOreRecipe(registry.getJeiHelpers(), recipe);
		}, VanillaRecipeCategoryUid.CRAFTING);
		registry.addIngredientInfo(new ItemStack(ModItems.animalGlue, 1, 0), ItemStack.class, "hungryanimals.jei.animalglue.description");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

	}

}
