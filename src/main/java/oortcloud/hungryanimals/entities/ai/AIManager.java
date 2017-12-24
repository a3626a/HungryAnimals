package oortcloud.hungryanimals.entities.ai;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAILlamaFollowCaravan;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;

public class AIManager {

	private static AIManager INSTANCE;

	public Map<Class<? extends EntityAnimal>, IAIContainer<EntityAnimal>> REGISTRY;
	public Map<String, Function<Class<? extends EntityAnimal>, IAIContainer<EntityAnimal>>> AITYPES;

	private AIManager() {
		REGISTRY = new HashMap<Class<? extends EntityAnimal>, IAIContainer<EntityAnimal>>();
		AITYPES = new HashMap<String, Function<Class<? extends EntityAnimal>, IAIContainer<EntityAnimal>>>();
	}

	public static AIManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AIManager();
		}
		return INSTANCE;
	}

	public void init() {
		AITYPES.put("herbivore", (animal) -> {
			AIContainer aiContainer = new AIContainer();
			aiContainer.putLast((entity) -> new EntityAISwimming(entity));
			aiContainer.putLast((entity) -> new EntityAIAvoidPlayer(entity, 16.0F, 1.0D, 2.0D));
			aiContainer.putLast((entity) -> new EntityAIMateModified(entity, 2.0D));
			aiContainer.putLast((entity) -> new EntityAIMoveToTrough(entity, 1.0D));
			aiContainer.putLast((entity) -> new EntityAITemptEdibleItem(entity, 1.5D, false));
			aiContainer.putLast((entity) -> new EntityAIMoveToEatItem(entity, 1.5D));
			aiContainer.putLast((entity) -> new EntityAIMoveToEatBlock(entity, 1.0D));
			aiContainer.putLast((entity) -> new EntityAIWander(entity, 1.0D));
			aiContainer.putLast((entity) -> new EntityAIWatchClosest(entity, EntityPlayer.class, 6.0F));
			aiContainer.putLast((entity) -> new EntityAILookIdle(entity));
			aiContainer.removeAll();
			return aiContainer;
		});
		AITYPES.put("pig", (animal) -> {
			AIContainer aiContainer = new AIContainer(1, (AIContainer) AITYPES.get("herbivore").apply(animal));
			aiContainer.priorTo(EntityAITemptEdibleItem.class)
					.put((entity) -> new EntityAITempt(entity, 1.5D, Items.CARROT_ON_A_STICK, false));
			return aiContainer;
		});
		AITYPES.put("horse", (animal) -> {
			AIContainer aiContainer = new AIContainer(1, (AIContainer) AITYPES.get("herbivore").apply(animal));
			aiContainer.priorTo(EntityAIMateModified.class).put((entity) -> new EntityAIRunAroundLikeCrazy((AbstractHorse)entity, 1.2D));
			return aiContainer;
		});
		AITYPES.put("llama", (animal) -> {
			AIContainer aiContainer = new AIContainer(1, (AIContainer) AITYPES.get("herbivore").apply(animal));
			aiContainer.priorTo(EntityAIAvoidPlayer.class).put((entity) -> new EntityAIRunAroundLikeCrazy((AbstractHorse)entity, 1.2D));
			aiContainer.priorTo(EntityAIAvoidPlayer.class).put((entity) -> new EntityAILlamaFollowCaravan((EntityLlama) entity, 2.1D));
			aiContainer.priorTo(EntityAIAvoidPlayer.class).put((entity) -> new EntityAIAttackRanged((IRangedAttackMob) entity, 1.25D, 40, 20.0F));
			return aiContainer;
		});

	}

}
