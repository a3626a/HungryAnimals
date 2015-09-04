package oortcloud.hungryanimals.api.nei;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.recipes.ShapedDistinctOreRecipe;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;

public class CompositeWoodRecipeHandler extends ShapedRecipeHandler {

	public class CachedDistinctOreShapedRecipe extends CachedShapedRecipe {

		final long offset = System.currentTimeMillis();

		private Character[] characters;
		private String[] ores;

		public CachedDistinctOreShapedRecipe(int width, int height, Object[] items, Character[] character, String[] ore, ItemStack out) {
			super(width, height, items, out);
			characters = character;
			ores = ore;
		}

		@Override
		public List<PositionedStack> getIngredients() {
			int cycle = cycleticks / 20;

			HashMap<String, HashMap<Character, Integer>> permMap = new HashMap<String, HashMap<Character, Integer>>();

			for (int itemIndex = 0; itemIndex < ingredients.size(); itemIndex++) {
				if (ores[itemIndex] != null) {
					if (permMap.containsKey(ores[itemIndex])) {
						HashMap<Character, Integer> charMap = permMap.get(ores[itemIndex]);
						if (charMap.containsKey(characters[itemIndex])) {
							ingredients.get(itemIndex).setPermutationToRender(charMap.get(characters[itemIndex]));
						} else {
							Random rand = new Random(cycle + itemIndex + offset);
							int preIndex = Math.abs(rand.nextInt()) % (ingredients.get(itemIndex).items.length - charMap.size());
							// TODO Exception Handling ; number of ores in
							// oredictname < characters with same oredictname
							Integer[] indices = new Integer[charMap.size()];
							indices = charMap.values().toArray(indices);
							Arrays.sort(indices);

							for (int i = 0; i < indices.length; i++) {
								if (indices[i].intValue() <= preIndex) {
									preIndex += 1;
								}
							}

							charMap.put(characters[itemIndex], preIndex);

							ingredients.get(itemIndex).setPermutationToRender(preIndex);
						}
					} else {
						Random rand = new Random(cycle + itemIndex + offset);
						int index = Math.abs(rand.nextInt()) % ingredients.get(itemIndex).items.length;
						ingredients.get(itemIndex).setPermutationToRender(index);
						HashMap<Character, Integer> map = new HashMap<Character, Integer>();
						map.put(characters[itemIndex], index);
						permMap.put(ores[itemIndex], map);
					}
				} else {
					randomRenderPermutation(ingredients.get(itemIndex), cycle + itemIndex);
				}
			}

			return ingredients;
		}

		@Override
		public void setIngredients(int width, int height, Object[] items) {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (items[y * width + x] == null)
						continue;

					PositionedStack stack = new PositionedStack(items[y * width + x], 25 + x * 18, 6 + y * 18, false);
					stack.setMaxSize(1);
					ingredients.add(stack);
				}
			}
		}
	}

	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 18), "compositewoodcrafting"));
	}

	@Override
	public String getRecipeName() {
		return "compositewoodcrafting";
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("compositewoodcrafting") && getClass() == CompositeWoodRecipeHandler.class) {
			for (IRecipe irecipe : (List<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
				CachedShapedRecipe recipe = null;
				if (irecipe instanceof ShapedDistinctOreRecipe)
					recipe = forgeShapedRecipe((ShapedDistinctOreRecipe) irecipe);

				if (recipe == null)
					continue;

				recipe.computeVisuals();
				arecipes.add(recipe);
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		for (IRecipe irecipe : (List<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
			if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
				CachedShapedRecipe recipe = null;
				if (irecipe instanceof ShapedDistinctOreRecipe)
					recipe = forgeShapedRecipe((ShapedDistinctOreRecipe) irecipe);

				if (recipe == null)
					continue;

				recipe.computeVisuals();
				arecipes.add(recipe);
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		for (IRecipe irecipe : (List<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
			CachedShapedRecipe recipe = null;
			if (irecipe instanceof ShapedDistinctOreRecipe)
				recipe = forgeShapedRecipe((ShapedDistinctOreRecipe) irecipe);

			if (recipe == null || !recipe.contains(recipe.ingredients, ingredient.getItem()))
				continue;

			recipe.computeVisuals();
			if (recipe.contains(recipe.ingredients, ingredient)) {
				recipe.setIngredientPermutation(recipe.ingredients, ingredient);
				arecipes.add(recipe);
			}
		}
	}

	public CachedDistinctOreShapedRecipe forgeShapedRecipe(ShapedDistinctOreRecipe recipe) {
		int width = recipe.getWidth();
		int height = recipe.getHeight();

		Object[] items = recipe.getInput();
		for (Object item : items)
			if (item instanceof List && ((List<?>) item).isEmpty())// ore
																	// handler,
																	// no ores
				return null;

		return new CachedDistinctOreShapedRecipe(width, height, items, recipe.getCharacters(), recipe.getOres(), recipe.getRecipeOutput());
	}

	@Override
	public String getGuiTexture() {
		return "textures/gui/container/crafting_table.png";
	}

	@Override
	public String getOverlayIdentifier() {
		return "compositewoodcrafting";
	}

}
