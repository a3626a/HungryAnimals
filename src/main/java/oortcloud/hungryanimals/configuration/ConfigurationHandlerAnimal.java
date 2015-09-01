package oortcloud.hungryanimals.configuration;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Configuration;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.configuration.util.ConfigurationHelper;
import oortcloud.hungryanimals.configuration.util.HashDropMeat;
import oortcloud.hungryanimals.configuration.util.HashDropRandom;
import oortcloud.hungryanimals.configuration.util.HashDropRare;
import oortcloud.hungryanimals.configuration.util.HashBlockState;
import oortcloud.hungryanimals.configuration.util.HashItemType;
import oortcloud.hungryanimals.configuration.util.StringParser;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryChicken;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryCow;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryPig;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryRabbit;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungrySheep;
import oortcloud.hungryanimals.entities.properties.handler.GeneralEntityManager;
import oortcloud.hungryanimals.entities.properties.handler.GeneralPropertiesHandler;
import oortcloud.hungryanimals.entities.properties.handler.GeneralProperty;
import oortcloud.hungryanimals.items.ModItems;

import org.apache.commons.lang3.ArrayUtils;

public class ConfigurationHandlerAnimal {

	public static Configuration config;

	public static final Class[] default_class = { EntityChicken.class, EntityCow.class, EntityPig.class, EntityRabbit.class, EntitySheep.class };

	public static final String KEY_hunger_bmr = "FoodConsumption: basic rate";
	public static final String KEY_hunger_max = "Abiltiy: max hunger";
	public static final String KEY_hunger_food = "HungerAcquisition: byFood Rate";
	public static final String KEY_hunger_block = "HungerAcquisition: byBlock Rate";
	public static final String KEY_drop_meat = "Ability: items drops that the amount is decided by hunger value";
	public static final String KEY_drop_random = "Ability: items drops that the amount is decided randomly";
	public static final String KEY_drop_rare = "Ability: items drops that is very rare";
	public static final String KEY_courtship_hunger = "FoodConsumption: to do the courting";
	public static final String KEY_courtship_probability = "Behavior: probability to do the courting per a tick";
	public static final String KEY_courtship_hungerCondition = "Behavior: needed ratio(hunger/maxhunger) to do the courting";
	public static final String KEY_excretion_factor = "Abiltiy: needed amount of hunger consumption to produce a pile of excreta";
	public static final String KEY_child_hunger = "FoodConsumption: to make a child";
	public static final String KEY_maxhealth = "Abiltiy: Max health of the animal";
	public static final String KEY_movespeed = "Abiltiy: Movement speed of the animal";
	public static final String KEY_crank_food_consumption = "FoodConsumption: food consumption during working on large crank";
	public static final String KEY_crank_production = "Abiltiy: Amount of power that this animal produces with large crank";
	public static final String KEY_milk_hunger = "FoodConsumption: to produce a bucket of milk";
	public static final String KEY_milk_delay = "Behavior: ticks needed to produce a bucket of milk";
	public static final String KEY_wool_hunger = "FoodConsumption: Wool";
	public static final String KEY_wool_delay = "Behavior: ticks needed to produce a block of wool";

	public static void init(File file) {
		config = new Configuration(file);
		config.load();
	}

