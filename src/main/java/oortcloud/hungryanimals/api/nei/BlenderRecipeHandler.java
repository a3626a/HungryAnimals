package oortcloud.hungryanimals.api.nei;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import oortcloud.hungryanimals.api.nei.AnimalGlueRecipeHandler.CachedAnimalGlueRecipe;
import oortcloud.hungryanimals.configuration.util.HashItemType;
import oortcloud.hungryanimals.configuration.util.HashPairedItemType;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue;
import oortcloud.hungryanimals.recipes.RecipeBlender;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class BlenderRecipeHandler extends TemplateRecipeHandler {
	
	public class CachedBlenderRecipe extends CachedRecipe {

		public PositionedStack[] ingred;
        public PositionedStack result;
        
        public CachedBlenderRecipe(ItemStack ingred1, ItemStack ingred2, ItemStack result) {
            this.ingred = new PositionedStack[2];
            this.ingred[0] = new PositionedStack(ingred1, 51, 15);
            this.ingred[1] = new PositionedStack(ingred2, 51, 33);
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
        transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), "blender"));
    }
	
	@Override
    public Class<? extends GuiContainer> getGuiClass() {
        return null;
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("api.nei.hanlder.blender");
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("blender") && getClass() == BlenderRecipeHandler.class) {//don't want subclasses getting a hold of this
            Map<HashPairedItemType, ItemStack> recipes = (Map<HashPairedItemType, ItemStack>) RecipeBlender.getRecipeList();
            for (Entry<HashPairedItemType, ItemStack> recipe : recipes.entrySet())
                arecipes.add(new CachedBlenderRecipe(recipe.getKey().getLeft().toItemStack(), recipe.getKey().getRight().toItemStack(), recipe.getValue()));
        } else
            super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
    	Map<HashPairedItemType, ItemStack> recipes = (Map<HashPairedItemType, ItemStack>) RecipeBlender.getRecipeList();
        for (Entry<HashPairedItemType, ItemStack> recipe : recipes.entrySet())
            if (NEIServerUtils.areStacksSameType(recipe.getValue(), result))
            	arecipes.add(new CachedBlenderRecipe(recipe.getKey().getLeft().toItemStack(), recipe.getKey().getRight().toItemStack(), recipe.getValue()));
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
    	Map<HashPairedItemType, ItemStack> recipes = (Map<HashPairedItemType, ItemStack>) RecipeBlender.getRecipeList();
        for (Entry<HashPairedItemType, ItemStack> recipe : recipes.entrySet())
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getKey().getLeft().toItemStack(), ingredient)||NEIServerUtils.areStacksSameTypeCrafting(recipe.getKey().getRight().toItemStack(), ingredient)) {
            	CachedBlenderRecipe arecipe = new CachedBlenderRecipe(recipe.getKey().getLeft().toItemStack(), recipe.getKey().getRight().toItemStack(), recipe.getValue());
                arecipe.setIngredientPermutation(Arrays.asList(arecipe.ingred), ingredient);
                arecipes.add(arecipe);
            }
    }

    @Override
    public String getGuiTexture() {
        return References.RESOURCESPREFIX+"textures/gui/blender.png";
    }

    @Override
    public void drawExtras(int recipe) {
        drawProgressBar(74, 23, 176, 0, 24, 16, 48, 0);
    }

    @Override
    public String getOverlayIdentifier() {
        return "blender";
    }
	
}
