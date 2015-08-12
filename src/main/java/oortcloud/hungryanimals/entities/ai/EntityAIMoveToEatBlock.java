package oortcloud.hungryanimals.entities.ai;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;

public class EntityAIMoveToEatBlock extends EntityAIBase {

	private final int serchingX = 8;
	private final int serchingY = 2;
	private final int serchingZ = 8;
	private final int serchingIteration = 10;
	private final int serchingApprox = 3;

	private EntityAnimal entity;
	private ExtendedPropertiesHungryAnimal property;
	private World worldObj;
	private int x_search;
	private int y_search;
	private int z_search;
	private int x_init;
	private int y_init;
	private int z_init;
	private double comX = 0;
	private double comY = 0;
	private double comZ = 0;
	private double closestX = 0;
	private double closestY = 0;
	private double closestZ = 0;
	private int bestX = 0;
	private int bestY = 0;
	private int bestZ = 0;
	private double bestf = 0;
	private int phase;
	private double speed;

	public EntityAIMoveToEatBlock(EntityAnimal entity, ExtendedPropertiesHungryAnimal property, double speed) {
		this.entity = entity;
		this.property = property;
		this.worldObj = this.entity.worldObj;
		this.speed = speed;
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {

		return this.property.getHungry() < this.property.courtship_hungerCondition;

	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.phase = 0;
		double posX = entity.posX;
		this.x_init = (int) posX;
		double posY = entity.posY;
		this.y_init = (int) posY;
		double posZ = entity.posZ;
		this.z_init = (int) posZ;
		this.y_search = -serchingY;
		this.x_search = -serchingX;
		this.z_search = -serchingZ;
		this.comX = 0;
		this.comY = 0;
		this.comZ = 0;
		this.bestf = 0;
		this.closestX = 0;
		this.closestY = 0;
		this.closestZ = 0;
		List list = this.worldObj.getEntitiesWithinAABB(entity.getClass(), entity.getEntityBoundingBox().expand(32, 4, 32));
		int size = list.size();
		if (list.isEmpty()) {
			entity.setDead();
			return;
		} else {
			double minR = 64;
			for (Object e : list) {
				double eX = ((EntityLiving) e).posX;
				double eZ = ((EntityLiving) e).posZ;
				double eY = ((EntityLiving) e).posY;
				comX += eX;
				comY += eY;
				comZ += eZ;

				double R = Math.sqrt((posX - eX) * (posX - eX) + (posY - eY) * (posY - eY) + (posZ - eZ) * (posZ - eZ));

				if (R < minR && R > 0.1) {
					this.closestX = eX;
					this.closestY = eY;
					this.closestZ = eZ;
					minR = R;
				}
			}
			comX /= (double) size;
			comY /= (double) size;
			comZ /= (double) size;
		}

	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		if (this.phase == 0) {

			int iter = 0;
			for (; this.y_search <= serchingY; this.y_search++) {
				for (; this.x_search <= serchingX; this.x_search++) {
					for (; this.z_search <= serchingZ; this.z_search++) {
						if (iter > serchingIteration)
							return true;

						int i = this.x_init + this.x_search;
						int j = this.y_init + this.y_search;
						int k = this.z_init + this.z_search;

						double f = this.property.getBlockPathWeight(i, j, k) * (1 + entity.getRNG().nextDouble())
								* centralizationFunction(Math.sqrt((this.comX - i) * (this.comX - i) + (this.comZ - k) * (this.comZ - k)))
								* (0.1 * (Math.sqrt((closestX - i) * (closestX - i) + (closestY - j) * (closestY - j) + (closestZ - k) * (closestZ - k))) + 1);

						if (f > bestf) {
							bestf = f;
							bestX = i;
							bestY = j;
							bestZ = k;
						}
						int rand = entity.getRNG().nextInt(serchingApprox);
						iter += rand + 1;
						this.z_search += rand;
					}
					this.z_search = ((this.z_search + serchingZ) % (serchingZ * 2 + 1)) - serchingZ;
				}
				this.x_search = ((this.x_search + serchingX) % (serchingX * 2 + 1)) - serchingX;
			}

			entity.getNavigator().tryMoveToXYZ(this.bestX, this.bestY, this.bestZ, this.speed);
			this.phase = 1;
			return true;
		}

		if (this.phase == 1) {
			if (entity.getNavigator().noPath()) {
				IBlockState block = this.worldObj.getBlockState(new BlockPos(this.bestX, this.bestY, this.bestZ));
				if (this.property.canEatBlock(block) && Math.abs(entity.posX - this.bestX - 0.5) <= 1.2 && Math.abs(entity.posY - this.bestY - 0.5) <= 1.2
						&& Math.abs(entity.posZ - this.bestZ - 0.5) <= 1.2) {
					if (this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
						this.worldObj.setBlockToAir(new BlockPos(bestX, bestY, bestZ));
					}

					property.eatBlockBonus(block);
				}
				return false;
			}
			return true;
		}

		return false;
	}

	private double centralizationFunction(double R) {
		double k = 0.5;
		if (R > 32)
			return 0;
		return -k * (R - 32) + 1;
	}
}
