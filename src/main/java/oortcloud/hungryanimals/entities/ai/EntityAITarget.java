package oortcloud.hungryanimals.entities.ai;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceManager;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreferenceSimple;

public class EntityAITarget extends EntityAINearestAttackableTarget<EntityLiving> {

	private ICapabilityHungryAnimal cap;
	private IFoodPreferenceSimple<EntityLiving> pref;
	
	public EntityAITarget(EntityCreature creature, int chance, boolean checkSight,
			boolean onlyNearby) {
		super(creature, EntityLiving.class, chance, checkSight, onlyNearby, new Predicate<EntityLiving>() {
			@Override
			public boolean apply(EntityLiving input) {
				ICapabilityHungryAnimal cap = creature.getCapability(ProviderHungryAnimal.CAP, null);
				IFoodPreferenceSimple<EntityLiving> pref = FoodPreferenceManager.getInstance().REGISTRY_ENTITY.get(creature.getClass());
				return pref.canEat(cap, input);
			}
		});
		pref = FoodPreferenceManager.getInstance().REGISTRY_ENTITY.get(creature.getClass());
		cap = creature.getCapability(ProviderHungryAnimal.CAP, null);
	}
	
	@Override
	public boolean shouldExecute() {
		return pref.shouldEat(cap) && super.shouldExecute();
	}

}
