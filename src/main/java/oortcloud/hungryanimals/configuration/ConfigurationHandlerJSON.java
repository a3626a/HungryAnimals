package oortcloud.hungryanimals.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.function.Consumer;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.JsonSyntaxException;

import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;

public class ConfigurationHandlerJSON {
	
	protected File directory;
	protected String descriptor;
	
	private Consumer<File> read;
	
	public ConfigurationHandlerJSON(File basefolder, String descriptor) {
		this.descriptor = descriptor;
		this.directory = basefolder;
	}
	
	public ConfigurationHandlerJSON(File basefolder, String descriptor, Consumer<File> read) {
		this.descriptor = descriptor;
		this.directory = basefolder;
		this.read = read;
	}
	
	public void sync() {
		checkDirectory();
		
		File file = new File(directory, descriptor + ".json");
		
		if (!file.exists()) {
			createDefaultConfigurationFile(file);
		}
		
		try {
			this.read.accept(file);
		} catch (JsonSyntaxException e) {
			HungryAnimals.logger.error("Couldn\'t load {} {}\n{}", new Object[] { this.descriptor, file, e });
		}
	}
	
	protected void checkDirectory() {
		if (!directory.exists()) {
			try {
				Files.createDirectories(directory.toPath());
			} catch (IOException e) {
				HungryAnimals.logger.error("Couldn\'t create {} folder {}\n{}", new Object[] { descriptor, directory, e });
				return;
			}
		}
	}
	
	protected void createDefaultConfigurationFile(File file) {
		String path_file = file.getAbsolutePath();
		int index_config = path_file.indexOf("config");
		String path_config = path_file.substring(index_config);
		int index_hungryanimals = path_config.indexOf(References.MODID);
		String resourceName = "/assets/" + path_config.substring(index_hungryanimals);
		
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
}
