package oortcloud.hungryanimals.api.jei.animalglue;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.items.ModItems;

public class RecipeWrapperAnimalGlue extends BlankRecipeWrapper	{

	private RecipeAnimalGlue recipe;
	
	public RecipeWrapperAnimalGlue(RecipeAnimalGlue recipe) {
		this.recipe = recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, recipe.input.toItemStack());
		ingredients.setOutput(ItemStack.class, new ItemStack(ModItems.animalGlue, recipe.num));
	}

}
