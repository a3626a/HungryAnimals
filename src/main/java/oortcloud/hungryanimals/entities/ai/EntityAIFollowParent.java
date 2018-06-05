package oortcloud.hungryanimals.entities.ai;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;

public class EntityAIFollowParent extends EntityAIBase {
	/** The child that is following its parent. */
	protected EntityAnimal childAnimal;
	protected EntityAnimal parentAnimal;
	double moveSpeed;
	private int delayCounter;

	public EntityAIFollowParent(EntityAnimal animal, double speed) {
		this.childAnimal = animal;
		this.moveSpeed = speed;

		setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.childAnimal.getGrowingAge() >= 0) {
			return false;
		} else {
			List<EntityAnimal> list = this.childAnimal.world.<EntityAnimal>getEntitiesWithinAABB(this.childAnimal.getClass(),
					this.childAnimal.getEntityBoundingBox().grow(8.0D, 4.0D, 8.0D), this::isParent);
			EntityAnimal entityanimal = null;
			double d0 = Double.MAX_VALUE;

			for (EntityAnimal entityanimal1 : list) {
				double d1 = this.childAnimal.getDistanceSq(entityanimal1);

				if (d1 <= d0) {
					d0 = d1;
					entityanimal = entityanimal1;
				}
			}

			if (entityanimal == null) {
				return false;
			} else if (d0 < 9.0D) {
				return false;
			} else {
				this.parentAnimal = entityanimal;
				return true;
			}
		}
	}

	protected boolean isParent(EntityAnimal parent) {
		return parent.getGrowingAge() >= 0;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting() {
		if (this.childAnimal.getGrowingAge() >= 0) {
			return false;
		} else if (!this.parentAnimal.isEntityAlive()) {
			return false;
		} else {
			double d0 = this.childAnimal.getDistanceSq(this.parentAnimal);
			return d0 >= 9.0D && d0 <= 256.0D;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.delayCounter = 0;
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void resetTask() {
		this.parentAnimal = null;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask() {
		if (--this.delayCounter <= 0) {
			this.delayCounter = 10;
			this.childAnimal.getNavigator().tryMoveToEntityLiving(this.parentAnimal, this.moveSpeed);
		}
	}

	public static void parse(JsonElement jsonEle, AIContainer aiContainer) {
		if (!(jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Follow Parent must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}

		JsonObject jsonObject = (JsonObject) jsonEle;

		float speed = JsonUtils.getFloat(jsonObject, "speed");

		AIFactory factory = (entity) -> new EntityAIFollowParent(entity, speed);
		aiContainer.getTask().after(EntityAISwimming.class).before(EntityAIWanderAvoidWater.class).put(factory);
	}
}