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
import oortcloud.hungryanimals.api.API;
import oortcloud.hungryanimals.configuration.util.ConfigurationHelper;
import oortcloud.hungryanimals.configuration.util.StringParser;
import oortcloud.hungryanimals.configuration.util.ValueDropMeat;
import oortcloud.hungryanimals.configuration.util.ValueDropRandom;
import oortcloud.hungryanimals.configuration.util.ValueDropRare;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState.HashBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;
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
		for (Object i : EntityList.CLASS_TO_NAME.keySet()) {
			if (EntityAnimal.class.isAssignableFrom((Class) i)) {
				HungryAnimals.logger.info("Configuration: " + (String) EntityList.CLASS_TO_NAME.get(i));
			}
		}
		HungryAnimals.logger.info("Configuration: Uncompatible entities' name :");
		for (Object i : EntityList.CLASS_TO_NAME.keySet()) {
			if (!EntityAnimal.class.isAssignableFrom((Class) i)) {
				HungryAnimals.logger.info("Configuration: " + (String) EntityList.CLASS_TO_NAME.get(i));
			}
		}

		HungryAnimals.logger.info("Configuration: Read and Register mod entities' from Animal.cfg to HungryAnimalManager");
		for (String i : config.get(CATEGORY_Generic, KEY_entities, ArrayUtils.EMPTY_STRING_ARRAY).getStringList()) {
			HungryAnimals.logger.info("Configuration: Read entity name " + i + " from Animal.cfg");
			Class entityClass = (Class) EntityList.NAME_TO_CLASS.get(i);
			if (entityClass != null && EntityAnimal.class.isAssignableFrom(entityClass) && !HungryAnimalManager.getInstance().isRegistered(entityClass)) {
				HungryAnimals.logger.info("Configuration: Register corresponding class " + entityClass);
				API.registerAnimal(entityClass);
			}
		}
		

		HungryAnimalManager.getInstance().readFromConfig(config);

		config.save();
	}

	public static String categoryGenerator(Class<? extends EntityAnimal> entityClass) {
		return (String) EntityList.CLASS_TO_NAME.get(entityClass);
	}

	public static void exceptionPrintPosition(String category, String key) {
		HungryAnimals.logger.warn("Exception occured at " + category + "-" + key);
	}

}
