package oortcloud.hungryanimals.api;

import java.util.function.Function;

import com.google.gson.JsonElement;

import oortcloud.hungryanimals.generation.ICondition;

public interface IGrassGeneratorRegistry {
	public void registerCondition(String name, Function<JsonElement, ICondition> parser);
}
