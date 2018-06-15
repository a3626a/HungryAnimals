package oortcloud.hungryanimals.api.jei.food_preference;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.core.lib.References;

public class RecipeCategoryFoodPreference implements IRecipeCategory<RecipeWrapperFoodPreferenceItem> {

	public static final String UID = "hungryanimals.food_preference";
	
	private String localizedName;
	
	private IDrawable background;
	
	public RecipeCategoryFoodPreference(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation(References.MODID, "textures/gui/food_preference.png");
		background = guiHelper.createDrawable(location, 7, 53, 162, 106, 0, 0, 0, 0);
		localizedName = I18n.format("hungryanimals.jei.food_preference");
	}
	
	@Override
	public String getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return localizedName;
	}

	@Override
	public String getModName() {
		return References.MODNAME;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperFoodPreferenceItem recipeWrapper, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, false, 72, 0);
		recipeLayout.getItemStacks().set(0, ingredients.getOutputs(ItemStack.class).get(0));
		
		for (int i = 0 ; i < ingredients.getInputs(ItemStack.class).size(); i++) {
			int index = i+1;
			int row = i/9;
			int col = i%9;
			recipeLayout.getItemStacks().init(index, true, 0+18*col, 22+22*row);
			recipeLayout.getItemStacks().set(index, ingredients.getInputs(ItemStack.class).get(i));		
		}
		
		recipeLayout.getItemStacks().addTooltipCallback(recipeWrapper);
	}

	
}
