package oortcloud.hungryanimals.configuration.master;

import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonElement;

public class NodeOverride extends Node {

	private Node overrider;
	private Node overridee;
	
	public NodeOverride(Node overrider, Node overridee) {
		this.overrider = overrider;
		this.overridee = overridee;
	}
	
	public Map<Path, JsonElement> build() {
		Map<Path, JsonElement> mapOverrider = overrider.build();
		Map<Path, JsonElement> mapOverridee = overridee.build();
		
		for (Entry<Path, JsonElement> i : mapOverrider.entrySet()) {
			mapOverridee.put(i.getKey(), i.getValue());
		}
		
		return mapOverridee;
	}
	
}
