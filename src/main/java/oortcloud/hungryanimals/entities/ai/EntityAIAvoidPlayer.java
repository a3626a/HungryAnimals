package oortcloud.hungryanimals.entities.ai;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;
import oortcloud.hungryanimals.entities.util.PredicateUtil;

import com.google.common.base.Predicate;

public class EntityAIAvoidPlayer extends EntityAIBase {

	private EntityCreature entity;
	private ExtendedPropertiesHungryAnimal property;
	private double xPosition;
	private double yPosition;
	private double zPosition;
	private double speed;
	public List predators;

	public EntityAIAvoidPlayer(EntityCreature entity, ExtendedPropertiesHungryAnimal property, double speed) {
		this.entity = entity;
		this.property = property;
		this.speed = speed;
		this.predators = new ArrayList<EntityPlayer>();
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {

		if (!(predators != null && !predators.isEmpty() && property.taming < -1) && !(this.entity.getAITarget() != null)) {
			return false;
		} else {
			EntityPlayer target = null;
			if (this.entity.getAITarget() == null) {
				double dist = 8 * 8;
				for (Object i : predators) {
					EntityPlayer player = ((EntityPlayer) i);
					if (!(player.capabilities.isCreativeMode || player.isSpectator())) {
						double distance = player.getPosition().distanceSq(entity.getPosition());
						if (distance < dist) {
							dist = distance;
							target = player;
						}
					}
				}
				if (target == null)
					return false;
			} else if (this.entity.getAITarget() instanceof EntityPlayer) {
				target = (EntityPlayer) this.entity.getAITarget();
				if (!predators.contains(target)) {
					predators.add(target);
				}
			} else {
				return false;
			}

			double radius = 16;
			List animals = target.worldObj.getEntitiesWithinAABB(EntityAnimal.class, target.getEntityBoundingBox().expand(radius, radius, radius), PredicateUtil.IS_HUNGRY_ANIMAL);
			for (Object i : animals) {
				ExtendedPropertiesHungryAnimal property = (ExtendedPropertiesHungryAnimal) ((EntityAnimal) i).getExtendedProperties(Strings.extendedPropertiesKey);
				property.ai_avoidPlayer.predators = predators;
			}

			Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, 16, 8, new Vec3(target.posX, target.posY, target.posZ));
			if (vec3 == null) {
				return false;
			} else {
				this.xPosition = vec3.xCoord;
				this.yPosition = vec3.yCoord;
				this.zPosition = vec3.zCoord;
				return true;
			}
		}

	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed * (this.entity.getAITarget() == null ? 1 : 1.2));
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return !this.entity.getNavigator().noPath();
	}

}
