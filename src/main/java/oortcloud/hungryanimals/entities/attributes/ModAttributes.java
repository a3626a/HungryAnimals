package oortcloud.hungryanimals.entities.attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MobEntityBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import oortcloud.hungryanimals.HungryAnimals;
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
	public static IAttribute fluid_weight;
	public static IAttribute fluid_amount;
	public static IAttribute wool_hunger;
	public static IAttribute wool_delay;

	private static ModAttributes INSTANCE;

	private Map<Class<? extends MobEntity>, List<IAttributeEntry>> REGISTRY;
	private Map<String, AttributePair> ATTRIBUTES;

	private ModAttributes() {
		REGISTRY = new HashMap<Class<? extends MobEntity>, List<IAttributeEntry>>();
		ATTRIBUTES = new HashMap<String, AttributePair>();
	}

	public static ModAttributes getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ModAttributes();
		}
		return INSTANCE;
	}

	public boolean registerAttribute(Class<? extends MobEntity> animalclass, String name, double val) {
		if (!ATTRIBUTES.containsKey(name)) {
			HungryAnimals.logger.warn("[Attribute] {} doesn't have attribute {}", animalclass, name);
			return false;
		}

		if (!REGISTRY.containsKey(animalclass)) {
			REGISTRY.put(animalclass, new ArrayList<IAttributeEntry>());
		}

		IAttribute attribute = ATTRIBUTES.get(name).attribute;
		boolean shouldRegister = ATTRIBUTES.get(name).shouldRegister;
		return REGISTRY.get(animalclass).add(new AttributeEntry(attribute, shouldRegister, val));
	}

	public boolean registerAttribute(Class<? extends MobEntity> animalclass, String name, double val, boolean shouldRegister) {
		if (!ATTRIBUTES.containsKey(name)) {
			HungryAnimals.logger.warn("[Attribute] {} doesn't have attribute {}", animalclass, name);
			return false;
		}

		if (!REGISTRY.containsKey(animalclass)) {
			REGISTRY.put(animalclass, new ArrayList<IAttributeEntry>());
		}

		IAttribute attribute = ATTRIBUTES.get(name).attribute;
		return REGISTRY.get(animalclass).add(new AttributeEntry(attribute, shouldRegister, val));
	}

	public void registerName(String name, IAttribute attribute, boolean shouldRegister) {
		ATTRIBUTES.put(name, pair(attribute, shouldRegister));
	}

	/**
	 * called EntityJoinWorldEvent
	 * It is called after entity construction
	 * 
	 * @param entity
	 */
	public void applyAttributes(MobEntityBase entity) {
		if (REGISTRY.containsKey(entity.getClass())) {
			for (IAttributeEntry i : REGISTRY.get(entity.getClass())) {
				i.apply(entity);
			}
		}
	}

	/**
	 * called EntityConstructing
	 * It is called before applyEntityAttributes and attachCapabilities
	 * @param entity
	 */
	public void registerAttributes(MobEntityBase entity) {
		if (REGISTRY.containsKey(entity.getClass())) {
			for (IAttributeEntry i : REGISTRY.get(entity.getClass())) {
				i.register(entity);
			}
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
		registerName(name, attribute, shouldRegister);
	}
}
