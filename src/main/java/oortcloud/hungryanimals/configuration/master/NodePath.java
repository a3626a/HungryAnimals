package oortcloud.hungryanimals.configuration.master;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.utils.R;

public class NodePath extends Node {

	private Path base;

	public NodePath(Path base) {
		this.base = base;
	}

	@Override
	public Map<R, JsonElement> build() {
		Map<R, JsonElement> map = new HashMap<>();
		try (Stream<Path> stream = Files.walk(base)) {
			stream.forEach((Path path) -> {
				if (Files.isRegularFile(path)) {
					if (path.toString().endsWith(".json")) {
						try {
							map.put(R.get(base.relativize(path)), (new JsonParser()).parse(new String(Files.readAllBytes(path))));
						} catch (JsonSyntaxException e) {
							HungryAnimals.logger.warn("{} is not a valid json file.", path);
						} catch (IOException e) {
							HungryAnimals.logger.warn("{} is corrupted.", path);
						}
					}
				}
			});
		} catch (IOException e) {
			// called when base doesn't exist. No problem.
		}

		return map;
	}

}