	public static void sync() {
		HungryAnimals.logger.info("Configuration: Animal start");

		HungryAnimals.logger.info("Configuration: Initialization GeneralPropertiesHandler");
		HungryAnimals.logger.info("Configuration: Initialization GeneralEntityManager");
		GeneralPropertiesHandler.getInstance().init();
		GeneralEntityManager.getInstance().init();

		HungryAnimals.logger.info("Configuration: Register vanilla EntityAnimal classes to GeneralEntityManager");
		for (Class i : default_class) {
			HungryAnimals.logger.info("Configuration: " + i);
			GeneralEntityManager.getInstance().entities.add(i);
		}

		for (int i = 0; i < default_class.length; i++) {
			HungryAnimals.logger.info("Configuration: Create GeneralProperty of " + default_class[i].getName());
			GeneralProperty property = new GeneralProperty();
			String category = categoryGenerator(default_class[i]);
			GeneralProperty default_property = GeneralProperty.default_generalproperty[i];
			property.hunger_bmr = config.get(category, KEY_hunger_bmr, default_property.hunger_bmr).getDouble();
			property.hunger_max = config.get(category, KEY_hunger_max, default_property.hunger_max).getDouble();
			property.courtship_hunger = config.get(category, KEY_courtship_hunger, default_property.courtship_hunger).getDouble();
			property.courtship_probability = config.get(category, KEY_courtship_probability, default_property.courtship_probability).getDouble();
			property.courtship_hungerCondition = config.get(category, KEY_courtship_hungerCondition, default_property.courtship_hungerCondition).getDouble();
			property.excretion_factor = 1 / config.get(category, KEY_excretion_factor, 1 / default_property.excretion_factor).getDouble();
			property.child_hunger = config.get(category, KEY_child_hunger, default_property.child_hunger).getDouble();
			property.attribute_maxhealth = config.get(category, KEY_maxhealth, default_property.attribute_maxhealth).getDouble();
			property.attribute_movespeed = config.get(category, KEY_movespeed, default_property.attribute_movespeed).getDouble();
			property.crank_production = config.get(category, KEY_crank_production, default_property.crank_production).getDouble();
			property.crank_food_consumption = config.get(category, KEY_crank_food_consumption, default_property.crank_food_consumption).getDouble();
			readDropMeat(default_property.toStringDropMeat(), category, property);
			readDropRandom(default_property.toStringDropRandom(), category, property);
			readDropRare(default_property.toStringDropRare(), category, property);
			ByFoodRate(default_property.toStringHungerFood(), category, property);
			ByBlockRate(default_property.toStringHungerBlock(), category, property);
			HungryAnimals.logger.info("Configuration: Register GeneralProperty of " + default_class[i].getName() + " to GeneralPropertiesHandler");
			GeneralPropertiesHandler.getInstance().propertyMap.put(default_class[i], property);
		}

		String categoryCow = categoryGenerator(EntityCow.class);
		String categorySheep = categoryGenerator(EntitySheep.class);
		ExtendedPropertiesHungryCow.default_milk_delay = config.get(categoryCow, KEY_milk_delay, 5 * 60 * 20).getInt();
		ExtendedPropertiesHungryCow.default_milk_hunger = config.get(categoryCow, KEY_milk_hunger, GeneralProperty.default_cow.hunger_max / 20.0).getDouble();
		ExtendedPropertiesHungrySheep.default_wool_delay = config.get(categorySheep, KEY_wool_delay, 5 * 60 * 20).getInt();
		ExtendedPropertiesHungrySheep.default_wool_hunger = config.get(categorySheep, KEY_wool_hunger, GeneralProperty.default_sheep.hunger_max / 20.0).getDouble();

		/*
		 * HungryAnimals.logger.info(
		 * "Configuration: Create GeneralProperty of EntityCow");
		 * GeneralProperty cow = new GeneralProperty(); String categoryCow =
		 * categoryGenerator(EntityCow.class); cow.hunger_bmr =
		 * config.get(categoryCow, KEY_hunger_bmr, 0.005).getDouble();
		 * cow.hunger_max = config.get(categoryCow, KEY_hunger_max,
		 * 500).getDouble(); cow.courtship_hunger = config.get(categoryCow,
		 * KEY_courtship_hunger, cow.hunger_max / 20.0).getDouble();
		 * cow.courtship_probability = config.get(categoryCow,
		 * KEY_courtship_probability, 0.0025).getDouble();
		 * cow.courtship_hungerCondition = config.get(categoryCow,
		 * KEY_courtship_hungerCondition, 0.8).getDouble(); cow.excretion_factor
		 * = 1 / config.get(categoryCow, KEY_excretion_factor, 50).getDouble();
		 * cow.child_hunger = config.get(categoryCow, KEY_child_hunger,
		 * cow.hunger_max / 4.0).getDouble(); cow.attribute_maxhealth =
		 * config.get(categoryCow, KEY_maxhealth, 30.0).getDouble();
		 * cow.attribute_movespeed = config.get(categoryCow, KEY_movespeed,
		 * 0.20).getDouble();
		 * 
		 * readDropMeat(new String[] { "((" +
		 * Item.itemRegistry.getNameForObject(Items.beef) + "),5,10)" },
		 * categoryCow, cow); readDropRandom(new String[] { "((" +
		 * Item.itemRegistry.getNameForObject(Items.leather) + "),5,10)", "((" +
		 * Item.itemRegistry.getNameForObject(ModItems.tendon) + "),2,3)" },
		 * categoryCow, cow); readDropRare(new String[] {}, categoryCow, cow);
		 * ByFoodRate(new String[] { "(" +
		 * Item.itemRegistry.getNameForObject(Items.wheat) + ")=(50.0)", "(" +
		 * Item.itemRegistry.getNameForObject(Items.reeds) + ")=(20.0)", "(" +
		 * Item.itemRegistry.getNameForObject(ModItems.straw) + ")=(10.0)", "("
		 * + Item.itemRegistry.getNameForObject(ModItems.mixedFeed) + ")=(80.0)"
		 * }, categoryCow, cow); ByBlockRate(new String[] { "(" +
		 * Block.blockRegistry.getNameForObject(Blocks.tallgrass) + ")=(15.0)",
		 * "(" + Block.blockRegistry.getNameForObject(Blocks.wheat) +
		 * ",((age,7)))=(50.0)" }, categoryCow, cow); HungryAnimals.logger.info(
		 * "Configuration: Register GeneralProperty of EntityCow to GeneralPropertiesHandler"
		 * );
		 * GeneralPropertiesHandler.getInstance().propertyMap.put(EntityCow.class
		 * , cow);
		 * 
		 * HungryAnimals.logger.info(
		 * "Configuration: Create GeneralProperty of EntityPig");
		 * GeneralProperty pig = new GeneralProperty(); String categoryPig =
		 * categoryGenerator(EntityPig.class); pig.hunger_bmr =
		 * config.get(categoryPig, KEY_hunger_bmr, 0.004).getDouble();
		 * pig.hunger_max = config.get(categoryPig, KEY_hunger_max,
		 * 400).getDouble(); pig.courtship_hunger = config.get(categoryPig,
		 * KEY_courtship_hunger, pig.hunger_max / 20.0).getDouble();
		 * pig.courtship_probability = config.get(categoryPig,
		 * KEY_courtship_probability, 0.0025).getDouble();
		 * pig.courtship_hungerCondition = config.get(categoryPig,
		 * KEY_courtship_hungerCondition, 0.8).getDouble(); pig.excretion_factor
		 * = 1 / config.get(categoryPig, KEY_excretion_factor, 50).getDouble();
		 * pig.child_hunger = config.get(categoryPig, KEY_child_hunger,
		 * pig.hunger_max / 4.0).getDouble(); pig.attribute_maxhealth =
		 * config.get(categoryPig, KEY_maxhealth, 20.0).getDouble();
		 * pig.attribute_movespeed = config.get(categoryPig, KEY_movespeed,
		 * 0.25).getDouble(); readDropMeat(new String[] { "((" +
		 * Item.itemRegistry.getNameForObject(Items.porkchop) + "),4,8)" },
		 * categoryPig, pig); readDropRandom(new String[] { "((" +
		 * Item.itemRegistry.getNameForObject(ModItems.tendon) + "),1,2)" },
		 * categoryPig, pig); readDropRare(new String[] {}, categoryPig, pig);
		 * ByFoodRate(new String[] { "(" +
		 * Item.itemRegistry.getNameForObject(Items.carrot) + ")=(40.0)", "(" +
		 * Item.itemRegistry.getNameForObject(Items.rotten_flesh) + ")=(15.0)",
		 * "(" + Item.itemRegistry.getNameForObject(ModItems.mixedFeed) +
		 * ")=(80.0)" }, categoryPig, pig); ByBlockRate(new String[] { "(" +
		 * Block.blockRegistry.getNameForObject(Blocks.tallgrass) + ")=(15.0)",
		 * "(" + Block.blockRegistry.getNameForObject(Blocks.carrots) +
		 * ",((age,7)))=(40.0)" }, categoryPig, pig); HungryAnimals.logger.info(
		 * "Configuration: Register GeneralProperty of EntityPig to GeneralPropertiesHandler"
		 * );
		 * GeneralPropertiesHandler.getInstance().propertyMap.put(EntityPig.class
		 * , pig);
		 * 
		 * HungryAnimals.logger.info(
		 * "Configuration: Create GeneralProperty of EntityRabbit");
		 * GeneralProperty rabbit = new GeneralProperty(); String categoryRabbit
		 * = categoryGenerator(EntityRabbit.class); rabbit.hunger_bmr =
		 * config.get(categoryRabbit, KEY_hunger_bmr, 0.003).getDouble();
		 * rabbit.hunger_max = config.get(categoryRabbit, KEY_hunger_max,
		 * 250).getDouble(); rabbit.courtship_hunger =
		 * config.get(categoryRabbit, KEY_courtship_hunger, rabbit.hunger_max /
		 * 20.0).getDouble(); rabbit.courtship_probability =
		 * config.get(categoryRabbit, KEY_courtship_probability,
		 * 0.0025).getDouble(); rabbit.courtship_hungerCondition =
		 * config.get(categoryRabbit, KEY_courtship_hungerCondition,
		 * 0.8).getDouble(); rabbit.excretion_factor = 1 /
		 * config.get(categoryRabbit, KEY_excretion_factor, 50).getDouble();
		 * rabbit.child_hunger = config.get(categoryRabbit, KEY_child_hunger,
		 * rabbit.hunger_max / 4.0).getDouble(); rabbit.attribute_maxhealth =
		 * config.get(categoryRabbit, KEY_maxhealth, 10.0).getDouble();
		 * rabbit.attribute_movespeed = config.get(categoryRabbit,
		 * KEY_movespeed, 0.25).getDouble(); readDropMeat(new String[] { "((" +
		 * Item.itemRegistry.getNameForObject(Items.rabbit) + "),1,2)" },
		 * categoryRabbit, rabbit); readDropRandom(new String[] { "((" +
		 * Item.itemRegistry.getNameForObject(Items.rabbit_hide) + "),1,2)" },
		 * categoryRabbit, rabbit); readDropRare(new String[] { "((" +
		 * Item.itemRegistry.getNameForObject(Items.rabbit_foot) + "),0.025)" },
		 * categoryRabbit, rabbit); ByFoodRate( new String[] { "(" +
		 * Item.itemRegistry.getNameForObject(Items.carrot) + ")=(40.0)", "(" +
		 * Item.itemRegistry.getNameForObject(ItemBlock.getItemFromBlock(Blocks.
		 * yellow_flower)) + ")=(20.0)", "(" +
		 * Item.itemRegistry.getNameForObject(Items.golden_carrot) +
		 * ")=(150.0)", "(" +
		 * Item.itemRegistry.getNameForObject(ModItems.mixedFeed) + ")=(80.0)"
		 * }, categoryRabbit, rabbit); ByBlockRate( new String[] { "(" +
		 * Block.blockRegistry.getNameForObject(Blocks.tallgrass) + ")=(15.0)",
		 * "(" + Block.blockRegistry.getNameForObject(Blocks.yellow_flower) +
		 * ")=(20.0)", "(" +
		 * Block.blockRegistry.getNameForObject(Blocks.carrots) +
		 * ",((age,7)))=(40.0)" }, categoryRabbit, rabbit);
		 * HungryAnimals.logger.info(
		 * "Configuration: Register GeneralProperty of EntityRabbit to GeneralPropertiesHandler"
		 * );
		 * GeneralPropertiesHandler.getInstance().propertyMap.put(EntityRabbit
		 * .class, rabbit);
		 * 
		 * HungryAnimals.logger.info(
		 * "Configuration: Create GeneralProperty of EntitySheep");
		 * GeneralProperty sheep = new GeneralProperty(); String categorySheep =
		 * categoryGenerator(EntitySheep.class); sheep.hunger_bmr =
		 * config.get(categorySheep, KEY_hunger_bmr, 0.004).getDouble();
		 * sheep.hunger_max = config.get(categorySheep, KEY_hunger_max,
		 * 400).getDouble(); sheep.courtship_hunger = config.get(categorySheep,
		 * KEY_courtship_hunger, sheep.hunger_max / 20.0).getDouble();
		 * sheep.courtship_probability = config.get(categorySheep,
		 * KEY_courtship_probability, 0.0025).getDouble();
		 * sheep.courtship_hungerCondition = config.get(categorySheep,
		 * KEY_courtship_hungerCondition, 0.8).getDouble();
		 * sheep.excretion_factor = 1 / config.get(categorySheep,
		 * KEY_excretion_factor, 50).getDouble(); sheep.child_hunger =
		 * config.get(categorySheep, KEY_child_hunger, sheep.hunger_max /
		 * 4.0).getDouble(); sheep.attribute_maxhealth =
		 * config.get(categorySheep, KEY_maxhealth, 20.0).getDouble();
		 * sheep.attribute_movespeed = config.get(categorySheep, KEY_movespeed,
		 * 0.20).getDouble(); readDropMeat(new String[] { "((" +
		 * Item.itemRegistry.getNameForObject(Items.mutton) + "),3,6)" },
		 * categorySheep, sheep); readDropRandom(new String[] { "((" +
		 * Item.itemRegistry.getNameForObject(ModItems.tendon) + "),1,2)" },
		 * categorySheep, sheep); readDropRare(new String[] {}, categorySheep,
		 * sheep); ByFoodRate(new String[] { "(" +
		 * Item.itemRegistry.getNameForObject(Items.wheat) + ")=(50.0)", "(" +
		 * Item.itemRegistry.getNameForObject(Items.reeds) + ")=(20.0)", "(" +
		 * Item.itemRegistry.getNameForObject(ModItems.straw) + ")=(10.0)", "("
		 * + Item.itemRegistry.getNameForObject(ModItems.mixedFeed) + ")=(80.0)"
		 * }, categorySheep, sheep); ByBlockRate(new String[] { "(" +
		 * Block.blockRegistry.getNameForObject(Blocks.tallgrass) + ")=(15.0)",
		 * "(" + Block.blockRegistry.getNameForObject(Blocks.wheat) +
		 * ",((age,7)))=(50.0)" }, categorySheep, sheep);
		 * HungryAnimals.logger.info(
		 * "Configuration: Register GeneralProperty of EntitySheep to GeneralPropertiesHandler"
		 * );
		 * GeneralPropertiesHandler.getInstance().propertyMap.put(EntitySheep.
		 * class, sheep);
		 */

		config.save();
	}

