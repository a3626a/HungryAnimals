package oortcloud.hungryanimals.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.api.HAPlugins;

public class ConfigurationHandlerJSON {

	protected File directory;
	protected String descriptor;

	private Consumer<JsonElement> read;

	public ConfigurationHandlerJSON(File basefolder, String descriptor) {
		this.descriptor = descriptor;
		this.directory = basefolder;
	}

	public ConfigurationHandlerJSON(File basefolder, String descriptor, Consumer<JsonElement> read) {
		this.descriptor = descriptor;
		this.directory = basefolder;
		this.read = read;
	}

	public void sync() {
		checkDirectory();
		File file = new File(directory, descriptor + ".json");
		try {
			JsonElement json = null;
			if (!file.exists()) {
				json = HAPlugins.getInstance().getJson(Paths.get(descriptor+".json"));
			} else {
				json = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath())));
			}
			this.read.accept(json);
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

	public String getDescriptor() {
		return this.descriptor;
	}
}
