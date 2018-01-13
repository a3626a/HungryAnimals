package oortcloud.hungryanimals.entities.ai.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.api.IAIRegistry;

public class AIContainers implements IAIRegistry {

	private static AIContainers INSTANCE;

	private Map<Class<? extends EntityAnimal>, IAIContainer<EntityAnimal>> REGISTRY;
	private Map<String, Function<JsonElement, IAIContainer<EntityAnimal>>> AICONTAINERS;
	private Map<String, Map<String, BiConsumer<JsonElement, AIContainer>>> MODIFIERS;

	private AIContainers() {
		REGISTRY = new HashMap<Class<? extends EntityAnimal>, IAIContainer<EntityAnimal>>();
		AICONTAINERS = new HashMap<String, Function<JsonElement, IAIContainer<EntityAnimal>>>();
		MODIFIERS = new HashMap<String, Map<String, BiConsumer<JsonElement, AIContainer>>>();
	}

	public static AIContainers getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AIContainers();
		}
		return INSTANCE;
	}

	public IAIContainer<EntityAnimal> register(Class<? extends EntityAnimal> animal, IAIContainer<EntityAnimal> aiContainer) {
		return REGISTRY.put(animal, aiContainer);
	}

	public void register(String name, Function<JsonElement, IAIContainer<EntityAnimal>> parser) {
		AICONTAINERS.put(name, parser);
	}

	public void register(String nameType, String nameModifier, BiConsumer<JsonElement, AIContainer> modifier) {
		if (!MODIFIERS.containsKey(nameType)) {
			MODIFIERS.put(nameType, new HashMap<String, BiConsumer<JsonElement, AIContainer>>());
		}
		MODIFIERS.get(nameType).put(nameModifier, modifier);
	}

	public void apply(EntityAnimal animal) {
		REGISTRY.get(animal.getClass()).registerAI(animal);
	}

	public IAIContainer<EntityAnimal> parse(JsonElement jsonEle) {
		if (!(jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI container must an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		JsonObject jsonObj = (JsonObject) jsonEle;

		String aiType = JsonUtils.getString(jsonObj, "type");
		jsonObj.remove("type");

		IAIContainer<EntityAnimal> aiContainer = AICONTAINERS.get(aiType).apply(jsonObj);

		for (Entry<String, JsonElement> i : jsonObj.entrySet()) {
			if (aiContainer instanceof AIContainer) {
				if (MODIFIERS.get(aiType).containsKey(i.getKey())) {
					MODIFIERS.get(aiType).get(i.getKey()).accept(i.getValue(), (AIContainer) aiContainer);
				}
			}
		}

		return aiContainer;
	}

	@Override
	public void registerAIContainerModifier(String type, String modifierName, BiConsumer<JsonElement, AIContainer> modifier) {
		register(type, modifierName, modifier);
	}

	@Override
	public void registerAIContainer(String type, Function<JsonElement, IAIContainer<EntityAnimal>> aiContainer) {
		register(type, aiContainer);
	}

}
