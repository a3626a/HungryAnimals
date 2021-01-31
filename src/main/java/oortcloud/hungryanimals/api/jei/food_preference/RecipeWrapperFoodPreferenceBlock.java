package oortcloud.hungryanimals.api.jei.food_preference;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.MobEntity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferences;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreference;
import oortcloud.hungryanimals.utils.Pair;

public class RecipeWrapperFoodPreferenceBlock extends RecipeWrapperFoodPreferenceItem {

	List<Pair<Double, Double>> nutrientAndStomach;

	public RecipeWrapperFoodPreferenceBlock(IJeiHelpers jeiHelpers, Class<? extends MobEntity> entityClass) {
		super(jeiHelpers, entityClass);
		nutrientAndStomach = new ArrayList<>();
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ItemStack spawnEgg = new ItemStack(Items.SPAWN_EGG);
		ItemMonsterPlacer.applyEntityIdToItemStack(spawnEgg, EntityList.getKey(entityClass));
		ingredients.setOutput(ItemStack.class, spawnEgg);

		IFoodPreference<BlockState> pref = FoodPreferences.getInstance().REGISTRY_BLOCK.get(entityClass);
		if (pref instanceof FoodPreferenceBlockState) {
			List<BlockState> list = ((FoodPreferenceBlockState) pref).getKeys();
			// TODO Convert BlockState to ItemStack is sometime impossible.
			//      because some blocks don't have placer.
			//      rendering BlockState itself?
			ingredients.setInputLists(ItemStack.class, jeiHelpers.getStackHelper().expandRecipeItemStackInputs(
					list.stream().map((entry) -> new ItemStack(ItemBlock.getItemFromBlock(entry.getBlock()), 1, entry.getBlock().getMetaFromState(entry))).collect(Collectors.toList())));
			list.forEach((blockstate) -> {
				nutrientAndStomach.add(new Pair<>(pref.getNutrient(blockstate), pref.getStomach(blockstate)));
			});
		}
	}

	@Override
	public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
		if (slotIndex > 0) {
			tooltip.add(String.format("nutrient : %.2fkg", nutrientAndStomach.get(slotIndex - 1).left));
			tooltip.add(String.format("stomach : %.2fkg", nutrientAndStomach.get(slotIndex - 1).right));
		}
	}

}
