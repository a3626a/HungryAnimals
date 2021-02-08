package oortcloud.hungryanimals.entities.ai;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.capability.TamingLevel;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferences;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreference;
import oortcloud.hungryanimals.utils.Tamings;

public class EntityAITemptEdibleItem extends TemptGoal {
	/** The entity using this AI that is tempted by the player. */
	private final EntityCreature temptedEntity;
	private IFoodPreference<ItemStack> pref;

	private ICapabilityHungryAnimal capHungry;
	@Nullable
	private ICapabilityTamableAnimal capTaming;
	
	public EntityAITemptEdibleItem(EntityCreature temptedEntityIn, double speedIn, boolean scaredByPlayerMovementIn) {
		super(temptedEntityIn, speedIn, scaredByPlayerMovementIn, null);
		
		temptedEntity = temptedEntityIn;
		pref = FoodPreferences.getInstance().REGISTRY_ITEM.get(this.temptedEntity.getClass());
		capHungry = temptedEntityIn.getCapability(ProviderHungryAnimal.CAP, null);
		capTaming = temptedEntityIn.getCapability(ProviderTamableAnimal.CAP, null);
	}

	@Override
	public boolean shouldExecute() {
		if (capHungry == null)
			return false;
		
		return super.shouldExecute() && Tamings.getLevel(capTaming) == TamingLevel.TAMED;
	}

	@Override
	protected boolean isTempting(ItemStack stack) {
		return stack.isEmpty() ? false : pref.canEat(capHungry, stack);
	}
	
	public static void parse(JsonElement jsonEle, AIContainer aiContainer) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Tempt must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		
		JsonObject jsonObject = (JsonObject)jsonEle ;
		
		float speed = JSONUtils.getFloat(jsonObject, "speed");
		boolean scaredBy = JSONUtils.getBoolean(jsonObject, "scared_by");
		
		AIFactory factory = (entity) -> {
			if (entity instanceof EntityCreature) {
				return new EntityAITemptEdibleItem((EntityCreature) entity, speed, scaredBy);
			} else {
				HungryAnimals.logger.error("Animals which uses AI Tempt Item must extend EntityCreature. {} don't.", EntityList.getKey(entity));
				return null;
			}
		};
		aiContainer.getTask().after(EntityAISwimming.class)
		                     .before(EntityAIMoveToEatItem.class)
		                     .before(EntityAIMoveToEatBlock.class)
		                     .before(EntityAIFollowParent.class)
		                     .before(EntityAIWanderAvoidWater.class)
		                     .put(factory);
	}
}
