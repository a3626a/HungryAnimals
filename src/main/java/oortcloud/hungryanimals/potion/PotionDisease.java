package oortcloud.hungryanimals.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;
import oortcloud.hungryanimals.entities.properties.handler.ModAttributes;

public class PotionDisease extends PotionHungryAnimals {

	public static ResourceLocation textureLocation = new ResourceLocation(References.MODID, "textures/potions/potiondisease.png");

	protected PotionDisease(boolean effect, int color) {
		super(textureLocation, effect, color);
		this.setPotionName(Strings.potionDiseaseName);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F16089D", -0.25, 2);
	}

	@Override
	public void performEffect(EntityLivingBase entity, int level) {
		ExtendedPropertiesHungryAnimal property = (ExtendedPropertiesHungryAnimal) entity.getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null)
			property.subHunger(entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_bmr).getAttributeValue() * 4);
	}

	@Override
	public boolean isReady(int duration, int level) {
		return true;
	}

}
