package oortcloud.hungryanimals.configuration.master;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;

public class Node {

	public Map<Path, JsonElement> build() {
		return new HashMap<>();
	}
	
}
