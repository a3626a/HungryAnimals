package oortcloud.hungryanimals.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.configuration.util.ConfigurationHelper;
import oortcloud.hungryanimals.configuration.util.HashItemType;
import oortcloud.hungryanimals.configuration.util.HashPairedItemType;
import oortcloud.hungryanimals.configuration.util.StringParser;

import org.apache.commons.lang3.tuple.Pair;

public class RecipeBlender {

	private static HashMap<HashPairedItemType, ItemStack> recipe;

	public static void init() {
		recipe = new HashMap<HashPairedItemType, ItemStack>();
	}

	public static void addRecipe(HashItemType input1, HashItemType input2, ItemStack output) {
		recipe.put(new HashPairedItemType(input1, input2), output);
	}

	public static ItemStack getRecipe(ItemStack input1, ItemStack input2) {

		HashPairedItemType key1 = new HashPairedItemType(new HashItemType(input1.getItem()), new HashItemType(input2.getItem()));
		HashPairedItemType key2 = new HashPairedItemType(new HashItemType(input1.getItem(), input1.getItemDamage()), new HashItemType(input2.getItem(), input2.getItemDamage()));
		HashPairedItemType key3 = new HashPairedItemType(new HashItemType(input1.getItem(), input1.getItemDamage()), new HashItemType(input2.getItem()));
		HashPairedItemType key4 = new HashPairedItemType(new HashItemType(input1.getItem()), new HashItemType(input2.getItem(), input2.getItemDamage()));
		
		if (recipe.containsKey(key1)) {
			return recipe.get(key1);
		} else if (recipe.containsKey(key2)) {
			return recipe.get(key2);
		}  else if (recipe.containsKey(key3)) {
			return recipe.get(key3);
		}  else if (recipe.containsKey(key4)) {
			return recipe.get(key4);
		} else {
			return null;
		}
	}

	public static void readConfiguration(String i) {
		String[] split = StringParser.splitByLevel(i.replaceAll(" ", ""), '=');
		if (split.length == 2) {
			ArrayList<HashItemType> input = ConfigurationHelper.instance.getListHashItem(split[0]);
			ItemStack output = ConfigurationHelper.instance.getItemStack(split[1]);
			RecipeBlender.addRecipe(input.get(0), input.get(1), output);
		} else {
			HungryAnimals.logger.warn("\"" + i + "\" is not added. Format error");
			return;
		}
	}

	public static Map<HashPairedItemType, ItemStack> getRecipeList() {
		return recipe;
	}
	
	

}
