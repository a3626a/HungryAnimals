package oortcloud.hungryanimals.api.jei.food_preference;

import java.util.List;
import java.util.stream.Collectors;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.MobEntity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceIngredient;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceIngredient.FoodPreferenceIngredientEntry;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferences;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreference;

public class RecipeWrapperFoodPreferenceItem implements IRecipeWrapper, ITooltipCallback<ItemStack> {

	protected Class<? extends MobEntity> entityClass;
	protected IJeiHelpers jeiHelpers;

	public RecipeWrapperFoodPreferenceItem(IJeiHelpers jeiHelpers, Class<? extends MobEntity> entityClass) {
		this.jeiHelpers = jeiHelpers;
		this.entityClass = entityClass;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ItemStack spawnEgg = new ItemStack(Items.SPAWN_EGG);
		ItemMonsterPlacer.applyEntityIdToItemStack(spawnEgg, EntityList.getKey(entityClass));
		ingredients.setOutput(ItemStack.class, spawnEgg);
		
		IFoodPreference<ItemStack> pref = FoodPreferences.getInstance().REGISTRY_ITEM.get(entityClass);
		if (pref instanceof FoodPreferenceIngredient) {
			List<FoodPreferenceIngredientEntry> list = ((FoodPreferenceIngredient) pref).getList();
			ingredients.setInputLists(ItemStack.class, jeiHelpers.getStackHelper().expandRecipeItemStackInputs(list.stream().map((entry)->entry.ingredient).collect(Collectors.toList())));
		}
	}

	@Override
	public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
		double nutrient = FoodPreferences.getInstance().REGISTRY_ITEM.get(entityClass).getNutrient(ingredient);
		double stomach = FoodPreferences.getInstance().REGISTRY_ITEM.get(entityClass).getStomach(ingredient);
		tooltip.add(String.format("nutrient : %.2fkg", nutrient));
		tooltip.add(String.format("stomach : %.2fkg", stomach));
	}

}
