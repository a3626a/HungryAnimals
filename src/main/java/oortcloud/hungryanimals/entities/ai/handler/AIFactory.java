package oortcloud.hungryanimals.entities.ai.handler;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

@FunctionalInterface
public interface AIFactory {
	public EntityAIBase apply(EntityLiving entity);
}
