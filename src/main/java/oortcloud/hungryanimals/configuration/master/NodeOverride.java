package oortcloud.hungryanimals.configuration.master;

import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonElement;

import oortcloud.hungryanimals.utils.R;

public class NodeOverride extends Node {

	private Node overrider;
	private Node overridee;
	
	public NodeOverride(Node overrider, Node overridee) {
		this.overrider = overrider;
		this.overridee = overridee;
	}
	
	public Map<R, JsonElement> build() {
		Map<R, JsonElement> mapOverrider = overrider.build();
		Map<R, JsonElement> mapOverridee = overridee.build();
		
		for (Entry<R, JsonElement> i : mapOverrider.entrySet()) {
			mapOverridee.put(i.getKey(), i.getValue());
		}
		
		return mapOverridee;
	}
	
}
