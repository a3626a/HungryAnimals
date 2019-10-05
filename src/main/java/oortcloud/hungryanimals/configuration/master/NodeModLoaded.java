package oortcloud.hungryanimals.configuration.master;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import net.minecraftforge.fml.common.Loader;
import oortcloud.hungryanimals.api.HAPlugins;
import oortcloud.hungryanimals.utils.R;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.*;

public class NodeModLoaded extends Node {

	private final List<String> PREFIXES = Lists.newArrayList(
			"ais/", "attributes/", "food_preferences/block/", "food_preferences/entity/",
			"food_preferences/fluid/", "food_preferences/item/", "loot_tables/", "productions/", "animal/"
	);

	private Node parent;
	private Set<String> targetModNames;

	public NodeModLoaded() {
		this(null, null);
	}

	public NodeModLoaded(Node parent, Set<String> targetModNames) {
		this.parent = parent;
		this.targetModNames = targetModNames;
	}

	@Override
	public Map<R, JsonElement> build() {
		Map<R, JsonElement> built = parent.build();
		Map<R, JsonElement> result = new HashMap<>();

		for (Map.Entry<R, JsonElement> e : built.entrySet()) {
			R i = e.getKey();
			boolean found = false;
			String foundDomain = null;

			for (String prefix : PREFIXES) {
				String r = i.toString();
				if (r.startsWith(prefix)) {
					String domain = r.substring(prefix.length());
					domain = domain.substring(0, domain.indexOf('/'));

					if (targetModNames.contains(domain)) {
						found = true;
						foundDomain = domain;
						break;
					}
				}
			}

			if (found) {
				// Pass When The Mod Loaded
				if (Loader.isModLoaded(foundDomain)) {
					result.put(i, e.getValue());
				}
			} else {
				// Just Pass
				result.put(i, e.getValue());
			}
		}

		return result;
	}
}
