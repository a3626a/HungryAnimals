package oortcloud.hungryanimals.api.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.api.jei.animalglue.RecipeCategoryAnimalGlue;
import oortcloud.hungryanimals.api.jei.animalglue.RecipeWrapperAnimalGlue;
import oortcloud.hungryanimals.api.jei.food_preference.RecipeCategoryFoodPreference;
import oortcloud.hungryanimals.api.jei.food_preference.RecipeWrapperFoodPreferenceItem;
import oortcloud.hungryanimals.api.jei.food_preference.RecipeWrapperFoodPreferenceBlock;
import oortcloud.hungryanimals.api.jei.production.RecipeCategoryProduction;
import oortcloud.hungryanimals.api.jei.production.RecipeWrapperProduction;
import oortcloud.hungryanimals.api.jei.shapeddistinctorerecipe.RecipeWrapperShapedDistinctOreRecipe;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;
import oortcloud.hungryanimals.entities.production.IProductionJEI;
import oortcloud.hungryanimals.entities.production.Productions;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue.RecipeAnimalGlueEntry;
import oortcloud.hungryanimals.recipes.ShapedDistinctOreRecipe;
import oortcloud.hungryanimals.utils.Pair;

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
		registry.addRecipeCategories(new RecipeCategoryProduction(registry.getJeiHelpers().getGuiHelper()));
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
		
		ItemStack spawnEgg = new ItemStack(Items.SPAWN_EGG);
		ItemMonsterPlacer.applyEntityIdToItemStack(spawnEgg, EntityList.getKey(EntityChicken.class));
		registry.addRecipeCatalyst(spawnEgg, RecipeCategoryProduction.UID);
		registry.addRecipes(buildProductions(EntityChicken.class), RecipeCategoryProduction.UID);
		registry.handleRecipes(Pair.class, (pair) -> {
			return new RecipeWrapperProduction(registry.getJeiHelpers(), (Class<? extends EntityLiving>)pair.left, (IProductionJEI)pair.right);
		}, RecipeCategoryProduction.UID);
		
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

	public List<Pair<Class<? extends EntityLiving>, IProductionJEI>> buildProductions(@Nonnull Class<? extends EntityLiving> entityClass) {
		List<IProductionJEI> productions = Productions.getInstance().apply(entityClass);
		if (productions != null) {
			return productions.stream().map((x)->new Pair<Class<? extends EntityLiving>, IProductionJEI>(entityClass, x)).collect(Collectors.toList());
		} else {
			return Lists.newArrayList();
		}
	}
	
}