	public static void setPropertiesGeneral(Configuration config) {
		for (Class<? extends EntityAnimal> i : GeneralEntityManager.getInstance().entities) {
			if (ArrayUtils.contains(default_class, i))
				continue;
			GeneralProperty iProperty = new GeneralProperty();
			String category = categoryGenerator(i);
			iProperty.hunger_bmr = config.get(category, KEY_hunger_bmr, 0.001).getDouble();
			iProperty.hunger_max = config.get(category, KEY_hunger_max, 100).getDouble();
			iProperty.courtship_hunger = config.get(category, KEY_courtship_hunger, 5.0).getDouble();
			iProperty.courtship_probability = config.get(category, KEY_courtship_probability, 0.0025).getDouble();
			iProperty.courtship_hungerCondition = config.get(category, KEY_courtship_hungerCondition, 0.8).getDouble();
			iProperty.excretion_factor = 1 / config.get(category, KEY_excretion_factor, 50).getDouble();
			iProperty.child_hunger = config.get(category, KEY_child_hunger, 25.0).getDouble();
			iProperty.attribute_maxhealth = config.get(category, KEY_maxhealth, 20.0).getDouble();
			iProperty.attribute_movespeed = config.get(category, KEY_movespeed, 0.20).getDouble();
			iProperty.crank_food_consumption = config.get(category, KEY_movespeed, 0).getDouble();
			iProperty.crank_production = config.get(category, KEY_movespeed, 0).getDouble();
			readDropMeat(new String[] {}, category, iProperty);
			readDropRandom(new String[] {}, category, iProperty);
			readDropRare(new String[] {}, category, iProperty);
			ByFoodRate(new String[] {}, category, iProperty);
			ByBlockRate(new String[] {}, category, iProperty);

			GeneralPropertiesHandler.getInstance().propertyMap.put(i, iProperty);
		}
	}

