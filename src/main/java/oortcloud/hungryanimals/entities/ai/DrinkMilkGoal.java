package oortcloud.hungryanimals.entities.ai;

import java.util.Collections;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.PacketClientSpawnParticle;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityProducingAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderProducingAnimal;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferences;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreference;
import oortcloud.hungryanimals.entities.production.IProduction;
import oortcloud.hungryanimals.entities.production.ProductionFluid;

import javax.annotation.Nullable;

public class DrinkMilkGoal extends FollowParentGoal {

	@Nullable
	private ICapabilityHungryAnimal childHungry;
	private IFluidHandler tank;
	private IFoodPreference<FluidStack> pref;
	private boolean noMilk;
	
	private int drinkCounter;
	private static final int DRINK = 10;

	private int searchCounter;
	private static final int SEARCH = 200;

	private FluidStack fluid;
	
	public DrinkMilkGoal(MobEntity animal, double speed, FluidStack fluid) {
		super(animal, speed);
		this.fluid = fluid;
		childHungry = animal.getCapability(ProviderHungryAnimal.CAP, null);
		pref = FoodPreferences.getInstance().REGISTRY_FLUID.get(animal.getClass());
	}

	public boolean shouldExecute() {
		if (ageable == null || childHungry == null) {
			return false;
		} else if (ageable.getAge() >= 0) {
			return false;
		} else if (childHungry.getStomach() >= childHungry.getMaxStomach()) {
			return false;
		} else {
			if (--searchCounter <= 0) {
				searchCounter = SEARCH;
				MobEntity entityanimal = findMother();

				if (entityanimal == null) {
					return false;
				} else {
					this.parentAnimal = entityanimal;
					return true;
				}
			} else {
				return false;
			}
		}
	}

	protected MobEntity findMother() {
		List<MobEntity> list = this.childAnimal.world.<MobEntity>getEntitiesWithinAABB(this.childAnimal.getClass(),
				this.childAnimal.getBoundingBox().grow(8.0D, 4.0D, 8.0D), this::isParent);

		Collections.shuffle(list);

		for (MobEntity entityanimal1 : list) {
			ICapabilityProducingAnimal capProducing = entityanimal1.getCapability(ProviderProducingAnimal.CAP, null);

			if (capProducing != null) {
				for (IProduction i : capProducing.getProductions()) {
					if (i instanceof ProductionFluid) {
						ProductionFluid iFluid = (ProductionFluid) i;
						IFluidHandler fluidHandler = iFluid.getFluidHandler();
						FluidStack drain = fluidHandler.drain(fluid, false);
						if (drain != null && drain.amount == fluid.amount) {
							tank = fluidHandler;
							return entityanimal1;
						}
					}
				}
			}
		}

		return null;
	}

	public boolean shouldContinueExecuting() {
		if (ageable.getAge() >= 0) {
			return false;
		} else if (!this.parentAnimal.isAlive()) {
			return false;
		} else if (childHungry.getStomach() >= childHungry.getMaxStomach()) {
			return false;
		} else {
			return !noMilk;
		}
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		drinkCounter = 0;
		noMilk = false;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (--drinkCounter <= 0) {
			if (childAnimal.getBoundingBox().grow(0.5).intersects(parentAnimal.getBoundingBox())) {
				if (childHungry.getStomach() < childHungry.getMaxStomach()) {
					FluidStack drain = tank.drain(fluid, true);
					if (drain != null && drain.amount > 0) {
						childHungry.addNutrient(pref.getNutrient(drain));
						childHungry.addStomach(pref.getStomach(drain));
						
						WorldServer world = (WorldServer) childAnimal.getEntityWorld();
						for (PlayerEntity i : world.getEntityTracker().getTrackingPlayers(childAnimal)) {
							AxisAlignedBB boxMilking = childAnimal.getBoundingBox().grow(0.5).intersect(parentAnimal.getBoundingBox());
							Vec3d pointMilking = new Vec3d(
									(boxMilking.maxX + boxMilking.minX) * 0.5D,
									(boxMilking.maxY + boxMilking.minY) * 0.5D,
									(boxMilking.maxZ + boxMilking.minZ) * 0.5D
							);
							PacketClientSpawnParticle packet = new PacketClientSpawnParticle(pointMilking);
							HungryAnimals.simpleChannel.sendTo(packet, (ServerPlayerEntity) i);
						}
					}
					
					if (drain == null || drain.amount < fluid.amount) {
						noMilk = true;
					}
				}

				this.drinkCounter = DRINK;
			}
		}
	}
	
	public static void parse(JsonElement jsonEle, AIContainer aiContainer) {
		if (!(jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Drink Milk must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}

		JsonObject jsonObject = (JsonObject) jsonEle;

		float speed = JSONUtils.getFloat(jsonObject, "speed");
		Fluid fluid = FluidRegistry.getFluid(JSONUtils.getString(jsonObject, "fluid"));
		int amount = JSONUtils.getInt(jsonObject, "amount");
		
		AIFactory factory = (entity) -> new DrinkMilkGoal(entity, speed, new FluidStack(fluid, amount));
		aiContainer.getTask().after(SwimGoal.class)
	                         .after(AvoidPlayerGoal.class)
	                         .after(MateModifiedGoal.class)
		                     .before(WaterAvoidingRandomWalkingGoal.class)
		                     .before(MoveToEatBlockGoal.class)
		                     .before(MoveToEatItemGoal.class)
		                     .before(MoveToTroughGoal.class)
		                     .put(factory);
	}

}
