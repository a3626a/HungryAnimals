package oortcloud.hungryanimals.entities.ai;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.BlockState;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.block.TroughBlock;
import oortcloud.hungryanimals.block.ModBlocks;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.capability.ICapabilityAgeable;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderAgeable;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.capability.TamingLevel;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferences;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreference;
import oortcloud.hungryanimals.potion.ModPotions;
import oortcloud.hungryanimals.tileentities.TileEntityTrough;
import oortcloud.hungryanimals.utils.Tamings;

import java.util.EnumSet;

public class MoveToTroughGoal extends Goal {

	private MobEntity entity;
	private double speed;
	private World world;
	public BlockPos pos;
	private int delayCounter;
	private static int delay = 100;
	private ICapabilityHungryAnimal capHungry;
	@Nullable
	private ICapabilityTamableAnimal capTaming;
	@Nullable
	private ICapabilityAgeable capAgeable;
	private IFoodPreference<ItemStack> pref;

	public MoveToTroughGoal(MobEntity entity, double speed) {
		this.delayCounter = entity.getRNG().nextInt(delay);
		this.capHungry = entity.getCapability(ProviderHungryAnimal.CAP).orElse(null);
		this.capTaming = entity.getCapability(ProviderTamableAnimal.CAP).orElse(null);
		this.capAgeable = entity.getCapability(ProviderAgeable.CAP).orElse(null);
		this.pref = FoodPreferences.getInstance().REGISTRY_ITEM.get(entity.getClass());

		this.entity = entity;
		this.world = this.entity.getEntityWorld();
		this.speed = speed;
		this.setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		if (capHungry == null)
			return false;

		if (pos == null)
			return false;

		if (this.delayCounter > 0) {
			--this.delayCounter;
			return false;
		} else {
			BlockState state = world.getBlockState(pos);
			if (state.getBlock() == ModBlocks.TROUGH.get()) {
				TileEntity temp = ((TroughBlock) state.getBlock()).getTileEntity(world, pos);
				if (Tamings.getLevel(capTaming) == TamingLevel.TAMED && temp != null && temp instanceof TileEntityTrough) {
					TileEntityTrough trough = (TileEntityTrough) temp;
					return !trough.stack.isEmpty() && pref.canEat(capHungry, trough.stack);
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	@Override
	public void startExecuting() {
		this.entity.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), this.speed);
	}

	@Override
	public boolean shouldContinueExecuting() {
		float distSq = 2;
		if (pos.distanceSq(entity.posX, entity.posY, entity.posZ, true) <= distSq) {
			BlockState state = world.getBlockState(pos);
			if (state.getBlock() == ModBlocks.TROUGH.get()) {
				TileEntity tileEntity = ((TroughBlock) state.getBlock()).getTileEntity(world, pos);
				if (tileEntity != null && tileEntity instanceof TileEntityTrough) {
					TileEntityTrough trough = (TileEntityTrough) tileEntity;
					while (!trough.stack.isEmpty() && pref.canEat(capHungry, trough.stack)) {
						eatFoodBonus(trough.stack);
						trough.stack.shrink(1);
					}
					world.notifyBlockUpdate(pos, state, state, 3);
				}
			}
			return false;
		}
		return !entity.getNavigator().noPath();
	}

	private void eatFoodBonus(ItemStack item) {
		if (item.isEmpty())
			return;

		IFoodPreference<ItemStack> pref = FoodPreferences.getInstance().REGISTRY_ITEM.get(entity.getClass());

		double nutrient = pref.getNutrient(item);
		capHungry.addNutrient(nutrient);

		double stomach = pref.getStomach(item);
		capHungry.addStomach(stomach);

		if (capAgeable != null && capAgeable.getAge() < 0) {
			CompoundNBT tag = item.getTag();
			if (tag == null || !tag.contains("isNatural") || !tag.getBoolean("isNatural")) {
				int duration = (int) (nutrient / entity.getAttribute(ModAttributes.hunger_weight_bmr).getValue());
				this.entity.addPotionEffect(new EffectInstance(ModPotions.potionGrowth, duration, 1));
			}
		}

		CompoundNBT tag = item.getTag();
		if (tag == null || !tag.contains("isNatural") || !tag.getBoolean("isNatural")) {
			if (this.capTaming != null) {
				this.capTaming.addTaming(0.0002 / entity.getAttribute(ModAttributes.hunger_weight_bmr).getValue() * nutrient);
			}
		}

	}

	@Override
	public void resetTask() {
		delayCounter = delay;
	}

	public static void parse(JsonElement jsonEle, AIContainer aiContainer) {
		if (!(jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Trough must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}

		JsonObject jsonObject = (JsonObject) jsonEle;

		float speed = JSONUtils.getFloat(jsonObject, "speed");

		AIFactory factory = (entity) -> new MoveToTroughGoal(entity, speed);
		aiContainer.getTask().after(SwimGoal.class)
		                     .before(IngredientTemptGoal.class)
		                     .before(EdibleItemTemptGoal.class)
		                     .before(EntityAIMoveToEatItem.class)
		                     .before(EntityAIMoveToEatBlock.class)
		                     .before(EntityAIFollowParent.class)
		                     .before(WaterAvoidingRandomWalkingGoal.class)
		                     .put(factory);
	}

}
