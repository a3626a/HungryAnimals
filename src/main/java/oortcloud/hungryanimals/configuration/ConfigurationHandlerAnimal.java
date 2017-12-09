package oortcloud.hungryanimals.configuration;

import java.io.File;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.common.config.Configuration;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.api.API;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;

public class ConfigurationHandlerAnimal {

	public static Configuration config;

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
		for (Class<? extends Entity> i : EntityList.CLASS_TO_NAME.keySet()) {
			if (EntityAnimal.class.isAssignableFrom(i)) {
				HungryAnimals.logger.info("Configuration: " + (String) EntityList.CLASS_TO_NAME.get(i));
			}
		}
		HungryAnimals.logger.info("Configuration: Uncompatible entities' name :");
		for (Class<? extends Entity> i : EntityList.CLASS_TO_NAME.keySet()) {
			if (!EntityAnimal.class.isAssignableFrom(i)) {
				HungryAnimals.logger.info("Configuration: " + (String) EntityList.CLASS_TO_NAME.get(i));
			}
		}

		HungryAnimals.logger
				.info("Configuration: Read and Register mod entities' from Animal.cfg to HungryAnimalManager");
		for (String i : config.get(CATEGORY_Generic, KEY_entities, ArrayUtils.EMPTY_STRING_ARRAY).getStringList()) {
			HungryAnimals.logger.info("Configuration: Read entity name " + i + " from Animal.cfg");
			Class<? extends Entity> entityClass = EntityList.NAME_TO_CLASS.get(i);
			if (entityClass != null) {
				if (EntityAnimal.class.isAssignableFrom(entityClass) && !HungryAnimalManager.getInstance()
						.isRegistered(entityClass.asSubclass(EntityAnimal.class))) {
					HungryAnimals.logger.info("Configuration: Register corresponding class " + entityClass);
					API.registerAnimal(entityClass.asSubclass(EntityAnimal.class));
				}
			}
		}
		
		config.save();
	}

	public static String categoryGenerator(Class<? extends EntityAnimal> entityClass) {
		return (String) EntityList.CLASS_TO_NAME.get(entityClass);
	}

	public static void exceptionPrintPosition(String category, String key) {
		HungryAnimals.logger.warn("Exception occured at " + category + "-" + key);
	}

}
