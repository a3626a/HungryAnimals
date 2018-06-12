package oortcloud.hungryanimals.entities.production.condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityLiving;
import oortcloud.hungryanimals.HungryAnimals;

public class Conditions {

public Map<String, Function<JsonElement, Predicate<EntityLiving>>> PARSERS = new HashMap<String, Function<JsonElement, Predicate<EntityLiving>>>();
	
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
	
	public void register(String name, Function<JsonElement, Predicate<EntityLiving>> parser) {
		PARSERS.put(name, parser);
	}
	
	public static Predicate<EntityLiving> parse(JsonElement jsonEle) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("Condition must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		JsonObject jsonObj = (JsonObject) jsonEle;

		List<Predicate<EntityLiving>> conditions = new ArrayList<Predicate<EntityLiving>>();
		for (Entry<String, JsonElement> i : jsonObj.entrySet()) {
			if (!getInstance().PARSERS.containsKey(i.getKey())) {
				HungryAnimals.logger.warn("{} is not a valid condition name.", i.getKey());
				continue;
			}
			conditions.add(getInstance().PARSERS.get(i.getKey()).apply(i.getValue()));
		}
		
		return Predicates.and(conditions);
	}
	
}
