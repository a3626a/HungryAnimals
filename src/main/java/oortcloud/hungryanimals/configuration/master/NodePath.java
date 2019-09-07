package oortcloud.hungryanimals.configuration.master;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import oortcloud.hungryanimals.utils.R;

public class NodePath extends Node {

	private Path base;
	private Path directory;

	/**
	 *
	 * @param base : The base path to calculate R. (resource name)
	 *               Resource name is calculated by relative path from base to the resource.
	 * @param directory : The directory to load json files.
	 */
	public NodePath(Path base, Path directory) {
		this.base = base;
		this.directory = directory;
	}

	public NodePath(Path base) {
		this(base, base);
	}

	@Override
	public Map<R, JsonElement> build() {
		Map<R, JsonElement> map = new HashMap<>();
		try (Stream<Path> stream = Files.walk(directory)) {
			stream.forEach((Path path) -> {
				if (Files.isRegularFile(path)) {
					if (path.toString().endsWith(".json")) {
						try {
							map.put(R.get(base.relativize(path)), (new JsonParser()).parse(new String(Files.readAllBytes(path))));
						} catch (JsonParseException | IOException e) {
							throw new RuntimeException(String.format("An error occured while parsing %s.", path.toString()), e);
						}
					}
				}
			});
		} catch (IOException e) {
			// When there's no configuration folder. 100% Ok.
		}

		return map;
	}

}
