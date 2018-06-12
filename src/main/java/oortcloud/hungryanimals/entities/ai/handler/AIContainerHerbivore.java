package oortcloud.hungryanimals.entities.ai.handler;

import com.google.gson.JsonElement;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAITempt;

public class AIContainerHerbivore extends AIContainer {

	public AIContainerHerbivore() {
		getTask().remove(EntityAIPanic.class);
		getTask().remove(EntityAIMate.class);
		getTask().remove(EntityAITempt.class);
		getTask().remove(EntityAIFollowParent.class);
		getTask().remove(EntityAIEatGrass.class); // For Sheep
	}
	
	public static IAIContainer<EntityLiving> parse(JsonElement jsonEle) {
		return new AIContainerHerbivore();
	}
	
}
