package oortcloud.hungryanimals.recipes;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.configuration.util.ConfigurationHelper;
import oortcloud.hungryanimals.configuration.util.HashItemType;
import oortcloud.hungryanimals.configuration.util.HashProbabilityItemStack;
import oortcloud.hungryanimals.configuration.util.StringParser;

public class RecipeThresher {
	
	private static HashMap<HashItemType, ArrayList<HashProbabilityItemStack>> recipe;
	
	public static void init() {
		recipe = new HashMap<HashItemType, ArrayList<HashProbabilityItemStack>>();
	}
	
	public static void addRecipe(Item item, ArrayList<HashProbabilityItemStack> output) {
		addRecipe(item, 0, output);
	}
	
	public static void addRecipe(Item item, int damage, ArrayList<HashProbabilityItemStack> output) {
		recipe.put(new HashItemType(item, damage), output);
	}
	
	public static void addRecipe(HashItemType input, ArrayList<HashProbabilityItemStack> output) {
		recipe.put(input, output);
	}
	
	public static ArrayList<HashProbabilityItemStack> getRecipe(ItemStack item) {
		
		if (recipe.containsKey(new HashItemType(item.getItem()))) {
			return recipe.get(new HashItemType(item.getItem()));
		} else if (recipe.containsKey(new HashItemType(item.getItem(), item.getItemDamage()))) {
			return recipe.get(new HashItemType(item.getItem(), item.getItemDamage()));
		} else {
			return null;
		}
	}
	
	public static void readConfiguration(String i) {
		
		String[] split = StringParser.splitByLevel(i.replaceAll(" ", ""), '=');
		
		if (split.length == 2) {
			HashItemType input = ConfigurationHelper.instance.getHashItem(split[0]);
			ArrayList<HashProbabilityItemStack> output = ConfigurationHelper.instance.getListProbItemStack(split[1]);
			if (input != null && output != null) {
				RecipeThresher.addRecipe(input, output);
			}
		} else {
			HungryAnimals.logger.warn("\""+i+ "\" is not added. Format error");
			return;
		}
	}

}
