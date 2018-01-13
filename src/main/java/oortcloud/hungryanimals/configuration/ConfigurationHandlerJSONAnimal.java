package oortcloud.hungryanimals.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.api.HAPlugins;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;

public class ConfigurationHandlerJSONAnimal extends ConfigurationHandlerJSON {

	
	private BiConsumer<JsonElement, Class<? extends EntityAnimal>> read;
	private String descriptor;
	/**
	 * 
	 * @param event
	 * @param descriptor : relative path, never start with /
	 * @param read
	 */
	public ConfigurationHandlerJSONAnimal(File basefolder, String descriptor, BiConsumer<JsonElement, Class<? extends EntityAnimal>> read) {
		super(new File(basefolder, descriptor), descriptor);
		this.read = read;
		this.descriptor = descriptor;
	}

	public void sync() {
		checkDirectory();

		for (Class<? extends EntityAnimal> i : HungryAnimalManager.getInstance().getRegisteredAnimal()) {
			String animalName = ConfigurationHandler.resourceLocationToString(EntityList.getKey(i));
			File iFile = new File(directory, animalName+".json");

			try {
				JsonElement json = null;
				if (!iFile.exists()) {
					json = HAPlugins.getInstance().getJson(Paths.get(descriptor, animalName+".json"));
				} else {
					json = (new JsonParser()).parse(new String(Files.readAllBytes(iFile.toPath())));
				}
				this.read.read(json, i);
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
