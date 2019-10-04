package oortcloud.hungryanimals.entities.ai.handler;

import com.google.gson.JsonElement;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.passive.EntitySheep;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;

public class AIContainerHerbivore extends AIContainer {

	public AIContainerHerbivore(Class<? extends EntityLiving> entityClass) {
		getTask().remove(EntityAIPanic.class);
		getTask().remove(EntityAIMate.class);
		getTask().remove(EntityAIFollowParent.class);

		if (HungryAnimalManager.getInstance().isHungry(entityClass)) {
			getTask().remove(EntityAIEatGrass.class); // For Sheep
			getTask().remove(EntityAITempt.class);
		}
	}
	
	public static IAIContainer<EntityLiving> parse(Class<? extends EntityLiving> entityClass, JsonElement jsonEle) {
		return new AIContainerHerbivore(entityClass);
	}
	
}
