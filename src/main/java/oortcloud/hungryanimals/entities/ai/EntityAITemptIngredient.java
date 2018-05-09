package oortcloud.hungryanimals.entities.ai;

import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.capability.TamingLevel;
import oortcloud.hungryanimals.utils.ModJsonUtils;
import oortcloud.hungryanimals.utils.Tamings;

public class EntityAITemptIngredient extends EntityAITempt {

	private List<Ingredient> tempt;
	@Nullable
	private ICapabilityTamableAnimal capTaming;
	
	public EntityAITemptIngredient(EntityCreature entity, double speed, boolean scaredBy, List<Ingredient> items) {
		super(entity, speed, null, scaredBy);
		tempt = items;
		capTaming = entity.getCapability(ProviderTamableAnimal.CAP, null);
	}
	
    protected boolean isTempting(ItemStack stack)
    {
    	for (Ingredient i : tempt) {
    		if (i.apply(stack))
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
		
		float speed = JsonUtils.getFloat(jsonObject, "speed");
		boolean scaredBy = JsonUtils.getBoolean(jsonObject, "scared_by");
		List<Ingredient> items = ModJsonUtils.getIngredients(jsonObject.get("items"));
		
		AIFactory factory = (entity) -> new EntityAITemptIngredient(entity, speed, scaredBy, items);
		aiContainer.getTask().after(EntityAISwimming.class)
		                     .before(EntityAITemptEdibleItem.class)
		                     .before(EntityAIMoveToEatItem.class)
		                     .before(EntityAIMoveToEatBlock.class)
		                     .before(EntityAIFollowParentFixed.class)
		                     .before(EntityAIWanderAvoidWater.class)
		                     .put(factory);
	}
	
}
