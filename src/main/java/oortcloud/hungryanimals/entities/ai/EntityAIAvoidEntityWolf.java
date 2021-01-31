package oortcloud.hungryanimals.entities.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntityBase;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityWolf;

public class EntityAIAvoidEntityWolf<T extends Entity> extends EntityAIAvoidEntity<T> {
	
        private final EntityWolf wolf;

        public EntityAIAvoidEntityWolf(EntityWolf wolf, Class<T> target, float distance, double farSpeed, double nearSpeed)
        {
            super(wolf, target, distance, farSpeed, nearSpeed);
            this.wolf = wolf;
        }

        /**
         * Returns whether the Goal should begin execution.
         */
        public boolean shouldExecute()
        {
            if (super.shouldExecute() && this.closestMobEntity instanceof EntityLlama)
            {
                return !this.wolf.isTamed() && this.avoidLlama((EntityLlama)this.closestMobEntity);
            }
            else
            {
                return false;
            }
        }

        private boolean avoidLlama(EntityLlama llama)
        {
            return llama.getStrength() >= wolf.getRNG().nextInt(5);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
        	wolf.setAttackTarget((MobEntityBase)null);
            super.startExecuting();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {
            wolf.setAttackTarget((MobEntityBase)null);
            super.updateTask();
        }
}
