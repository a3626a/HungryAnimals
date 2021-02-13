package oortcloud.hungryanimals.entities.ai.handler;

import com.google.gson.JsonElement;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.NonTamedTargetGoal;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;

public class AIContainerWolf extends AIContainer {
	
	public AIContainerWolf(EntityType<?> entityType) {
		getTask().remove(BreedGoal.class);
		getTask().remove(FollowParentGoal.class);

		if (HungryAnimalManager.getInstance().isHungry(entityType)) {
			getTarget().remove(NonTamedTargetGoal.class);
		}
	}
	
	public static IAIContainer<MobEntity> parse(EntityType<?> entityType, JsonElement jsonEle) {
		return new AIContainerWolf(entityType);
	}
	
}
