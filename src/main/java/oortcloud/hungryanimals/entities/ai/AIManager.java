package oortcloud.hungryanimals.entities.ai;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.init.Items;
import oortcloud.hungryanimals.entities.ai.AIContainer.AIRemoverIsInstance;

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
			aiContainer.priorTo(EntityAIFollowParent.class).put((entity) -> new EntityAIAvoidPlayer(entity, 16.0F, 1.0D, 2.0D));
			aiContainer.priorTo(EntityAIFollowParent.class).put((entity) -> new EntityAIMateModified(entity, 2.0D));
			aiContainer.priorTo(EntityAIFollowParent.class).put((entity) -> new EntityAIMoveToTrough(entity, 1.0D));
			aiContainer.priorTo(EntityAIFollowParent.class).put((entity) -> new EntityAITemptEdibleItem(entity, 1.5D, false));
			aiContainer.priorTo(EntityAIFollowParent.class).put((entity) -> new EntityAIMoveToEatItem(entity, 1.5D));
			aiContainer.priorTo(EntityAIFollowParent.class).put((entity) -> new EntityAIMoveToEatBlock(entity, 1.0D));
			aiContainer.remove(EntityAIPanic.class);
			aiContainer.remove(EntityAIMate.class);
			aiContainer.remove(EntityAITempt.class);
			aiContainer.remove(EntityAIEatGrass.class); // For Sheep
			return aiContainer;
		});
		
		AITYPES.put("rabbit", (animal) -> {
			AIContainer aiContainer = new AIContainer();
			aiContainer.priorTo(EntityAIWanderAvoidWater.class).put((entity) -> new EntityAIAvoidPlayer(entity, 16.0F, 1.0D, 2.0D));
			aiContainer.priorTo(EntityAIWanderAvoidWater.class).put((entity) -> new EntityAIMateModified(entity, 2.0D));
			aiContainer.priorTo(EntityAIWanderAvoidWater.class).put((entity) -> new EntityAIMoveToTrough(entity, 1.0D));
			aiContainer.priorTo(EntityAIWanderAvoidWater.class).put((entity) -> new EntityAITemptEdibleItem(entity, 1.5D, false));
			aiContainer.priorTo(EntityAIWanderAvoidWater.class).put((entity) -> new EntityAIMoveToEatItem(entity, 1.5D));
			aiContainer.priorTo(EntityAIWanderAvoidWater.class).put((entity) -> new EntityAIMoveToEatBlock(entity, 1.0D));
			aiContainer.remove(new AIRemoverIsInstance(EntityAIPanic.class));
			aiContainer.remove(EntityAIMate.class);
			aiContainer.remove(EntityAITempt.class);
			aiContainer.remove(new AIRemoverIsInstance(EntityAIAvoidEntity.class));
			aiContainer.remove(new AIRemoverIsInstance(EntityAIMoveToBlock.class));
			return aiContainer;
		});
		
		AITYPES.put("pig", (animal) -> {
			AIContainer aiContainer = new AIContainer((AIContainer) AITYPES.get("herbivore").apply(animal));
			aiContainer.priorTo(EntityAITemptEdibleItem.class)
					.put((entity) -> new EntityAITempt(entity, 1.5D, Items.CARROT_ON_A_STICK, false));
			return aiContainer;
		});
		
		AITYPES.put("wolf", (animal)->{
			AIContainerDuplex aiContainer = new AIContainerDuplex();
			aiContainer.getTask().priorTo(EntityAIWanderAvoidWater.class).put((entity) -> new EntityAIMateModified(entity, 2.0D));
			aiContainer.getTask().priorTo(EntityAIWanderAvoidWater.class).put((entity) -> new EntityAIMoveToTrough(entity, 1.0D));
			aiContainer.getTask().priorTo(EntityAIWanderAvoidWater.class).put((entity) -> new EntityAITemptEdibleItem(entity, 1.5D, false));
			aiContainer.getTask().priorTo(EntityAIWanderAvoidWater.class).put((entity) -> new EntityAIMoveToEatItem(entity, 1.5D));
			aiContainer.getTask().priorTo(EntityAIWanderAvoidWater.class).put((entity) -> new EntityAIMoveToEatBlock(entity, 1.0D));
			aiContainer.getTask().remove(EntityAIMate.class);
			
			aiContainer.getTarget().priorTo(EntityAINearestAttackableTarget.class).put((entity) -> new EntityAITargetNonTamed((EntityTameable) entity, false, true));
			aiContainer.getTarget().remove(net.minecraft.entity.ai.EntityAITargetNonTamed.class);
			
			return aiContainer;
	    });

		AITYPES.put("polar_bear", (animal)->{
			AIContainerDuplex aiContainer = new AIContainerDuplex();
			aiContainer.getTask().priorTo(EntityAIFollowParent.class).put((entity) -> new EntityAIMateModified(entity, 2.0D));
			aiContainer.getTask().priorTo(EntityAIFollowParent.class).put((entity) -> new EntityAIMoveToTrough(entity, 1.0D));
			aiContainer.getTask().priorTo(EntityAIFollowParent.class).put((entity) -> new EntityAITemptEdibleItem(entity, 1.5D, false));
			aiContainer.getTask().priorTo(EntityAIFollowParent.class).put((entity) -> new EntityAIMoveToEatItem(entity, 1.5D));
			aiContainer.getTask().priorTo(EntityAIFollowParent.class).put((entity) -> new EntityAIMoveToEatBlock(entity, 1.0D));
			
			aiContainer.getTarget().putLast((entity) -> new EntityAITarget(entity, 1, false, false, false));
			
			return aiContainer;
	    });

	}

}
