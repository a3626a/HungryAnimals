package oortcloud.hungryanimals.entities.ai.handler;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;

@FunctionalInterface
public interface AIFactory {
	public Goal apply(MobEntity entity);
}
