package oortcloud.hungryanimals.potion;

import net.minecraft.entity.MobEntityBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.capability.ICapabilityAgeable;
import oortcloud.hungryanimals.entities.capability.ProviderAgeable;

public class PotionYoung extends PotionHungryAnimals {
	public static ResourceLocation textureLocation = new ResourceLocation(References.MODID, "textures/potions/potionyoung.png");
	
	protected PotionYoung(int potionColor) {
		super(textureLocation, true, potionColor);
		setRegistryName(References.MODID, Strings.potionYoungName);
		setPotionName(Strings.potionYoungUnlocalizedName);
		registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "e703c0b2-e5f2-11e7-80c1-9a214cf093ae", -0.5, 1);
		registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, "e703c0b2-e5f2-11e7-80c1-9a214cf093ae", -0.5, 1);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void performEffect(MobEntityBase entity, int amplifier) {
		ICapabilityAgeable ageable =  entity.getCapability(ProviderAgeable.CAP, null);
		if (ageable != null) {
			if (ageable.getAge() >= 0) {
				entity.removePotionEffect(this);
			}
		} else {
			entity.removePotionEffect(this);
		}
	}
}
