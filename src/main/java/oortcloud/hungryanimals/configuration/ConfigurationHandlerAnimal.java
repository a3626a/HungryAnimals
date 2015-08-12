package oortcloud.hungryanimals.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Configuration;
import oortcloud.hungryanimals.configuration.util.DropMeat;
import oortcloud.hungryanimals.configuration.util.DropRandom;
import oortcloud.hungryanimals.configuration.util.DropRare;
import oortcloud.hungryanimals.configuration.util.HashBlock;
import oortcloud.hungryanimals.configuration.util.HashItem;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryChicken;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryCow;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryPig;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryRabbit;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungrySheep;
import oortcloud.hungryanimals.items.ModItems;

public class ConfigurationHandlerAnimal {

	public static Configuration config;

	public static final String CATEGORY_Chicken = "chicken";
	public static final String CATEGORY_Cow = "cow";
	public static final String CATEGORY_Mooshroom = "mooshroom";
	public static final String CATEGORY_Pig = "pig";
	public static final String CATEGORY_Sheep = "sheep";
	public static final String CATEGORY_Rabbit = "rabbit";

	public static final String KEY_hunger_bmr = "HungerUsage: basic rate";
	public static final String KEY_hunger_max = "Abiltiy: max hunger";
	public static final String KEY_hunger_food = "HungerAcquisition: byFood Rate";
	public static final String KEY_hunger_block = "HungerAcquisition: byBlock Rate";
	public static final String KEY_drop_meat = "Ability: items drops that the amount is decided by hunger value";
	public static final String KEY_drop_random = "Ability: items drops that the amount is decided randomly";
	public static final String KEY_drop_rare = "Ability: items drops that is very rare";
	public static final String KEY_courtship_hunger = "HungerUsage: to do the courting";
	public static final String KEY_courtship_probability = "Behavior: probability to do the courting per a tick";
	public static final String KEY_courtship_hungerCondition = "Behavior: needed ratio(hunger/maxhunger) to do the courting";
	public static final String KEY_excretion_factor = "Abiltiy: needed amount of hunger consumption to produce a pile of excreta";
	public static final String KEY_child_hunger = "HungerUsage: to make a child";
	public static final String KEY_milk_hunger = "HungerUsage: to produce a bucket of milk";
	public static final String KEY_milk_delay = "Behavior: ticks needed to produce a bucket of milk";
	public static final String KEY_wool_hunger = "HungerUsage: Wool";
	public static final String KEY_wool_delay = "Behavior: ticks needed to produce a block of wool";

	public static void init(File file) {
		config = new Configuration(file);
		config.load();
	}

	public static void sync() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

		ExtendedPropertiesHungryChicken.default_hunger_bmr = config.get(CATEGORY_Chicken, KEY_hunger_bmr, 0.002).getDouble();
		ExtendedPropertiesHungryCow.default_hunger_bmr = config.get(CATEGORY_Cow, KEY_hunger_bmr, 0.005).getDouble();
		ExtendedPropertiesHungryPig.default_hunger_bmr = config.get(CATEGORY_Pig, KEY_hunger_bmr, 0.004).getDouble();
		ExtendedPropertiesHungrySheep.default_hunger_bmr = config.get(CATEGORY_Sheep, KEY_hunger_bmr, 0.004).getDouble();
		ExtendedPropertiesHungryRabbit.default_hunger_bmr = config.get(CATEGORY_Rabbit, KEY_hunger_bmr, 0.003).getDouble();

		ExtendedPropertiesHungryChicken.default_hunger_max = config.get(CATEGORY_Chicken, KEY_hunger_max, 150).getDouble();
		ExtendedPropertiesHungryCow.default_hunger_max = config.get(CATEGORY_Cow, KEY_hunger_max, 500).getDouble();
		ExtendedPropertiesHungryPig.default_hunger_max = config.get(CATEGORY_Pig, KEY_hunger_max, 400).getDouble();
		ExtendedPropertiesHungrySheep.default_hunger_max = config.get(CATEGORY_Sheep, KEY_hunger_max, 400).getDouble();
		ExtendedPropertiesHungryRabbit.default_hunger_max = config.get(CATEGORY_Rabbit, KEY_hunger_max, 250).getDouble();

