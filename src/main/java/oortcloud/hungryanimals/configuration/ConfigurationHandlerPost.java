package oortcloud.hungryanimals.configuration;

import java.io.File;

import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.properties.handler.GeneralEntityManager;
import oortcloud.hungryanimals.entities.properties.handler.GeneralPropertiesHandler;
import oortcloud.hungryanimals.entities.properties.handler.GeneralProperty;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandlerPost {

	public static Configuration config;
	
	public static final String CATEGORY_Generic = "Generic";
	public static final String KEY_entities = "Added Mod Entities";
	
	public static void init(File file) {
		config = new Configuration(file);
		config.load();
	}
	
	public static void sync() {
		HungryAnimals.logger.info("Configuration: Post start");
		
		HungryAnimals.logger.info("Configuration: Check compatibility of registered Entity Classes");
		HungryAnimals.logger.info("Configuration: Compatible entities' name :");
		for (Object i : EntityList.classToStringMapping.keySet()) {
			if (EntityAnimal.class.isAssignableFrom((Class)i)) {
				HungryAnimals.logger.info("Configuration: " + (String)EntityList.classToStringMapping.get(i));
			}
		}
		HungryAnimals.logger.info("Configuration: Uncompatible entities' name :");
		for (Object i : EntityList.classToStringMapping.keySet()) {
			if (!EntityAnimal.class.isAssignableFrom((Class)i)) {
				HungryAnimals.logger.info("Configuration: " + (String)EntityList.classToStringMapping.get(i));
			}
		}
		
		HungryAnimals.logger.info("Configuration: Read and Register mod entities' from Animal.cfg to GeneralEntityManager");
		for (String i : config.get(CATEGORY_Generic, KEY_entities, ArrayUtils.EMPTY_STRING_ARRAY).getStringList()) {
			HungryAnimals.logger.info("Configuration: Read entity name " + i + " from Animal.cfg");
			Class entityClass = (Class) EntityList.stringToClassMapping.get(i);
			if (entityClass != null && EntityAnimal.class.isAssignableFrom(entityClass)) {
				HungryAnimals.logger.info("Configuration: Register corresponding class " + entityClass);
				GeneralEntityManager.getInstance().entities.add(entityClass);
			}
		}

		HungryAnimals.logger.info("Configuration: Create and Register GeneralProperty of Mod animals");
		ConfigurationHandlerAnimal.setPropertiesGeneral(config);
		
		config.save();
	}
	
}
