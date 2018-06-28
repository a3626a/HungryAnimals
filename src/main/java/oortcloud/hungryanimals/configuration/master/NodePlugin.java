package oortcloud.hungryanimals.configuration.master;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;

import oortcloud.hungryanimals.api.HAPlugins;
import oortcloud.hungryanimals.utils.R;

public class NodePlugin extends Node {

	private Path base;
	
	public NodePlugin() {
		this(null);
	}
	
	public NodePlugin(Path base) {
		this.base = base;
	}
	
	@Override
	public Map<R, JsonElement> build() {
		Map<R, JsonElement> map = new HashMap<>();
		try {
			HAPlugins.getInstance().walkPlugins((path, jsonElement)->{
				if (base != null) {
					if (path.toString().startsWith(base.toString())) {
						map.put(path, jsonElement);
					}
				} else {
					map.put(path, jsonElement);
				}
			}, null);
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException("An error occured while parsing default(built-in) configuration files. Please report to mod author.", e);
		}
		return map;
	}
	
}
