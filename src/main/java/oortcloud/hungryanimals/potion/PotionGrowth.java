package oortcloud.hungryanimals.potion;

import net.minecraft.entity.MobEntityBase;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.capability.ICapabilityAgeable;
import oortcloud.hungryanimals.entities.capability.ProviderAgeable;

public class PotionGrowth extends PotionHungryAnimals {

	public static ResourceLocation textureLocation = new ResourceLocation(References.MODID, "textures/potions/potiongrowth.png");

	protected PotionGrowth(int color) {
		super(textureLocation, false, color);
		setRegistryName(References.MODID, Strings.potionGrowthName);
		setPotionName(Strings.potionGrowthUnlocalizedName);
	}

	@Override
	public void performEffect(MobEntityBase entity, int level) {
		if (!entity.getEntityWorld().isRemote) {
			ICapabilityAgeable ageable = entity.getCapability(ProviderAgeable.CAP, null);
			if (ageable != null) {
				int j = ageable.getAge();
				if (j < 0) {
					j += (level) * entity.getRNG().nextInt(2);
					ageable.setAge(j);
				}
			}
		}
	}

	@Override
	public boolean isReady(int duration, int level) {
		return true;
	}

}
