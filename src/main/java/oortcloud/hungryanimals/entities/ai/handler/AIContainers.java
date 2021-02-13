package oortcloud.hungryanimals.entities.ai.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.MobEntity;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.registries.DeferredRegister;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.api.IAIRegistry;
import oortcloud.hungryanimals.core.lib.LazyForgeRegistry;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;

public class AIContainers implements IAIRegistry {

	private static AIContainers INSTANCE;

	private Map<Class<? extends MobEntity>, IAIContainer<MobEntity>> REGISTRY;
	private Map<String, BiFunction<Class<? extends MobEntity>, JsonElement, IAIContainer<MobEntity>>> AICONTAINERS;
	private Map<String, Map<String, BiConsumer<JsonElement, AIContainer>>> MODIFIERS;

	public static final DeferredRegister<ModAttributes.AttributePair> AI_CONTAINERS = new DeferredRegister<>(LazyForgeRegistry.of(ModAttributes.AttributePair.class), References.MODID);


	private AIContainers() {
		REGISTRY = new HashMap<>();
		AICONTAINERS = new HashMap<>();
		MODIFIERS = new HashMap<>();
	}

	public static AIContainers getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AIContainers();
		}
		return INSTANCE;
	}

	public IAIContainer<MobEntity> register(Class<? extends MobEntity> animal, IAIContainer<MobEntity> aiContainer) {
		return REGISTRY.put(animal, aiContainer);
	}

	public void register(String name, BiFunction<Class<? extends MobEntity>, JsonElement, IAIContainer<MobEntity>> parser) {
		AICONTAINERS.put(name, parser);
	}

	public void register(String nameType, String nameModifier, BiConsumer<JsonElement, AIContainer> modifier) {
		if (!MODIFIERS.containsKey(nameType)) {
			MODIFIERS.put(nameType, new HashMap<>());
		}
		MODIFIERS.get(nameType).put(nameModifier, modifier);
	}

	public void apply(MobEntity animal) {
		REGISTRY.get(animal.getClass()).registerAI(animal);
	}

	public IAIContainer<MobEntity> parse(Class<? extends MobEntity> entityClass, JsonElement jsonEle) {
		if (!(jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI container must an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		JsonObject jsonObj = (JsonObject) jsonEle;

		String aiType = JSONUtils.getString(jsonObj, "type");
		jsonObj.remove("type");

		IAIContainer<MobEntity> aiContainer = AICONTAINERS.get(aiType).apply(entityClass, jsonObj);

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
	public void registerAIContainer(String type, BiFunction<Class<? extends MobEntity>, JsonElement, IAIContainer<MobEntity>> aiContainer) {
		register(type, aiContainer);
	}

}
