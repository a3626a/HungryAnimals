package oortcloud.hungryanimals.entities.attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.EntityAnimal;
import oortcloud.hungryanimals.api.IAttributeRegistry;

public class ModAttributes implements IAttributeRegistry {

	public static IAttribute hunger_weight_bmr;
	public static IAttribute hunger_stomach_max;
	public static IAttribute hunger_stomach_digest;
	public static IAttribute hunger_weight_normal;
	public static IAttribute hunger_weight_normal_child;
	public static IAttribute courtship_weight;
	public static IAttribute courtship_probability;
	public static IAttribute courtship_stomach_condition;
	public static IAttribute excretion_factor;
	public static IAttribute child_delay;
	public static IAttribute child_growing_length;
	public static IAttribute taming_factor_food;
	public static IAttribute taming_factor_near_wild;
	public static IAttribute taming_factor_near_tamed;
	public static IAttribute milk_hunger;
	public static IAttribute milk_delay;
	public static IAttribute wool_hunger;
	public static IAttribute wool_delay;
	
	private static ModAttributes INSTANCE;

	private Map<Class<? extends EntityAnimal>, List<IAttributeEntry>> REGISTRY;
	public Map<String, AttributePair> ATTRIBUTES;

	private ModAttributes() {
		REGISTRY = new HashMap<Class<? extends EntityAnimal>, List<IAttributeEntry>>();
		ATTRIBUTES = new HashMap<String, AttributePair>();
	}

	public static ModAttributes getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ModAttributes();
		}
		return INSTANCE;
	}

	public boolean register(Class<? extends EntityAnimal> animalclass, IAttribute attribute, double val, boolean shouldRegistered) {
		if (!REGISTRY.containsKey(animalclass)) {
			REGISTRY.put(animalclass, new ArrayList<IAttributeEntry>());
		}
		return REGISTRY.get(animalclass).add(new AttributeEntry(attribute, shouldRegistered, val));
	}
	
	public void register(String name, IAttribute attribute, boolean shouldRegister) {
		ATTRIBUTES.put(name, pair(attribute, shouldRegister));
	}
	

	/**
	 * called EntityJoinWorldEvent
	 * 
	 * @param entity
	 */
	public void applyAttributes(EntityLivingBase entity) {
		for (IAttributeEntry i : REGISTRY.get(entity.getClass())) {
			i.apply(entity);
		}
	}

	/**
	 * called EntityConstructing
	 * 
	 * @param entity
	 */
	public void registerAttributes(EntityLivingBase entity) {
		for (IAttributeEntry i : REGISTRY.get(entity.getClass())) {
			i.register(entity);
		}
	}

	private static AttributePair pair(IAttribute attribute, boolean shouldRegister) {
		return new AttributePair(attribute, shouldRegister);
	}

	public static class AttributePair {
		public IAttribute attribute;
		public boolean shouldRegister;

		public AttributePair(IAttribute attribute, boolean shouldRegister) {
			this.attribute = attribute;
			this.shouldRegister = shouldRegister;
		}
	}

	@Override
	public void registerAttribute(String name, IAttribute attribute, boolean shouldRegister) {
		register(name, attribute, shouldRegister);
	}
}
