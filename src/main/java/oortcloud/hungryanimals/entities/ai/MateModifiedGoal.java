package oortcloud.hungryanimals.entities.ai;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.Stats;
import net.minecraft.util.JSONUtils;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilitySexual;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderSexual;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.utils.Tamings;

public class MateModifiedGoal extends Goal {
	private AnimalEntity animal;
	@Nullable
	private ICapabilityHungryAnimal theAnimalCapHungry;
	@Nullable
	private ICapabilityTamableAnimal theAnimalCapTamable;
	World theWorld;
	private AnimalEntity targetMate;
	/** Delay preventing a baby from spawning immediately when two mate-able animals find each other. */
	int spawnBabyDelay;
	/** The speed the creature moves at during mating behavior. */
	double moveSpeed;

	public MateModifiedGoal(AnimalEntity animal, double speed) {
		this.animal = animal;
		this.theWorld = animal.getEntityWorld();
		this.moveSpeed = speed;
		this.theAnimalCapHungry = animal.getCapability(ProviderHungryAnimal.CAP).orElse(null);
		this.theAnimalCapTamable = animal.getCapability(ProviderTamableAnimal.CAP).orElse(null);
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		if (!this.animal.isInLove()) {
			return false;
		} else {
			this.targetMate = this.getNearbyMate();
			return this.targetMate != null;
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.targetMate.isAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
	}

	@Override
	public void resetTask() {
		this.targetMate = null;
		this.spawnBabyDelay = 0;
	}

	@Override
	public void tick() {
		this.animal.getLookController().setLookPositionWithEntity(this.targetMate, 10.0F, (float) this.animal.getVerticalFaceSpeed());
		this.animal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
		++this.spawnBabyDelay;
		if (this.spawnBabyDelay >= 60 && this.animal.getDistanceSq(this.targetMate) < 9.0D) {
			this.spawnBaby();
		}
	}

	private AnimalEntity getNearbyMate() {
		List<AnimalEntity> list = this.theWorld.getEntitiesWithinAABB(this.animal.getClass(), this.animal.getBoundingBox().grow(8.0D));
		double d0 = Double.MAX_VALUE;
		AnimalEntity entityanimal = null;

		ICapabilitySexual sexual = animal.getCapability(ProviderSexual.CAP).orElse(null);

		if (sexual != null) {
			for (AnimalEntity entityanimal1 : list) {
				if (this.animal.canMateWith(entityanimal1)) {
					ICapabilitySexual sexual1 = entityanimal1.getCapability(ProviderSexual.CAP).orElse(null);
					if (sexual1 != null && sexual.getSex() != sexual1.getSex() && this.animal.getDistanceSq(entityanimal1) < d0) {
						entityanimal = entityanimal1;
						d0 = this.animal.getDistanceSq(entityanimal1);
					}
				}
			}
		} else {
			for (AnimalEntity entityanimal1 : list) {
				if (this.animal.canMateWith(entityanimal1) && !entityanimal1.getCapability(ProviderSexual.CAP).isPresent()
						&& this.animal.getDistanceSq(entityanimal1) < d0) {
					entityanimal = entityanimal1;
					d0 = this.animal.getDistanceSq(entityanimal1);
				}
			}
		}
		return entityanimal;
	}

	private static double calculateBabyTaming(ICapabilityTamableAnimal parent1, ICapabilityTamableAnimal parent2) {
		return (Tamings.get(parent1) + Tamings.get(parent2)) / 2.0;
	}

	/**
	 * Spawns a baby animal of the same type.
	 */
	private void spawnBaby() {
		// Get Capability
		ICapabilityHungryAnimal targetMateCapHungry = this.targetMate.getCapability(ProviderHungryAnimal.CAP).orElse(null);
		ICapabilityTamableAnimal targetMateCapTamable = this.targetMate.getCapability(ProviderTamableAnimal.CAP).orElse(null);

		// Create Child 1
		AgeableEntity entityageable = this.animal.createChild(this.targetMate);

		// Check Validity
		boolean createChildDeclared = false;
		try {
			Method createChild = animal.getClass().getDeclaredMethod("createChild", AgeableEntity.class);
			if (createChild != null)
				createChildDeclared = true;
		} catch (NoSuchMethodException | SecurityException ignored) {
		}

		// Create Child 2
		if (!createChildDeclared || entityageable == null) {
			entityageable = createChild();
		}

		if (entityageable != null) {
			ICapabilityTamableAnimal childTamable = entityageable.getCapability(ProviderTamableAnimal.CAP).orElse(null);

			// Pay Hunger
			double weight_child = entityageable.getAttribute(ModAttributes.HUNGER_WEIGHT_NORMAL_CHILD.get().attribute).getValue();
			if (targetMateCapHungry != null) {
				targetMateCapHungry.addWeight(-weight_child / 2);
			}
			if (theAnimalCapHungry != null) {
				theAnimalCapHungry.addWeight(-weight_child / 2);
			}

			double childTaming = calculateBabyTaming(theAnimalCapTamable, targetMateCapTamable);
			if (childTamable != null) {
				childTamable.setTaming(childTaming);
			}

			ServerPlayerEntity entityplayermp = this.animal.getLoveCause();

			if (entityplayermp == null && this.targetMate.getLoveCause() != null) {
				entityplayermp = this.targetMate.getLoveCause();
			}

			if (entityplayermp != null) {
				entityplayermp.addStat(Stats.ANIMALS_BRED);
				CriteriaTriggers.BRED_ANIMALS.trigger(entityplayermp, this.animal, this.targetMate, entityageable);
			}

			int animalDelay = (int) animal.getAttribute(ModAttributes.CHILD_DELAY.get().attribute).getValue();
			int targetMateDelay = (int) targetMate.getAttribute(ModAttributes.CHILD_DELAY.get().attribute).getValue();
			int childGrowingLength = (int) entityageable.getAttribute(ModAttributes.CHILD_GROWING_LENGTH.get().attribute).getValue();
			this.animal.setGrowingAge(animalDelay);
			this.targetMate.setGrowingAge(targetMateDelay);
			this.animal.resetInLove();
			this.targetMate.resetInLove();
			entityageable.setGrowingAge(-childGrowingLength);
			entityageable.setLocationAndAngles(this.animal.posX, this.animal.posY, this.animal.posZ, 0.0F, 0.0F);
			this.theWorld.addEntity(entityageable);
			this.theWorld.setEntityState(this.animal, (byte)18);
			if (this.theWorld.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
				this.theWorld.addEntity(new ExperienceOrbEntity(this.theWorld, this.animal.posX, this.animal.posY, this.animal.posZ, this.animal.getRNG().nextInt(7) + 1));
			}
		} else {
			this.animal.resetInLove();
			this.targetMate.resetInLove();
		}
	}

	public AnimalEntity createChild() {
		Constructor<? extends AnimalEntity> constructor;
		try {
			constructor = animal.getClass().getConstructor(World.class);
			AnimalEntity baby;
			try {
				baby = (AnimalEntity) constructor.newInstance(theWorld);
				return baby;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void parse(JsonElement jsonEle, AIContainer aiContainer) {
		if (!(jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Mate must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}

		JsonObject jsonObject = (JsonObject) jsonEle;

		float speed = JSONUtils.getFloat(jsonObject, "speed");

		AIFactory factory = (entity) -> {
			if (entity instanceof CreatureEntity) {
				return new MateModifiedGoal((AnimalEntity) entity, speed);
			} else {
				HungryAnimals.logger.error("Animals which uses AI Mate must extend AnimalEntity. {} don't.", entity.getType().getRegistryName());
				return null;
			}
		};
		aiContainer.getTask().after(SwimGoal.class).before(MoveToTroughGoal.class).before(IngredientTemptGoal.class)
				.before(EdibleItemTemptGoal.class).before(MoveToEatItemGoal.class).before(MoveToEatBlockGoal.class)
				.before(FollowParentGoal.class).before(WaterAvoidingRandomWalkingGoal.class).put(factory);
	}
}