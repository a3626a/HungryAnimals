package oortcloud.hungryanimals.configuration;

import java.io.File;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import oortcloud.hungryanimals.core.lib.References;

public class ConfigurationHandler {
	
	public static void init(FMLPreInitializationEvent event) {
		ConfigurationHandlerAnimal.init(new File(event.getModConfigurationDirectory() + "/" + References.MODNAME + "/Animal.cfg"));
		ConfigurationHandlerWorld.init(new File(event.getModConfigurationDirectory() + "/" + References.MODNAME + "/World.cfg"));
		ConfigurationHandlerRecipe.init(new File(event.getModConfigurationDirectory() + "/" + References.MODNAME + "/Recipe.cfg"));
	}
	
	public static void sync() {
		ConfigurationHandlerWorld.sync();
		ConfigurationHandlerRecipe.sync();
	}
	
	public static void postSync() {
		ConfigurationHandlerAnimal.sync();
	}
	
}
