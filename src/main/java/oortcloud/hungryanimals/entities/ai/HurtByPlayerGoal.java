package oortcloud.hungryanimals.entities.ai;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.JSONUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;

public class HurtByPlayerGoal extends HurtByTargetGoal {

	public HurtByPlayerGoal(CreatureEntity creatureIn, boolean entityCallsForHelpIn) {
		super(creatureIn);
		if (entityCallsForHelpIn) {
			setCallsForHelp();
		}
	}

	@Override
	public boolean shouldExecute() {
		return super.shouldExecute() && this.goalOwner.getRevengeTarget() instanceof PlayerEntity ;
	}

	public static void parse(JsonElement jsonEle, AIContainer aiContainer) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Hurt By Player must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		
		JsonObject jsonObject = (JsonObject)jsonEle ;
		
		boolean callHelp = JSONUtils.getBoolean(jsonObject, "call_help");
		
		AIFactory factory = (entity) -> {
			if (entity instanceof CreatureEntity) {
				return new HurtByPlayerGoal((CreatureEntity) entity, callHelp);
			} else {
				HungryAnimals.logger.error("Animals which uses AI Hunt By Player must extend CreatureEntity. {} don't.", entity.getType().getRegistryName());
				return null;
			}
		};
		aiContainer.getTarget().putLast(factory);
	}
	
}
