package oortcloud.hungryanimals.configuration.master;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.google.gson.JsonElement;

import oortcloud.hungryanimals.utils.Pair;

public class NodeModifier extends Node {

	private Node parent;
	private Supplier<List<Pair<Predicate<Path>, UnaryOperator<JsonElement>>>> modifier;

	public NodeModifier(Node parent, Supplier<List<Pair<Predicate<Path>, UnaryOperator<JsonElement>>>> modifier) {
		this.parent = parent;
		this.modifier = modifier;
	}

	@Override
	public Map<Path, JsonElement> build() {
		Map<Path, JsonElement> map = parent.build();
		List<Pair<Predicate<Path>, UnaryOperator<JsonElement>>> pairs = modifier.get();

		for (Path i : map.keySet()) {
			for (Pair<Predicate<Path>, UnaryOperator<JsonElement>> pair : pairs) {
				Predicate<Path> selector = pair.left;
				UnaryOperator<JsonElement> operator = pair.right;
				if (selector.test(i))
					map.put(i, operator.apply(map.get(i)));
			}
		}

		return map;
	}

}
