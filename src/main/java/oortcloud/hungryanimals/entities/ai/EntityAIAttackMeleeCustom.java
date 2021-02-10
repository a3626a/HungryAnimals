package oortcloud.hungryanimals.entities.ai;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.MobEntityBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.JSONUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;

public class EntityAIAttackMeleeCustom extends EntityAIAttackMelee {

	public EntityAIAttackMeleeCustom(CreatureEntity creature, double speedIn, boolean useLongMemory) {
		super(creature, speedIn, useLongMemory);
	}

	@Override
	protected void checkAndPerformAttack(MobEntityBase target, double distance) {
		double d0 = this.getAttackReachSqr(target);

		if (distance <= d0 && this.attackTick <= 0) {
			this.attackTick = 20;
			this.attacker.swingArm(EnumHand.MAIN_HAND);
			attackEntityAsMob(target);
		}
	}

	public boolean attackEntityAsMob(Entity target) {
		// TODO Play Sound, Animation
		if (this.attacker.attackEntityAsMob(target)) {
			return true;
		}
		float damage = (float) this.attacker.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
		return target.attackEntityFrom(DamageSource.causeMobDamage(this.attacker), damage);
	}

	public static void parse(JsonElement jsonEle, AIContainer aiContainer) {
		if (!(jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Attack Melee must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}

		JsonObject jsonObject = (JsonObject) jsonEle;

		double speed = JSONUtils.getFloat(jsonObject, "speed");
		boolean useLongMemory = JSONUtils.getBoolean(jsonObject, "use_long_memory");

		AIFactory factory = (entity) -> {
			if (entity instanceof CreatureEntity) {
				return new EntityAIAttackMeleeCustom((CreatureEntity) entity, speed, useLongMemory);
			} else {
				HungryAnimals.logger.error("Animals which uses AI Attack Melee must extend CreatureEntity. {} don't.", EntityList.getKey(entity));
				return null;
			}
		};
		aiContainer.getTask().after(SwimGoal.class).before(EntityAIAvoidPlayer.class).before(EntityAIMateModified.class)
				.before(MoveToTroughGoal.class).before(IngredientTemptGoal.class).before(EdibleItemTemptGoal.class)
				.before(MoveToEatItemGoal.class).before(MoveToEatBlockGoal.class).before(EntityAIFollowParent.class)
				.before(WaterAvoidingRandomWalkingGoal.class).put(factory);
	}

}
