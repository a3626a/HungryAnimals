package oortcloud.hungryanimals.entities.ai.handler;

import com.google.gson.JsonElement;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIMate;

public class AIContainerWolf extends AIContainer {
	
	public AIContainerWolf() {
		getTask().remove(EntityAIMate.class);
		getTask().remove(EntityAIFollowParent.class);
		getTarget().remove(net.minecraft.entity.ai.EntityAITargetNonTamed.class);
	}
	
	public static IAIContainer<EntityLiving> parse(JsonElement jsonEle) {
		return new AIContainerWolf();
	}
	
}
