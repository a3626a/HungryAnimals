package oortcloud.hungryanimals.potion;

import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModPotions {

	public static Potion potionDisease;
	public static Potion potionGrowth;

	public static void init() {
		potionDisease = new PotionDisease(true, (200 << 16) + (60 << 8) + (200));
		potionGrowth = new PotionGrowth(false, (100 << 16) + (200 << 8) + (0));
		
		// TODO Check register name naming convention. potion.disease...?
		GameRegistry.register(potionDisease.setRegistryName(potionDisease.getName()));
		GameRegistry.register(potionGrowth.setRegistryName(potionGrowth.getName()));
	}

}
