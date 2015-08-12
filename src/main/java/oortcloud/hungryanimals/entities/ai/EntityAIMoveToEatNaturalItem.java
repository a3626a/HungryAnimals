package oortcloud.hungryanimals.entities.ai;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;

import com.google.common.base.Predicate;

public class EntityAIMoveToEatNaturalItem extends EntityAIBase {
	private EntityLiving entity;
	private ExtendedPropertiesHungryAnimal property;
	private World worldObj;
	private double xPosition;
	private double yPosition;
	private double zPosition;
	private double speed;
	private Predicate foodSelector;
	private EntityItem target;

	public EntityAIMoveToEatNaturalItem(EntityLiving entity, ExtendedPropertiesHungryAnimal prop, double speed) {
		this.entity = entity;
		this.property = prop;
		this.worldObj = this.entity.worldObj;
		this.speed = speed;
		foodSelector = new Predicate() {
			public boolean apply(Entity entityIn) {
				ItemStack item = ((EntityItem) entityIn).getEntityItem();
				NBTTagCompound tag = item.getTagCompound();
				if (tag != null) {
					return property.canEatFood(item)&&tag.hasKey("isNatural")&&tag.getBoolean("isNatural");
				}
				return property.canEatFood(item);
			}

			@Override
			public boolean apply(Object object) {
				return this.apply((Entity) object);
			}
		};
		this.setMutexBits(1);
	}

	public int executeProbability() {
		double hunger = property.getHungry();

		return (int) (50 * hunger) + 1;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {

		if (!(this.property.getHungry() < this.property.courtship_hungerCondition && entity.getRNG().nextInt(executeProbability()) == 0))
			return false;

		List list = this.worldObj.getEntitiesWithinAABB(EntityItem.class, entity.getEntityBoundingBox().expand(10, 2, 10), foodSelector);

		double maxhunger = 0;

		if (list.isEmpty()) {
			return false;
		} else {
			for (Object i : list) {
				ItemStack item = ((EntityItem) i).getEntityItem();
				if (property.getFoodHunger(item) > maxhunger) {
					maxhunger = property.getFoodHunger(item);
					this.target = (EntityItem) i;
				}
			}
			if (this.target != null) {
				this.xPosition = target.posX;
				this.yPosition = target.posY;
				this.zPosition = target.posZ;
				return true;
			} else {
				return false;
			}
		}

	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		if (entity.getNavigator().noPath()) {

			if (entity.getEntityBoundingBox().expand(1, 1, 1).isVecInside(new Vec3(target.posX, target.posY, target.posZ))) {

				ItemStack foodStack = target.getEntityItem();
				foodStack.stackSize--;
				if (foodStack.stackSize <= 0)
					target.setDead();

				this.property.eatFoodBonus(target.getEntityItem());
			}

			return false;
		}
		if (target == null)
			return false;

		return true;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
	}
}
