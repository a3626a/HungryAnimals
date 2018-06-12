package oortcloud.hungryanimals.entities.ai.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.api.IAIRegistry;

public class AIContainers implements IAIRegistry {

	private static AIContainers INSTANCE;

	private Map<Class<? extends EntityLiving>, IAIContainer<EntityLiving>> REGISTRY;
	private Map<String, Function<JsonElement, IAIContainer<EntityLiving>>> AICONTAINERS;
	private Map<String, Map<String, BiConsumer<JsonElement, AIContainer>>> MODIFIERS;

	private AIContainers() {
		REGISTRY = new HashMap<Class<? extends EntityLiving>, IAIContainer<EntityLiving>>();
		AICONTAINERS = new HashMap<String, Function<JsonElement, IAIContainer<EntityLiving>>>();
		MODIFIERS = new HashMap<String, Map<String, BiConsumer<JsonElement, AIContainer>>>();
	}

	public static AIContainers getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AIContainers();
		}
		return INSTANCE;
	}

	public IAIContainer<EntityLiving> register(Class<? extends EntityLiving> animal, IAIContainer<EntityLiving> aiContainer) {
		return REGISTRY.put(animal, aiContainer);
	}

	public void register(String name, Function<JsonElement, IAIContainer<EntityLiving>> parser) {
		AICONTAINERS.put(name, parser);
	}

	public void register(String nameType, String nameModifier, BiConsumer<JsonElement, AIContainer> modifier) {
		if (!MODIFIERS.containsKey(nameType)) {
			MODIFIERS.put(nameType, new HashMap<String, BiConsumer<JsonElement, AIContainer>>());
		}
		MODIFIERS.get(nameType).put(nameModifier, modifier);
	}

	public void apply(EntityLiving animal) {
		REGISTRY.get(animal.getClass()).registerAI(animal);
	}

	public IAIContainer<EntityLiving> parse(JsonElement jsonEle) {
		if (!(jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI container must an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		JsonObject jsonObj = (JsonObject) jsonEle;

		String aiType = JsonUtils.getString(jsonObj, "type");
		jsonObj.remove("type");

		IAIContainer<EntityLiving> aiContainer = AICONTAINERS.get(aiType).apply(jsonObj);

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
	public void registerAIContainer(String type, Function<JsonElement, IAIContainer<EntityLiving>> aiContainer) {
		register(type, aiContainer);
	}

}
