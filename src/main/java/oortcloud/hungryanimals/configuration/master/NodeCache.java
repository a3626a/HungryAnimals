package oortcloud.hungryanimals.configuration.master;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;

public class NodeCache extends Node {

	private Node parent;
	public Map<Path, JsonElement> cached;
	
	public NodeCache(Node parent) {
		this.parent = parent;
	}
	
	@Override
	public Map<Path, JsonElement> build() {
		Map<Path, JsonElement> built = parent.build();
		cached = new HashMap<>(built);
		return built;
	}
	
}