		ExtendedPropertiesHungryChicken.default_courtship_hunger = config.get(CATEGORY_Chicken, KEY_courtship_hunger, ExtendedPropertiesHungryChicken.default_hunger_max / 20.0)
				.getDouble();
		ExtendedPropertiesHungryCow.default_courtship_hunger = config.get(CATEGORY_Cow, KEY_courtship_hunger, ExtendedPropertiesHungryCow.default_hunger_max / 20.0).getDouble();
		ExtendedPropertiesHungryPig.default_courtship_hunger = config.get(CATEGORY_Pig, KEY_courtship_hunger, ExtendedPropertiesHungryPig.default_hunger_max / 20.0).getDouble();
		ExtendedPropertiesHungrySheep.default_courtship_hunger = config.get(CATEGORY_Sheep, KEY_courtship_hunger, ExtendedPropertiesHungrySheep.default_hunger_max / 20.0)
				.getDouble();
		ExtendedPropertiesHungryRabbit.default_courtship_hunger = config.get(CATEGORY_Rabbit, KEY_courtship_hunger, ExtendedPropertiesHungryRabbit.default_hunger_max / 20.0)
				.getDouble();

		ExtendedPropertiesHungryChicken.default_courtship_probability = config.get(CATEGORY_Chicken, KEY_courtship_probability, 0.0025).getDouble();
		ExtendedPropertiesHungryCow.default_courtship_probability = config.get(CATEGORY_Cow, KEY_courtship_probability, 0.0025).getDouble();
		ExtendedPropertiesHungryPig.default_courtship_probability = config.get(CATEGORY_Pig, KEY_courtship_probability, 0.0025).getDouble();
		ExtendedPropertiesHungrySheep.default_courtship_probability = config.get(CATEGORY_Sheep, KEY_courtship_probability, 0.0025).getDouble();
		ExtendedPropertiesHungryRabbit.default_courtship_probability = config.get(CATEGORY_Rabbit, KEY_courtship_probability, 0.0025).getDouble();

		ExtendedPropertiesHungryChicken.default_courtship_hungerCondition = config.get(CATEGORY_Chicken, KEY_courtship_hungerCondition, 0.8).getDouble();
		ExtendedPropertiesHungryCow.default_courtship_hungerCondition = config.get(CATEGORY_Cow, KEY_courtship_hungerCondition, 0.8).getDouble();
		ExtendedPropertiesHungryPig.default_courtship_hungerCondition = config.get(CATEGORY_Pig, KEY_courtship_hungerCondition, 0.8).getDouble();
		ExtendedPropertiesHungrySheep.default_courtship_hungerCondition = config.get(CATEGORY_Sheep, KEY_courtship_hungerCondition, 0.8).getDouble();
		ExtendedPropertiesHungryRabbit.default_courtship_hungerCondition = config.get(CATEGORY_Rabbit, KEY_courtship_hungerCondition, 0.8).getDouble();

		ExtendedPropertiesHungryChicken.default_excretion_factor = 1 / config.get(CATEGORY_Chicken, KEY_excretion_factor, 50).getDouble();
		ExtendedPropertiesHungryCow.default_excretion_factor = 1 / config.get(CATEGORY_Cow, KEY_excretion_factor, 50).getDouble();
		ExtendedPropertiesHungryPig.default_excretion_factor = 1 / config.get(CATEGORY_Pig, KEY_excretion_factor, 50).getDouble();
		ExtendedPropertiesHungrySheep.default_excretion_factor = 1 / config.get(CATEGORY_Sheep, KEY_excretion_factor, 50).getDouble();
		ExtendedPropertiesHungryRabbit.default_excretion_factor = 1 / config.get(CATEGORY_Rabbit, KEY_excretion_factor, 50).getDouble();

