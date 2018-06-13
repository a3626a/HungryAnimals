package oortcloud.hungryanimals.api.jei.production;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.core.lib.References;

public class RecipeCategoryProduction implements IRecipeCategory<RecipeWrapperProduction> {

	public static final String UID = "hungryanimals.production";
	
	private String localizedName;
	
	private IDrawable background;
	
	public RecipeCategoryProduction(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation(References.MODID, "textures/gui/animalglue.png");
		background = guiHelper.createDrawable(location, 55, 30, 81, 25, 10, 10, 0, 0);
		localizedName = I18n.format("hungryanimals.jei.production");
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
	public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperProduction recipeWrapper, IIngredients ingredients) {
		//recipeLayout.getItemStacks().init(0, true, 0, 14);
		//recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
		recipeLayout.getItemStacks().init(1, true, 60, 14);
		recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).get(0));		
	}

}
