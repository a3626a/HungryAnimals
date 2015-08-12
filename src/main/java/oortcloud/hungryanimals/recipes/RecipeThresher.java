package oortcloud.hungryanimals.recipes;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.configuration.ConfigurationHelper;
import oortcloud.hungryanimals.configuration.StringParser;
import oortcloud.hungryanimals.configuration.util.HashItem;
import oortcloud.hungryanimals.configuration.util.ProbItemStack;

public class RecipeThresher {
	
	private static HashMap<HashItem, ArrayList<ProbItemStack>> recipe;
	
	public static void init() {
		recipe = new HashMap<HashItem, ArrayList<ProbItemStack>>();
	}
	
	public static void addRecipe(Item item, ArrayList<ProbItemStack> output) {
		addRecipe(item, 0, output);
	}
	
	public static void addRecipe(Item item, int damage, ArrayList<ProbItemStack> output) {
		recipe.put(new HashItem(item, damage), output);
	}
	
	public static void addRecipe(HashItem input, ArrayList<ProbItemStack> output) {
		recipe.put(input, output);
	}
	
	public static ArrayList<ProbItemStack> getRecipe(ItemStack item) {
		
		if (recipe.containsKey(new HashItem(item.getItem()))) {
			return recipe.get(new HashItem(item.getItem()));
		} else if (recipe.containsKey(new HashItem(item.getItem(), item.getItemDamage()))) {
			return recipe.get(new HashItem(item.getItem(), item.getItemDamage()));
		} else {
			return null;
		}
	}
	
	public static void readConfiguration(String i) {
		
		String[] split = StringParser.splitByLevel(i.replaceAll(" ", ""), '=');
		
		if (split.length == 2) {
			HashItem input = ConfigurationHelper.instance.getHashItem(split[0]);
			ArrayList<ProbItemStack> output = ConfigurationHelper.instance.getListProbItemStack(split[1]);
			if (input != null && output != null) {
				RecipeThresher.addRecipe(input, output);
			}
		} else {
			HungryAnimals.logger.warn("\""+i+ "\" is not added. Format error");
			return;
		}
	}

}
