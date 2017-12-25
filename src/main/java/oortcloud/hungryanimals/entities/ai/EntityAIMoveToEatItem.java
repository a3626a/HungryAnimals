package oortcloud.hungryanimals.entities.ai;

import java.util.ArrayList;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceManager;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreference;
import oortcloud.hungryanimals.potion.ModPotions;

public class EntityAIMoveToEatItem extends EntityAIBase {

	private EntityAgeable entity;
	private World worldObj;
	private double speed;
	private EntityItem target;

	private IFoodPreference<ItemStack> pref;
	private ICapabilityHungryAnimal capHungry;
	private ICapabilityTamableAnimal capTaming;
	private int delayCounter;
	private static int delay = 100;

	private Predicate<EntityItem> EAT_EDIBLE = new Predicate<EntityItem>() {
		public boolean apply(EntityItem entityIn) {
			return pref.canEat(capHungry, entityIn.getItem());
		}
	};
	private Predicate<EntityItem> EAT_NATURAL = new Predicate<EntityItem>() {
		public boolean apply(EntityItem entityIn) {
			ItemStack item = entityIn.getItem();
			NBTTagCompound tag = item.getTagCompound();
			if (tag != null) {
				return tag.hasKey("isNatural") && tag.getBoolean("isNatural");
			}
			return false;
		}
	};

	public EntityAIMoveToEatItem(EntityAgeable entity, double speed) {
		this.delayCounter = entity.getRNG().nextInt(delay);

		this.entity = entity;
		this.worldObj = this.entity.getEntityWorld();
		this.speed = speed;
		this.pref = FoodPreferenceManager.getInstance().REGISTRY_ITEM.get(entity.getClass());
		this.capHungry = entity.getCapability(ProviderHungryAnimal.CAP, null);
		this.capTaming = entity.getCapability(ProviderTamableAnimal.CAP, null);
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (!pref.shouldEat(capHungry))
			return false;

		if (this.delayCounter > 0) {
			--this.delayCounter;
			return false;
		} else {
			float radius = 16.0F;
			ArrayList<EntityItem> list = (ArrayList<EntityItem>) worldObj.getEntitiesWithinAABB(EntityItem.class,
					entity.getEntityBoundingBox().expand(radius, radius, radius), Predicates.and(EAT_EDIBLE, EAT_NATURAL));
			if (!list.isEmpty()) {
				this.target = list.get(0);
				return true;
			}
			if (entity.getRNG().nextInt(executeProbability()) != 0) {
				return false;
			}

			list = (ArrayList<EntityItem>) worldObj.getEntitiesWithinAABB(EntityItem.class, entity.getEntityBoundingBox().expand(radius, radius, radius),
					EAT_EDIBLE);
			if (!list.isEmpty()) {
				this.target = list.get(0);
				return true;
			}
			return false;
		}
	}

	@Override
	public void startExecuting() {
		this.entity.getNavigator().tryMoveToEntityLiving(target, speed);
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (target.isDead) {
			this.entity.getNavigator().clearPath();
			return false;
		}
		if (entity.getNavigator().noPath()) {
			float distanceSq = 2;
			if (entity.getPosition().distanceSq(target.getPosition()) < distanceSq) {
				ItemStack foodStack = target.getItem();
				foodStack.shrink(1);
				if (foodStack.isEmpty())
					target.setDead();
				this.eatFoodBonus(target.getItem());
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
		double taming = capTaming.getTaming();
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

		IFoodPreference<ItemStack> pref = FoodPreferenceManager.getInstance().REGISTRY_ITEM.get(entity.getClass());

		double nutrient = pref.getNutrient(item);
		capHungry.addNutrient(nutrient);
		
		double stomach = pref.getStomach(item);
		capHungry.addStomach(stomach);
		
		if (this.entity.getGrowingAge() < 0) {
			NBTTagCompound tag = item.getTagCompound();
			if (tag == null || !tag.hasKey("isNatural") || !tag.getBoolean("isNatural")) {
				int duration = (int) (nutrient / entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_weight_bmr).getAttributeValue());
				this.entity.addPotionEffect(new PotionEffect(ModPotions.potionGrowth, duration, 1));
			}
		}

		NBTTagCompound tag = item.getTagCompound();
		if (tag == null || !tag.hasKey("isNatural") || !tag.getBoolean("isNatural")) {
			this.capTaming.addTaming(0.0001 / entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_weight_bmr).getAttributeValue() * nutrient);
		}
	}
}
