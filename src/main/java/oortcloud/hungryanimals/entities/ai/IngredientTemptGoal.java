package oortcloud.hungryanimals.entities.ai;

import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.capability.TamingLevel;
import oortcloud.hungryanimals.utils.ModJsonUtils;
import oortcloud.hungryanimals.utils.Tamings;

public class IngredientTemptGoal extends TemptGoal {

	private List<Ingredient> tempt;
	@Nullable
	private ICapabilityTamableAnimal capTaming;
	
	public IngredientTemptGoal(CreatureEntity entity, double speed, boolean scaredBy, List<Ingredient> items) {
		super(entity, speed, null, scaredBy);
		tempt = items;
		capTaming = entity.getCapability(ProviderTamableAnimal.CAP).orElse(null);
	}
	
    protected boolean isTempting(ItemStack stack)
    {
    	for (Ingredient i : tempt) {
    		if (i.test(stack))
    			return true;
    	}
    	return false;
    }
	
	@Override
	public boolean shouldExecute() {
		return super.shouldExecute() && Tamings.getLevel(capTaming) == TamingLevel.TAMED;
	}

	public static void parse(JsonElement jsonEle, AIContainer aiContainer) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Tempt must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		
		JsonObject jsonObject = (JsonObject)jsonEle ;
		
		float speed = JSONUtils.getFloat(jsonObject, "speed");
		boolean scaredBy = JSONUtils.getBoolean(jsonObject, "scared_by");
		List<Ingredient> items = ModJsonUtils.getIngredients(jsonObject.get("items"));

		if (items.isEmpty()) {
			HungryAnimals.logger.error(
					"items can't be empty for EntityAITemptIngredient. {} is not a valid AI modifier.",
					jsonEle
			);
			return;
		}

		AIFactory factory = (entity) -> {
			if (entity instanceof CreatureEntity) {
				return new IngredientTemptGoal((CreatureEntity) entity, speed, scaredBy, items);
			} else {
				HungryAnimals.logger.error("Animals which uses AI Tempt Ingredient must extend CreatureEntity. {} don't.", entity.getType().getRegistryName());
				return null;
			}
		};
		aiContainer.getTask().after(SwimGoal.class)
		                     .before(EdibleItemTemptGoal.class)
		                     .before(MoveToEatItemGoal.class)
		                     .before(EntityAIMoveToEatBlock.class)
		                     .before(EntityAIFollowParent.class)
		                     .before(WaterAvoidingRandomWalkingGoal.class)
		                     .put(factory);
	}
	
}
