package oortcloud.hungryanimals.entities.ai;

import java.util.ArrayList;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.JsonUtils;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.capability.ICapabilityAgeable;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderAgeable;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferences;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreference;
import oortcloud.hungryanimals.potion.ModPotions;
import oortcloud.hungryanimals.utils.Tamings;

public class EntityAIMoveToEatItem extends Goal {

	private MobEntity entity;
	private World worldObj;
	private double speed;
	private EntityItem target;
	private boolean onlyNatural;

	private IFoodPreference<ItemStack> pref;
	private ICapabilityHungryAnimal capHungry;
	@Nullable
	private ICapabilityTamableAnimal capTaming;
	@Nullable
	private ICapabilityAgeable capAgeable;
	private int delayCounter;
	private static int delay = 100;

	private Predicate<EntityItem> EAT_EDIBLE = new Predicate<EntityItem>() {
		public boolean apply(@Nullable EntityItem entityIn) {
			if (entityIn == null)
				return false;
			return pref.canEat(capHungry, entityIn.getItem());
		}
	};
	private Predicate<EntityItem> EAT_NATURAL = new Predicate<EntityItem>() {
		public boolean apply(@Nullable EntityItem entityIn) {
			if (entityIn == null)
				return false;
			ItemStack item = entityIn.getItem();
			CompoundNBT tag = item.getTagCompound();
			if (tag != null) {
				return tag.hasKey("isNatural") && tag.getBoolean("isNatural");
			}
			return false;
		}
	};

	public EntityAIMoveToEatItem(MobEntity entity, double speed, boolean onlyNatural) {
		this.delayCounter = entity.getRNG().nextInt(delay);

		this.entity = entity;
		this.worldObj = this.entity.getEntityWorld();
		this.speed = speed;
		this.onlyNatural = onlyNatural;
		this.pref = FoodPreferences.getInstance().REGISTRY_ITEM.get(entity.getClass());
		this.capHungry = entity.getCapability(ProviderHungryAnimal.CAP, null);
		this.capTaming = entity.getCapability(ProviderTamableAnimal.CAP, null);
		this.capAgeable = entity.getCapability(ProviderAgeable.CAP, null);
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (capHungry == null)
			return false;

		if (!pref.shouldEat(capHungry))
			return false;

		if (this.delayCounter > 0) {
			--this.delayCounter;
			return false;
		} else {
			float radius = 16.0F;

			ArrayList<EntityItem> list = (ArrayList<EntityItem>) worldObj.getEntitiesWithinAABB(EntityItem.class,
					entity.getEntityBoundingBox().grow(radius), Predicates.and(EAT_EDIBLE, EAT_NATURAL));
			if (!list.isEmpty()) {
				this.target = list.get(0);
				return true;
			}

			if (!onlyNatural) {
				if (entity.getRNG().nextInt(executeProbability()) == 0) {
					list = (ArrayList<EntityItem>) worldObj.getEntitiesWithinAABB(EntityItem.class,
							entity.getEntityBoundingBox().grow(radius), EAT_EDIBLE);
					if (!list.isEmpty()) {
						this.target = list.get(0);
						return true;
					}
				}
			}
			
			return false;
		}
	}

	@Override
	public void startExecuting() {
		this.entity.getNavigator().tryMoveToMobEntity(target, speed);
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (target.isDead) {
			this.entity.getNavigator().clearPath();
			return false;
		}
		if (entity.getNavigator().noPath()) {
			if (entity.getEntityBoundingBox().grow(0.5).intersects(target.getEntityBoundingBox())) {
				ItemStack foodStack = target.getItem();
				while (!foodStack.isEmpty() && pref.canEat(capHungry, foodStack)) {
					// This Code Run At Most 64
					this.eatFoodBonus(foodStack);
					foodStack.shrink(1);
				}
				if (foodStack.isEmpty()) {
					target.setDead();
				}
			}
			return false;
		}

		return true;
	}

	@Override
	public void resetTask() {
		this.target = null;
		this.delayCounter = delay;
	}

	private int executeProbability() {
		double taming = Tamings.get(capTaming);
		double hunger = capHungry.getStomach() / capHungry.getMaxStomach();
		if (taming > 1) {
			taming = 1;
		}
		if (taming < -1) {
			taming = -1;
		}
		return (int) (200 * (taming - 1) * (taming - 1) * hunger) + 1;
	}

	private void eatFoodBonus(ItemStack item) {
		if (item.isEmpty())
			return;

		double nutrient = pref.getNutrient(item);
		capHungry.addNutrient(nutrient);

		double stomach = pref.getStomach(item);
		capHungry.addStomach(stomach);

		if (capAgeable != null && capAgeable.getAge() < 0) {
			CompoundNBT tag = item.getTagCompound();
			if (tag == null || !tag.hasKey("isNatural") || !tag.getBoolean("isNatural")) {
				int duration = (int) (nutrient
						/ entity.getEntityAttribute(ModAttributes.hunger_weight_bmr).getAttributeValue());
				entity.addPotionEffect(new PotionEffect(ModPotions.potionGrowth, duration, 1));
			}
		}

		CompoundNBT tag = item.getTagCompound();
		if (tag == null || !tag.hasKey("isNatural") || !tag.getBoolean("isNatural")) {
			double taming_factor = entity.getEntityAttribute(ModAttributes.taming_factor_food).getAttributeValue();
			if (capTaming != null) {
				capTaming.addTaming(taming_factor
						/ entity.getEntityAttribute(ModAttributes.hunger_weight_bmr).getAttributeValue() * nutrient);
			}
		}
	}

	public static void parse(JsonElement jsonEle, AIContainer aiContainer) {
		if (!(jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Eat Item must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}

		JsonObject jsonObject = (JsonObject) jsonEle;

		float speed = JsonUtils.getFloat(jsonObject, "speed");
		boolean onlyNatural = JsonUtils.getBoolean(jsonObject, "only_natural", false);
		AIFactory factory = (entity) -> new EntityAIMoveToEatItem(entity, speed, onlyNatural);
		aiContainer.getTask().after(EntityAISwimming.class).before(EntityAIMoveToEatBlock.class)
				.before(EntityAIFollowParent.class).before(EntityAIWanderAvoidWater.class).put(factory);
	}
}
