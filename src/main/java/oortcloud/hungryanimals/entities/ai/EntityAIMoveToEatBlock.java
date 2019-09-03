package oortcloud.hungryanimals.entities.ai;

import java.util.ArrayList;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferences;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreference;

public class EntityAIMoveToEatBlock extends EntityAIBase {

	enum State {
		IDLE, MOVING, EATING
	}

	private Predicate<Entity> predicate = new Predicate<Entity>() {
		public boolean apply(@Nullable Entity obj) {
			if (obj == null)
				return false;
			return obj.getClass() == entity.getClass();
		}
	};

	protected EntityLiving entity;
	protected World worldObj;
	private BlockPos bestPos;
	private double speed;

	protected IFoodPreference<IBlockState> pref;
	protected ICapabilityHungryAnimal capHungry;
	private int delayCounter;
	private int timeoutCounter;
	private static int delay = 100;

	private State state = State.IDLE;
	int eatingGrassTimer;

	public EntityAIMoveToEatBlock(EntityLiving entity, double speed) {
		this.delayCounter = entity.getRNG().nextInt(delay);
		this.entity = entity;
		this.worldObj = this.entity.getEntityWorld();
		this.speed = speed;
		this.pref = FoodPreferences.getInstance().REGISTRY_BLOCK.get(this.entity.getClass());
		this.capHungry = entity.getCapability(ProviderHungryAnimal.CAP, null);
		this.setMutexBits(7);
	}
	
	@Override
	public boolean shouldExecute() {
		if (!pref.shouldEat(capHungry)) {
			return false;
		}

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

		ArrayList<Entity> list = (ArrayList<Entity>) this.worldObj.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().grow(herdRadius),
				predicate);
		for (Entity e : list) {
			centralPos = centralPos.add(e.getPosition());

			double dist = entity.getPosition().distanceSq(e.getPosition());

			if (dist < minimumDistanceSq) {
				closestPos = e.getPosition();
				minimumDistanceSq = dist;
			}
		}
		double size = list.size() + 1;
		centralPos = new BlockPos(centralPos.getX() / size, centralPos.getY() / size, centralPos.getZ() / size);

		// find best block to go
		int searchRadius = 8;
		double bestValue = -Double.MAX_VALUE;

		for (int i = -searchRadius; i <= searchRadius; i++) {
			for (int j = -searchRadius; j <= searchRadius; j++) {
				for (int k = -searchRadius; k <= searchRadius; k++) {
					double value;
					BlockPos iPos = entity.getPosition().add(i, j, k);
					if (closestPos == null) {
						value = this.getBlockPathWeight(iPos) * (1 + entity.getRNG().nextDouble())
								* centralizationFunction(Math.sqrt(centralPos.distanceSq(iPos)));
					} else {
						value = this.getBlockPathWeight(iPos) * (1 + entity.getRNG().nextDouble())
								* centralizationFunction(Math.sqrt(centralPos.distanceSq(iPos))) * (0.1 * (Math.sqrt(closestPos.distanceSq(iPos))) + 1);
					}

					if (value > bestValue) {
						bestValue = value;
						bestPos = iPos;
					}
				}
			}
		}

		entity.getNavigator().tryMoveToXYZ(bestPos.getX(), bestPos.getY(), bestPos.getZ(), this.speed);

		state = State.MOVING;
		timeoutCounter = 0;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (state == State.IDLE) {
			return false;
		}

		if (state == State.MOVING) {
			// Escape to IDLE state
			IBlockState block = this.worldObj.getBlockState(bestPos);
			if (!this.pref.canEat(capHungry, block)) {
				this.entity.getNavigator().clearPath();
				state = State.IDLE;
				return false;
			}
			
			// Timeout
			if (timeoutCounter > 1200) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void updateTask() {
		if (state == State.IDLE) {

		} else if (state == State.MOVING) {
				float distanceSq = 1;
				if (bestPos.distanceSqToCenter(entity.posX, entity.posY, entity.posZ) <= distanceSq) {
					state = State.EATING;
					eatingGrassTimer = 40;
					if (entity instanceof EntitySheep) {
						worldObj.setEntityState(entity, (byte)10);
					}
					entity.getNavigator().clearPath();
				} else {
					if (entity.getNavigator().noPath()) {
						state = State.IDLE;
						return;
					}
					
					timeoutCounter += 1;
					
		            if (this.timeoutCounter % 40 == 0)
		            {
		            	entity.getNavigator().tryMoveToXYZ(bestPos.getX(), bestPos.getY(), bestPos.getZ(), this.speed);
		            }
				}
			
		} else if (state == State.EATING) {
			eatingGrassTimer -= 1;
			if (eatingGrassTimer == 4) {
				// Finish eating
				IBlockState block = worldObj.getBlockState(bestPos);
				if (this.worldObj.getGameRules().getBoolean("mobGriefing")) {
					this.worldObj.destroyBlock(bestPos, false);
				}
				eatBlockBonus(block);
			}

			if (eatingGrassTimer == 0) {
				state = State.IDLE;
			}
		}
	}

	@Override
	public void resetTask() {
		bestPos = null;
		delayCounter = delay;
		state = State.IDLE;
		eatingGrassTimer = 0;
	}

	private double centralizationFunction(double R) {
		double k = 0.5;
		if (R > 32)
			return 0;
		return -k * (R - 32) + 1;
	}

	private double getBlockPathWeight(BlockPos pos) {
		IBlockState state = this.worldObj.getBlockState(pos);
		if (state.getBlock() == ModBlocks.excreta) {
			return -1.0;
		} else if (pref.canEat(capHungry, state)) {
			return pref.getNutrient(state);
		} else {
			return 0.01;
		}
	}

	public void eatBlockBonus(IBlockState block) {
		if (block == null)
			return;
		double nutrient = pref.getNutrient(block);
		capHungry.addNutrient(nutrient);

		double stomach = pref.getStomach(block);
		capHungry.addStomach(stomach);

		if (entity instanceof EntitySheep) {
			entity.eatGrassBonus();
		}
	}

	public static void parse(JsonElement jsonEle, AIContainer aiContainer) {
		if (!(jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Eat Block must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}

		JsonObject jsonObject = (JsonObject) jsonEle;

		float speed = JsonUtils.getFloat(jsonObject, "speed");

		AIFactory factory = (entity) -> new EntityAIMoveToEatBlock(entity, speed);
		aiContainer.getTask().after(EntityAISwimming.class)
					         .before(EntityAIFollowParent.class)
				 	         .before(EntityAIWanderAvoidWater.class)
		                     .put(factory);
	}
}
