package oortcloud.hungryanimals.api.jei;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.api.jei.animalglue.RecipeCategoryAnimalGlue;
import oortcloud.hungryanimals.api.jei.animalglue.RecipeWrapperAnimalGlue;
import oortcloud.hungryanimals.api.jei.food_preference.RecipeCategoryFoodPreference;
import oortcloud.hungryanimals.api.jei.food_preference.RecipeWrapperFoodPreferenceBlock;
import oortcloud.hungryanimals.api.jei.food_preference.RecipeWrapperFoodPreferenceItem;
import oortcloud.hungryanimals.api.jei.production.RecipeCategoryProductionEgg;
import oortcloud.hungryanimals.api.jei.production.RecipeCategoryProductionFluid;
import oortcloud.hungryanimals.api.jei.production.RecipeCategoryProductionMilk;
import oortcloud.hungryanimals.api.jei.production.RecipeCategoryProductionShear;
import oortcloud.hungryanimals.api.jei.production.RecipeWrapperProductionEgg;
import oortcloud.hungryanimals.api.jei.production.RecipeWrapperProductionFluid;
import oortcloud.hungryanimals.api.jei.production.RecipeWrapperProductionMilk;
import oortcloud.hungryanimals.api.jei.production.RecipeWrapperProductionShear;
import oortcloud.hungryanimals.api.jei.shapeddistinctorerecipe.RecipeWrapperShapedDistinctOreRecipe;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;
import oortcloud.hungryanimals.entities.production.IProductionJEI;
import oortcloud.hungryanimals.entities.production.Productions;
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
		registry.addRecipeCategories(new RecipeCategoryProductionEgg(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new RecipeCategoryProductionFluid(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new RecipeCategoryProductionMilk(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new RecipeCategoryProductionShear(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new RecipeCategoryFoodPreference(registry.getJeiHelpers().getGuiHelper()));
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

		List<RecipeWrapperProductionEgg> productionEggs = new ArrayList<>();
		List<RecipeWrapperProductionFluid> productionFluids = new ArrayList<>();
		List<RecipeWrapperProductionMilk> productionMilks = new ArrayList<>();
		List<RecipeWrapperProductionShear> productionShears = new ArrayList<>();
		for (Class<? extends EntityLiving> i : HungryAnimalManager.getInstance().getRegisteredAnimal()) {
			List<IProductionJEI> productions = Productions.getInstance().apply(i);
			if (productions != null) {
				for (IProductionJEI j : productions) {
					if (j.getCategoryUid().equals(RecipeCategoryProductionEgg.UID)) {
						productionEggs.add(new RecipeWrapperProductionEgg(registry.getJeiHelpers(), i, j));
					} else if (j.getCategoryUid().equals(RecipeCategoryProductionFluid.UID)) {
						productionFluids.add(new RecipeWrapperProductionFluid(registry.getJeiHelpers(), i, j));
					} else if (j.getCategoryUid().equals(RecipeCategoryProductionMilk.UID)) {
						productionMilks.add(new RecipeWrapperProductionMilk(registry.getJeiHelpers(), i, j));
					} else if (j.getCategoryUid().equals(RecipeCategoryProductionShear.UID)) {
						productionShears.add(new RecipeWrapperProductionShear(registry.getJeiHelpers(), i, j));
					}
				}
			}
		}
		registry.addRecipes(productionEggs, RecipeCategoryProductionEgg.UID);
		registry.addRecipes(productionFluids, RecipeCategoryProductionFluid.UID);
		registry.addRecipes(productionMilks, RecipeCategoryProductionMilk.UID);
		registry.addRecipes(productionShears, RecipeCategoryProductionShear.UID);
		
		// Food Preference
		List<RecipeWrapperFoodPreferenceItem> foodPreferences = new ArrayList<>();
		for (Class<? extends EntityLiving> i : HungryAnimalManager.getInstance().getRegisteredAnimal()) {
			foodPreferences.add(new RecipeWrapperFoodPreferenceItem(registry.getJeiHelpers(), i));
			foodPreferences.add(new RecipeWrapperFoodPreferenceBlock(registry.getJeiHelpers(), i));
		}
		registry.addRecipes(foodPreferences, RecipeCategoryFoodPreference.UID);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
	}

}
