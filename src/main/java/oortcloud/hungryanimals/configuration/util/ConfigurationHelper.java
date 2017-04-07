package oortcloud.hungryanimals.configuration.util;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;

public class ConfigurationHelper {

	public static ConfigurationHelper instance = new ConfigurationHelper();

	private ConfigurationHelper() {
	}

	public ArrayList<ItemStack> getListItemStack(String input) {
		String[] processedInput = StringParser.splitByLevel(StringParser.reduceLevel(input));
		ArrayList<ItemStack> output = new ArrayList<ItemStack>();
		for (String i : processedInput) {
			ItemStack j = getItemStack(i);
			if (j == null) {
				exceptionInvalidFormat(i);
				return null;
			}
			output.add(j);
		}
		if (output.isEmpty()) {
			exceptionEmptyList(input);
			return null;
		}
		return output;
	}

	/**
	 * 
	 * @param input
	 *            : (modid:itemname, number, damage)
	 * @return
	 */
	public ItemStack getItemStack(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		if (split.length == 1) {
			Item item = (Item) Item.getByNameOrId(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new ItemStack(item);
		} else if (split.length == 2) {
			Item item = (Item) Item.getByNameOrId(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new ItemStack(item, Integer.parseInt(split[1]));
		} else if (split.length == 3) {
			Item item = (Item) Item.getByNameOrId(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new ItemStack(item, Integer.parseInt(split[1]), Integer.parseInt(split[2]));
		} else {
			exceptionInvalidNumberOfArgument(input);
			return null;
		}
	}

	/**
	 * 
	 * @param input
	 *            : ((item),(item),...)
	 * @return
	 */
	public ArrayList<HashItemType> getListHashItem(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		ArrayList<HashItemType> output = new ArrayList<HashItemType>();
		for (String i : split) {
			HashItemType j = getHashItem(i);
			if (j == null) {
				exceptionInvalidFormat(i);
				return null;
			}
			output.add(j);
		}
		if (output.isEmpty()) {
			exceptionEmptyList(input);
			return null;
		}
		return output;
	}

	/**
	 * 
	 * @param input
	 *            : (modid:itemname,damage)
	 * @return
	 */
	public HashItemType getHashItem(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		if (split.length == 1) {
			Item item = (Item) Item.getByNameOrId(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new HashItemType(item);
		} else if (split.length == 2) {
			Item item = (Item) Item.getByNameOrId(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new HashItemType(item, Integer.parseInt(split[1]));
		} else {
			exceptionInvalidNumberOfArgument(input);
			return null;
		}
	}

	public static void exceptionInvalidNumberOfArgument(String input) {
		HungryAnimals.logger.warn("Invalid number of arguments : " + input);
	}

	public static void exceptionNameDoesntExist(String name) {
		HungryAnimals.logger.warn(name + " doesn't exist.");
	}

	public static void exceptionInvalidFormat(String argument) {
		HungryAnimals.logger.warn(argument + " is invalid.");
	}

	public static void exceptionEmptyList(String input) {
		HungryAnimals.logger.warn(input + " is an empty list.");
	}

}
