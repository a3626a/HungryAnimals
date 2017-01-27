package oortcloud.hungryanimals.entities.ai;

import java.util.Map;

import net.minecraft.entity.passive.EntityAnimal;

public class AIManager {

	public Map<Class<? extends EntityAnimal>, IAIContainer> REGISTRY;
	
}
