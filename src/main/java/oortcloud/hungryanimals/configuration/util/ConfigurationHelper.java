package oortcloud.hungryanimals.configuration.util;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.properties.FoodPreferenceBlockState.HashBlockState;
import oortcloud.hungryanimals.entities.properties.FoodPreferenceItemStack.HashItemType;

public class ConfigurationHelper {

	public static ConfigurationHelper instance = new ConfigurationHelper();

	private ConfigurationHelper() {
	}

	/**
	 * 
	 * @param input
	 *            ; ((item),min_amount,max_amount)
	 * @return
	 */
	public ValueDropMeat getDropMeat(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		if (split.length == 3) {
			HashItemType item = getHashItem(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new ValueDropMeat(item, Integer.parseInt(split[1]), Integer.parseInt(split[2]));
		} else {
			exceptionInvalidNumberOfArgument(input);
			return null;
		}
	}

	/**
	 * 
	 * @param input
	 *            ; ((item),min_amount,max_amount)
	 * @return
	 */
	public ValueDropRandom getDropRandom(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		if (split.length == 3) {
			HashItemType item = getHashItem(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new ValueDropRandom(item, Integer.parseInt(split[1]), Integer.parseInt(split[2]));
		} else {
			exceptionInvalidNumberOfArgument(input);
			return null;
		}
	}

	/**
	 * 
	 * @param input
	 *            ; ((item),probability)
	 * @return
	 */
	public ValueDropRare getDropRare(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		if (split.length == 2) {
			HashItemType item = getHashItem(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new ValueDropRare(item, Double.parseDouble(split[1]));
		} else {
			exceptionInvalidNumberOfArgument(input);
			return null;
		}
	}

	/**
	 * 
	 * @param input
	 *            : ((block),(block),...)
	 * @return
	 */
	public ArrayList<HashBlockState> getListHashBlock(String input) {
		String[] processedInput = StringParser.splitByLevel(StringParser.reduceLevel(input));
		ArrayList<HashBlockState> output = new ArrayList<HashBlockState>();
		for (String i : processedInput) {
			HashBlockState j = getHashBlock(i);
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
	 *            : ((modid:blockname),((propertyname1,propertyvalue1),(
	 *            propertyname2,propertyvalue2),...))
	 * @return
	 */
	public HashBlockState getHashBlock(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		if (split.length == 1) {
			Block block = Block.getBlockFromName(StringParser.reduceLevel(split[0]));
			if (block == null) {
				exceptionNameDoesntExist(StringParser.reduceLevel(split[0]));
				return null;
			}
			return new HashBlockState(block);
		} else if (split.length == 2) {
			Block block = Block.getBlockFromName(StringParser.reduceLevel(split[0]));
			if (block == null) {
				exceptionNameDoesntExist(StringParser.reduceLevel(split[0]));
				return null;
			}
			IBlockState state = block.getDefaultState();
			ImmutableSet key = state.getProperties().keySet();
			String[] properties = StringParser.splitByLevel(StringParser.reduceLevel(split[1]));
			for (String i : properties) {
				String[] propertyParameter = StringParser.splitByLevel(StringParser.reduceLevel(i));
				if (propertyParameter.length == 2) {
					String name = propertyParameter[0];
					String value = propertyParameter[1];
					boolean containsPropertyName = false;
					for (Object j : key) {
						IProperty property = ((IProperty) j);
						if (property.getName().equals(name)) {
							containsPropertyName = true;
							Collection values = property.getAllowedValues();
							boolean containsPropertyValue = false;
							for (Object k : values) {
								Comparable com = (Comparable) k;
								if (property.getName(com).equals(value)) {
									containsPropertyValue = true;
									state = state.withProperty(property, com);
								}
							}
							if (!containsPropertyValue) {
								exceptionNameDoesntExist(value);
								return null;
							}
						}
					}
					if (!containsPropertyName) {
						exceptionNameDoesntExist(name);
						return null;
					}
				} else {
					exceptionInvalidNumberOfArgument(i);
					return null;
				}
			}
			return new HashBlockState(state);
		} else {
			exceptionInvalidNumberOfArgument(input);
			return null;
		}
	}

	/**
	 * 
	 * @param input
	 *            : ((itemstack),(itemstack),...)
	 * @return
	 */
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
