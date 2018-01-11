package oortcloud.hungryanimals.entities.ai.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.passive.EntityAnimal;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.EntityAIHunt;
import oortcloud.hungryanimals.entities.ai.EntityAIHuntNonTamed;
import oortcloud.hungryanimals.entities.ai.EntityAIMateModified;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToEatBlock;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToEatItem;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToTrough;
import oortcloud.hungryanimals.entities.ai.EntityAITemptEdibleItem;

public class AIContainerWolf extends AIContainer {
	
	public AIContainerWolf(AIFactory mate, AIFactory trough, AIFactory tempt, AIFactory eatItem, AIFactory eatBlock, AIFactory hunt) {
		putBefore(mate, EntityAIWanderAvoidWater.class);
		putBefore(trough, EntityAIWanderAvoidWater.class);
		putBefore(tempt, EntityAIWanderAvoidWater.class);
		putBefore(eatItem, EntityAIWanderAvoidWater.class);
		putBefore(eatBlock, EntityAIWanderAvoidWater.class);
		getTask().remove(EntityAIMate.class);
		
		if (hunt != null) {
			getTarget().putLast(hunt);
		}
		getTarget().remove(net.minecraft.entity.ai.EntityAITargetNonTamed.class);
	}
	
	public static IAIContainer<EntityAnimal> parse(JsonElement jsonEle) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI container must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		
		JsonObject jsonObj = (JsonObject)jsonEle;
		
		JsonElement jsonHuntNonTamed = jsonObj.get("hunt_non_tamed");
		JsonElement jsonHunt = jsonObj.get("hunt");

		if (jsonHuntNonTamed != null && jsonHunt != null) {
			HungryAnimals.logger.warn("2 hunt AIs are declared. Ignore hunt_non_tamed");
			jsonHuntNonTamed = null;
		}
		
		AIFactory mate = parseField(jsonObj, "mate", EntityAIMateModified::parse);
		AIFactory trough = parseField(jsonObj, "trough", EntityAIMoveToTrough::parse);
		AIFactory tempt = parseField(jsonObj, "tempt", EntityAITemptEdibleItem::parse);
		AIFactory eatItem = parseField(jsonObj, "eat_item", EntityAIMoveToEatItem::parse);
		AIFactory eatBlock = parseField(jsonObj, "eat_block", EntityAIMoveToEatBlock::parse);

		AIFactory hunt = null;
		if (jsonHuntNonTamed != null) {
			hunt = EntityAIHuntNonTamed.parse(jsonHuntNonTamed);
		}
		if (jsonHunt != null) {
			hunt = EntityAIHunt.parse(jsonHunt);
		}
		
		return new AIContainerWolf(mate, trough, tempt, eatItem, eatBlock, hunt);
	}
	
}
