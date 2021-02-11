package oortcloud.hungryanimals.potion;

import net.minecraft.entity.MobEntityBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;

public class PotionOvereat extends PotionHungryAnimals {

	public static double multiplyMovementSpeed = -0.3;
	
	public static ResourceLocation textureLocation = new ResourceLocation(References.MODID, "textures/potions/potionovereat.png");
	
	protected PotionOvereat(int potionColor) {
		super(textureLocation, true, potionColor);
		setRegistryName(References.MODID, Strings.potionOvereatName);
		setPotionName(Strings.potionOvereatUnlocalizedName);
		registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "7327DE5E-7CE8-4030-940E-514C1F16089D", multiplyMovementSpeed, 1);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void performEffect(MobEntityBase entity, int amplifier) {
		ICapabilityHungryAnimal cap = entity.getCapability(ProviderHungryAnimal.CAP).orElse(null);
		if (cap == null)
			return;
		
		if (cap.getStomach() < cap.getMaxStomach()) {
			entity.removePotionEffect(this);
		}
	}

}
