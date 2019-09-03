package oortcloud.hungryanimals.entities.ai;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;

public class EntityAIHurtByPlayer extends EntityAIHurtByTarget {

	public EntityAIHurtByPlayer(EntityCreature creatureIn, boolean entityCallsForHelpIn) {
		super(creatureIn, entityCallsForHelpIn);
	}

	@Override
	public boolean shouldExecute() {
		return super.shouldExecute() && this.taskOwner.getRevengeTarget() instanceof EntityPlayer ;
	}

	public static void parse(JsonElement jsonEle, AIContainer aiContainer) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Hurt By Player must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		
		JsonObject jsonObject = (JsonObject)jsonEle ;
		
		boolean callHelp = JsonUtils.getBoolean(jsonObject, "call_help");
		
		AIFactory factory = (entity) -> {
			if (entity instanceof EntityCreature) {
				return new EntityAIHurtByPlayer((EntityCreature) entity, callHelp);
			} else {
				HungryAnimals.logger.error("Animals which uses AI Hunt By Player must extend EntityCreature. {} don't.", EntityList.getKey(entity));
				return null;
			}
		};
		aiContainer.getTarget().putLast(factory);
	}
	
}
