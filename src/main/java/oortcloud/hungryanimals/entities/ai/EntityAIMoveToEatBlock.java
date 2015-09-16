package oortcloud.hungryanimals.entities.ai;

import java.util.ArrayList;
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

	private EntityLiving entity;
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
	private BlockPos bestPos;
	private double bestf = 0;
	private int phase;
	private double speed;

	public EntityAIMoveToEatBlock(EntityLiving entity, ExtendedPropertiesHungryAnimal property, double speed) {
		this.entity = entity;
		this.property = property;
		this.worldObj = this.entity.worldObj;
		this.speed = speed;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		return this.property.getHungry() < this.property.courtship_hungerCondition;
	}

	@Override
	public void startExecuting() {
		float radius = 32.0F;

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
		ArrayList<EntityLiving> list = (ArrayList<EntityLiving>) this.worldObj.getEntitiesWithinAABB(entity.getClass(), entity.getEntityBoundingBox().expand(radius, radius, radius));
		int size = list.size();
		if (list.isEmpty()) {
			entity.setDead();
			return;
		} else {
			double minR = Double.MAX_VALUE;
			for (EntityLiving e : list) {
				double eX = e.posX;
				double eZ = e.posZ;
				double eY = e.posY;
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

	@Override
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

						double f = this.property.getBlockPathWeight(i, j, k) * (1 + entity.getRNG().nextDouble()) * centralizationFunction(Math.sqrt((this.comX - i) * (this.comX - i) + (this.comZ - k) * (this.comZ - k)))
								* (0.1 * (Math.sqrt((closestX - i) * (closestX - i) + (closestY - j) * (closestY - j) + (closestZ - k) * (closestZ - k))) + 1);

						if (f > bestf) {
							bestf = f;
							bestPos = new BlockPos(i, j, k);
						}
						int rand = entity.getRNG().nextInt(serchingApprox);
						iter += rand + 1;
						this.z_search += rand;
					}
					this.z_search = ((this.z_search + serchingZ) % (serchingZ * 2 + 1)) - serchingZ;
				}
				this.x_search = ((this.x_search + serchingX) % (serchingX * 2 + 1)) - serchingX;
			}

			entity.getNavigator().tryMoveToXYZ(bestPos.getX(), bestPos.getY(), bestPos.getZ(), this.speed);
			this.phase = 1;
			return true;
		}
		if (this.phase == 1) {
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
		return false;
	}

	private double centralizationFunction(double R) {
		double k = 0.5;
		if (R > 32)
			return 0;
		return -k * (R - 32) + 1;
	}
}
