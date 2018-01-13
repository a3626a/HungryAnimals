package oortcloud.hungryanimals.entities.ai.handler;

import com.google.gson.JsonElement;

import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.passive.EntityAnimal;

public class AIContainerWolf extends AIContainer {
	
	public AIContainerWolf() {
		getTask().remove(EntityAIMate.class);
		getTarget().remove(net.minecraft.entity.ai.EntityAITargetNonTamed.class);
	}
	
	public static IAIContainer<EntityAnimal> parse(JsonElement jsonEle) {
		return new AIContainerWolf();
	}
	
}
