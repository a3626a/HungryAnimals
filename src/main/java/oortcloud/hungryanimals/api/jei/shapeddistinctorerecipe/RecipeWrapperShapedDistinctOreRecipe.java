package oortcloud.hungryanimals.api.jei.shapeddistinctorerecipe;

import java.util.List;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.recipes.RotatableListWrapper;
import oortcloud.hungryanimals.recipes.ShapedDistinctOreRecipe;

public class RecipeWrapperShapedDistinctOreRecipe implements IShapedCraftingRecipeWrapper {

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
		List<List<ItemStack>> inputs = getJEIInputs(stackHelper);
		ingredients.setInputLists(ItemStack.class, inputs);
		ingredients.setOutput(ItemStack.class, recipeOutput);
	}
	
    public List<List<ItemStack>> getJEIInputs(IStackHelper stackHelper) {
    	List<List<ItemStack>> inputs = stackHelper.expandRecipeItemStackInputs(recipe.getIngredients());
    	for (int i = 0 ; i < inputs.size(); i++) {
    		if (recipe.getRotation(i) > 0) {
    			List<ItemStack> slot = inputs.get(i);
    			inputs.set(i, new RotatableListWrapper<ItemStack>(slot, recipe.getRotation(i)));
    		}
    	}
    	return inputs;
    }

	@Override
	public int getWidth() { 
		return recipe.getRecipeWidth();
	}

	@Override
	public int getHeight() {
		return recipe.getRecipeHeight();
	}

}
