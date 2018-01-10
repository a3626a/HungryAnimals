package oortcloud.hungryanimals.entities.ai.handler;

import net.minecraft.entity.passive.EntityAnimal;

public class AIContainer implements IAIContainer<EntityAnimal> {
	
	private AIContainerTask task;
	private AIContainerTarget target;
	
	public AIContainer() {
		task = new AIContainerTask();
		target = new AIContainerTarget();
	}
	
	public AIContainerTask getTask() {
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