		ExtendedPropertiesHungryChicken.default_child_hunger = config.get(CATEGORY_Chicken, KEY_child_hunger, ExtendedPropertiesHungryChicken.default_hunger_max / 4.0).getDouble();
		ExtendedPropertiesHungryCow.default_child_hunger = config.get(CATEGORY_Cow, KEY_child_hunger, ExtendedPropertiesHungryCow.default_hunger_max / 4.0).getDouble();
		ExtendedPropertiesHungryPig.default_child_hunger = config.get(CATEGORY_Pig, KEY_child_hunger, ExtendedPropertiesHungryPig.default_hunger_max / 4.0).getDouble();
		ExtendedPropertiesHungrySheep.default_child_hunger = config.get(CATEGORY_Sheep, KEY_child_hunger, ExtendedPropertiesHungrySheep.default_hunger_max / 4.0).getDouble();
		ExtendedPropertiesHungryRabbit.default_child_hunger = config.get(CATEGORY_Rabbit, KEY_child_hunger, ExtendedPropertiesHungrySheep.default_hunger_max / 4.0).getDouble();

		ExtendedPropertiesHungryCow.default_milk_delay = config.get(CATEGORY_Cow, KEY_milk_delay, 5 * 60 * 20).getInt();
		ExtendedPropertiesHungryCow.default_milk_hunger = config.get(CATEGORY_Cow, KEY_milk_hunger, ExtendedPropertiesHungryCow.default_hunger_max / 20.0).getDouble();

		ExtendedPropertiesHungrySheep.default_wool_delay = config.get(CATEGORY_Sheep, KEY_wool_delay, 5 * 60 * 20).getInt();
		ExtendedPropertiesHungrySheep.default_wool_hunger = config.get(CATEGORY_Sheep, KEY_wool_hunger, ExtendedPropertiesHungrySheep.default_hunger_max / 20.0).getDouble();

		readDropMeat(new String[] { "((" + Item.itemRegistry.getNameForObject(Items.chicken) + "),2,4)" }, CATEGORY_Chicken, ExtendedPropertiesHungryChicken.class);
		readDropMeat(new String[] { "((" + Item.itemRegistry.getNameForObject(Items.beef) + "),5,10)" }, CATEGORY_Cow, ExtendedPropertiesHungryCow.class);
		readDropMeat(new String[] { "((" + Item.itemRegistry.getNameForObject(Items.porkchop) + "),4,8)" }, CATEGORY_Pig, ExtendedPropertiesHungryPig.class);
		readDropMeat(new String[] { "((" + Item.itemRegistry.getNameForObject(Items.rabbit) + "),1,2)" }, CATEGORY_Rabbit, ExtendedPropertiesHungryRabbit.class);
		readDropMeat(new String[] { "((" + Item.itemRegistry.getNameForObject(Items.mutton) + "),3,6)" }, CATEGORY_Sheep, ExtendedPropertiesHungrySheep.class);

		readDropRandom(new String[] { "((" + Item.itemRegistry.getNameForObject(Items.feather) + "),3,6)" }, CATEGORY_Chicken, ExtendedPropertiesHungryChicken.class);
		readDropRandom(
				new String[] { "((" + Item.itemRegistry.getNameForObject(Items.leather) + "),5,10)", "((" + Item.itemRegistry.getNameForObject(ModItems.tendon) + "),2,3)" },
				CATEGORY_Cow, ExtendedPropertiesHungryCow.class);
		readDropRandom(new String[] { "((" + Item.itemRegistry.getNameForObject(ModItems.tendon) + "),1,2)" }, CATEGORY_Pig, ExtendedPropertiesHungryPig.class);
		readDropRandom(new String[] { "((" + Item.itemRegistry.getNameForObject(Items.rabbit_hide) + "),1,2)" }, CATEGORY_Rabbit, ExtendedPropertiesHungryRabbit.class);
		readDropRandom(new String[] { "((" + Item.itemRegistry.getNameForObject(ModItems.tendon) + "),1,2)" }, CATEGORY_Sheep, ExtendedPropertiesHungrySheep.class);

