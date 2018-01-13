package oortcloud.hungryanimals.api;

import java.util.function.Function;

import com.google.gson.JsonElement;

import oortcloud.hungryanimals.generation.ICondition;

public interface IGrassGeneratorRegistry {
	public boolean registerCondition(String name, ICondition condition, Function<JsonElement, ICondition> parser);
}
