package oortcloud.hungryanimals.entities.ai;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;

public class EntityAIAvoidPlayer extends EntityAIBase {

	private EntityCreature entity;
	private ExtendedPropertiesHungryAnimal property;
	private double xPosition;
	private double yPosition;
	private double zPosition;
	private double speed;
	public List players;

	public EntityAIAvoidPlayer(EntityCreature entity, ExtendedPropertiesHungryAnimal property, double speed) {
		this.entity = entity;
		this.property = property;
		this.speed = speed;
		this.players = new ArrayList<EntityPlayer>();
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {

		if (!(players != null && !players.isEmpty() && property.taming < -1) && !(this.entity.getAITarget() != null)) {
			return false;
		} else {
			EntityPlayer target = null;
			if (this.entity.getAITarget() == null) {
				double dist = 8 * 8;
				for (Object i : players) {
					EntityPlayer p = ((EntityPlayer) i);
					if (!p.capabilities.isCreativeMode) {
						double distance = (p.posX - entity.posX) * (p.posX - entity.posX) + (p.posY - entity.posY) * (p.posY - entity.posY) + (p.posZ - entity.posZ) * (p.posZ - entity.posZ);
						if (distance < dist) {
							dist = distance;
							target = p;
						}
					}
				}
				if (target == null)
					return false;
			} else {
				target = (EntityPlayer) this.entity.getAITarget();
				players.add(entity);
			}

			double radius = 16;
			List animals = target.worldObj.getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(target.posX - radius, target.posY - radius, target.posZ - radius, target.posX + radius, target.posY + radius, target.posZ + radius));
			for (Object i : animals) {
				ExtendedPropertiesHungryAnimal property = (ExtendedPropertiesHungryAnimal) ((EntityAnimal) i).getExtendedProperties(Strings.extendedPropertiesKey);
				if (property != null) {
					property.ai_avoidPlayer.players = players;
				}
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
		this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed * (this.entity.getAITarget() == null ? 1 : 1.5));
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return !this.entity.getNavigator().noPath();
	}

}
