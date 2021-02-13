package oortcloud.hungryanimals.entities.attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.LazyForgeRegistry;
import oortcloud.hungryanimals.core.lib.References;

public class ModAttributes {
	private static ModAttributes INSTANCE;

	private Map<EntityType<?>, List<IAttributeEntry>> REGISTRY;
	public static final DeferredRegister<AttributePair> ATTRIBUTES = new DeferredRegister<>(LazyForgeRegistry.of(AttributePair.class), References.MODID);
	public static final RegistryObject<AttributePair> HUNGER_WEIGHT_BMR = register(References.MODID, "hunger_weight_bmr");
	public static final RegistryObject<AttributePair> HUNGER_STOMACH_DIGEST = register(References.MODID, "hunger_stomach_digest");
	public static final RegistryObject<AttributePair> HUNGER_STOMACH_MAX = register(References.MODID, "hunger_stomach_max", true);
	public static final RegistryObject<AttributePair> HUNGER_WEIGHT_NORMAL = register(References.MODID, "hunger_weight_normal");
	public static final RegistryObject<AttributePair> HUNGER_WEIGHT_NORMAL_CHILD = register(References.MODID, "hunger_weight_normal_child");
	public static final RegistryObject<AttributePair> COURTSHIP_WEIGHT = register(References.MODID, "courtship_weight");
	public static final RegistryObject<AttributePair> COURTSHIP_PROBABILIT = register(References.MODID, "courtship_probability", 0, 0, 1, false, true);
	public static final RegistryObject<AttributePair> COURTSHIP_STOMACH_CONDITION = register(References.MODID, "courtship_stomach_condition", 0, 0, 1, false, true);
	public static final RegistryObject<AttributePair> EXCRETION_FACTOR = register(References.MODID, "excretion_factor");
	public static final RegistryObject<AttributePair> CHILD_DELAY = register(References.MODID, "child_delay");
	public static final RegistryObject<AttributePair> CHILD_GROWING_LENGTH = register(References.MODID, "child_growing_length");
	public static final RegistryObject<AttributePair> TAMING_FACTOR_FOOD = register(References.MODID, "taming_factor_food");
	public static final RegistryObject<AttributePair> TAMING_FACTOR_NEAR_WILD = register(References.MODID, "taming_factor_near_wild", 0, 0, 1, false, true);
	public static final RegistryObject<AttributePair> TAMING_FACTOR_NEAR_TAMED = register(References.MODID, "taming_factor_near_tamed", 0, 0, 1, false, true);
	public static final RegistryObject<AttributePair> FLUID_WEIGHT = register(References.MODID, "fluid_weight", false);
	public static final RegistryObject<AttributePair> FLUID_AMOUNT = register(References.MODID, "fluid_amount", false);
	public static final RegistryObject<AttributePair> WOOL_HUNGER = register(References.MODID, "wool_hunger", false);
	public static final RegistryObject<AttributePair> WOOL_DELAY = register(References.MODID, "wool_delay", true);

	static {
		register("generic.maxHealth", SharedMonsterAttributes.MAX_HEALTH, false);
		register("generic.movementSpeed", SharedMonsterAttributes.MOVEMENT_SPEED, false);
		register("generic.knockbackResistance", SharedMonsterAttributes.KNOCKBACK_RESISTANCE, false);
		register("generic.armor", SharedMonsterAttributes.ARMOR, false);
		register("generic.armorToughness", SharedMonsterAttributes.ARMOR_TOUGHNESS, false);
		register("generic.followRange", SharedMonsterAttributes.FOLLOW_RANGE, false);
		register("generic.attackDamage", SharedMonsterAttributes.ATTACK_DAMAGE, true);
		register("generic.flyingSpeed", SharedMonsterAttributes.FLYING_SPEED, true);
		register("generic.attackSpeed", SharedMonsterAttributes.ATTACK_SPEED, true);
		register("generic.luck", SharedMonsterAttributes.LUCK, true);
	}

	private ModAttributes() {
		REGISTRY = new HashMap<>();
	}

	public static ModAttributes getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ModAttributes();
		}
		return INSTANCE;
	}

	public boolean registerAttribute(EntityType<?> entityType, String name, double val) {
		return registerAttribute(entityType, name, val, null);
	}

	public boolean registerAttribute(EntityType<?> entityType, String name, double val, Boolean shouldRegister) {
		IForgeRegistry<AttributePair> registry = GameRegistry.findRegistry(AttributePair.class);
		ResourceLocation resourceLocation = new ResourceLocation(name);
		AttributePair attributePair = registry.getValue(resourceLocation);

		if (attributePair == null) {
			HungryAnimals.logger.warn("[Attribute] {} doesn't have attribute {}", entityType, name);
			return false;
		}

		if (!REGISTRY.containsKey(entityType)) {
			REGISTRY.put(entityType, new ArrayList<>());
		}

		IAttribute attribute = attributePair.attribute;
		if (shouldRegister == null) {
			shouldRegister = attributePair.shouldRegister;
		}
		return REGISTRY.get(entityType).add(new AttributeEntry(attribute, shouldRegister, val));
	}

	/**
	 * called EntityJoinWorldEvent
	 * It is called after entity construction
	 * 
	 * @param entity
	 */
	public void applyAttributes(LivingEntity entity) {
		if (REGISTRY.containsKey(entity.getType())) {
			for (IAttributeEntry i : REGISTRY.get(entity.getType())) {
				i.apply(entity);
			}
		}
	}

	/**
	 * called EntityConstructing
	 * It is called before applyEntityAttributes and attachCapabilities
	 * @param entity
	 */
	public void registerAttributes(LivingEntity entity) {
		if (REGISTRY.containsKey(entity.getType())) {
			for (IAttributeEntry i : REGISTRY.get(entity.getType())) {
				i.register(entity);
			}
		}
	}

	private static AttributePair pair(IAttribute attribute, boolean shouldRegister) {
		return new AttributePair(attribute, shouldRegister);
	}

	public static class AttributePair extends ForgeRegistryEntry<AttributePair> {
		public IAttribute attribute;
		public boolean shouldRegister;

		public AttributePair(IAttribute attribute, boolean shouldRegister) {
			this.attribute = attribute;
			this.shouldRegister = shouldRegister;
		}
	}

	private static RegistryObject<AttributePair> register(String name, IAttribute attribute, boolean shouldRegister) {
		return ATTRIBUTES.register(name,
				() -> new AttributePair(
						attribute,
						shouldRegister
				)
		);
	}

	private static RegistryObject<AttributePair> register(String domain, String name, double defVal, double minVal, double maxVal, boolean shouldwatch, boolean shouldRegister) {
		return ATTRIBUTES.register(name,
				() -> new AttributePair(
						new RangedAttribute(null, domain+"."+name, defVal, minVal, maxVal)
								.setShouldWatch(shouldwatch),
						shouldRegister
				)
		);
	}

	private static RegistryObject<AttributePair> register(String domain, String name) {
		return register(domain, name, false);
	}

	private static RegistryObject<AttributePair> register(String domain, String name, boolean shouldwatch) {
		return register(domain, name, shouldwatch, true);
	}

	private static RegistryObject<AttributePair> register(String domain, String name, boolean shouldwatch, boolean shouldRegister) {
		return register(domain, name, 0, 0, Double.MAX_VALUE, shouldwatch, shouldRegister);
	}
}
