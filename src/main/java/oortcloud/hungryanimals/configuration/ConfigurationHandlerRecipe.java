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

public class ConfigurationHandlerRecipe {

	private static final String nameAnimalGlue = "animalglue";

	private Consumer<File> read;
	private File directory;
	private String descriptor;
	
	
	public ConfigurationHandlerRecipe(File basefolder, String descriptor, Consumer<File> read) {
		this.descriptor = descriptor;
		this.directory = new File(basefolder, descriptor);
		this.read = read;
	}
	
	public void sync() {
		if (!directory.exists()) {
			try {
				Files.createDirectories(directory.toPath());
			} catch (IOException e) {
				HungryAnimals.logger.error("Couldn\'t create {} folder {}\n{}", new Object[] { descriptor, directory, e });
				return;
			}
		}
		
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

	private void createDefaultConfigurationFile(File file) {
		String resourceName = "/assets/" + References.MODID + "/" + this.descriptor + "/" + file.getName();
		URL url = getClass().getResource(resourceName);
		if (url == null) {
			HungryAnimals.logger.error("Couldn\'t load {} {} from assets", new Object[] { this.descriptor, resourceName });
			return;
		}

		try {
			file.createNewFile();
			FileWriter o = new FileWriter(file);
			o.write(Resources.toString(url, Charsets.UTF_8));
			o.close();
		} catch (IOException e) {
			HungryAnimals.logger.error("Couldn\'t load {} {} from {}\n{}", new Object[] { this.descriptor, file, url, e });
		}
		
	}

	public String getDescriptor() {
		return this.descriptor;
	}
	
	@FunctionalInterface
	public static interface Consumer<T> {
		public void read(T file);
	}
	
}
