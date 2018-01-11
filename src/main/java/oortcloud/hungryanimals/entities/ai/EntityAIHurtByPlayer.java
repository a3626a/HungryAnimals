package oortcloud.hungryanimals.entities.ai;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;

public class EntityAIHurtByPlayer extends EntityAIHurtByTarget {

	public EntityAIHurtByPlayer(EntityCreature creatureIn, boolean entityCallsForHelpIn) {
		super(creatureIn, entityCallsForHelpIn);
	}

	@Override
	public boolean shouldExecute() {
		return super.shouldExecute() && this.taskOwner.getRevengeTarget() instanceof EntityPlayer ;
	}

	public static AIFactory parse(JsonElement jsonEle) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Hurt By Player must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		
		JsonObject jsonObject = (JsonObject)jsonEle ;
		
		boolean callHelp = JsonUtils.getBoolean(jsonObject, "call_help");
		
		return (entity) -> new EntityAIHurtByPlayer(entity, callHelp);
	}
	
}
