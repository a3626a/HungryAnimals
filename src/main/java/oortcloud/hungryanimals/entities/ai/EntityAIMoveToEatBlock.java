package oortcloud.hungryanimals.entities.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;

import com.google.common.base.Predicate;

public class EntityAIMoveToEatBlock extends EntityAIBase {

	private Predicate predicate = new Predicate() {
		public boolean apply(Object obj) {
			return obj.getClass() == entity.getClass();
		}
	};

	private EntityLiving entity;
	private ExtendedPropertiesHungryAnimal property;
	private World worldObj;
	private BlockPos bestPos;
	private double speed;

	private double foodCondition = Double.MAX_VALUE;

	private int delayCounter;
	private static int delay = 100;

	public EntityAIMoveToEatBlock(EntityLiving entity, ExtendedPropertiesHungryAnimal property, double speed) {
		this.delayCounter = entity.getRNG().nextInt(delay);
		
		this.entity = entity;
		this.property = property;
		this.worldObj = this.entity.worldObj;
		this.speed = speed;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		initializeFoodCondition();
		if (property.hunger_max - property.hunger < foodCondition)
			return false;

		if (this.delayCounter > 0) {
			--this.delayCounter;
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void startExecuting() {
		float herdRadius = 32.0F;

		// find central position of the herd
		// find closest entity in the herd

		BlockPos centralPos = new BlockPos(entity);
		BlockPos closestPos = null;
		double minimumDistanceSq = Double.MAX_VALUE;

		ArrayList<EntityLiving> list = (ArrayList<EntityLiving>) this.worldObj.func_175674_a(entity, entity.getEntityBoundingBox().expand(herdRadius, herdRadius, herdRadius), predicate);
		for (EntityLiving e : list) {
			centralPos = centralPos.add(e.getPosition());

			double dist = entity.getPosition().distanceSq(e.getPosition());

			if (dist < minimumDistanceSq) {
				closestPos = e.getPosition();
				minimumDistanceSq = dist;
			}
		}
		double size = list.size()+1;
		centralPos=new BlockPos(centralPos.getX()/size,centralPos.getY()/size,centralPos.getZ()/size);

		// find best block to go
		int searchRadius = 8;
		double bestValue = -Double.MAX_VALUE;
		
		for (int i = -searchRadius; i <= searchRadius; i++) {
			for (int j = -searchRadius; j <= searchRadius; j++) {
				for (int k = -searchRadius; k <= searchRadius; k++) {
					double value;
					BlockPos iPos = entity.getPosition().add(i, j, k);
					if (closestPos == null) {
						value = this.property.getBlockPathWeight(iPos) * (1 + entity.getRNG().nextDouble()) * centralizationFunction(Math.sqrt(centralPos.distanceSq(iPos)));
					} else {
						value = this.property.getBlockPathWeight(iPos) * (1 + entity.getRNG().nextDouble()) * centralizationFunction(Math.sqrt(centralPos.distanceSq(iPos)))
								* (0.1 * (Math.sqrt(closestPos.distanceSq(iPos))) + 1);
					}
					
					if (value > bestValue) {
						bestValue = value;
						bestPos=iPos;
					}
				}
			}
		}

		entity.getNavigator().tryMoveToXYZ(bestPos.getX(), bestPos.getY(), bestPos.getZ(), this.speed);
	}

	private class Point {
		public BlockPos pos;
		public int distance;
		
		public Point(BlockPos pos, int distance) {
			this.pos = pos;
			this.distance = distance;
		}
		
		@Override
		public boolean equals(Object obj) {
			return pos.equals(((Point)obj).pos);
		}
		
		@Override
		public int hashCode() {
			return pos.hashCode();
		}
	}
	
	@Override
	public boolean continueExecuting() {
		if (entity.getNavigator().noPath()) {
			float distanceSq = 2;
			IBlockState block = this.worldObj.getBlockState(bestPos);
			if (this.property.canEatBlock(block) && bestPos.distanceSqToCenter(entity.posX, entity.posY, entity.posZ) <= distanceSq) {
				if (this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
					this.worldObj.setBlockToAir(bestPos);
				}
				property.eatBlockBonus(block);
			}
			return false;
		}
		return true;
	}

	@Override
	public void resetTask() {
		bestPos=null;
		delayCounter = delay;
	}

	private double centralizationFunction(double R) {
		double k = 0.5;
		if (R > 32)
			return 0;
		return -k * (R - 32) + 1;
	}

	/**
	 * set "foodCondition" to minimum food value of edible foods.
	 */
	private void initializeFoodCondition() {
		if (foodCondition == Double.MAX_VALUE) {
			for (double i : property.hunger_block.values()) {
				if (i < foodCondition)
					foodCondition = i;
			}
		}
	}
}
