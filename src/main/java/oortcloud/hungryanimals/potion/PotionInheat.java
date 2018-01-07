package oortcloud.hungryanimals.potion;

import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;

public class PotionInheat extends PotionHungryAnimals {
	public static ResourceLocation textureLocation = new ResourceLocation(References.MODID, "textures/potions/potioninheat.png");

	protected PotionInheat(int color) {
		super(textureLocation, false, color);
		setRegistryName(References.MODID, Strings.potionInheatName);
		setPotionName(Strings.potionInheatUnlocalizedName);
		registerPotionAttributeModifier(ModAttributes.courtship_probability, "e703c0b2-e5f0-11e7-80c1-9a214cf093ae", +0.0025, 0);
	}

	@Override
	public boolean isReady(int duration, int level) {
		return true;
	}
	
}
