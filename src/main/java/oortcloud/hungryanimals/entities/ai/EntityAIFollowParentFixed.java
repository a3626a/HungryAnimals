package oortcloud.hungryanimals.entities.ai;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;

public class EntityAIFollowParentFixed extends EntityAIFollowParent {
	
    public EntityAIFollowParentFixed(EntityAnimal animal, double speed)
    {
        super(animal, speed);
        setMutexBits(3);
    }
    
	public static void parse(JsonElement jsonEle, AIContainer aiContainer) {
		if (!(jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Follow Parent must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}

		JsonObject jsonObject = (JsonObject) jsonEle;

		float speed = JsonUtils.getFloat(jsonObject, "speed");

		AIFactory factory = (entity) -> new EntityAIFollowParentFixed(entity, speed);
		aiContainer.getTask().after(EntityAISwimming.class)
		                     .before(EntityAIWanderAvoidWater.class)
		                     .put(factory);
	}
}
