package oortcloud.hungryanimals.entities.ai.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.init.Items;
import oortcloud.hungryanimals.entities.ai.EntityAIAvoidPlayer;
import oortcloud.hungryanimals.entities.ai.EntityAIMateModified;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToEatBlock;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToEatItem;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToTrough;
import oortcloud.hungryanimals.entities.ai.EntityAITarget;
import oortcloud.hungryanimals.entities.ai.EntityAITargetNonTamed;
import oortcloud.hungryanimals.entities.ai.EntityAITemptEdibleItem;
import oortcloud.hungryanimals.entities.ai.handler.AIContainerTask.AIRemoverIsInstance;

public class AIContainers {

	private static AIContainers INSTANCE;

	public Map<Class<? extends EntityAnimal>, IAIContainer<EntityAnimal>> REGISTRY;
	public Map<String, Function<Class<? extends EntityAnimal>, IAIContainer<EntityAnimal>>> AITYPES;

	private AIContainers() {
		REGISTRY = new HashMap<Class<? extends EntityAnimal>, IAIContainer<EntityAnimal>>();
		AITYPES = new HashMap<String, Function<Class<? extends EntityAnimal>, IAIContainer<EntityAnimal>>>();
	}

	public static AIContainers getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AIContainers();
		}
		return INSTANCE;
	}

	public IAIContainer<EntityAnimal> registerAIContainer(Class<? extends EntityAnimal> animal, IAIContainer<EntityAnimal> aiContainer) {
		return REGISTRY.put(animal, aiContainer);
	}
	
	public void init() {
		AITYPES.put("herbivore", (animal) -> {
			AIContainerTask aiContainer = new AIContainerTask();
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
			AIContainerTask aiContainer = new AIContainerTask();
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
			AIContainerTask aiContainer = new AIContainerTask((AIContainerTask) AITYPES.get("herbivore").apply(animal));
			aiContainer.priorTo(EntityAITemptEdibleItem.class)
					.put((entity) -> new EntityAITempt(entity, 1.5D, Items.CARROT_ON_A_STICK, false));
			return aiContainer;
		});
		
		AITYPES.put("wolf", (animal)->{
			AIContainer aiContainer = new AIContainer();
			aiContainer.getTask().priorTo(EntityAIWanderAvoidWater.class).put((entity) -> new EntityAIMateModified(entity, 2.0D));
			aiContainer.getTask().priorTo(EntityAIWanderAvoidWater.class).put((entity) -> new EntityAIMoveToTrough(entity, 1.0D));
			aiContainer.getTask().priorTo(EntityAIWanderAvoidWater.class).put((entity) -> new EntityAITemptEdibleItem(entity, 1.5D, false));
			aiContainer.getTask().priorTo(EntityAIWanderAvoidWater.class).put((entity) -> new EntityAIMoveToEatItem(entity, 1.5D));
			aiContainer.getTask().priorTo(EntityAIWanderAvoidWater.class).put((entity) -> new EntityAIMoveToEatBlock(entity, 1.0D));
			aiContainer.getTask().remove(EntityAIMate.class);
			
			aiContainer.getTarget().putLast((entity) -> new EntityAITargetNonTamed((EntityTameable) entity, false, true));
			aiContainer.getTarget().remove(net.minecraft.entity.ai.EntityAITargetNonTamed.class);
			
			return aiContainer;
	    });

		AITYPES.put("polar_bear", (animal)->{
			AIContainer aiContainer = new AIContainer();
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
