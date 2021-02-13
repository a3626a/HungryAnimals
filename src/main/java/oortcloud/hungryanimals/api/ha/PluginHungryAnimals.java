package oortcloud.hungryanimals.api.ha;

import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import oortcloud.hungryanimals.api.HAPlugin;
import oortcloud.hungryanimals.api.IAIRegistry;
import oortcloud.hungryanimals.api.IHAPlugin;
import oortcloud.hungryanimals.api.ILootTableRegistry;
import oortcloud.hungryanimals.api.IProductionRegistry;
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
	public void registerProductions(IProductionRegistry registry) {
		registry.registerParser("milk", ProductionMilk::parse);
		registry.registerParser("egg", ProductionEgg::parse);
		registry.registerParser("shear", ProductionShear::parse);
		registry.registerParser("fluid", ProductionFluid::parse);
		
		registry.registerCondition("age", ConditionAge::parse);
		registry.registerCondition("sex", ConditionSex::parse);
	}
}
