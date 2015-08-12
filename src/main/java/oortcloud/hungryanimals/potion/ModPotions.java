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
		int id = getEmptyID();
		if (id != -1) {
			potionDisease = new PotionDisease(id, true, (200<<16) + (60<<8) + (200));
		} else {
			expand();
			potionDisease = new PotionDisease(getEmptyID(), true, (200<<16) + (60<<8) + (200) );
		}
		
		id = getEmptyID();
		if (id != -1) {
			potionGrowth = new PotionGrowth(id, false, (100<<16) + (200<<8) + (0));
		} else {
			expand();
			potionGrowth = new PotionGrowth(getEmptyID(), false, (100<<16) + (200<<8) + (0));
		}
		
	}
	
	public static void expand() {
		 Potion[] potionTypes = null;

		    for (Field f : Potion.class.getDeclaredFields()) {
		        f.setAccessible(true);
		        try {
		            if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a")) {
		                Field modfield = Field.class.getDeclaredField("modifiers");
		                modfield.setAccessible(true);
		                modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);

		                potionTypes = (Potion[])f.get(null);
		                final Potion[] newPotionTypes = new Potion[2*Potion.potionTypes.length];
		                System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
		                f.set(null, newPotionTypes);
		            }
		        } catch (Exception e) {
				    FMLLog.log(Level.ERROR, "Error during register potion effects");
		            System.err.println(e);
		        }
		    }
	}
	
	
	public static int getEmptyID() {
		for (int i = 0 ; i < Potion.potionTypes.length; i++) {
			if (Potion.potionTypes[i] == null) return i;
		}
		return -1;
	}
}
