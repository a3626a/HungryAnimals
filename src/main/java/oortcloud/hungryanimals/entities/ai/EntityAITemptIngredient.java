package oortcloud.hungryanimals.entities.ai;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.capability.TamingLevel;
import oortcloud.hungryanimals.utils.ModJsonUtils;

public class EntityAITemptIngredient extends EntityAITempt {

	private List<Ingredient> tempt;
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
		return super.shouldExecute() && capTaming.getTamingLevel() == TamingLevel.TAMED;
	}

	public static AIFactory parse(JsonElement jsonEle) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Tempt must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		
		JsonObject jsonObject = (JsonObject)jsonEle ;
		
		float speed = JsonUtils.getFloat(jsonObject, "speed");
		boolean scaredBy = JsonUtils.getBoolean(jsonObject, "scared_by");
		List<Ingredient> items = ModJsonUtils.getIngredients(jsonObject.get("items"));
		return (entity) -> new EntityAITemptIngredient(entity, speed, scaredBy, items);
	}
	
}
