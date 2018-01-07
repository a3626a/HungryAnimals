package oortcloud.hungryanimals.entities.ai;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.AxisAlignedBB;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceManager;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreferenceSimple;

public class EntityAITarget extends EntityAINearestAttackableTarget<EntityLiving> {

	private ICapabilityHungryAnimal cap;
	private IFoodPreferenceSimple<EntityLiving> pref;
	private boolean herding;

	public EntityAITarget(EntityCreature creature, int chance, boolean checkSight, boolean onlyNearby, boolean herding) {
		super(creature, EntityLiving.class, chance, checkSight, onlyNearby, new Predicate<EntityLiving>() {
			@Override
			public boolean apply(EntityLiving input) {
				ICapabilityHungryAnimal cap = creature.getCapability(ProviderHungryAnimal.CAP, null);
				IFoodPreferenceSimple<EntityLiving> pref = FoodPreferenceManager.getInstance().REGISTRY_ENTITY.get(creature.getClass());

				// DON'T EAT BABY
				if (input instanceof EntityAgeable) {
					int age = ((EntityAgeable) input).getGrowingAge();
					if (age < 0)
						return false;
				}

				return pref.canEat(cap, input);
			}
		});
		pref = FoodPreferenceManager.getInstance().REGISTRY_ENTITY.get(creature.getClass());
		cap = creature.getCapability(ProviderHungryAnimal.CAP, null);
		this.herding = herding;
	}

	@Override
	public boolean shouldExecute() {
		return pref.shouldEat(cap) && super.shouldExecute();
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

}
