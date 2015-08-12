package oortcloud.hungryanimals.recipes;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.configuration.ConfigurationHelper;
import oortcloud.hungryanimals.configuration.StringParser;
import oortcloud.hungryanimals.configuration.util.HashItem;
import oortcloud.hungryanimals.configuration.util.PairHashItem;

import org.apache.commons.lang3.tuple.Pair;

public class RecipeBlender {

	private static HashMap<PairHashItem, ItemStack> recipe;

	public static void init() {
		recipe = new HashMap<PairHashItem, ItemStack>();
	}

	public static void addRecipe(HashItem input1, HashItem input2, ItemStack output) {
		recipe.put(new PairHashItem(input1, input2), output);
	}

	public static ItemStack getRecipe(ItemStack input1, ItemStack input2) {

		PairHashItem key1 = new PairHashItem(new HashItem(input1.getItem()), new HashItem(input2.getItem()));
		PairHashItem key2 = new PairHashItem(new HashItem(input1.getItem(), input1.getItemDamage()), new HashItem(input2.getItem(), input2.getItemDamage()));
		PairHashItem key3 = new PairHashItem(new HashItem(input1.getItem(), input1.getItemDamage()), new HashItem(input2.getItem()));
		PairHashItem key4 = new PairHashItem(new HashItem(input1.getItem()), new HashItem(input2.getItem(), input2.getItemDamage()));
		
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
			ArrayList<HashItem> input = ConfigurationHelper.instance.getListHashItem(split[0]);
			ItemStack output = ConfigurationHelper.instance.getItemStack(split[1]);
			RecipeBlender.addRecipe(input.get(0), input.get(1), output);
		} else {
			HungryAnimals.logger.warn("\"" + i + "\" is not added. Format error");
			return;
		}
	}
	
	

}
