package oortcloud.hungryanimals.entities.ai;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MobEntityBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.AxisAlignedBB;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;
import oortcloud.hungryanimals.entities.capability.ICapabilityAgeable;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderAgeable;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferences;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreferenceSimple;

public class EntityAIHunt extends EntityAINearestAttackableTarget<MobEntity> {

	private ICapabilityHungryAnimal cap;
	private IFoodPreferenceSimple<MobEntity> pref;
	private boolean herding;
    private int delay;
	
	public EntityAIHunt(CreatureEntity creature, int chance, boolean checkSight, boolean onlyNearby, boolean herding) {
		super(creature, MobEntity.class, chance, checkSight, onlyNearby, new Predicate<MobEntity>() {
			@Override
			public boolean apply(@Nullable MobEntity input) {
				ICapabilityHungryAnimal cap = creature.getCapability(ProviderHungryAnimal.CAP, null);
				IFoodPreferenceSimple<MobEntity> pref = FoodPreferences.getInstance().getRegistryEntity().get(creature.getClass());

				if (input == null)
					return false;
				
				// DON'T EAT BABY
				ICapabilityAgeable ageable = input.getCapability(ProviderAgeable.CAP, null);
				if (ageable != null) {
					int age = ageable.getAge();
					if (age < 0)
						return false;
				}

				return pref.canEat(cap, input);
			}
		});
		pref = FoodPreferences.getInstance().getRegistryEntity().get(creature.getClass());
		cap = creature.getCapability(ProviderHungryAnimal.CAP, null);
		this.herding = herding;
	}

	@Override
	public boolean shouldExecute() {
		if (cap == null)
			return false;

		if (delay > 0) {
			delay--;
			return false;
		}
		
		if (!pref.shouldEat(cap))
			return false;
		if (!super.shouldExecute())
			return false;

		delay = 200;
		
		return true;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();

		if (herding) {
			this.alertOthers();
		}
	}

	protected void alertOthers() {
		double d0 = this.getTargetDistance();

		for (CreatureEntity entitycreature : this.taskOwner.world.getEntitiesWithinAABB(this.taskOwner.getClass(),
				(new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D,
						this.taskOwner.posZ + 1.0D)).grow(d0, 10.0D, d0))) {
			boolean isItself = this.taskOwner == entitycreature;
			boolean isBusy = entitycreature.getAttackTarget() == null;
			boolean isAlly = (this.taskOwner instanceof TameableEntity)
					&& ((TameableEntity) this.taskOwner).getOwner() == ((TameableEntity) entitycreature).getOwner();
			boolean isTeam = entitycreature.isOnSameTeam(this.taskOwner.getAttackTarget());
			if (!isItself && !isBusy && !isAlly && !isTeam) {
				this.setEntityAttackTarget(entitycreature, this.taskOwner.getAttackTarget());
			}
		}
	}

	protected void setEntityAttackTarget(CreatureEntity creatureIn, MobEntityBase MobEntityBaseIn) {
		creatureIn.setAttackTarget(MobEntityBaseIn);
	}

	public static void parse(JsonElement jsonEle, AIContainer aiContainer) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Target must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		
		JsonObject jsonObject = (JsonObject)jsonEle ;
		
		int chance = JSONUtils.getInt(jsonObject, "chance");
		boolean checkSight = JSONUtils.getBoolean(jsonObject, "check_sight");
		boolean onlyNearby = JSONUtils.getBoolean(jsonObject, "only_nearby");
		boolean herding = JSONUtils.getBoolean(jsonObject, "herding");
		
		AIFactory factory = (entity) -> {
			if (entity instanceof CreatureEntity) {
				return new EntityAIHunt((CreatureEntity) entity, chance, checkSight, onlyNearby, herding);
			} else {
				HungryAnimals.logger.error("Animals which uses AI Hunt must extend CreatureEntity. {} don't.", EntityList.getKey(entity));
				return null;
			}
		};
		aiContainer.getTarget().putLast(factory);
	}
	
}
