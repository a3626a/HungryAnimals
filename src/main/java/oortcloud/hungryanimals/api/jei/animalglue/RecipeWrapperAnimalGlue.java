package oortcloud.hungryanimals.api.jei.animalglue;

import com.google.common.collect.Lists;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue.RecipeAnimalGlueEntry;

public class RecipeWrapperAnimalGlue implements IRecipeWrapper {

	private RecipeAnimalGlueEntry recipe;
	private IJeiHelpers jeiHelpers;
	
	public RecipeWrapperAnimalGlue(IJeiHelpers jeiHelpers, RecipeAnimalGlueEntry recipe) {
		this.jeiHelpers = jeiHelpers;
		this.recipe = recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, jeiHelpers.getStackHelper().expandRecipeItemStackInputs(Lists.newArrayList(recipe.ingredient)));
		ingredients.setOutput(ItemStack.class, new ItemStack(ModItems.animalGlue, recipe.count));
	}

}
