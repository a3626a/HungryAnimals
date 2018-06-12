package oortcloud.hungryanimals.configuration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.google.gson.JsonElement;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;

public class ConfigurationHandlerJSONAnimal extends ConfigurationHandlerJSON {

	
	private BiConsumer<JsonElement, Class<? extends EntityLiving>> read;
	private String descriptor;
	/**
	 * 
	 * @param event
	 * @param descriptor : relative path, never start with /
	 * @param read
	 */
	public ConfigurationHandlerJSONAnimal(Path basefolder, String descriptor, BiConsumer<JsonElement, Class<? extends EntityLiving>> read) {
		super(basefolder.resolve(descriptor), descriptor);
		this.read = read;
		this.descriptor = descriptor;
	}

	public void sync(Map<Path, JsonElement> map) {
		for (Class<? extends EntityLiving> i : HungryAnimalManager.getInstance().getRegisteredAnimal()) {
			Path path = ConfigurationHandler.resourceLocationToPath(EntityList.getKey(i), "json");
			path = Paths.get(descriptor).resolve(path);
			
			if (map.containsKey(path)) {
				read.read(map.get(path), i);
			} else {
				HungryAnimals.logger.warn("couldn\'t load {}. this could be intended.", path);
			}
		}
	}

	@FunctionalInterface
	public static interface BiConsumer<T, U> {
		public void read(T file, U entity);
	}

}
