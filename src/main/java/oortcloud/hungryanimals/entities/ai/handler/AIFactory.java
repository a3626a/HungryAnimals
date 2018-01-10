package oortcloud.hungryanimals.entities.ai.handler;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;

@FunctionalInterface
public interface AIFactory {
	public EntityAIBase apply(EntityAnimal entity);
}
