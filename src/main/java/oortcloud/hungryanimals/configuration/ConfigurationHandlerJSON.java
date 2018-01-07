package oortcloud.hungryanimals.configuration;

import java.io.File;
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

	private Consumer<String> read;

	public ConfigurationHandlerJSON(File basefolder, String descriptor) {
		this.descriptor = descriptor;
		this.directory = basefolder;
	}

	public ConfigurationHandlerJSON(File basefolder, String descriptor, Consumer<String> read) {
		this.descriptor = descriptor;
		this.directory = basefolder;
		this.read = read;
	}

	public void sync() {
		checkDirectory();
		File file = new File(directory, descriptor + ".json");
		try {
			String text = null;
			if (!file.exists()) {
				text = getDefaultConfigurationText(file);
			} else {
				text = new String(Files.readAllBytes(file.toPath()));
			}
			this.read.accept(text);
		} catch (JsonSyntaxException | IOException e) {
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

	protected String getDefaultConfigurationText(File file) {
		String path_file = file.getPath();
		int index_config = path_file.indexOf("config");
		String path_config = path_file.substring(index_config);
		int index_hungryanimals = path_config.indexOf(References.MODID);
		String path_hungryanimals = path_config.substring(index_hungryanimals);
		String resourceName = "/assets/" + path_hungryanimals.replace("\\", "/");

		URL url = getClass().getResource(resourceName);
		if (url == null) {
			HungryAnimals.logger.error("Couldn\'t load {} {} from assets", new Object[] { this.descriptor, resourceName });
			return null;
		}

		try {
			return Resources.toString(url, Charsets.UTF_8);
		} catch (IOException e) {
			HungryAnimals.logger.error("Couldn\'t load {} {} from {}\n{}", new Object[] { this.descriptor, file, url, e });
		}
		return null;
	}

	public String getDescriptor() {
		return this.descriptor;
	}
}
