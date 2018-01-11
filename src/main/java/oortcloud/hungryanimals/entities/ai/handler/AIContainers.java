package oortcloud.hungryanimals.entities.ai.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainerTask.AIRemoverIsInstance;

public class AIContainers {

	private static AIContainers INSTANCE;

	private Map<Class<? extends EntityAnimal>, IAIContainer<EntityAnimal>> REGISTRY;
	private Map<String, Function<JsonElement, IAIContainer<EntityAnimal>>> PARSERS;
	
	private AIContainers() {
		REGISTRY = new HashMap<Class<? extends EntityAnimal>, IAIContainer<EntityAnimal>>();
		PARSERS = new HashMap<String, Function<JsonElement, IAIContainer<EntityAnimal>>>();
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
	
	public void apply(EntityAnimal animal) {
		REGISTRY.get(animal.getClass()).registerAI(animal);
	}
	
	public void init() {
		PARSERS.put("herbivore", AIContainerHerbivore::parse);
		
		PARSERS.put("rabbit", (jsonEle) -> {
			AIContainer aiContainer = (AIContainer) AIContainerHerbivore.parse(jsonEle);
			aiContainer.getTask().remove(new AIRemoverIsInstance(EntityAIPanic.class));
			aiContainer.getTask().remove(new AIRemoverIsInstance(EntityAIAvoidEntity.class));
			aiContainer.getTask().remove(new AIRemoverIsInstance(EntityAIMoveToBlock.class));
			return aiContainer;
		});
		
		PARSERS.put("wolf", AIContainerWolf::parse);

	}
	
	public IAIContainer<EntityAnimal> parse(JsonElement jsonEle) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI container must an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		JsonObject jsonObj = (JsonObject) jsonEle;

		String aiType = JsonUtils.getString(jsonObj, "type");
		
		return PARSERS.get(aiType).apply(jsonObj);
	}
	
}
