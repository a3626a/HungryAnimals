package oortcloud.hungryanimals.entities.ai;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.JSONUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;

public class CustomMeleeAttackGoal extends MeleeAttackGoal {

	public CustomMeleeAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory) {
		super(creature, speedIn, useLongMemory);
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity target, double distance) {
		double d0 = this.getAttackReachSqr(target);

		if (distance <= d0 && this.attackTick <= 0) {
			this.attackTick = 20;
			this.attacker.swingArm(Hand.MAIN_HAND);
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
				return new CustomMeleeAttackGoal((CreatureEntity) entity, speed, useLongMemory);
			} else {
				HungryAnimals.logger.error("Animals which uses AI Attack Melee must extend CreatureEntity. {} don't.", entity.getType().getRegistryName());
				return null;
			}
		};
		aiContainer.getTask().after(SwimGoal.class).before(AvoidPlayerGoal.class).before(MateModifiedGoal.class)
				.before(MoveToTroughGoal.class).before(IngredientTemptGoal.class).before(EdibleItemTemptGoal.class)
				.before(MoveToEatItemGoal.class).before(MoveToEatBlockGoal.class).before(FollowParentGoal.class)
				.before(WaterAvoidingRandomWalkingGoal.class).put(factory);
	}

}
