package oortcloud.hungryanimals.entities.attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.passive.EntityAnimal;
import oortcloud.hungryanimals.core.lib.References;

public class ModAttributes {

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
	public static IAttribute taming_factor_near;
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

	public void init() {
		String id = References.MODID;
		hunger_weight_bmr = register(id, "hunger_weight_bmr");
		hunger_stomach_digest = register(id, "hunger_stomach_digest");
		hunger_stomach_max = register(id, "hunger_stomach_max", true);
		hunger_weight_normal = register(id, "hunger_weight_normal");
		hunger_weight_normal_child = register(id, "hunger_weight_normal_child");
		courtship_weight = register(id, "courtship_weight");
		courtship_probability = register(id, "courtship_probability", 0, 0, 1, false, true);
		courtship_stomach_condition = register(id, "courtship_stomach_condition", 0, 0, 1, false, true);
		excretion_factor = register(id, "excretion_factor");
		child_delay = register(id, "child_delay");
		child_growing_length = register(id, "child_growing_length");
		taming_factor_food = register(id, "taming_factor_food");
		taming_factor_near = register(id, "taming_factor_near", 0, 0, 1, false, true);
		milk_hunger = register(id, "milk_hunger", false);
		milk_delay = register(id, "milk_delay", true);
		wool_hunger = register(id, "wool_hunger", false);
		wool_delay = register(id, "wool_delay", true);
		ATTRIBUTES.put("generic.maxHealth", pair(SharedMonsterAttributes.MAX_HEALTH, false));
		ATTRIBUTES.put("generic.movementSpeed", pair(SharedMonsterAttributes.MOVEMENT_SPEED, false));
		ATTRIBUTES.put("generic.attackDamage", pair(SharedMonsterAttributes.ATTACK_DAMAGE, true));
	}

	public boolean register(Class<? extends EntityAnimal> animalclass, IAttribute attribute, double val, boolean shouldRegistered) {
		if (!REGISTRY.containsKey(animalclass)) {
			REGISTRY.put(animalclass, new ArrayList<IAttributeEntry>());
		}
		return REGISTRY.get(animalclass).add(new AttributeEntry(attribute, shouldRegistered, val));
	}
	
	private IAttribute register(String domain, String name, double defVal, double minVal, double maxVal, boolean shouldwatch, boolean shouldRegister) {
		String registeryName = domain+"."+name;
		IAttribute attribtue = new RangedAttribute((IAttribute)null, registeryName, defVal, minVal, maxVal).setShouldWatch(shouldwatch);
		ATTRIBUTES.put(registeryName, pair(attribtue, shouldRegister));
		return attribtue;
	}
	
	public IAttribute register(String domain, String name) {
		return register(domain, name, false);
	}
	
	public IAttribute register(String domain, String name, boolean shouldwatch) {
		return register(domain, name, shouldwatch, true);
	}
	
	public IAttribute register(String domain, String name, boolean shouldwatch, boolean shouldRegister) {
		return register(domain, name, 0, 0, Double.MAX_VALUE, shouldwatch, shouldRegister);
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
