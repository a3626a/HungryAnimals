package oortcloud.hungryanimals.configuration.master;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.utils.Pair;
import oortcloud.hungryanimals.utils.R;

public class Master {

	public static Supplier<List<Pair<Predicate<R>, UnaryOperator<JsonElement>>>> get(Node node, String domain) {
		return () -> {
			List<Pair<Predicate<R>, UnaryOperator<JsonElement>>> ret = new ArrayList<>();
			Map<R, JsonElement> map = node.build();
			
			JsonElement master = map.get(R.get("master", "master.json"));
			if (master.isJsonObject()) {
				JsonObject jsonObj = (JsonObject) master;
				JsonElement difficulty = map.get(R.get("master", "difficulty", JsonUtils.getString(jsonObj, "difficulty") + ".json"));
				JsonElement tempo = map.get(R.get("master", "tempo", JsonUtils.getString(jsonObj, "tempo") + ".json"));
				ret.addAll(parse(difficulty, domain));
				ret.addAll(parse(tempo, domain));
				// TODO custom master config
				return ret;
			} else {
				throw new JsonParseException("master/master.json is not an json object.");
			}
		};
	}

	public static List<Pair<Predicate<R>, UnaryOperator<JsonElement>>> parse(JsonElement jsonEle, String domain) {
		List<Pair<Predicate<R>, UnaryOperator<JsonElement>>> list = new ArrayList<>();

		if (jsonEle.isJsonArray()) {
			JsonArray jsonArray = (JsonArray) jsonEle;
			for (JsonElement i : jsonArray) {
				if (i.isJsonObject()) {
					JsonObject iObj = (JsonObject) i;
					if (domain.equals("all") || domain.equals(JsonUtils.getString(iObj, "domain"))) {
						list.add(new Pair<Predicate<R>, UnaryOperator<JsonElement>>(parsePattern(JsonUtils.getString(iObj, "pattern")),
								parseModifier(iObj.get("modifier"))));
					} else {
						continue;
					}
				} else {
					throw new JsonParseException(i.toString() + " is not an json object.");
				}
			}
			return list;
		} else {
			throw new JsonParseException("master config must be an json array.");
		}
	}

	private static Predicate<R> parsePattern(String pattern) {
		// File seperator must be replaced into /, because RE considers \ as special character.
		pattern = pattern.replace("\\", "/");
		pattern = pattern.replace("*", ".*");
		Pattern p = Pattern.compile(pattern);

		return (path) -> {
			Matcher m = p.matcher(path.toString().replace("\\", "/"));
			return m.matches();
		};

	}

	public static UnaryOperator<JsonElement> parseModifier(JsonElement modifier) {
		return (modifiee) -> traverse(modifier, modifiee);
	}

	public static JsonElement traverse(JsonElement modifier, JsonElement modifiee) {
		if (isOperator(modifier)) {
			return operate(modifier, modifiee);
		} else {
			if (modifier.isJsonObject() && modifiee.isJsonObject()) {
				JsonObject modifieeObj = modifiee.getAsJsonObject();
				for (Entry<String, JsonElement> i : modifier.getAsJsonObject().entrySet()) {
					if (modifieeObj.has(i.getKey())) {
						modifieeObj.add(i.getKey(), traverse(i.getValue(), modifieeObj.get(i.getKey())));
					} else {
						HungryAnimals.logger.warn("{} doesn\'t have {}. silently ignored.", modifiee, i.getKey());
					}
				}
				return modifiee;
			} else if (modifier.isJsonArray() && modifiee.isJsonArray()) {
				JsonElement modifierFirst = modifier.getAsJsonArray().get(0);
				JsonArray modifieeArr = modifiee.getAsJsonArray();
				for (int i = 0; i < modifieeArr.size(); i++) {
					modifieeArr.set(i, traverse(modifierFirst, modifieeArr.get(i)));
				}
				return modifiee;
			} else {
				throw new JsonParseException(modifiee.toString() + " and " + modifier.toString() + " must be both json object or json array.");
			}
		}
	}

	public static JsonElement operate(JsonElement modifier, JsonElement modifiee) {
		JsonObject jsonObj = (JsonObject) modifier;
		String operation = JsonUtils.getString(jsonObj, "operation");
		float value = JsonUtils.getFloat(jsonObj, "value");
		if (operation.equals("+")) {
			return new JsonPrimitive(modifiee.getAsFloat() + value);
		} else if (operation.equals("*")) {
			return new JsonPrimitive(modifiee.getAsFloat() * value);
		} else if (operation.equals("=")) {
			return new JsonPrimitive(value);
		} else {
			throw new JsonParseException("Unsupported operation " + operation + ".");
		}
	}

	public static boolean isOperator(JsonElement jsonEle) {
		if (jsonEle.isJsonObject()) {
			JsonObject jsonObj = (JsonObject) jsonEle;
			return jsonObj.has("operation") && jsonObj.has("value");
		} else {
			return false;
		}
	}

}
