package oortcloud.hungryanimals.potion;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.FMLLog;

import org.apache.logging.log4j.Level;

public class ModPotions {

	public static Potion potionDisease;
	public static Potion potionGrowth;

	public static void init() {
		potionDisease = new PotionDisease(true, (200 << 16) + (60 << 8) + (200));
		potionGrowth = new PotionGrowth(false, (100 << 16) + (200 << 8) + (0));
	}

}
