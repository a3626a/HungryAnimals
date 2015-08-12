package oortcloud.hungryanimals.entities.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;
import oortcloud.hungryanimals.tileentities.TileEntityCrankAnimal;

public class EntityAICrank extends EntityAIBase {

	public TileEntityCrankAnimal crankAnimal;
	private EntityAnimal entity;
	private ExtendedPropertiesHungryAnimal property;
	private World world;

	private double speed = 1.5D;
	private double lastAngleDiff;
	private long timer = 0;
	private static int period = 100;
	
	public EntityAICrank(EntityAnimal entity, ExtendedPropertiesHungryAnimal property) {
		this.entity = entity;
		this.property = property;
		this.world = this.entity.worldObj;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		return continueExecuting();
		/*
		if (world.getWorldTime()-timer > period) {
			timer = world.getWorldTime();
			return continueExecuting();
		} else {
			return false;
		}
		*/
	}

	@Override
	public boolean continueExecuting() {
		if (crankAnimal != null && crankAnimal.isInvalid()) {
			crankAnimal = null;
			this.entity.getNavigator().func_179678_a(1.0F);
			return false;
		}
		if (property.getHungry() > 0.5 && property.taming >= 1 && crankAnimal != null && crankAnimal.getLeashedAnimal() == entity) {
			if (this.entity.getNavigator().noPath()) {
				if (!tryMove()) {
					this.entity.getNavigator().func_179678_a(1.0F);
					return false;
				} else {
					return true;
				}
			}
		}
		this.entity.getNavigator().func_179678_a(1.0F);
		return false;
	}

	@Override
	public void startExecuting() {
		this.entity.getNavigator().func_179678_a(0.1F);
	}

	private PathPoint findPathPoint(int x, int y, int z) {
		BlockPos ipos = new BlockPos(x, y, z);

		for (int i = -1; i <= 1; i++) {
			BlockPos pos = ipos.add(0, i, 0);
			if (world.getBlockState(pos.up()).getBlock().isPassable(world, pos.up()) && world.getBlockState(pos).getBlock().isPassable(world, pos)
					&& !world.getBlockState(pos.down()).getBlock().isPassable(world, pos.down())) {
				return new PathPoint(pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return null;
	}

	private PathPoint getNextPathPoint(PathPoint pnt) {
		int x1 = crankAnimal.getPos().getX() - 2;
		int x2 = crankAnimal.getPos().getX() + 2;
		int z1 = crankAnimal.getPos().getZ() - 2;
		int z2 = crankAnimal.getPos().getZ() + 2;

		if (pnt.xCoord >= x2 && pnt.zCoord < z2) {
			return findPathPoint(x2, pnt.yCoord, pnt.zCoord + 1);
		}
		if (pnt.zCoord >= z2 && pnt.xCoord > x1) {
			return findPathPoint(pnt.xCoord - 1, pnt.yCoord, z2);
		}
		if (pnt.xCoord <= x1 && pnt.zCoord > z1) {
			return findPathPoint(x1, pnt.yCoord, pnt.zCoord - 1);
		}
		if (pnt.zCoord <= z1 && pnt.xCoord < x2) {
			return findPathPoint(pnt.xCoord + 1, pnt.yCoord, z1);
		}
		return null;
	}

	private PathEntity getNextPath() {
		int num = 4;
		PathPoint temp = new PathPoint(entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ());
		PathPoint[] path = new PathPoint[num];
		for (int i = 0; i < num; i++) {
			path[i] = getNextPathPoint(temp);
			if (path[i] == null) {
				return null;
			}
			temp = path[i];
		}
		return new PathEntity(path);
	}

	private boolean resetPosition() {
		boolean flag1 = entity.posX > crankAnimal.getPos().getX() + 0.5;
		boolean flag2 = entity.posZ > crankAnimal.getPos().getZ() + 0.5;

		int x1 = crankAnimal.getPos().getX() - 2;
		int x2 = crankAnimal.getPos().getX() + 2;
		int z1 = crankAnimal.getPos().getZ() - 2;
		int z2 = crankAnimal.getPos().getZ() + 2;

		if (flag1) {
			if (flag2) {
				return entity.getNavigator().tryMoveToXYZ(x1, crankAnimal.getPos().getY(), z2, speed);
			} else {
				return entity.getNavigator().tryMoveToXYZ(x2, crankAnimal.getPos().getY(), z2, speed);
			}
		} else {
			if (flag2) {
				return entity.getNavigator().tryMoveToXYZ(x1, crankAnimal.getPos().getY(), z1, speed);
			} else {
				return entity.getNavigator().tryMoveToXYZ(x2, crankAnimal.getPos().getY(), z1, speed);
			}
		}
	}

	private boolean tryMove() {
		PathEntity path = getNextPath();
		if (path != null) {
			return entity.getNavigator().setPath(path, speed) ? true : resetPosition();
		} else {
			return resetPosition();
		}
		
	}

	@Override
	public void updateTask() {
		updateSpeed();
	}

	public double getAngleDifference() {
		double angleEntity = Math.toDegrees(Math.atan2(entity.posZ - crankAnimal.getPos().getZ() - 0.5, entity.posX - crankAnimal.getPos().getX() - 0.5)) - 90;
		angleEntity = (angleEntity + 360) % 360;
		double angleTile = crankAnimal.getNetwork().getAngle(0);
		double angleDiff = angleEntity - angleTile;
		angleDiff = (angleDiff + 360) % 360;
		if (angleDiff > 270)
			angleDiff = 0;
		if (angleDiff > 180)
			angleDiff = 180;
		return angleDiff;
	}

	private void updateSpeed() {
		double angleDiff = getAngleDifference();
		speed += -0.005 * (angleDiff - 90) / 90.0 - 0.005 * (angleDiff - lastAngleDiff);
		if (speed < 1)
			speed = 1;
		lastAngleDiff = angleDiff;
	}
}
