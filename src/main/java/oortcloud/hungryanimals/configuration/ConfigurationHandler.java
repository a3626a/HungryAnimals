package oortcloud.hungryanimals.configuration;

import java.io.File;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigurationHandler {
	
	public static void init(FMLPreInitializationEvent event) {
		ConfigurationHandlerAnimal.init(new File(event.getModConfigurationDirectory() + "/HungryAnimals/Animal.cfg"));
		ConfigurationHandlerWorld.init(new File(event.getModConfigurationDirectory() + "/HungryAnimals/World.cfg"));
		ConfigurationHandlerRecipe.init(new File(event.getModConfigurationDirectory() + "/HungryAnimals/Recipe.cfg"));
		ConfigurationHandlerPost.init(new File(event.getModConfigurationDirectory() + "/HungryAnimals/ModAnimal.cfg"));
	}
	
	public static void sync() {
		ConfigurationHandlerWorld.sync();
		ConfigurationHandlerRecipe.sync();
	}
	
	public static void postSync() {
		ConfigurationHandlerAnimal.sync();
		ConfigurationHandlerPost.sync();
	}
	
}
