package oortcloud.hungryanimals.configuration;

import java.io.File;

import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.properties.handler.GenericEntityManager;

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
		GenericEntityManager.getInstance().init();

		for (String i : config.get(CATEGORY_Generic, KEY_entities, ArrayUtils.EMPTY_STRING_ARRAY).getStringList()) {
			HungryAnimals.logger.info("Configuration: finding entity " + i + " in Entity List...");
			Class entityClass = (Class) EntityList.stringToClassMapping.get(i);
			if (entityClass != null && EntityAnimal.class.isAssignableFrom(entityClass)) {
				HungryAnimals.logger.info("Configuration: have detected mod entity: " + i);
				GenericEntityManager.getInstance().entities.add(entityClass);
			}
		}

		for (Object i : EntityList.classToStringMapping.keySet()) {
			if (EntityAnimal.class.isAssignableFrom((Class)i)) {
				HungryAnimals.logger.info("Configuration: Registered Entity Class " + (String)EntityList.classToStringMapping.get(i) + " is compatible.");
			} else {
				HungryAnimals.logger.info("Configuration: Registered Entity Class " + (String)EntityList.classToStringMapping.get(i) + " is not compatible.");
			}
		}
		
		config.save();
	}
	
}
