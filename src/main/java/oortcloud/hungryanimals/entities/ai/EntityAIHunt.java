package oortcloud.hungryanimals.entities.ai;

import com.google.common.base.Predicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.AxisAlignedBB;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferences;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreferenceSimple;

public class EntityAIHunt extends EntityAINearestAttackableTarget<EntityLiving> {

	private ICapabilityHungryAnimal cap;
	private IFoodPreferenceSimple<EntityLiving> pref;
	private boolean herding;

	public EntityAIHunt(EntityCreature creature, int chance, boolean checkSight, boolean onlyNearby, boolean herding) {
		super(creature, EntityLiving.class, chance, checkSight, onlyNearby, new Predicate<EntityLiving>() {
			@Override
			public boolean apply(EntityLiving input) {
				ICapabilityHungryAnimal cap = creature.getCapability(ProviderHungryAnimal.CAP, null);
				IFoodPreferenceSimple<EntityLiving> pref = FoodPreferences.getInstance().REGISTRY_ENTITY.get(creature.getClass());

				// DON'T EAT BABY
				if (input instanceof EntityAgeable) {
					int age = ((EntityAgeable) input).getGrowingAge();
					if (age < 0)
						return false;
				}

				return pref.canEat(cap, input);
			}
		});
		pref = FoodPreferences.getInstance().REGISTRY_ENTITY.get(creature.getClass());
		cap = creature.getCapability(ProviderHungryAnimal.CAP, null);
		this.herding = herding;
	}

	@Override
	public boolean shouldExecute() {
		if (!pref.shouldEat(cap))
			return false;
		if (super.shouldExecute())
			return false;
		
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

		for (EntityCreature entitycreature : this.taskOwner.world.getEntitiesWithinAABB(this.taskOwner.getClass(),
				(new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D,
						this.taskOwner.posZ + 1.0D)).grow(d0, 10.0D, d0))) {
			boolean isItself = this.taskOwner == entitycreature;
			boolean isBusy = entitycreature.getAttackTarget() == null;
			boolean isAlly = (this.taskOwner instanceof EntityTameable)
					&& ((EntityTameable) this.taskOwner).getOwner() == ((EntityTameable) entitycreature).getOwner();
			boolean isTeam = entitycreature.isOnSameTeam(this.taskOwner.getAttackTarget());
			if (!isItself && !isBusy && !isAlly && !isTeam) {
				this.setEntityAttackTarget(entitycreature, this.taskOwner.getAttackTarget());
			}
		}
	}

	protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn) {
		creatureIn.setAttackTarget(entityLivingBaseIn);
	}

	public static AIFactory parse(JsonElement jsonEle) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Target must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		
		JsonObject jsonObject = (JsonObject)jsonEle ;
		
		int chance = JsonUtils.getInt(jsonObject, "chance");
		boolean checkSight = JsonUtils.getBoolean(jsonObject, "check_sight");
		boolean onlyNearby = JsonUtils.getBoolean(jsonObject, "only_nearby");
		boolean herding = JsonUtils.getBoolean(jsonObject, "herding");
		return (entity) -> new EntityAIHunt(entity, chance, checkSight, onlyNearby, herding);
	}
	
}
