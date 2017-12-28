package oortcloud.hungryanimals.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedDistinctOreRecipe extends ShapedOreRecipe {

	// Added in for future ease of change, but hard coded for now.
	public static final int MAX_CRAFT_GRID_WIDTH = 3;
	public static final int MAX_CRAFT_GRID_HEIGHT = 3;

	protected List<Character> characters = null;
	protected List<Integer> rotations = null;

	public static class ShapedPrimerDistinct extends ShapedPrimer {
		public List<Character> characters;
		public List<Integer> rotations;
	}

	public ShapedDistinctOreRecipe(ResourceLocation group, Block result, Object... recipe) {
		this(group, new ItemStack(result), parse(recipe));
	}

	public ShapedDistinctOreRecipe(ResourceLocation group, Item result, Object... recipe) {
		this(group, new ItemStack(result), parse(recipe));
	}

	public ShapedDistinctOreRecipe(ResourceLocation group, ItemStack result, Object... recipe) {
		this(group, result, parse(recipe));
	}

	public ShapedDistinctOreRecipe(ResourceLocation group, ItemStack result, ShapedPrimerDistinct primer) {
		super(group, result, primer);
		//setRegistryName(group);
		characters = primer.characters;
		rotations = primer.rotations;
	}

	public static ShapedPrimerDistinct parse(boolean mirrored, List<String> pattern, Map<Character, Ingredient> ingMap, Map<Character, String> oreMap) {
		ShapedPrimerDistinct ret = new ShapedPrimerDistinct();
		ret.mirrored = mirrored;
		ret.width = pattern.get(0).length();
		ret.height = pattern.size();
		
		ret.input = NonNullList.withSize(ret.width * ret.height, Ingredient.EMPTY);

		HashMap<Character, Integer> charToRotation = new HashMap<Character, Integer>();
		HashMap<String, Integer> oreToLastRotation = new HashMap<String, Integer>();
		Set<Character> keys = Sets.newHashSet(ingMap.keySet());
		keys.remove(' ');

		int x = 0;
		ret.characters = new ArrayList<Character>();
		ret.rotations = new ArrayList<Integer>();
		for (String str : pattern) {
			for (char chr : str.toCharArray()) {
				Ingredient ing = ingMap.get(chr);
				if (ing == null)
					throw new IllegalArgumentException("Pattern references symbol '" + chr + "' but it's not defined in the key");

				if (oreMap.containsKey(chr)) {
					if (!charToRotation.containsKey(chr)) {
						if (!oreToLastRotation.containsKey(oreMap.get(chr))) {
							oreToLastRotation.put(oreMap.get(chr), 0);
						} else {
							oreToLastRotation.put(oreMap.get(chr), oreToLastRotation.get(oreMap.get(chr)) + 1);
						}
						charToRotation.put(chr, oreToLastRotation.get(oreMap.get(chr)));
					}
					ret.rotations.add(charToRotation.get(chr));
					ret.characters.add(chr);
				} else {
					ret.rotations.add(-1);
					ret.characters.add(null);
				}

				ret.input.set(x++, ing);
				keys.remove(chr);
			}
		}

		if (!keys.isEmpty())
			throw new IllegalArgumentException("Key defines symbols that aren't used in pattern: " + keys);

		return ret;
	}

	/**
	 * This code is based on net.minecraftforge.common.crafting.CraftingHelper.parseShaped
	 * @param recipe
	 * @return
	 */
	public static ShapedPrimerDistinct parse(Object... recipe) {
		ArrayList<String> pattern = new ArrayList<String>();
		int idx = 0;
		
		boolean mirrored = false;
		
		if (recipe[idx] instanceof Boolean) {
			mirrored = (Boolean) recipe[idx];
			if (recipe[idx + 1] instanceof Object[])
				recipe = (Object[]) recipe[idx + 1];
			else
				idx = 1;
		}

		if (recipe[idx] instanceof String[]) {
			String[] parts = ((String[]) recipe[idx++]);
			for (String s : parts) {
				pattern.add(s);
			}
		} else {
			while (recipe[idx] instanceof String) {
				String s = (String) recipe[idx++];
				pattern.add(s);
			}
		}

		int width = pattern.get(0).length();
		for (String s : pattern) {
			if (s.length() != width) {
				String err = "Invalid shaped ore recipe: ";
				for (Object tmp : recipe) {
					err += tmp + ", ";
				}
				throw new RuntimeException(err);
			}
		}

		HashMap<Character, Ingredient> ingMap = Maps.newHashMap();
		ingMap.put(' ', Ingredient.EMPTY);
		Map<Character, String> oreMap = new HashMap<Character, String>();
		for (; idx < recipe.length; idx += 2) {
			Character chr = (Character) recipe[idx];
			Object in = recipe[idx + 1];
			if (in instanceof String) {
				oreMap.put(chr, (String) in);
			}
			Ingredient ing = CraftingHelper.getIngredient(in);

			if (' ' == chr.charValue())
				throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

			if (ing != null) {
				ingMap.put(chr, ing);
			} else {
				String err = "Invalid shaped ore recipe: ";
				for (Object tmp : recipe) {
					err += tmp + ", ";
				}
				throw new RuntimeException(err);
			}
		}

		return parse(mirrored, pattern, ingMap, oreMap);
	}

	@Override
	protected boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
		return super.checkMatch(inv, startX, startY, mirror) && checkDistinct(inv, startX, startY, mirror);
	}

	protected boolean checkDistinct(InventoryCrafting inv, int startX, int startY, boolean mirror) {

		HashMap<Character, ItemStack> map = new HashMap<Character, ItemStack>();

		for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++) {
			for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++) {
				int subX = x - startX;
				int subY = y - startY;
				Character target = null;

				if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
					if (mirror) {
						target = characters.get(width - subX - 1 + subY * width);
					} else {
						target = characters.get(subX + subY * width);
					}
				}

				ItemStack slot = inv.getStackInRowAndColumn(x, y);

				if (target != null) {
					if (!map.containsKey(target)) {
						for (ItemStack i : map.values()) {
							if (OreDictionary.itemMatches(i, slot, false)) {
								return false;
							}
						}
						map.put(target, slot);
					}
					if (map.containsKey(target)) {
						if (!OreDictionary.itemMatches(map.get(target), slot, false)) {
							return false;
						}
					}
				}

			}
		}

		return true;
	}

	public int getRotation(int index) {
		return rotations.get(index);
	}
}
