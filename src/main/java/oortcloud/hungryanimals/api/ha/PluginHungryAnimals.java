package oortcloud.hungryanimals.api.ha;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import oortcloud.hungryanimals.api.HAPlugin;
import oortcloud.hungryanimals.api.IAIRegistry;
import oortcloud.hungryanimals.api.IAttributeRegistry;
import oortcloud.hungryanimals.api.IGrassGeneratorRegistry;
import oortcloud.hungryanimals.api.IHAPlugin;
import oortcloud.hungryanimals.api.ILootTableRegistry;
import oortcloud.hungryanimals.api.IProductionRegistry;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.ai.EntityAIAttackMeleeCustom;
import oortcloud.hungryanimals.entities.ai.EntityAIAvoidPlayer;
import oortcloud.hungryanimals.entities.ai.EntityAIFollowParentFixed;
import oortcloud.hungryanimals.entities.ai.EntityAIHunt;
import oortcloud.hungryanimals.entities.ai.EntityAIHuntNonTamed;
import oortcloud.hungryanimals.entities.ai.EntityAIHurtByPlayer;
import oortcloud.hungryanimals.entities.ai.EntityAIMateModified;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToEatBlock;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToEatItem;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToTrough;
import oortcloud.hungryanimals.entities.ai.EntityAITemptEdibleItem;
import oortcloud.hungryanimals.entities.ai.EntityAITemptIngredient;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIContainerHerbivore;
import oortcloud.hungryanimals.entities.ai.handler.AIContainerTask.AIRemoverIsInstance;
import oortcloud.hungryanimals.entities.ai.handler.AIContainerWolf;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.loot_tables.SetCountBaseOnWeight;
import oortcloud.hungryanimals.entities.production.ProductionEgg;
import oortcloud.hungryanimals.entities.production.ProductionFluid;
import oortcloud.hungryanimals.entities.production.ProductionMilk;
import oortcloud.hungryanimals.entities.production.ProductionShear;
import oortcloud.hungryanimals.entities.production.condition.ConditionAge;
import oortcloud.hungryanimals.entities.production.condition.ConditionSex;
import oortcloud.hungryanimals.generation.ConditionAdjacent;
import oortcloud.hungryanimals.generation.ConditionBelow;
import oortcloud.hungryanimals.generation.ConditionChance;

@HAPlugin
public class PluginHungryAnimals implements IHAPlugin {

	@Override
	public String getJsonInjectionPath() {
		return "/assets/hungryanimals/config";
	}

	@Override
	public void registerAIs(IAIRegistry registry) {
		registry.registerAIContainer("herbivore", AIContainerHerbivore::parse);
		registry.registerAIContainerModifier("herbivore", "attack_melee", EntityAIAttackMeleeCustom::parse);
		registry.registerAIContainerModifier("herbivore", "avoid_player", EntityAIAvoidPlayer::parse);
		registry.registerAIContainerModifier("herbivore", "mate", EntityAIMateModified::parse);
		registry.registerAIContainerModifier("herbivore", "trough", EntityAIMoveToTrough::parse);
		registry.registerAIContainerModifier("herbivore", "tempt", EntityAITemptIngredient::parse);
		registry.registerAIContainerModifier("herbivore", "tempt_edible", EntityAITemptEdibleItem::parse);
		registry.registerAIContainerModifier("herbivore", "eat_item", EntityAIMoveToEatItem::parse);
		registry.registerAIContainerModifier("herbivore", "eat_block", EntityAIMoveToEatBlock::parse);
		registry.registerAIContainerModifier("herbivore", "follow_parent", EntityAIFollowParentFixed::parse);
		registry.registerAIContainerModifier("herbivore", "hurt_by_player", EntityAIHurtByPlayer::parse);
		
		registry.registerAIContainer("rabbit", (jsonEle) -> {
			AIContainer aiContainer = (AIContainer) AIContainerHerbivore.parse(jsonEle);
			aiContainer.getTask().remove(new AIRemoverIsInstance(EntityAIPanic.class));
			aiContainer.getTask().remove(new AIRemoverIsInstance(EntityAIAvoidEntity.class));
			aiContainer.getTask().remove(new AIRemoverIsInstance(EntityAIMoveToBlock.class));
			return aiContainer;
		});
		registry.registerAIContainerModifier("rabbit", "attack_melee", EntityAIAttackMeleeCustom::parse);
		registry.registerAIContainerModifier("rabbit", "avoid_player", EntityAIAvoidPlayer::parse);
		registry.registerAIContainerModifier("rabbit", "mate", EntityAIMateModified::parse);
		registry.registerAIContainerModifier("rabbit", "trough", EntityAIMoveToTrough::parse);
		registry.registerAIContainerModifier("rabbit", "tempt", EntityAITemptIngredient::parse);
		registry.registerAIContainerModifier("rabbit", "tempt_edible", EntityAITemptEdibleItem::parse);
		registry.registerAIContainerModifier("rabbit", "eat_item", EntityAIMoveToEatItem::parse);
		registry.registerAIContainerModifier("rabbit", "eat_block", EntityAIMoveToEatBlock::parse);
		registry.registerAIContainerModifier("rabbit", "follow_parent", EntityAIFollowParentFixed::parse);
		registry.registerAIContainerModifier("rabbit", "hurt_by_player", EntityAIHurtByPlayer::parse);
		
		registry.registerAIContainer("wolf", AIContainerWolf::parse);
		registry.registerAIContainerModifier("wolf", "mate", EntityAIMateModified::parse);
		registry.registerAIContainerModifier("wolf", "trough", EntityAIMoveToTrough::parse);
		registry.registerAIContainerModifier("wolf", "tempt", EntityAITemptIngredient::parse);
		registry.registerAIContainerModifier("wolf", "tempt_edible", EntityAITemptEdibleItem::parse);
		registry.registerAIContainerModifier("wolf", "eat_item", EntityAIMoveToEatItem::parse);
		registry.registerAIContainerModifier("wolf", "eat_block", EntityAIMoveToEatBlock::parse);
		registry.registerAIContainerModifier("wolf", "follow_parent", EntityAIFollowParentFixed::parse);
		registry.registerAIContainerModifier("wolf", "hunt", EntityAIHunt::parse);
		registry.registerAIContainerModifier("wolf", "hunt_non_tamed", EntityAIHuntNonTamed::parse);
	}

