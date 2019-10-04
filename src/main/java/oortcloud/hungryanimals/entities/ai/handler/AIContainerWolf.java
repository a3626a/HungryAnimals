package oortcloud.hungryanimals.entities.ai.handler;

import com.google.gson.JsonElement;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIMate;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;

public class AIContainerWolf extends AIContainer {
	
	public AIContainerWolf(Class<? extends EntityLiving> entityClass) {
		getTask().remove(EntityAIMate.class);
		getTask().remove(EntityAIFollowParent.class);

		if (HungryAnimalManager.getInstance().isHungry(entityClass)) {
			getTarget().remove(net.minecraft.entity.ai.EntityAITargetNonTamed.class);
		}
	}
	
	public static IAIContainer<EntityLiving> parse(Class<? extends EntityLiving> entityClass, JsonElement jsonEle) {
		return new AIContainerWolf(entityClass);
	}
	
}
