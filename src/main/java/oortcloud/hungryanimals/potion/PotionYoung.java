package oortcloud.hungryanimals.potion;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;

public class PotionYoung extends PotionHungryAnimals {
	public static ResourceLocation textureLocation = new ResourceLocation(References.MODID, "textures/potions/potionyoung.png");
	
	protected PotionYoung(int potionColor) {
		super(textureLocation, true, potionColor);
		setRegistryName(References.MODID, Strings.potionYoungName);
		setPotionName(Strings.potionYoungUnlocalizedName);
		registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "e703c0b2-e5f2-11e7-80c1-9a214cf093ae", -0.5, 1);
		registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, "e703c0b2-e5f2-11e7-80c1-9a214cf093ae", -0.5, 1);
	}

}
