package oortcloud.hungryanimals.api.jei.animalglue;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableAnimated.StartDirection;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.items.ModItems;

public class RecipeCategoryAnimalGlue implements IRecipeCategory<RecipeWrapperAnimalGlue> {

	public static final String UID = "hungryanimals.animalglue";

	private String localizedName;
	
	private IDrawableAnimated progress;
	private IDrawable background;
	private IDrawable icon;
	
	public RecipeCategoryAnimalGlue(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation(References.MODID, "textures/gui/animalglue.png");
		background = guiHelper.createDrawable(location, 55, 30, 81, 25, 10, 10, 0, 0);
		localizedName = I18n.format("hungryanimals.jei.animalglue");
		progress = guiHelper.createAnimatedDrawable(guiHelper.createDrawable(location, 176, 0, 16, 20, 13, 0, 29, 0), 64, StartDirection.TOP, false);
		icon = guiHelper.createDrawableIngredient(new ItemStack(ModItems.animalGlue));
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
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperAnimalGlue recipeWrapper, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 0, 14);
		recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
		
		recipeLayout.getItemStacks().init(1, true, 60, 14);
		recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).get(0));
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {
		progress.draw(minecraft);
	}
	
	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public String getModName() {
		return References.MODNAME;
	}
	
}
