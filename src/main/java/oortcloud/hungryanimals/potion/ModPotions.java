package oortcloud.hungryanimals.potion;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ModPotions {

	public static Potion potionDisease;
	public static Potion potionGrowth;
	public static Potion potionInheat;
	
	public static void init() {
		potionDisease = new PotionDisease(true, (200 << 16) + (60 << 8) + (200));
		potionGrowth = new PotionGrowth(false, (100 << 16) + (200 << 8) + (0));
		potionInheat = new PotionInheat(false, (255 << 16) + (50 << 8) + (50));
	}
	
	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> event) {
	    event.getRegistry().registerAll(potionDisease, potionGrowth, potionInheat);
	}

}
