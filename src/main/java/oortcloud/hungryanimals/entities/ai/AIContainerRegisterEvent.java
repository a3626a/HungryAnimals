package oortcloud.hungryanimals.entities.ai;

import net.minecraft.entity.passive.EntityAnimal;
import oortcloud.hungryanimals.entities.food_preferences.HungryAnimalRegisterEvent;

public class AIContainerRegisterEvent extends HungryAnimalRegisterEvent {

	private final AIContainer container;
			
	public AIContainerRegisterEvent(Class<? extends EntityAnimal> entity, AIContainer container) {
		super(entity);
		this.container = container;
	}
	
	public AIContainer getContainer() {
		return container;
	}
	
}