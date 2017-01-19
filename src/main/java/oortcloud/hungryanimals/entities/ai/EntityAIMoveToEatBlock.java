package oortcloud.hungryanimals.entities.ai;

import java.util.ArrayList;

import com.google.common.base.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;
import oortcloud.hungryanimals.entities.properties.IFoodPreference;
import oortcloud.hungryanimals.entities.properties.handler.ModAttributes;

public class EntityAIMoveToEatBlock extends EntityAIBase {

	private Predicate predicate = new Predicate() {
		public boolean apply(Object obj) {
			return obj.getClass() == entity.getClass();
		}
	};

	private EntityLiving entity;
	private World worldObj;
	private BlockPos bestPos;
	private double speed;

	private double foodCondition = Double.MAX_VALUE;
	private IFoodPreference<IBlockState> pref;
	private int delayCounter;
	private static int delay = 100;

	public EntityAIMoveToEatBlock(EntityLiving entity, IFoodPreference<IBlockState> pref, double speed) {
		this.delayCounter = entity.getRNG().nextInt(delay);
		this.entity = entity;
		this.worldObj = this.entity.worldObj;
		this.speed = speed;
		this.pref = pref;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		initializeFoodCondition();
		
		// (left stomach storage) < (minimum edible food energy) - sleep this AI
		if (entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_max).getAttributeValue() - entity.getCapability(ProviderHungryAnimal.CAP_HUNGRYANIMAL, null).getHunger() < foodCondition)
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

		ArrayList<Entity> list = (ArrayList<Entity>) this.worldObj.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(herdRadius, herdRadius, herdRadius), predicate);
		for (Entity e : list) {
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
		IBlockState block = this.worldObj.getBlockState(bestPos);
		if (!this.pref.canEat(block)) {
			this.entity.getNavigator().clearPathEntity();
			return false;
		}
		if (entity.getNavigator().noPath()) {
			float distanceSq = 2;
			if (bestPos.distanceSqToCenter(entity.posX, entity.posY, entity.posZ) <= distanceSq) {
				if (this.worldObj.getGameRules().getBoolean("mobGriefing")) {
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
			for (IBlockState i : this.pref.getFoods()) {
				if (this.pref.getHunger(i) < foodCondition)
					foodCondition = this.pref.getHunger(i);
			}
		}
	}
}
