package oortcloud.hungryanimals.entities.ai;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
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

public class HuntGoal extends NearestAttackableTargetGoal<MobEntity> {

	private ICapabilityHungryAnimal cap;
	private IFoodPreferenceSimple<MobEntity> pref;
	private boolean herding;
    private int delay;
	
	public HuntGoal(CreatureEntity creature, int chance, boolean checkSight, boolean onlyNearby, boolean herding) {
		super(creature, MobEntity.class, chance, checkSight, onlyNearby, livingEntity -> {
			if (!(livingEntity instanceof MobEntity))
				return false;

			MobEntity mobEntity = (MobEntity)livingEntity;

			ICapabilityHungryAnimal cap = creature.getCapability(ProviderHungryAnimal.CAP).orElse(null);
			IFoodPreferenceSimple<MobEntity> pref = FoodPreferences.getInstance().getRegistryEntity().get(creature.getClass());

			// DON'T EAT BABY
			ICapabilityAgeable ageable = mobEntity.getCapability(ProviderAgeable.CAP).orElse(null);
			if (ageable != null) {
				int age = ageable.getAge();
				if (age < 0)
					return false;
			}

			return pref.canEat(cap, mobEntity);
		});
		pref = FoodPreferences.getInstance().getRegistryEntity().get(creature.getClass());
		cap = creature.getCapability(ProviderHungryAnimal.CAP).orElse(null);
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

		for (MobEntity entitycreature : this.goalOwner.world.getEntitiesWithinAABB(this.goalOwner.getClass(),
				(new AxisAlignedBB(this.goalOwner.posX, this.goalOwner.posY, this.goalOwner.posZ, this.goalOwner.posX + 1.0D, this.goalOwner.posY + 1.0D,
						this.goalOwner.posZ + 1.0D)).grow(d0, 10.0D, d0))) {
			boolean isItself = this.goalOwner == entitycreature;
			boolean isBusy = entitycreature.getAttackTarget() == null;
			boolean isAlly = (this.goalOwner instanceof TameableEntity)
					&& ((TameableEntity) this.goalOwner).getOwner() == ((TameableEntity) entitycreature).getOwner();
			boolean isTeam = entitycreature.isOnSameTeam(this.goalOwner.getAttackTarget());
			if (!isItself && !isBusy && !isAlly && !isTeam) {
				this.setEntityAttackTarget(entitycreature, this.goalOwner.getAttackTarget());
			}
		}
	}

	protected void setEntityAttackTarget(MobEntity creatureIn, LivingEntity MobEntityBaseIn) {
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
				return new HuntGoal((CreatureEntity) entity, chance, checkSight, onlyNearby, herding);
			} else {
				HungryAnimals.logger.error("Animals which uses AI Hunt must extend CreatureEntity. {} don't.", entity.getType().getRegistryName());
				return null;
			}
		};
		aiContainer.getTarget().putLast(factory);
	}
	
}
