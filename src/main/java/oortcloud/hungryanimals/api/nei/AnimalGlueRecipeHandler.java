package oortcloud.hungryanimals.api.nei;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import oortcloud.hungryanimals.configuration.util.HashItemType;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue;

public class AnimalGlueRecipeHandler extends TemplateRecipeHandler {

	public class CachedAnimalGlueRecipe extends CachedRecipe {

		public PositionedStack ingred;
        public PositionedStack result;
        
        public CachedAnimalGlueRecipe(ItemStack ingred, ItemStack result) {
            ingred.stackSize = 1;
            this.ingred = new PositionedStack(ingred, 51, 24);
            this.result = new PositionedStack(result, 111, 24);
        }
        
        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 48, Arrays.asList(ingred));
        }

        @Override
        public PositionedStack getResult() {
            return result;
        }

	}
	
	@Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(79, 22, 16, 20), "animalglue"));
    }
	
	@Override
    public Class<? extends GuiContainer> getGuiClass() {
        return null;
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("api.nei.hanlder.animalglue");
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("animalglue") && getClass() == AnimalGlueRecipeHandler.class) {//don't want subclasses getting a hold of this
            Map<HashItemType, Integer> recipes = (Map<HashItemType, Integer>) RecipeAnimalGlue.getRecipeList();
            for (Entry<HashItemType, Integer> recipe : recipes.entrySet())
                arecipes.add(new CachedAnimalGlueRecipe(recipe.getKey().toItemStack(), new ItemStack(ModItems.animalGlue,recipe.getValue())));
        } else
            super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
    	Map<HashItemType, Integer> recipes = (Map<HashItemType, Integer>) RecipeAnimalGlue.getRecipeList();
        for (Entry<HashItemType, Integer> recipe : recipes.entrySet())
            if (NEIServerUtils.areStacksSameType(new ItemStack(ModItems.animalGlue,recipe.getValue()), result))
            	arecipes.add(new CachedAnimalGlueRecipe(recipe.getKey().toItemStack(), new ItemStack(ModItems.animalGlue,recipe.getValue())));
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
    	Map<HashItemType, Integer> recipes = (Map<HashItemType, Integer>) RecipeAnimalGlue.getRecipeList();
        for (Entry<HashItemType, Integer> recipe : recipes.entrySet())
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getKey().toItemStack(), ingredient)) {
            	CachedAnimalGlueRecipe arecipe = new CachedAnimalGlueRecipe(recipe.getKey().toItemStack(), new ItemStack(ModItems.animalGlue,recipe.getValue()));
                arecipe.setIngredientPermutation(Arrays.asList(arecipe.ingred), ingredient);
                arecipes.add(arecipe);
            }
    }

    @Override
    public String getGuiTexture() {
        return References.RESOURCESPREFIX+"textures/gui/animalglue.png";
    }

    @Override
    public void drawExtras(int recipe) {
        drawProgressBar(79, 22, 176, 0, 16, 20, 40, 7);
    }

    @Override
    public String getOverlayIdentifier() {
        return "animalglue";
    }
	
}
