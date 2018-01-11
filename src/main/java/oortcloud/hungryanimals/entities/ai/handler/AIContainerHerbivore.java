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
import oortcloud.hungryanimals.entities.ai.EntityAIAttackMeleeCustom;
import oortcloud.hungryanimals.entities.ai.EntityAIAvoidPlayer;
import oortcloud.hungryanimals.entities.ai.EntityAIHurtByPlayer;
import oortcloud.hungryanimals.entities.ai.EntityAIMateModified;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToEatBlock;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToEatItem;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToTrough;
import oortcloud.hungryanimals.entities.ai.EntityAITemptEdibleItem;
import oortcloud.hungryanimals.entities.ai.EntityAITemptIngredient;

public class AIContainerHerbivore extends AIContainer {

	public AIContainerHerbivore(AIFactory attackMelee, AIFactory avoidPlayer, AIFactory mate, AIFactory trough, AIFactory tempt, AIFactory temptEdible, AIFactory eatItem, AIFactory eatBlock, AIFactory hurtByPlayer) {
		putBefore(attackMelee, EntityAIFollowParent.class);
		putBefore(avoidPlayer, EntityAIFollowParent.class);
		putBefore(mate, EntityAIFollowParent.class);
		putBefore(trough, EntityAIFollowParent.class);
		putBefore(tempt, EntityAIFollowParent.class);
		putBefore(temptEdible, EntityAIFollowParent.class);
		putBefore(eatItem, EntityAIFollowParent.class);
		putBefore(eatBlock, EntityAIFollowParent.class);
		getTask().remove(EntityAIPanic.class);
		getTask().remove(EntityAIMate.class);
		getTask().remove(EntityAITempt.class);
		getTask().remove(EntityAIEatGrass.class); // For Sheep
		
		if (hurtByPlayer != null) {
			getTarget().putLast(hurtByPlayer);
		}
	}
	
	public static IAIContainer<EntityAnimal> parse(JsonElement jsonEle) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI container must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		
		JsonObject jsonObj = (JsonObject)jsonEle;
		
		AIFactory attackMelee = parseField(jsonObj, "attack_melee", EntityAIAttackMeleeCustom::parse);
		AIFactory avoidPlayer = parseField(jsonObj, "avoid_player", EntityAIAvoidPlayer::parse);
		AIFactory mate = parseField(jsonObj, "mate", EntityAIMateModified::parse);
		AIFactory trough = parseField(jsonObj, "trough", EntityAIMoveToTrough::parse);
		AIFactory tempt = parseField(jsonObj, "tempt", EntityAITemptIngredient::parse);
		AIFactory temptEdible = parseField(jsonObj, "tempt_edible", EntityAITemptEdibleItem::parse);
		AIFactory eatItem = parseField(jsonObj, "eat_item", EntityAIMoveToEatItem::parse);
		AIFactory eatBlock = parseField(jsonObj, "eat_block", EntityAIMoveToEatBlock::parse);
		AIFactory hurtByPlayer = parseField(jsonObj, "hurt_by_player", EntityAIHurtByPlayer::parse);
		
		return new AIContainerHerbivore(attackMelee, avoidPlayer, mate, trough, tempt, temptEdible, eatItem, eatBlock, hurtByPlayer);
	}
	
}
