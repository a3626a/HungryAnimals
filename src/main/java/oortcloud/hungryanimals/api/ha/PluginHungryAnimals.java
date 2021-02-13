package oortcloud.hungryanimals.api.ha;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import oortcloud.hungryanimals.api.HAPlugin;
import oortcloud.hungryanimals.api.IAIRegistry;
import oortcloud.hungryanimals.api.IAttributeRegistry;
import oortcloud.hungryanimals.api.IHAPlugin;
import oortcloud.hungryanimals.api.ILootTableRegistry;
import oortcloud.hungryanimals.api.IProductionRegistry;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.ai.CustomMeleeAttackGoal;
import oortcloud.hungryanimals.entities.ai.AvoidPlayerGoal;
import oortcloud.hungryanimals.entities.ai.DrinkMilkGoal;
import oortcloud.hungryanimals.entities.ai.FollowParentGoal;
import oortcloud.hungryanimals.entities.ai.HuntGoal;
import oortcloud.hungryanimals.entities.ai.NonTamedHuntGoal;
import oortcloud.hungryanimals.entities.ai.HurtByPlayerGoal;
import oortcloud.hungryanimals.entities.ai.MateModifiedGoal;
import oortcloud.hungryanimals.entities.ai.MoveToEatBlockGoal;
import oortcloud.hungryanimals.entities.ai.MoveToEatItemGoal;
import oortcloud.hungryanimals.entities.ai.MoveToTroughGoal;
import oortcloud.hungryanimals.entities.ai.EdibleItemTemptGoal;
import oortcloud.hungryanimals.entities.ai.IngredientTemptGoal;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIContainerHerbivore;
import oortcloud.hungryanimals.entities.ai.handler.AIContainerTask.AIRemoverIsInstance;
import oortcloud.hungryanimals.entities.ai.handler.AIContainerWolf;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;
import oortcloud.hungryanimals.entities.loot_tables.SetCountBaseOnWeight;
import oortcloud.hungryanimals.entities.production.ProductionEgg;
import oortcloud.hungryanimals.entities.production.ProductionFluid;
import oortcloud.hungryanimals.entities.production.ProductionMilk;
import oortcloud.hungryanimals.entities.production.ProductionShear;
import oortcloud.hungryanimals.entities.production.condition.ConditionAge;
import oortcloud.hungryanimals.entities.production.condition.ConditionSex;

@HAPlugin
public class PluginHungryAnimals implements IHAPlugin {

	@Override
	public String getJsonInjectionPath() {
		return "/assets/hungryanimals/config";
	}

	@Override
	public void registerAIs(IAIRegistry registry) {
		registry.registerAIContainer("herbivore", AIContainerHerbivore::parse);
		registry.registerAIContainerModifier("herbivore", "attack_melee", CustomMeleeAttackGoal::parse);
		registry.registerAIContainerModifier("herbivore", "avoid_player", AvoidPlayerGoal::parse);
		registry.registerAIContainerModifier("herbivore", "mate", MateModifiedGoal::parse);
		registry.registerAIContainerModifier("herbivore", "drink_milk", DrinkMilkGoal::parse);
		registry.registerAIContainerModifier("herbivore", "trough", MoveToTroughGoal::parse);
		registry.registerAIContainerModifier("herbivore", "tempt", IngredientTemptGoal::parse);
		registry.registerAIContainerModifier("herbivore", "tempt_edible", EdibleItemTemptGoal::parse);
		registry.registerAIContainerModifier("herbivore", "eat_item", MoveToEatItemGoal::parse);
		registry.registerAIContainerModifier("herbivore", "eat_block", MoveToEatBlockGoal::parse);
		registry.registerAIContainerModifier("herbivore", "follow_parent", FollowParentGoal::parse);
		registry.registerAIContainerModifier("herbivore", "hurt_by_player", HurtByPlayerGoal::parse);
		
		registry.registerAIContainer("rabbit", (entityClass, jsonEle) -> {
			AIContainer aiContainer = (AIContainer) AIContainerHerbivore.parse(entityClass, jsonEle);
			aiContainer.getTask().remove(new AIRemoverIsInstance(PanicGoal.class));
			aiContainer.getTask().remove(new AIRemoverIsInstance(AvoidEntityGoal.class));
			if (HungryAnimalManager.getInstance().isHungry(entityClass)) {
				aiContainer.getTask().remove(new AIRemoverIsInstance(MoveToBlockGoal.class));
			}
			return aiContainer;
		});
		registry.registerAIContainerModifier("rabbit", "attack_melee", CustomMeleeAttackGoal::parse);
		registry.registerAIContainerModifier("rabbit", "avoid_player", AvoidPlayerGoal::parse);
		registry.registerAIContainerModifier("rabbit", "mate", MateModifiedGoal::parse);
		registry.registerAIContainerModifier("rabbit", "drink_milk", DrinkMilkGoal::parse);
		registry.registerAIContainerModifier("rabbit", "trough", MoveToTroughGoal::parse);
		registry.registerAIContainerModifier("rabbit", "tempt", IngredientTemptGoal::parse);
		registry.registerAIContainerModifier("rabbit", "tempt_edible", EdibleItemTemptGoal::parse);
		registry.registerAIContainerModifier("rabbit", "eat_item", MoveToEatItemGoal::parse);
		registry.registerAIContainerModifier("rabbit", "eat_block", MoveToEatBlockGoal::parse);
		registry.registerAIContainerModifier("rabbit", "follow_parent", FollowParentGoal::parse);
		registry.registerAIContainerModifier("rabbit", "hurt_by_player", HurtByPlayerGoal::parse);
		
		registry.registerAIContainer("wolf", AIContainerWolf::parse);
		registry.registerAIContainerModifier("wolf", "mate", MateModifiedGoal::parse);
		registry.registerAIContainerModifier("wolf", "drink_milk", DrinkMilkGoal::parse);
		registry.registerAIContainerModifier("wolf", "trough", MoveToTroughGoal::parse);
		registry.registerAIContainerModifier("wolf", "tempt", IngredientTemptGoal::parse);
		registry.registerAIContainerModifier("wolf", "tempt_edible", EdibleItemTemptGoal::parse);
		registry.registerAIContainerModifier("wolf", "eat_item", MoveToEatItemGoal::parse);
		registry.registerAIContainerModifier("wolf", "eat_block", MoveToEatBlockGoal::parse);
		registry.registerAIContainerModifier("wolf", "follow_parent", FollowParentGoal::parse);
		registry.registerAIContainerModifier("wolf", "hunt", HuntGoal::parse);
		registry.registerAIContainerModifier("wolf", "hunt_non_tamed", NonTamedHuntGoal::parse);
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
