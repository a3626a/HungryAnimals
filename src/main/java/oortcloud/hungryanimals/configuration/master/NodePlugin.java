package oortcloud.hungryanimals.configuration.master;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;

import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.api.HAPlugins;

public class NodePlugin extends Node {

	private Path base;
	
	public NodePlugin() {
		this(null);
	}
	
	public NodePlugin(Path base) {
		this.base = base;
	}
	
	@Override
	public Map<Path, JsonElement> build() {
		Map<Path, JsonElement> map = new HashMap<>();
		try {
			HAPlugins.getInstance().walkPlugins((path, jsonElement)->{
				if (base != null) {
					if (path.startsWith(base)) {
						map.put(path, jsonElement);
					}
				} else {
					map.put(path, jsonElement);
				}
			}, null);
		} catch (IOException | URISyntaxException e) {
			HungryAnimals.logger.error("Failed to load default json files.");
		}
		return map;
	}
	
}
