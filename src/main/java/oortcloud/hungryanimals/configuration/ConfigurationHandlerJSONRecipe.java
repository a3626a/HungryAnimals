package oortcloud.hungryanimals.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.JsonSyntaxException;

import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;

public class ConfigurationHandlerJSONRecipe extends ConfigurationHandlerJSON{

	private static final String nameAnimalGlue = "animalglue";

	private Consumer<File> read;
	
	public ConfigurationHandlerJSONRecipe(File basefolder, String descriptor, Consumer<File> read) {
		this.descriptor = descriptor;
		this.directory = new File(basefolder, descriptor);
		this.read = read;
	}
	
	public void sync() {
		checkDirectory();
		
		File file = new File(directory, nameAnimalGlue + ".json");
		
		if (!file.exists()) {
			createDefaultConfigurationFile(file);
		}
		
		try {
			this.read.read(file);
		} catch (JsonSyntaxException e) {
			HungryAnimals.logger.error("Couldn\'t load {} {}\n{}", new Object[] { this.descriptor, file, e });
		}
	}

	@FunctionalInterface
	public static interface Consumer<T> {
		public void read(T file);
	}
	
}
