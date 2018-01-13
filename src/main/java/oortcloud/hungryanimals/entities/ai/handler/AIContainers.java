package oortcloud.hungryanimals.entities.ai.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
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
import oortcloud.hungryanimals.entities.ai.EntityAIAttackMeleeCustom;
import oortcloud.hungryanimals.entities.ai.EntityAIAvoidPlayer;
import oortcloud.hungryanimals.entities.ai.EntityAIHunt;
import oortcloud.hungryanimals.entities.ai.EntityAIHuntNonTamed;
import oortcloud.hungryanimals.entities.ai.EntityAIHurtByPlayer;
import oortcloud.hungryanimals.entities.ai.EntityAIMateModified;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToEatBlock;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToEatItem;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToTrough;
import oortcloud.hungryanimals.entities.ai.EntityAITemptEdibleItem;
import oortcloud.hungryanimals.entities.ai.EntityAITemptIngredient;
import oortcloud.hungryanimals.entities.ai.handler.AIContainerTask.AIRemoverIsInstance;

public class AIContainers {

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

	public void init() {
		register("herbivore", AIContainerHerbivore::parse);
		register("herbivore", "attack_melee", EntityAIAttackMeleeCustom::parse);
		register("herbivore", "avoid_player", EntityAIAvoidPlayer::parse);
		register("herbivore", "mate", EntityAIMateModified::parse);
		register("herbivore", "trough", EntityAIMoveToTrough::parse);
		register("herbivore", "tempt", EntityAITemptIngredient::parse);
		register("herbivore", "tempt_edible", EntityAITemptEdibleItem::parse);
		register("herbivore", "eat_item", EntityAIMoveToEatItem::parse);
		register("herbivore", "eat_block", EntityAIMoveToEatBlock::parse);
		register("herbivore", "hurt_by_player", EntityAIHurtByPlayer::parse);
		
		register("rabbit", (jsonEle) -> {
			AIContainer aiContainer = (AIContainer) AIContainerHerbivore.parse(jsonEle);
			aiContainer.getTask().remove(new AIRemoverIsInstance(EntityAIPanic.class));
			aiContainer.getTask().remove(new AIRemoverIsInstance(EntityAIAvoidEntity.class));
			aiContainer.getTask().remove(new AIRemoverIsInstance(EntityAIMoveToBlock.class));
			return aiContainer;
		});
		register("rabbit", "attack_melee", EntityAIAttackMeleeCustom::parse);
		register("rabbit", "avoid_player", EntityAIAvoidPlayer::parse);
		register("rabbit", "mate", EntityAIMateModified::parse);
		register("rabbit", "trough", EntityAIMoveToTrough::parse);
		register("rabbit", "tempt", EntityAITemptIngredient::parse);
		register("rabbit", "tempt_edible", EntityAITemptEdibleItem::parse);
		register("rabbit", "eat_item", EntityAIMoveToEatItem::parse);
		register("rabbit", "eat_block", EntityAIMoveToEatBlock::parse);
		register("rabbit", "hurt_by_player", EntityAIHurtByPlayer::parse);
		
		register("wolf", AIContainerWolf::parse);
		register("wolf", "mate", EntityAIMateModified::parse);
		register("wolf", "trough", EntityAIMoveToTrough::parse);
		register("wolf", "tempt", EntityAITemptIngredient::parse);
		register("wolf", "tempt_edible", EntityAITemptEdibleItem::parse);
		register("wolf", "eat_item", EntityAIMoveToEatItem::parse);
		register("wolf", "eat_block", EntityAIMoveToEatBlock::parse);
		register("wolf", "hunt", EntityAIHunt::parse);
		register("wolf", "hunt_non_tamed", EntityAIHuntNonTamed::parse);
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

}
