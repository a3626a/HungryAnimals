package oortcloud.hungryanimals.entities.attributes;

import java.util.List;

import net.minecraft.entity.passive.EntityAnimal;
import oortcloud.hungryanimals.entities.food_preferences.HungryAnimalRegisterEvent;

public class AttributeRegisterEvent extends HungryAnimalRegisterEvent {

	private final List<IAttributeEntry> attributes;
	
	public AttributeRegisterEvent(Class<? extends EntityAnimal> entity, List<IAttributeEntry> container) {
		super(entity);
		this.attributes = container;
	}
	
	public List<IAttributeEntry> getAttributes() {
		return attributes;
	}
	
}
