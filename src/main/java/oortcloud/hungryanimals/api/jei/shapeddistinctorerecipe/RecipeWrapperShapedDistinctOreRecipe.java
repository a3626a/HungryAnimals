package oortcloud.hungryanimals.api.jei.shapeddistinctorerecipe;

import java.util.Arrays;
import java.util.List;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.recipes.ShapedDistinctOreRecipe;

@SuppressWarnings("unchecked")
public class RecipeWrapperShapedDistinctOreRecipe extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper {

	private ShapedDistinctOreRecipe recipe;
	private IJeiHelpers jeiHelpers;
	
	public RecipeWrapperShapedDistinctOreRecipe(IJeiHelpers jeiHelpers, ShapedDistinctOreRecipe recipe) {
		this.jeiHelpers = jeiHelpers;
		this.recipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ItemStack recipeOutput = recipe.getRecipeOutput();
		IStackHelper stackHelper = jeiHelpers.getStackHelper();
		List<List<ItemStack>> inputs = stackHelper.expandRecipeItemStackInputs(Arrays.asList(recipe.getInput()));
		ingredients.setInputLists(ItemStack.class, inputs);
		ingredients.setOutput(ItemStack.class, recipeOutput);
	}

	@Override
	public int getWidth() { 
		return recipe.getWidth();
	}

	@Override
	public int getHeight() {
		return recipe.getHeight();
	}

}
