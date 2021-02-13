package oortcloud.hungryanimals.entities.ai.handler;

import com.google.gson.JsonElement;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.*;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;

public class AIContainerHerbivore extends AIContainer {
	public AIContainerHerbivore(EntityType<?> entityType) {
		getTask().remove(PanicGoal.class);
		getTask().remove(BreedGoal.class);
		getTask().remove(FollowParentGoal.class);

		if (HungryAnimalManager.getInstance().isHungry(entityType)) {
			getTask().remove(EatGrassGoal.class); // For Sheep
			getTask().remove(TemptGoal.class);
		}
	}
	
	public static IAIContainer<MobEntity> parse(EntityType<?> entityType, JsonElement jsonEle) {
		return new AIContainerHerbivore(entityType);
	}
}
