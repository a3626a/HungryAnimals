package oortcloud.hungryanimals.entities.production;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;

public interface IProductionJEI {
	public void getIngredients(IJeiHelpers jeiHelpers, IIngredients ingredients);
	public String getCategoryUid();
}
