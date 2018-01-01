package oortcloud.hungryanimals.entities.ai;

import net.minecraft.entity.passive.EntityAnimal;

public class AIContainerDuplex implements IAIContainer<EntityAnimal> {
	
	private AIContainer task;
	private AIContainerTarget target;
	
	public AIContainerDuplex() {
		task = new AIContainer();
		target = new AIContainerTarget();
	}
	
	public AIContainer getTask() {
		return task;
	}
	
	public AIContainerTarget getTarget() {
		return target;
	}
	
	@Override
	public void registerAI(EntityAnimal entity) {
		task.registerAI(entity);
		target.registerAI(entity);
	}

}
