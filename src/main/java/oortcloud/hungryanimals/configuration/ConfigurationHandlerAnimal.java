package oortcloud.hungryanimals.configuration;

import java.io.File;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraftforge.common.config.Configuration;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.configuration.util.ConfigurationHelper;
import oortcloud.hungryanimals.configuration.util.HashBlockState;
import oortcloud.hungryanimals.configuration.util.HashItemType;
import oortcloud.hungryanimals.configuration.util.StringParser;
import oortcloud.hungryanimals.configuration.util.ValueDropMeat;
import oortcloud.hungryanimals.configuration.util.ValueDropRandom;
import oortcloud.hungryanimals.configuration.util.ValueDropRare;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryCow;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungrySheep;
import oortcloud.hungryanimals.entities.properties.handler.AnimalCharacteristic;
import oortcloud.hungryanimals.entities.properties.handler.HungryAnimalManager;

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

	public static final String CATEGORY_Generic = "Generic";
	public static final String KEY_entities = "Added Mod Entities";
	
	public static void init(File file) {
		config = new Configuration(file);
		config.load();
	}

	public static void sync() {
		HungryAnimals.logger.info("Configuration: Animal start");

		HungryAnimals.logger.info("Configuration: Check compatibility of registered Entity Classes");
		HungryAnimals.logger.info("Configuration: Compatible entities' name :");
		for (Object i : EntityList.classToStringMapping.keySet()) {
			if (EntityAnimal.class.isAssignableFrom((Class) i)) {
				HungryAnimals.logger.info("Configuration: " + (String) EntityList.classToStringMapping.get(i));
			}
		}
		HungryAnimals.logger.info("Configuration: Uncompatible entities' name :");
		for (Object i : EntityList.classToStringMapping.keySet()) {
			if (!EntityAnimal.class.isAssignableFrom((Class) i)) {
				HungryAnimals.logger.info("Configuration: " + (String) EntityList.classToStringMapping.get(i));
			}
		}

		HungryAnimals.logger.info("Configuration: Read and Register mod entities' from Animal.cfg to HungryAnimalManager");
		for (String i : config.get(CATEGORY_Generic, KEY_entities, ArrayUtils.EMPTY_STRING_ARRAY).getStringList()) {
			HungryAnimals.logger.info("Configuration: Read entity name " + i + " from Animal.cfg");
			Class entityClass = (Class) EntityList.stringToClassMapping.get(i);
			if (entityClass != null && EntityAnimal.class.isAssignableFrom(entityClass) && !HungryAnimalManager.getInstance().isRegistered(entityClass)) {
				HungryAnimals.logger.info("Configuration: Register corresponding class " + entityClass);
				HungryAnimalManager.getInstance().registerHungryAnimal(entityClass, null);
			}
		}

		HungryAnimalManager.getInstance().readFromConfig(config);

		config.save();
	}

	public static String categoryGenerator(Class<? extends EntityAnimal> entityClass) {
		return (String) EntityList.classToStringMapping.get(entityClass);
	}

	public static void readDropMeat(Configuration config, String[] defaultfood, String category, AnimalCharacteristic target) {
		String[] drops;
		drops = config.get(category, KEY_drop_meat, defaultfood).getStringList();
		for (String i : drops) {
			ValueDropMeat j = ConfigurationHelper.instance.getDropMeat(i);
			if (j == null) {
				ConfigurationHelper.exceptionInvalidFormat(i);
				exceptionPrintPosition(category, KEY_drop_meat);
				continue;
			}
			target.drop_meat.add(j);
		}
	}

	public static void readDropRandom(Configuration config, String[] defaultfood, String category, AnimalCharacteristic target) {
		String[] drops;
		drops = config.get(category, KEY_drop_random, defaultfood).getStringList();
		for (String i : drops) {
			ValueDropRandom j = ConfigurationHelper.instance.getDropRandom(i);
			if (j == null) {
				ConfigurationHelper.exceptionInvalidFormat(i);
				exceptionPrintPosition(category, KEY_drop_random);
				continue;
			}
			target.drop_random.add(j);
		}
	}

	public static void readDropRare(Configuration config, String[] defaultfood, String category, AnimalCharacteristic target) {
		String[] drops;
		drops = config.get(category, KEY_drop_rare, defaultfood).getStringList();
		for (String i : drops) {
			ValueDropRare j = ConfigurationHelper.instance.getDropRare(i);
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
	public static void ByFoodRate(Configuration config, String[] defaultfood, String category, AnimalCharacteristic target) {
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
	public static void ByBlockRate(Configuration config, String[] defaultBlock, String category, AnimalCharacteristic target) {
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

	public static void exceptionPrintPosition(String category, String key) {
		HungryAnimals.logger.warn("Exception occured at " + category + "-" + key);
	}

}
