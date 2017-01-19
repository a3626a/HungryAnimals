package oortcloud.hungryanimals.potion;

import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class PotionGrowth extends PotionHungryAnimals {

	public static ResourceLocation textureLocation = new ResourceLocation(References.MODID, "textures/potions/potiongrowth.png");

	protected PotionGrowth(boolean effect, int color) {
		super(textureLocation, effect, color);
		this.setPotionName(Strings.potionGrowthName);
	}

	@Override
	public void performEffect(EntityLivingBase entity, int level) {
		
		EntityAgeable entityAgealbe = ((EntityAgeable) entity);
		
		if (entity.hasCapability(ProviderHungryAnimal.CAP_HUNGRYANIMAL, null)) {
			int j = entityAgealbe.getGrowingAge();
			if (j < 0) {
				j += (level) * entity.getRNG().nextInt(2);
				entityAgealbe.setGrowingAge(j);
			}
		}
	}

	@Override
	public boolean isReady(int duration, int level) {
		return true;
	}

}
