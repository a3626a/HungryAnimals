package oortcloud.hungryanimals.entities.attributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;

public class AttributeManager {
	
	private static AttributeManager INSTANCE;
	
	public Map<Class<? extends EntityAnimal>, List<IAttributeEntry>> REGISTRY;
	
	private AttributeManager() {
		REGISTRY = new HashMap<Class<? extends EntityAnimal>, List<IAttributeEntry>>();
	}
	
	public static AttributeManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AttributeManager();
		}
		return INSTANCE;
	}
	
	/**
	 * called EntityJoinWorldEvent
	 * @param entity
	 */
	public void applyAttributes(EntityLivingBase entity) {
		for (IAttributeEntry i : REGISTRY.get(entity.getClass())) {
			i.apply(entity);
		}
	}
	
	/**
	 * called EntityConstructing
	 * @param entity
	 */
	public void registerAttributes(EntityLivingBase entity) {
		for (IAttributeEntry i : REGISTRY.get(entity.getClass())) {
			i.register(entity);
		}
	}
	
}
