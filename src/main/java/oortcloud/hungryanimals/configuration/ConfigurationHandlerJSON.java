package oortcloud.hungryanimals.configuration;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gson.JsonElement;

import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.utils.R;

public class ConfigurationHandlerJSON {

	protected Path directory;
	protected String descriptor;

	private Consumer<JsonElement> read;

	public ConfigurationHandlerJSON(Path basefolder, String descriptor) {
		this.descriptor = descriptor;
		this.directory = basefolder;
	}

	public ConfigurationHandlerJSON(Path basefolder, String descriptor, Consumer<JsonElement> read) {
		this.descriptor = descriptor;
		this.directory = basefolder;
		this.read = read;
	}

	public void sync(Map<R, JsonElement> map) {
		R path = R.get(descriptor + ".json");

		if (map.containsKey(path)) {
			read.accept(map.get(path));
		} else {
			HungryAnimals.logger.error("couldn\'t load {}", path);
		}

	}

	public String getDescriptor() {
		return this.descriptor;
	}
}
