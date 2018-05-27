package oortcloud.hungryanimals.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
	public ConfigurationHandlerJSONAnimal(Path basefolder, String descriptor, BiConsumer<JsonElement, Class<? extends EntityAnimal>> read) {
		super(basefolder.resolve(descriptor), descriptor);
		this.read = read;
		this.descriptor = descriptor;
	}

	public void sync() {
		checkDirectory();

		for (Class<? extends EntityAnimal> i : HungryAnimalManager.getInstance().getRegisteredAnimal()) {
			Path path = ConfigurationHandler.resourceLocationToPath(EntityList.getKey(i), "json");
			Path configPath = directory.resolve(path);

			try {
				JsonElement json = null;
				if (Files.notExists(configPath)) {
					// Config file not exist, so use default(internal)
					json = HAPlugins.getInstance().getJson(Paths.get(descriptor).resolve(path));
					
					if (json == null)
						continue;
				} else {
					json = (new JsonParser()).parse(new String(Files.readAllBytes(configPath)));
				}
				this.read.read(json, i);
			} catch (JsonSyntaxException | IOException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { this.descriptor, configPath, i, e });
			}
		}

	}

	@FunctionalInterface
	public static interface BiConsumer<T, U> {
		public void read(T file, U entity);
	}

}