	private static String categoryGenerator(Class<? extends EntityAnimal> entityClass) {
		return (String) EntityList.classToStringMapping.get(entityClass);
	}

	private static void readDropMeat(String[] defaultfood, String category, GeneralProperty target) {
		String[] drops;
		drops = config.get(category, KEY_drop_meat, defaultfood).getStringList();
		for (String i : drops) {
			HashDropMeat j = ConfigurationHelper.instance.getDropMeat(i);
			if (j == null) {
				ConfigurationHelper.exceptionInvalidFormat(i);
				exceptionPrintPosition(category, KEY_drop_meat);
				continue;
			}
			target.drop_meat.add(j);
		}
	}

	private static void readDropRandom(String[] defaultfood, String category, GeneralProperty target) {
		String[] drops;
		drops = config.get(category, KEY_drop_random, defaultfood).getStringList();
		for (String i : drops) {
			HashDropRandom j = ConfigurationHelper.instance.getDropRandom(i);
			if (j == null) {
				ConfigurationHelper.exceptionInvalidFormat(i);
				exceptionPrintPosition(category, KEY_drop_random);
				continue;
			}
			target.drop_random.add(j);
		}
	}

	private static void readDropRare(String[] defaultfood, String category, GeneralProperty target) {
		String[] drops;
		drops = config.get(category, KEY_drop_rare, defaultfood).getStringList();
		for (String i : drops) {
			HashDropRare j = ConfigurationHelper.instance.getDropRare(i);
			if (j == null) {
				ConfigurationHelper.exceptionInvalidFormat(i);
				exceptionPrintPosition(category, KEY_drop_rare);
				continue;
			}
			target.drop_rare.add(j);
		}
	}

