package oortcloud.hungryanimals.entities.attributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.EntityAnimal;

public class AttributeManager {

	private static AttributeManager INSTANCE;

	public Map<Class<? extends EntityAnimal>, List<IAttributeEntry>> REGISTRY;
	public Map<String, AttributePair> ATTRIBUTES;

	private AttributeManager() {
		REGISTRY = new HashMap<Class<? extends EntityAnimal>, List<IAttributeEntry>>();
		ATTRIBUTES = new HashMap<String, AttributePair>();
	}

	public static AttributeManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AttributeManager();
		}
		return INSTANCE;
	}

	public void init() {
		ATTRIBUTES.put(ModAttributes.NAME_hunger_max, pair(ModAttributes.hunger_max, true));
		ATTRIBUTES.put(ModAttributes.NAME_hunger_bmr, pair(ModAttributes.hunger_bmr, true));
		ATTRIBUTES.put(ModAttributes.NAME_courtship_hunger, pair(ModAttributes.courtship_hunger, true));
		ATTRIBUTES.put(ModAttributes.NAME_courtship_probability,
				pair(ModAttributes.courtship_probability, true));
		ATTRIBUTES.put(ModAttributes.NAME_courtship_hungerCondition,
				pair(ModAttributes.courtship_hungerCondition, true));
		ATTRIBUTES.put(ModAttributes.NAME_excretion_factor, pair(ModAttributes.excretion_factor, true));
		ATTRIBUTES.put(ModAttributes.NAME_child_hunger, pair(ModAttributes.child_hunger, true));
		ATTRIBUTES.put(ModAttributes.NAME_hunger_max, pair(ModAttributes.hunger_max, true));
		ATTRIBUTES.put(ModAttributes.NAME_milk_hunger, pair(ModAttributes.milk_hunger, true));
		ATTRIBUTES.put(ModAttributes.NAME_milk_delay, pair(ModAttributes.milk_delay, true));
		ATTRIBUTES.put(ModAttributes.NAME_wool_hunger, pair(ModAttributes.wool_hunger, true));
		ATTRIBUTES.put(ModAttributes.NAME_wool_delay, pair(ModAttributes.wool_delay, true));
		ATTRIBUTES.put("generic.maxHealth", pair(SharedMonsterAttributes.MAX_HEALTH, false));
		ATTRIBUTES.put("generic.movementSpeed", pair(SharedMonsterAttributes.MOVEMENT_SPEED, false));
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
}
