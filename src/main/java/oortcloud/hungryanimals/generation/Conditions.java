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
import oortcloud.hungryanimals.api.IGrassGeneratorRegistry;

public class Conditions implements IGrassGeneratorRegistry {

	public Map<String, Function<JsonElement, ICondition>> PARSERS = new HashMap<String, Function<JsonElement, ICondition>>();
	
	private static Conditions INSTANCE;
	
	public Conditions() {
		PARSERS = new HashMap<>();
	}
	
	public static Conditions getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Conditions();
		}
		return INSTANCE;
	}
	
	public void register(String name, Function<JsonElement, ICondition> parser) {
		PARSERS.put(name, parser);
	}
	
	public static ICondition parse(JsonElement jsonEle) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("Condition must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		JsonObject jsonObj = (JsonObject) jsonEle;

		List<ICondition> conditions = new ArrayList<ICondition>();
		for (Entry<String, JsonElement> i : jsonObj.entrySet()) {
			if (!getInstance().PARSERS.containsKey(i.getKey())) {
				HungryAnimals.logger.warn("{} is not a valid condition name.", i.getKey());
				continue;
			}
			conditions.add(getInstance().PARSERS.get(i.getKey()).apply(i.getValue()));
		}
		
		return new ConditionAnd(conditions);
	}

	@Override
	public void registerCondition(String name, Function<JsonElement, ICondition> parser) {
		register(name, parser);
	}
	
}
