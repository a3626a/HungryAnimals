package oortcloud.hungryanimals.api.nei;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import oortcloud.hungryanimals.core.lib.References;
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

		public void setIngredientPermutation(Collection<PositionedStack> ingredients, ItemStack ingredient) {
			Character shape = null;
			PositionedStack[] stacks = new PositionedStack[ingredients.size()];
			stacks = ingredients.toArray(stacks);
			for (int i = 0; i < ingredients.size(); i++) {
				PositionedStack stack = stacks[i];
				if (ores[i] == null)
					for (int j = 0; j < stack.items.length; j++) {
						if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, stack.items[j])) {
							stack.item = stack.items[j];
							stack.item.setItemDamage(ingredient.getItemDamage());
							stack.items = new ItemStack[] { stack.item };
							stack.setPermutationToRender(0);
							break;
						}
					}
				else {
					if (shape==null) {
						for (int j = 0; j < stack.items.length; j++) {
							if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, stack.items[j])) {
								stack.item = stack.items[j];
								stack.item.setItemDamage(ingredient.getItemDamage());
								stack.items = new ItemStack[] { stack.item };
								stack.setPermutationToRender(0);
								shape = characters[i];
								break;
							}
						}
					} else if (shape.equals(characters[i])) {
						for (int j = 0; j < stack.items.length; j++) {
							if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, stack.items[j])) {
								stack.item = stack.items[j];
								stack.item.setItemDamage(ingredient.getItemDamage());
								stack.items = new ItemStack[] { stack.item };
								stack.setPermutationToRender(0);
								break;
							}
						}
					}
				}
			}
		}

		@Override
		public List<PositionedStack> getIngredients() {
			int cycle = cycleticks / 20;

			HashMap<String, HashMap<Character, ItemStack>> permMap = new HashMap<String, HashMap<Character, ItemStack>>();

			for (int itemIndex = 0; itemIndex < ingredients.size(); itemIndex++) {
				if (ores[itemIndex] != null) {
					if (permMap.containsKey(ores[itemIndex])) {
						HashMap<Character, ItemStack> charMap = permMap.get(ores[itemIndex]);
						if (charMap.containsKey(characters[itemIndex])) {
							ingredients.get(itemIndex).item = charMap.get(characters[itemIndex]).copy();
					        if(ingredients.get(itemIndex).item.getItem() == null)
					        	ingredients.get(itemIndex).item = new ItemStack(Blocks.fire);
					        else if(ingredients.get(itemIndex).item.getItemDamage() == OreDictionary.WILDCARD_VALUE)
					        	ingredients.get(itemIndex).item.setItemDamage(0);
						} else {
							Random rand = new Random(cycle + itemIndex + offset);
							int preIndex = Math.abs(rand.nextInt()) % (ingredients.get(itemIndex).items.length - charMap.size());
							// TODO Exception Handling ; number of ores in oredictname < characters with same oredictname
							Integer[] indices = new Integer[charMap.size()];
							int next=0;
							for (ItemStack i : charMap.values()) {
								ItemStack[] stacks = ingredients.get(itemIndex).items;
								for (int j = 0 ; j < stacks.length; j++) {
									if (OreDictionary.itemMatches(i, stacks[j], false)) {
										indices[next++] = j;
									}
								}
							}
							Arrays.sort(indices);

							for (int i = 0; i < indices.length; i++) {
								if (indices[i].intValue() <= preIndex) {
									preIndex += 1;
								}
							}
							
							ingredients.get(itemIndex).setPermutationToRender(preIndex);
							
							charMap.put(characters[itemIndex], ingredients.get(itemIndex).item);
						}
					} else {
						Random rand = new Random(cycle + itemIndex + offset);
						int index = Math.abs(rand.nextInt()) % ingredients.get(itemIndex).items.length;
						ingredients.get(itemIndex).setPermutationToRender(index);
						HashMap<Character, ItemStack> map = new HashMap<Character, ItemStack>();
						map.put(characters[itemIndex], ingredients.get(itemIndex).item);
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
		return StatCollector.translateToLocal("api.nei.hanlder.compositewoodcrafting");
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("compositewoodcrafting") && getClass() == CompositeWoodRecipeHandler.class) {
			for (IRecipe irecipe : (List<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
				CachedDistinctOreShapedRecipe recipe = null;
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
				CachedDistinctOreShapedRecipe recipe = null;
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
			CachedDistinctOreShapedRecipe recipe = null;
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
	public String getOverlayIdentifier() {
		return "compositewoodcrafting";
	}

}
