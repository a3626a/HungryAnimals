package oortcloud.hungryanimals.entities.ai;

import java.util.ArrayList;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;
import oortcloud.hungryanimals.entities.properties.IFoodPreference;
import oortcloud.hungryanimals.entities.properties.handler.ModAttributes;

public class EntityAIMoveToEatItem extends EntityAIBase {

	private EntityLiving entity;
	private World worldObj;
	private double speed;
	private EntityItem target;

	private IFoodPreference<ItemStack> pref;
	private int delayCounter;
	private static int delay = 100;
	
	private Predicate EAT_EDIBLE = new Predicate() {
		public boolean apply(EntityItem entityIn) {
			return pref.canEat(entityIn.getEntityItem());
		}

		@Override
		public boolean apply(Object object) {
			return this.apply((EntityItem) object);
		}
	};
	private Predicate EAT_NATURAL = new Predicate() {
		public boolean apply(EntityItem entityIn) {
			ItemStack item = entityIn.getEntityItem();
			NBTTagCompound tag = item.getTagCompound();
			if (tag != null) {
				return tag.hasKey("isNatural") && tag.getBoolean("isNatural");
			}
			return false;
		}

		@Override
		public boolean apply(Object object) {
			return this.apply((EntityItem) object);
		}
	};

	public EntityAIMoveToEatItem(EntityLiving entity, IFoodPreference<ItemStack> pref, double speed) {
		this.delayCounter = entity.getRNG().nextInt(delay);
		
		this.entity = entity;
		this.worldObj = this.entity.worldObj;
		this.speed = speed;
		this.pref = pref;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (pref.shouldEat())
			return false;

		if (this.delayCounter > 0) {
			--this.delayCounter;
			return false;
		} else {
			float radius = 16.0F;
			ArrayList<EntityItem> list = (ArrayList<EntityItem>) worldObj.getEntitiesWithinAABB(EntityItem.class, entity.getEntityBoundingBox().expand(radius, radius, radius), Predicates.and(EAT_EDIBLE, EAT_NATURAL));
			if (!list.isEmpty()) {
				this.target = list.get(0);
				return true;
			}
			if (entity.getRNG().nextInt(executeProbability()) != 0) {
				return false;
			}
			
			list = (ArrayList<EntityItem>) worldObj.getEntitiesWithinAABB(EntityItem.class, entity.getEntityBoundingBox().expand(radius, radius, radius), EAT_EDIBLE);
			if (!list.isEmpty()) {
				this.target = list.get(0);
				return true;
			}
			return false;
		}
	}

	@Override
	public void startExecuting() {
		this.entity.getNavigator().tryMoveToEntityLiving(target, speed);
	}

	@Override
	public boolean continueExecuting() {
		if (target.isDead) {
			this.entity.getNavigator().clearPathEntity();
			return false;
		}
		if (entity.getNavigator().noPath()) {
			float distanceSq = 2;
			if (entity.getPosition().distanceSq(target.getPosition()) < distanceSq) {
				ItemStack foodStack = target.getEntityItem();
				foodStack.stackSize--;
				if (foodStack.stackSize <= 0)
					target.setDead();
				this.property.eatFoodBonus(target.getEntityItem());
			}
			return false;
		}

		return true;
	}

	@Override
	public void resetTask() {
		this.target = null;
		this.delayCounter = delay;
	}

	private int executeProbability() {
		double taming = property.taming;
		double hunger = entity.getCapability(ProviderHungryAnimal.CAP_HUNGRYANIMAL, null).getHunger()/entity.getCapability(ProviderHungryAnimal.CAP_HUNGRYANIMAL, null).getMaxHunger();
		if (taming > 1) {
			taming = 1;
		}
		if (taming < -1) {
			taming = -1;
		}
		return (int) (200 * (taming - 1) * (taming - 1) * hunger) + 1;
	}

}
