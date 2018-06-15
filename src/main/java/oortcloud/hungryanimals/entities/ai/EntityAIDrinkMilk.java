package oortcloud.hungryanimals.entities.ai;

import java.util.Collections;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
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

public class EntityAIDrinkMilk extends EntityAIFollowParent {

	private ICapabilityHungryAnimal childHungry;
	private IFluidHandler tank;
	private IFoodPreference<FluidStack> pref;
	private boolean noMilk;
	
	private int drinkCounter;
	private static final int DRINK = 10;

	private int searchCounter;
	private static final int SEARCH = 200;

	private FluidStack fluid;
	
	public EntityAIDrinkMilk(EntityLiving animal, double speed, FluidStack fluid) {
		super(animal, speed);
		this.fluid = fluid;
		childHungry = animal.getCapability(ProviderHungryAnimal.CAP, null);
		pref = FoodPreferences.getInstance().REGISTRY_FLUID.get(animal.getClass());
	}

	public boolean shouldExecute() {
		if (ageable == null) {
			return false;
		} else if (ageable.getAge() >= 0) {
			return false;
		} else if (childHungry.getStomach() >= childHungry.getMaxStomach()) {
			return false;
		} else {
			if (--searchCounter <= 0) {
				searchCounter = SEARCH;
				EntityLiving entityanimal = findMother();

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

	protected EntityLiving findMother() {
		List<EntityLiving> list = this.childAnimal.world.<EntityLiving>getEntitiesWithinAABB(this.childAnimal.getClass(),
				this.childAnimal.getEntityBoundingBox().grow(8.0D, 4.0D, 8.0D), this::isParent);
		EntityLiving entityanimal = null;

		Collections.shuffle(list);

		for (EntityLiving entityanimal1 : list) {
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

		return entityanimal;
	}

	public boolean shouldContinueExecuting() {
		if (ageable.getAge() >= 0) {
			return false;
		} else if (!this.parentAnimal.isEntityAlive()) {
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
			if (childAnimal.getEntityBoundingBox().grow(0.5).intersects(parentAnimal.getEntityBoundingBox())) {
				if (childHungry.getStomach() < childHungry.getMaxStomach()) {
					FluidStack drain = tank.drain(fluid, true);
					if (drain != null && drain.amount > 0) {
						childHungry.addNutrient(pref.getNutrient(drain));
						childHungry.addStomach(pref.getStomach(drain));
						
						WorldServer world = (WorldServer) childAnimal.getEntityWorld();
						for (EntityPlayer i : world.getEntityTracker().getTrackingPlayers(childAnimal)) {
							PacketClientSpawnParticle packet = new PacketClientSpawnParticle(childAnimal.getEntityBoundingBox().grow(0.5).intersect(parentAnimal.getEntityBoundingBox()).getCenter());
							HungryAnimals.simpleChannel.sendTo(packet, (EntityPlayerMP) i);
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

		float speed = JsonUtils.getFloat(jsonObject, "speed");
		Fluid fluid = FluidRegistry.getFluid(JsonUtils.getString(jsonObject, "fluid"));
		int amount = JsonUtils.getInt(jsonObject, "amount");
		
		AIFactory factory = (entity) -> new EntityAIDrinkMilk(entity, speed, new FluidStack(fluid, amount));
		aiContainer.getTask().after(EntityAISwimming.class)
	                         .after(EntityAIAvoidPlayer.class)
	                         .after(EntityAIMateModified.class)
		                     .before(EntityAIWanderAvoidWater.class)
		                     .before(EntityAIMoveToEatBlock.class)
		                     .before(EntityAIMoveToEatItem.class)
		                     .before(EntityAIMoveToTrough.class)
		                     .put(factory);
	}

}