	/**
	 * 
	 * @param defaultfood
	 *            : (item)=(hunger)
	 * @param category
	 * @param target
	 */
	private static void ByFoodRate(String[] defaultfood, String category, GeneralProperty target) {
		String[] food;
		food = config.get(category, KEY_hunger_food, defaultfood).getStringList();
		for (String i : food) {
			String[] split = StringParser.splitByLevel(i.replaceAll(" ", ""), '=');
			if (split.length == 2) {
				HashItemType item = ConfigurationHelper.instance.getHashItem(split[0]);
				double hunger = Double.parseDouble(StringParser.reduceLevel(split[1]));
				if (item == null) {
					ConfigurationHelper.exceptionInvalidFormat(split[0]);
					exceptionPrintPosition(category, KEY_hunger_food);
					continue;
				}
				target.hunger_food.put(item, hunger);
			} else {
				ConfigurationHelper.exceptionInvalidNumberOfArgument(i);
				exceptionPrintPosition(category, KEY_hunger_food);
				continue;
			}
		}
	}

	/**
	 * 
	 * @param defaultBlock
	 *            : (block)=(hunger)
	 * @param category
	 * @param target
	 */
	private static void ByBlockRate(String[] defaultBlock, String category, GeneralProperty target) {
		String[] block;
		block = config.get(category, KEY_hunger_block, defaultBlock).getStringList();
		for (String i : block) {
			String[] split = StringParser.splitByLevel(i.replaceAll(" ", ""), '=');
			if (split.length == 2) {
				HashBlockState hashblock = ConfigurationHelper.instance.getHashBlock(split[0]);
				double hunger = Double.parseDouble(StringParser.reduceLevel(split[1]));
				if (hashblock == null) {
					ConfigurationHelper.exceptionInvalidFormat(split[0]);
					exceptionPrintPosition(category, KEY_hunger_block);
					continue;
				}
				target.hunger_block.put(hashblock, hunger);
			} else {
				ConfigurationHelper.exceptionInvalidNumberOfArgument(i);
				exceptionPrintPosition(category, KEY_hunger_block);
				continue;
			}
		}
	}

	private static void exceptionPrintPosition(String category, String key) {
		HungryAnimals.logger.warn("Exception occured at " + category + "-" + key);
	}

}
