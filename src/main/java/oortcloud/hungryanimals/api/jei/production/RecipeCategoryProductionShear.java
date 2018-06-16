package oortcloud.hungryanimals.api.jei.production;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableAnimated.StartDirection;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.core.lib.References;

public class RecipeCategoryProductionShear implements IRecipeCategory<RecipeWrapperProductionShear> {

	public static final String UID = "hungryanimals.production.shear";
	
	private String localizedName;
	
	private IDrawableAnimated progress;
	private IDrawable background;
	private IDrawable icon;
	
	public RecipeCategoryProductionShear(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation(References.MODID, "textures/gui/2_to_1.png");
		background = guiHelper.createDrawable(location, 0, 0, 102, 26, 0, 0, 0, 0);
		localizedName = I18n.format("hungryanimals.jei.production.shear");
		progress = guiHelper.createAnimatedDrawable(guiHelper.createDrawable(location, 0, 26, 24, 16, 5, 0, 44, 0), 64, StartDirection.LEFT, false);
		icon = guiHelper.createDrawableIngredient(new ItemStack(Items.SHEARS));
	}
	
	@Override
	public IDrawable getIcon() {
		return icon;
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
	public void drawExtras(Minecraft minecraft) {
		progress.draw(minecraft);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperProductionShear recipeWrapper, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 0, 4);
		recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
		recipeLayout.getItemStacks().init(1, true, 20, 4);
		recipeLayout.getItemStacks().set(1, ingredients.getInputs(ItemStack.class).get(1));
		recipeLayout.getItemStacks().init(2, false, 80, 4);
		recipeLayout.getItemStacks().set(2, ingredients.getOutputs(ItemStack.class).get(0));
	}

}
