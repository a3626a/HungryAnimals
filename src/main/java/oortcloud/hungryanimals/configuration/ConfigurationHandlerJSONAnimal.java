package oortcloud.hungryanimals.configuration;

import java.io.File;

import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;

public class ConfigurationHandlerJSONAnimal extends ConfigurationHandlerJSON {

	private BiConsumer<File, Class<? extends EntityAnimal>> read;

	/**
	 * 
	 * @param event
	 * @param descriptor : relative path, never start with /
	 * @param read
	 */
	public ConfigurationHandlerJSONAnimal(File basefolder, String descriptor, BiConsumer<File, Class<? extends EntityAnimal>> read) {
		this.descriptor = descriptor;
		this.directory = new File(basefolder, descriptor);
		this.read = read;
	}

	public void sync() {
		checkDirectory();

		for (Class<? extends EntityAnimal> i : HungryAnimalManager.getInstance().getRegisteredAnimal()) {
			File iFile = new File(directory, EntityList.CLASS_TO_NAME.get(i).toLowerCase() + ".json");

			if (!iFile.exists()) {
				createDefaultConfigurationFile(iFile);
			}

			try {
				this.read.read(iFile, i);
			} catch (JsonSyntaxException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { this.descriptor, iFile, i, e });
			}

		}
	}

	@FunctionalInterface
	public static interface BiConsumer<T, U> {
		public void read(T file, U entity);
	}

}
