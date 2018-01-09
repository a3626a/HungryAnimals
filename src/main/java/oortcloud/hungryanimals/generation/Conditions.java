package oortcloud.hungryanimals.generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import oortcloud.hungryanimals.HungryAnimals;

public class Conditions {

	public static Map<String, Function<JsonElement, ICondition>> REGISTRY = new HashMap<String, Function<JsonElement, ICondition>>();
	
	static {
		REGISTRY.put("below", ConditionBelow::parse);
		REGISTRY.put("chance", ConditionChance::parse);
		REGISTRY.put("not_adjacent", ConditionAdjacent::parse);
	}
	
	public static ICondition parse(JsonElement jsonEle) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("Condition must an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		JsonObject jsonObj = (JsonObject) jsonEle;

		List<ICondition> conditions = new ArrayList<ICondition>();
		for (Entry<String, JsonElement> i : jsonObj.entrySet()) {
			conditions.add(REGISTRY.get(i.getKey()).apply(i.getValue()));
		}
		
		return new ConditionAnd(conditions);
	}
	
}
