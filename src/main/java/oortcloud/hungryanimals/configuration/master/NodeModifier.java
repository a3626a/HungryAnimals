package oortcloud.hungryanimals.configuration.master;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.google.gson.JsonElement;

import oortcloud.hungryanimals.utils.Pair;
import oortcloud.hungryanimals.utils.R;

public class NodeModifier extends Node {

	private Node parent;
	private Supplier<List<Pair<Predicate<R>, UnaryOperator<JsonElement>>>> modifier;

	public NodeModifier(Node parent, Supplier<List<Pair<Predicate<R>, UnaryOperator<JsonElement>>>> modifier) {
		this.parent = parent;
		this.modifier = modifier;
	}

	@Override
	public Map<R, JsonElement> build() {
		Map<R, JsonElement> map = parent.build();
		List<Pair<Predicate<R>, UnaryOperator<JsonElement>>> pairs = modifier.get();

		for (R i : map.keySet()) {
			for (Pair<Predicate<R>, UnaryOperator<JsonElement>> pair : pairs) {
				Predicate<R> selector = pair.left;
				UnaryOperator<JsonElement> operator = pair.right;
				if (selector.test(i))
					map.put(i, operator.apply(map.get(i)));
			}
		}

		return map;
	}

}
