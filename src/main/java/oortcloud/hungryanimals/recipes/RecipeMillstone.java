package oortcloud.hungryanimals.recipes;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.configuration.ConfigurationHelper;
import oortcloud.hungryanimals.configuration.StringParser;
import oortcloud.hungryanimals.configuration.util.HashItem;

public class RecipeMillstone {

	private static HashMap<HashItem, Double> recipe;

	public static void init() {
		recipe = new HashMap<HashItem, Double>();
	}

	public static void addRecipe(HashItem input, double output) {
		recipe.put(input, output);
	}

	public static double getRecipe(ItemStack item) {

		if (recipe.containsKey(new HashItem(item.getItem()))) {
			return recipe.get(new HashItem(item.getItem()));
		} else if (recipe.containsKey(new HashItem(item.getItem(), item.getItemDamage()))) {
			return recipe.get(new HashItem(item.getItem(), item.getItemDamage()));
		} else {
			return 0;
		}
	}

	public static void readConfiguration(String i) {
		String[] split = StringParser.splitByLevel(i.replaceAll(" ", ""), '=');
		if (split.length == 2) {
			HashItem input = ConfigurationHelper.instance.getHashItem(split[0]);
			if (input != null) {
				RecipeMillstone.addRecipe(input, Double.parseDouble(split[1].substring(1, split[1].length() - 1)));
			}
		} else {
			HungryAnimals.logger.warn("\"" + i + "\" is not added. Format error");
			return;
		}
	}

}
