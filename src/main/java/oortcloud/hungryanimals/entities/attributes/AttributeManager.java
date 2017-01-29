package oortcloud.hungryanimals.entities.attributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;

public class AttributeManager {
	
	public Map<Class<? extends EntityAnimal>, List<IAttributeEntry>> REGISTRY;
	
	private AttributeManager() {
		REGISTRY = new HashMap<Class<? extends EntityAnimal>, List<IAttributeEntry>>();
	}
	
	public void applyAttributes(EntityLivingBase entity) {
		for (IAttributeEntry i : REGISTRY.get(entity.getClass())) {
			i.apply(entity);
		}
	}

	public void registerAttributes(EntityLivingBase entity) {
		for (IAttributeEntry i : REGISTRY.get(entity.getClass())) {
			i.register(entity);
		}
	}
	
}
