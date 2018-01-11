package oortcloud.hungryanimals.entities.ai;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;

public class EntityAIAttackMeleeCustom extends EntityAIAttackMelee {

	public EntityAIAttackMeleeCustom(EntityCreature creature, double speedIn, boolean useLongMemory) {
		super(creature, speedIn, useLongMemory);
	}

	@Override
	protected void checkAndPerformAttack(EntityLivingBase target, double distance) {
        double d0 = this.getAttackReachSqr(target);

        if (distance <= d0 && this.attackTick <= 0)
        {
            this.attackTick = 20;
            this.attacker.swingArm(EnumHand.MAIN_HAND);
            attackEntityAsMob(target);
        }
	}
	
    public boolean attackEntityAsMob(Entity target)
    {
    	// TODO Play Sound, Animation
    	if (this.attacker.attackEntityAsMob(target)) {
    		return true;
    	}
    	float damage = (float) this.attacker.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        return target.attackEntityFrom(DamageSource.causeMobDamage(this.attacker), damage);
    }
	
	public static AIFactory parse(JsonElement jsonEle) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Attack Melee must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		
		JsonObject jsonObject = (JsonObject)jsonEle ;
		
		double speed = JsonUtils.getFloat(jsonObject, "speed");
		boolean useLongMemory = JsonUtils.getBoolean(jsonObject, "use_long_memory");
		
		return (entity) -> new EntityAIAttackMeleeCustom(entity, speed, useLongMemory);
	}
	
}
