package oortcloud.hungryanimals.entities.ai.handler;

import net.minecraft.entity.passive.EntityAnimal;
import oortcloud.hungryanimals.entities.food_preferences.HungryAnimalRegisterEvent;

public class EventAIContainerRegister extends HungryAnimalRegisterEvent {

	private final AIContainerTask container;
			
	public EventAIContainerRegister(Class<? extends EntityAnimal> entity, AIContainerTask container) {
		super(entity);
		this.container = container;
	}
	
	public AIContainerTask getContainer() {
		return container;
	}
	
}