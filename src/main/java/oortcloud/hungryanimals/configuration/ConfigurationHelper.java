package oortcloud.hungryanimals.configuration;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.Marker;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.configuration.util.DropMeat;
import oortcloud.hungryanimals.configuration.util.DropRandom;
import oortcloud.hungryanimals.configuration.util.DropRare;
import oortcloud.hungryanimals.configuration.util.HashBlock;
import oortcloud.hungryanimals.configuration.util.HashItem;
import oortcloud.hungryanimals.configuration.util.ProbItemStack;

import com.google.common.collect.ImmutableSet;

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
	public DropMeat getDropMeat(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		if (split.length == 3) {
			HashItem item = getHashItem(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new DropMeat(item, Integer.parseInt(split[1]), Integer.parseInt(split[2]));
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
	public DropRandom getDropRandom(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		if (split.length == 3) {
			HashItem item = getHashItem(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new DropRandom(item, Integer.parseInt(split[1]), Integer.parseInt(split[2]));
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
	public DropRare getDropRare(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		if (split.length == 2) {
			HashItem item = getHashItem(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new DropRare(item, Double.parseDouble(split[1]));
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
	public ArrayList<HashBlock> getListHashBlock(String input) {
		String[] processedInput = StringParser.splitByLevel(StringParser.reduceLevel(input));
		ArrayList<HashBlock> output = new ArrayList<HashBlock>();
		for (String i : processedInput) {
			HashBlock j = getHashBlock(i);
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
	public HashBlock getHashBlock(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		if (split.length == 1) {
			Block block = Block.getBlockFromName(StringParser.reduceLevel(split[0]));
			if (block == null) {
				exceptionNameDoesntExist(StringParser.reduceLevel(split[0]));
				return null;
			}
			return new HashBlock(block);
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
			return new HashBlock(state);
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
			Item item = (Item) Item.itemRegistry.getObject(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new ItemStack(item);
		} else if (split.length == 2) {
			Item item = (Item) Item.itemRegistry.getObject(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new ItemStack(item, Integer.parseInt(split[1]));
		} else if (split.length == 3) {
			Item item = (Item) Item.itemRegistry.getObject(split[0]);
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
	public ArrayList<HashItem> getListHashItem(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		ArrayList<HashItem> output = new ArrayList<HashItem>();
		for (String i : split) {
			HashItem j = getHashItem(i);
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
	public HashItem getHashItem(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		if (split.length == 1) {
			Item item = (Item) Item.itemRegistry.getObject(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new HashItem(item);
		} else if (split.length == 2) {
			Item item = (Item) Item.itemRegistry.getObject(split[0]);
			if (item == null) {
				exceptionNameDoesntExist(split[0]);
				return null;
			}
			return new HashItem(item, Integer.parseInt(split[1]));
		} else {
			exceptionInvalidNumberOfArgument(input);
			return null;
		}
	}

	/**
	 * 
	 * @param input
	 *            : (probability,(itemstack))
	 * @return
	 */
	public ProbItemStack getProbItemStack(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		if (split.length == 2) {
			ArrayList<ProbItemStack> output = new ArrayList<ProbItemStack>();
			ItemStack itemStack = getItemStack(split[1]);
			if (itemStack == null) {
				exceptionInvalidFormat(split[1]);
				return null;
			}
			return new ProbItemStack(Double.parseDouble(split[0]), itemStack);
		} else {
			exceptionInvalidNumberOfArgument(input);
			return null;
		}
	}

	/**
	 * 
	 * @param input : ((probitemstack),(probitemstack),...)
	 * @return
	 */
	public ArrayList<ProbItemStack> getListProbItemStack(String input) {
		String[] split = StringParser.splitByLevel(StringParser.reduceLevel(input));
		ArrayList<ProbItemStack> output = new ArrayList<ProbItemStack>();
		for (String i : split) {
			ProbItemStack j = getProbItemStack(i);
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
