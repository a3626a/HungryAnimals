package oortcloud.hungryanimals.configuration;

import java.util.ArrayList;
import java.util.Collection;

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
		input = StringParser.reduceLevel(input);
		String[] split = StringParser.splitByLevel(input);
		if (split.length == 3) {
			HashItem item = getHashItem(split[0]);
			if (item != null) {
				return new DropMeat(item, Integer.parseInt(split[1]), Integer.parseInt(split[2]));
			} else {
				HungryAnimals.logger.warn("The Drop Meat Item \"" + split[0] + "\" is not considered. Format error at " + input);
				return null;
			}
		} else {
			HungryAnimals.logger.warn("The Drop Meat \"" + input + "\" is not considered. Format error at " + input);
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
		input = StringParser.reduceLevel(input);
		String[] split = StringParser.splitByLevel(input);

		if (split.length == 3) {
			HashItem item = getHashItem(split[0]);
			if (item != null) {
				return new DropRandom(item, Integer.parseInt(split[1]), Integer.parseInt(split[2]));
			} else {
				HungryAnimals.logger.warn("The Drop Random Item \"" + split[0] + "\" is not considered. Format error at " + input);
				return null;
			}
		} else {
			HungryAnimals.logger.warn("The Drop Random \"" + input + "\" is not considered. Format error at " + input);
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
		input = StringParser.reduceLevel(input);
		String[] split = StringParser.splitByLevel(input);

		if (split.length == 2) {
			HashItem item = getHashItem(split[0]);
			if (item != null) {
				return new DropRare(item, Double.parseDouble(split[1]));
			} else {
				HungryAnimals.logger.warn("The Item \"" + split[0] + "\" is not considered. Format error at " + input);
				return null;
			}
		} else {
			HungryAnimals.logger.warn("The Drop Rare \"" + input + "\" is not added. Format error at " + input);
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
		input = StringParser.reduceLevel(input);
		String[] processedInput = StringParser.splitByLevel(input);
		ArrayList<HashBlock> output = new ArrayList<HashBlock>();
		for (String i : processedInput) {
			HashBlock j = getHashBlock(i);
			if (j != null) {
				output.add(j);
			}
		}
		if (!output.isEmpty()) {
			return output;
		} else {
			return output;
		}
	}

	/**
	 * 
	 * @param input
	 *            : ((modid:blockname),((propertyname1,propertyvalue1),(
	 *            propertyname2,propertyvalue2),...))
	 * @return
	 */
	public HashBlock getHashBlock(String input) {

		input = StringParser.reduceLevel(input);
		String[] split = StringParser.splitByLevel(input);

		if (split.length == 1) {
			Block block = Block.getBlockFromName(StringParser.reduceLevel(split[0]));
			if (block != null) {
				return new HashBlock(block);
			} else {
				HungryAnimals.logger.warn("The Block \"" + split[0] + "\" is not added. Format error at " + input);
				return null;
			}
		} else if (split.length == 2) {

			Block block = Block.getBlockFromName(StringParser.reduceLevel(split[0]));
			split[1] = StringParser.reduceLevel(split[1]);
			String[] properties = StringParser.splitByLevel(split[1]);
			IBlockState state = block.getDefaultState();
			for (String i : properties) {
				i = StringParser.reduceLevel(i);
				String[] propertyParameter = StringParser.splitByLevel(i);

				if (propertyParameter.length == 2) {
					String name = propertyParameter[0];
					String value = propertyParameter[1];

					ImmutableSet key = state.getProperties().keySet();
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
								HungryAnimals.logger.warn("The Property \"" + value + "\" is not considered. Format error at " + input);
							}
						}
					}
					if (!containsPropertyName) {
						HungryAnimals.logger.warn("The Property \"" + name + "\" is not considered. Format error at " + input);
					}
				} else {
					HungryAnimals.logger.warn("The Property \"" + i + "\" is not considered. Format error at " + input);
				}
			}
			return new HashBlock(state);
		} else {
			HungryAnimals.logger.warn("The Block \"" + input + "\" is not added. Format error at " + input);
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
		input = StringParser.reduceLevel(input);
		String[] processedInput = StringParser.splitByLevel(input);
		ArrayList<ItemStack> output = new ArrayList<ItemStack>();
		for (String i : processedInput) {
			ItemStack j = getItemStack(i);
			if (j != null) {
				output.add(j);
			}
		}

		if (!output.isEmpty()) {
			return output;
		} else {
			return output;
		}
	}

	/**
	 * 
	 * @param input
	 *            : (modid:itemname, number, damage)
	 * @return
	 */
	public ItemStack getItemStack(String input) {
		input = StringParser.reduceLevel(input);
		String[] split = StringParser.splitByLevel(input);
		if (split.length == 1) {
			return new ItemStack((Item) Item.itemRegistry.getObject(split[0]));
		} else if (split.length == 2) {
			return new ItemStack((Item) Item.itemRegistry.getObject(split[0]), Integer.parseInt(split[1]));
		} else if (split.length == 3) {
			return new ItemStack((Item) Item.itemRegistry.getObject(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
		} else {
			HungryAnimals.logger.warn("\"" + input + "\" is not added. Format error at " + input);
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
		input = StringParser.reduceLevel(input);
		String[] processedInput = StringParser.splitByLevel(input);
		ArrayList<HashItem> output = new ArrayList<HashItem>();

		for (String i : processedInput) {
			HashItem j = getHashItem(i);
			if (j != null)
				output.add(j);
		}

		if (!output.isEmpty()) {
			return output;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param input
	 *            : (modid:itemname,damage)
	 * @return
	 */
	public HashItem getHashItem(String input) {
		input = StringParser.reduceLevel(input);
		String[] split = StringParser.splitByLevel(input);

		if (split.length == 1) {
			Item item = (Item) Item.itemRegistry.getObject(split[0]);
			if (item != null) {
				return new HashItem(item);
			} else {
				HungryAnimals.logger.warn("\"" + split[0] + "\" is not added. Format error at " + input);
				return null;
			}
		} else if (split.length == 2) {
			Item item = (Item) Item.itemRegistry.getObject(split[0]);
			if (item != null) {
				return new HashItem(item, Integer.parseInt(split[1]));
			} else {
				HungryAnimals.logger.warn("\"" + split[0] + "\" is not added. Format error at " + input);
				return null;
			}
		} else {
			HungryAnimals.logger.warn("\"" + input + "\" is not added. Format error at " + input);
			return null;
		}
	}

	public ArrayList<ProbItemStack> getListProbItemStack(String input) {

		String[] rhs = input.split("\\),\\(");

		ArrayList<ProbItemStack> output = new ArrayList<ProbItemStack>();

		for (String j : rhs) {

			if (j.startsWith("(")) {
				j = j.substring(1, j.length());
			}
			if (j.endsWith(")")) {
				j = j.substring(0, j.length() - 1);
			}

			String[] split2 = j.split(",");

			if (split2.length == 2) {
				output.add(new ProbItemStack(Double.parseDouble(split2[0]), new ItemStack((Item) Item.itemRegistry.getObject(split2[1]))));
			} else if (split2.length == 3) {
				output.add(new ProbItemStack(Double.parseDouble(split2[0]), new ItemStack((Item) Item.itemRegistry.getObject(split2[1]), Integer.parseInt(split2[2]))));
			} else if (split2.length == 4) {
				output.add(new ProbItemStack(Double.parseDouble(split2[0]), new ItemStack((Item) Item.itemRegistry.getObject(split2[1]), Integer.parseInt(split2[2]), Integer.parseInt(split2[3]))));
			} else {
				HungryAnimals.logger.warn("\"" + input + "\" is not added. Format error at " + j);
				output = null;
				break;
			}
		}

		return output;
	}

}