		readDropRare(new String[] {}, CATEGORY_Chicken, ExtendedPropertiesHungryChicken.class);
		readDropRare(new String[] {}, CATEGORY_Cow, ExtendedPropertiesHungryCow.class);
		readDropRare(new String[] {}, CATEGORY_Pig, ExtendedPropertiesHungryPig.class);
		readDropRare(new String[] { "((" + Item.itemRegistry.getNameForObject(Items.rabbit_foot) + "),0.025)" }, CATEGORY_Rabbit, ExtendedPropertiesHungryRabbit.class);
		readDropRare(new String[] {}, CATEGORY_Sheep, ExtendedPropertiesHungrySheep.class);

		ByFoodRate(new String[] { "(" + Item.itemRegistry.getNameForObject(Items.wheat_seeds) + ")=(20.0)",
				"(" + Item.itemRegistry.getNameForObject(Items.pumpkin_seeds) + ")=(25.0)", "(" + Item.itemRegistry.getNameForObject(Items.melon_seeds) + ")=(25.0)",
				"(" + Item.itemRegistry.getNameForObject(ModItems.poppyseed) + ")=(20.0)", "(" + Item.itemRegistry.getNameForObject(ModItems.mixedFeed) + ")=(80.0)" },
				CATEGORY_Chicken, ExtendedPropertiesHungryChicken.class);
		ByBlockRate(new String[] { "(" + Block.blockRegistry.getNameForObject(Blocks.tallgrass) + ")=(15.0)" }, CATEGORY_Chicken, ExtendedPropertiesHungryChicken.class);

		ByFoodRate(new String[] { "(" + Item.itemRegistry.getNameForObject(Items.wheat) + ")=(50.0)", "(" + Item.itemRegistry.getNameForObject(Items.reeds) + ")=(20.0)",
				"(" + Item.itemRegistry.getNameForObject(ModItems.straw) + ")=(10.0)", "(" + Item.itemRegistry.getNameForObject(ModItems.mixedFeed) + ")=(80.0)" }, CATEGORY_Cow,
				ExtendedPropertiesHungryCow.class);
		ByBlockRate(new String[] { "(" + Block.blockRegistry.getNameForObject(Blocks.tallgrass) + ")=(15.0)" }, CATEGORY_Cow, ExtendedPropertiesHungryCow.class);

		ByFoodRate(new String[] { "(" + Item.itemRegistry.getNameForObject(Items.carrot) + ")=(40.0)", "(" + Item.itemRegistry.getNameForObject(Items.rotten_flesh) + ")=(15.0)",
				"(" + Item.itemRegistry.getNameForObject(ModItems.mixedFeed) + ")=(80.0)" }, CATEGORY_Pig, ExtendedPropertiesHungryPig.class);
		ByBlockRate(new String[] { "(" + Block.blockRegistry.getNameForObject(Blocks.tallgrass) + ")=(15.0)" }, CATEGORY_Pig, ExtendedPropertiesHungryPig.class);

		ByFoodRate(new String[] { "(" + Item.itemRegistry.getNameForObject(Items.wheat) + ")=(50.0)", "(" + Item.itemRegistry.getNameForObject(Items.reeds) + ")=(20.0)",
				"(" + Item.itemRegistry.getNameForObject(ModItems.straw) + ")=(10.0)", "(" + Item.itemRegistry.getNameForObject(ModItems.mixedFeed) + ")=(80.0)" }, CATEGORY_Sheep,
				ExtendedPropertiesHungrySheep.class);
		ByBlockRate(new String[] { "(" + Block.blockRegistry.getNameForObject(Blocks.tallgrass) + ")=(15.0)" }, CATEGORY_Sheep, ExtendedPropertiesHungrySheep.class);

		ByFoodRate(
				new String[] { "(" + Item.itemRegistry.getNameForObject(Items.carrot) + ")=(40.0)",
						"(" + Item.itemRegistry.getNameForObject(ItemBlock.getItemFromBlock(Blocks.yellow_flower)) + ")=(20.0)",
						"(" + Item.itemRegistry.getNameForObject(Items.golden_carrot) + ")=(150.0)", "(" + Item.itemRegistry.getNameForObject(ModItems.mixedFeed) + ")=(80.0)" },
				CATEGORY_Rabbit, ExtendedPropertiesHungryRabbit.class);
		ByBlockRate(new String[] { "(" + Block.blockRegistry.getNameForObject(Blocks.tallgrass) + ")=(15.0)",
				"(" + Block.blockRegistry.getNameForObject(Blocks.yellow_flower) + ")=(20.0)", "(" + Block.blockRegistry.getNameForObject(Blocks.carrots) + ",((age,7)))=(40.0)" },
				CATEGORY_Rabbit, ExtendedPropertiesHungryRabbit.class);

