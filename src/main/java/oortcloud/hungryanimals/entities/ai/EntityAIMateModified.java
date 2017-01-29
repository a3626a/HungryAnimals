package oortcloud.hungryanimals.entities.ai;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;

public class EntityAIMateModified extends EntityAIBase
{
    private EntityAnimal theAnimal;
    private ICapabilityHungryAnimal theAnimalCapHungry;
    private ICapabilityTamableAnimal theAnimalCapTamable;
    World theWorld;
    private EntityAnimal targetMate;
    /** Delay preventing a baby from spawning immediately when two mate-able animals find each other. */
    int spawnBabyDelay;
    /** The speed the creature moves at during mating behavior. */
    double moveSpeed;

    public EntityAIMateModified(EntityAnimal animal, double speed)
    {
        this.theAnimal = animal;
        this.theWorld = animal.worldObj;
        this.moveSpeed = speed;
        this.theAnimalCapHungry = animal.getCapability(ProviderHungryAnimal.CAP, null);
        this.theAnimalCapTamable = animal.getCapability(ProviderTamableAnimal.CAP, null);
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.theAnimal.isInLove())
        {
            return false;
        }
        else
        {
            this.targetMate = this.getNearbyMate();
            return this.targetMate != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {	
        this.theAnimal.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0F, (float)this.theAnimal.getVerticalFaceSpeed());
        this.theAnimal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;

        if (this.spawnBabyDelay >= 60 && this.theAnimal.getDistanceSqToEntity(this.targetMate) < 9.0D)
        {
            this.spawnBaby();
        }
    }

    private EntityAnimal getNearbyMate()
    {
        List<EntityAnimal> list = this.theWorld.<EntityAnimal>getEntitiesWithinAABB(this.theAnimal.getClass(), this.theAnimal.getEntityBoundingBox().expandXyz(8.0D));
        double d0 = Double.MAX_VALUE;
        EntityAnimal entityanimal = null;

        for (EntityAnimal entityanimal1 : list)
        {
            if (this.theAnimal.canMateWith(entityanimal1) && this.theAnimal.getDistanceSqToEntity(entityanimal1) < d0)
            {
                entityanimal = entityanimal1;
                d0 = this.theAnimal.getDistanceSqToEntity(entityanimal1);
            }
        }

        return entityanimal;
    }

    /**
     * Spawns a baby animal of the same type.
     */
    private void spawnBaby()
    {
    	//Get Capability
    	ICapabilityHungryAnimal targetMateCapHungry = this.targetMate.getCapability(ProviderHungryAnimal.CAP, null);
    	ICapabilityTamableAnimal targetMateCapTamable = this.targetMate.getCapability(ProviderTamableAnimal.CAP, null);
    	
    	//Pay Hunger
    	theAnimalCapHungry.addHunger(-theAnimal.getAttributeMap().getAttributeInstance(ModAttributes.child_hunger).getAttributeValue());
    	targetMateCapHungry.addHunger(-theAnimal.getAttributeMap().getAttributeInstance(ModAttributes.child_hunger).getAttributeValue());
        
    	//Create Child 1
    	EntityAgeable entityageable = this.theAnimal.createChild(this.targetMate);
    	
    	//Check Validity
    	boolean createChildDeclared = false;
    	try {
			Method createChild = theAnimal.getClass().getDeclaredMethod("createChild", EntityAgeable.class);
			if (createChild != null) createChildDeclared = true;
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}
    	
    	//Create Child 2
    	if (!createChildDeclared||entityageable==null) {
        	entityageable = createChild();
        }
    	
        if (entityageable != null)
        {
        	entityageable.getCapability(ProviderTamableAnimal.CAP, null).setTaming((theAnimalCapTamable.getTaming() + targetMateCapTamable.getTaming())/2.0);
        	
            EntityPlayer entityplayer = this.theAnimal.getPlayerInLove();

            if (entityplayer == null && this.targetMate.getPlayerInLove() != null)
            {
                entityplayer = this.targetMate.getPlayerInLove();
            }

            if (entityplayer != null)
            {
                entityplayer.addStat(StatList.ANIMALS_BRED);

                if (this.theAnimal instanceof EntityCow)
                {
                    entityplayer.addStat(AchievementList.BREED_COW);
                }
            }
            this.theAnimal.setGrowingAge(48000);
            this.targetMate.setGrowingAge(48000);
            this.theAnimal.resetInLove();
            this.targetMate.resetInLove();
            entityageable.setGrowingAge(-96000);
            entityageable.setLocationAndAngles(this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, 0.0F, 0.0F);
            this.theWorld.spawnEntityInWorld(entityageable);
            Random random = this.theAnimal.getRNG();

            for (int i = 0; i < 7; ++i)
            {
            	double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                double d3 = random.nextDouble() * (double)this.theAnimal.width * 2.0D - (double)this.theAnimal.width;
                double d4 = 0.5D + random.nextDouble() * (double)this.theAnimal.height;
                double d5 = random.nextDouble() * (double)this.theAnimal.width * 2.0D - (double)this.theAnimal.width;
                this.theWorld.spawnParticle(EnumParticleTypes.HEART, this.theAnimal.posX + d3, this.theAnimal.posY + d4, this.theAnimal.posZ + d5, d0, d1, d2, new int[0]);
            }

            if (this.theWorld.getGameRules().getBoolean("doMobLoot"))
            {
                this.theWorld.spawnEntityInWorld(new EntityXPOrb(this.theWorld, this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, random.nextInt(7) + 1));
            }
        } else {
        	this.theAnimal.resetInLove();
            this.targetMate.resetInLove();
        }
    }
    
    public EntityAnimal createChild() {
		Constructor<? extends EntityAnimal> constructor;
		try {
			constructor = theAnimal.getClass().getConstructor(World.class);
			EntityAnimal baby;
			try {
				baby = (EntityAnimal) constructor.newInstance(theWorld);
				return baby;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
}