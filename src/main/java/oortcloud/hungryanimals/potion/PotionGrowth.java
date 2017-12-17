	package oortcloud.hungryanimals.potion;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;

public class PotionGrowth extends PotionHungryAnimals {

	public static ResourceLocation textureLocation = new ResourceLocation(References.MODID, "textures/potions/potiongrowth.png");

	protected PotionGrowth(boolean effect, int color) {
		super(textureLocation, effect, color);
		this.setRegistryName(References.MODID, Strings.potionGrowthName);
		this.setPotionName(Strings.potionGrowthUnlocalizedName);
	}

	@Override
	public void performEffect(EntityLivingBase entity, int level) {
		
		EntityAgeable entityAgealbe = ((EntityAgeable) entity);
		
		if (entity.hasCapability(ProviderHungryAnimal.CAP, null)) {
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
