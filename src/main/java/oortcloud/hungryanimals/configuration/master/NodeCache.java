package oortcloud.hungryanimals.configuration.master;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;

import oortcloud.hungryanimals.utils.R;

public class NodeCache extends Node {

	private Node parent;
	public Map<R, JsonElement> cached;
	
	public NodeCache(Node parent) {
		this.parent = parent;
	}
	
	@Override
	public Map<R, JsonElement> build() {
		Map<R, JsonElement> built = parent.build();
		cached = new HashMap<>(built);
		return built;
	}
	
}