	@Override
	public void registerGrassGenerators(IGrassGeneratorRegistry registry) {
		registry.registerCondition("below", ConditionBelow::parse);
		registry.registerCondition("chance", ConditionChance::parse);
		registry.registerCondition("not_adjacent", ConditionAdjacent::parse);
	}

	@Override
	public void registerLootTables(ILootTableRegistry registry) {
		registry.registerFunction(new SetCountBaseOnWeight.Serializer());
	}

	@Override
	public void registerAttributes(IAttributeRegistry registry) {
		String id = References.MODID;
		ModAttributes.hunger_weight_bmr = register(registry, id, "hunger_weight_bmr");
		ModAttributes.hunger_stomach_digest = register(registry, id, "hunger_stomach_digest");
		ModAttributes.hunger_stomach_max = register(registry, id, "hunger_stomach_max", true);
		ModAttributes.hunger_weight_normal = register(registry, id, "hunger_weight_normal");
		ModAttributes.hunger_weight_normal_child = register(registry, id, "hunger_weight_normal_child");
		ModAttributes.courtship_weight = register(registry, id, "courtship_weight");
		ModAttributes.courtship_probability = register(registry, id, "courtship_probability", 0, 0, 1, false, true);
		ModAttributes.courtship_stomach_condition = register(registry, id, "courtship_stomach_condition", 0, 0, 1, false, true);
		ModAttributes.excretion_factor = register(registry, id, "excretion_factor");
		ModAttributes.child_delay = register(registry, id, "child_delay");
		ModAttributes.child_growing_length = register(registry, id, "child_growing_length");
		ModAttributes.taming_factor_food = register(registry, id, "taming_factor_food");
		ModAttributes.taming_factor_near_wild = register(registry, id, "taming_factor_near_wild", 0, 0, 1, false, true);
		ModAttributes.taming_factor_near_tamed = register(registry, id, "taming_factor_near_tamed", 0, 0, 1, false, true);
		ModAttributes.fluid_weight = register(registry, id, "fluid_weight", false);
		ModAttributes.fluid_amount = register(registry, id, "fluid_amount", false);
		ModAttributes.wool_hunger = register(registry, id, "wool_hunger", false);
		ModAttributes.wool_delay = register(registry, id, "wool_delay", true);
		registry.registerAttribute("generic.maxHealth", SharedMonsterAttributes.MAX_HEALTH, false);
		registry.registerAttribute("generic.movementSpeed", SharedMonsterAttributes.MOVEMENT_SPEED, false);
		registry.registerAttribute("generic.knockbackResistance", SharedMonsterAttributes.KNOCKBACK_RESISTANCE, false);
		registry.registerAttribute("generic.armor", SharedMonsterAttributes.ARMOR, false);
		registry.registerAttribute("generic.armorToughness", SharedMonsterAttributes.ARMOR_TOUGHNESS, false);
		registry.registerAttribute("generic.followRange", SharedMonsterAttributes.FOLLOW_RANGE, false);
		registry.registerAttribute("generic.attackDamage", SharedMonsterAttributes.ATTACK_DAMAGE, true);
		registry.registerAttribute("generic.flyingSpeed", SharedMonsterAttributes.FLYING_SPEED, true);
		registry.registerAttribute("generic.attackSpeed", SharedMonsterAttributes.ATTACK_SPEED, true);
		registry.registerAttribute("generic.luck", SharedMonsterAttributes.LUCK, true);
	}

	private IAttribute register(IAttributeRegistry registry, String domain, String name, double defVal, double minVal, double maxVal, boolean shouldwatch, boolean shouldRegister) {
		String registeryName = domain+"."+name;
		IAttribute attribtue = new RangedAttribute((IAttribute)null, registeryName, defVal, minVal, maxVal).setShouldWatch(shouldwatch);
		registry.registerAttribute(registeryName, attribtue, shouldRegister);
		return attribtue;
	}
	
	private IAttribute register(IAttributeRegistry registry, String domain, String name) {
		return register(registry, domain, name, false);
	}
	
	private IAttribute register(IAttributeRegistry registry, String domain, String name, boolean shouldwatch) {
		return register(registry, domain, name, shouldwatch, true);
	}
	
	private IAttribute register(IAttributeRegistry registry, String domain, String name, boolean shouldwatch, boolean shouldRegister) {
		return register(registry, domain, name, 0, 0, Double.MAX_VALUE, shouldwatch, shouldRegister);
	}

	@Override
	public void registerProductions(IProductionRegistry registry) {
		registry.registerParser("milk", ProductionMilk::parse);
		registry.registerParser("egg", ProductionEgg::parse);
		registry.registerParser("shear", ProductionShear::parse);
		registry.registerParser("fluid", ProductionFluid::parse);
		
		registry.registerCondition("age", ConditionAge::parse);
		registry.registerCondition("sex", ConditionSex::parse);
	}
	
}
