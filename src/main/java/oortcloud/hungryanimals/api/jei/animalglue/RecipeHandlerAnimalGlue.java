package oortcloud.hungryanimals.api.jei.animalglue;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class RecipeHandlerAnimalGlue implements IRecipeHandler<RecipeAnimalGlue> {

	@Override
	public Class<RecipeAnimalGlue> getRecipeClass() {
		return RecipeAnimalGlue.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategoryAnimalGlue.UID;
	}

	@Override
	public String getRecipeCategoryUid(RecipeAnimalGlue recipe) {
		return RecipeCategoryAnimalGlue.UID;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(RecipeAnimalGlue recipe) {
		return new RecipeWrapperAnimalGlue(recipe);
	}

	@Override
	public boolean isRecipeValid(RecipeAnimalGlue recipe) {
		return true;
	}

}
