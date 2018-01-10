package oortcloud.hungryanimals.entities.ai.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.passive.EntityAnimal;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.EntityAIAvoidPlayer;
import oortcloud.hungryanimals.entities.ai.EntityAIMateModified;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToEatBlock;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToEatItem;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToTrough;
import oortcloud.hungryanimals.entities.ai.EntityAITemptEdibleItem;

public class AIContainerHerbivore extends AIContainer {

	public AIContainerHerbivore(AIFactory avoidPlayer, AIFactory mate, AIFactory trough, AIFactory tempt, AIFactory eatItem, AIFactory eatBlock) {
		getTask().before(EntityAIFollowParent.class).put(avoidPlayer);
		getTask().before(EntityAIFollowParent.class).put(mate);
		getTask().before(EntityAIFollowParent.class).put(trough);
		getTask().before(EntityAIFollowParent.class).put(tempt);
		getTask().before(EntityAIFollowParent.class).put(eatItem);
		getTask().before(EntityAIFollowParent.class).put(eatBlock);
		getTask().remove(EntityAIPanic.class);
		getTask().remove(EntityAIMate.class);
		getTask().remove(EntityAITempt.class);
		getTask().remove(EntityAIEatGrass.class); // For Sheep
	}
	
	public static IAIContainer<EntityAnimal> parse(JsonElement jsonEle) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI container must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		
		JsonObject jsonObj = (JsonObject)jsonEle;
		
		JsonElement jsonAvoidPlayer = jsonObj.get("avoid_player");
		JsonElement jsonMate = jsonObj.get("mate");
		JsonElement jsonTrough = jsonObj.get("trough");
		JsonElement jsonTempt = jsonObj.get("tempt");
		JsonElement jsonEatItem = jsonObj.get("eat_item");
		JsonElement jsonEatBlock = jsonObj.get("eat_block");
		
		HungryAnimals.logger.info(jsonAvoidPlayer);
		
		AIFactory avoidPlayer = EntityAIAvoidPlayer.parse(jsonAvoidPlayer);
		AIFactory mate = EntityAIMateModified.parse(jsonMate);
		AIFactory trough = EntityAIMoveToTrough.parse(jsonTrough);
		AIFactory tempt = EntityAITemptEdibleItem.parse(jsonTempt);
		AIFactory eatItem = EntityAIMoveToEatItem.parse(jsonEatItem);
		AIFactory eatBlock = EntityAIMoveToEatBlock.parse(jsonEatBlock);
		
		return new AIContainerHerbivore(avoidPlayer, mate, trough, tempt, eatItem, eatBlock);
	}
	
}