		config.save();

	}

	private static void readDropMeat(String[] defaultfood, String category, Class<? extends ExtendedPropertiesHungryAnimal> target) throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		String[] drops;
		drops = config.get(category, KEY_drop_meat, defaultfood).getStringList();
		for (String i : drops) {
			DropMeat j = ConfigurationHelper.instance.getDropMeat(i);
			if (j != null) {
				((ArrayList<DropMeat>) target.getField("default_drop_meat").get(target)).add(j);
			} else {
				System.out.println("\"" + i + "\" is not added. Format error");
				continue;
			}
		}
	}

	private static void readDropRandom(String[] defaultfood, String category, Class<? extends ExtendedPropertiesHungryAnimal> target) throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		String[] drops;
		drops = config.get(category, KEY_drop_random, defaultfood).getStringList();
		for (String i : drops) {
			DropRandom j = ConfigurationHelper.instance.getDropRandom(i);
			if (j != null) {
				((ArrayList<DropRandom>) target.getField("default_drop_random").get(target)).add(j);
			} else {
				System.out.println("\"" + i + "\" is not added. Format error");
				continue;
			}
		}
	}

	private static void readDropRare(String[] defaultfood, String category, Class<? extends ExtendedPropertiesHungryAnimal> target) throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		String[] drops;
		drops = config.get(category, KEY_drop_rare, defaultfood).getStringList();
		for (String i : drops) {
			DropRare j = ConfigurationHelper.instance.getDropRare(i);
			if (j != null) {
				((ArrayList<DropRare>) target.getField("default_drop_rare").get(target)).add(j);
			} else {
				System.out.println("\"" + i + "\" is not added. Format error");
				continue;
			}
		}
	}

	/**
	 * 
	 * @param defaultfood
	 *            : (item)=(hunger)
	 * @param category
	 * @param target
	 */
	private static void ByFoodRate(String[] defaultfood, String category, Class<? extends ExtendedPropertiesHungryAnimal> target) {
		String[] food;
		food = config.get(category, KEY_hunger_food, defaultfood).getStringList();
		for (String i : food) {
			String[] split = StringParser.splitByLevel(i.replaceAll(" ", ""), '=');

			if (split.length == 2) {
				HashItem item = ConfigurationHelper.instance.getHashItem(split[0]);
				double hunger = Double.parseDouble(StringParser.reduceLevel(split[1]));
				try {
					((HashMap<HashItem, Double>) target.getField("default_hunger_food").get(target)).put(item, hunger);
				} catch (Exception e) {
					System.out.println("\"" + i + "\" is not added. Format error");
					e.printStackTrace();
					continue;
				}

			} else {
				System.out.println("\"" + i + "\" is not added. Format error");
				continue;
			}
		}
	}

	private static void ByBlockRate(String[] defaultBlock, String category, Class<? extends ExtendedPropertiesHungryAnimal> target) {
		String[] block;
		block = config.get(category, KEY_hunger_block, defaultBlock).getStringList();
		for (String i : block) {
			String[] split = StringParser.splitByLevel(i.replaceAll(" ", ""), '=');

			if (split.length == 2) {
				HashBlock hashblock = ConfigurationHelper.instance.getHashBlock(split[0]);
				double hunger = Double.parseDouble(StringParser.reduceLevel(split[1]));
				try {
					((HashMap<HashBlock, Double>) target.getField("default_hunger_block").get(target)).put(hashblock, hunger);
				} catch (Exception e) {
					System.out.println("\"" + i + "\" is not added. Format error");
					e.printStackTrace();
					continue;
				}

			} else {
				System.out.println("\"" + i + "\" is not added. Format error");
				continue;
			}
		}
	}

}
