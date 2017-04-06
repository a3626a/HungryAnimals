package oortcloud.hungryanimals.entities.ai;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceManager;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreference;

public class EntityAITemptEdibleItem extends EntityAITempt {
	/** The entity using this AI that is tempted by the player. */
	private final EntityCreature temptedEntity;
	private IFoodPreference<ItemStack> pref;
	/** Hunger Value */
	private ICapabilityHungryAnimal capHungry;

	public EntityAITemptEdibleItem(EntityCreature temptedEntityIn, double speedIn, boolean scaredByPlayerMovementIn) {
		super(temptedEntityIn, speedIn, scaredByPlayerMovementIn, null);
		
		this.temptedEntity = temptedEntityIn;
		this.pref = FoodPreferenceManager.getInstance().REGISTRY_ITEM.get(this.temptedEntity.getClass());
		this.capHungry = temptedEntityIn.getCapability(ProviderHungryAnimal.CAP, null);
	}

	@Override
	public boolean shouldExecute() {
		return super.shouldExecute() && temptedEntity.getCapability(ProviderTamableAnimal.CAP, null).getTaming() < 1;
	}

	protected boolean isTempting(@Nullable ItemStack stack) {
		return stack == null ? false : pref.canEat(capHungry, stack);
	}
}
