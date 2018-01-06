package oortcloud.hungryanimals.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;

public class ConfigurationHandlerJSONAnimal extends ConfigurationHandlerJSON {

	private BiConsumer<String, Class<? extends EntityAnimal>> read;

	/**
	 * 
	 * @param event
	 * @param descriptor : relative path, never start with /
	 * @param read
	 */
	public ConfigurationHandlerJSONAnimal(File basefolder, String descriptor, BiConsumer<String, Class<? extends EntityAnimal>> read) {
		super(new File(basefolder, descriptor), descriptor);
		this.read = read;
	}

	public void sync() {
		checkDirectory();

		for (Class<? extends EntityAnimal> i : HungryAnimalManager.getInstance().getRegisteredAnimal()) {
			File iFile = new File(directory, ConfigurationHandler.resourceLocationToString(EntityList.getKey(i)) + ".json");

			try {
				String text = null;
				if (!iFile.exists()) {
					text = getDefaultConfigurationText(iFile);
				} else {
					text = new String(Files.readAllBytes(iFile.toPath()));
				}
				this.read.read(text, i);
			} catch (JsonSyntaxException | IOException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { this.descriptor, iFile, i, e });
			}
		}

	}

	@FunctionalInterface
	public static interface BiConsumer<T, U> {
		public void read(T file, U entity);
	}

}
