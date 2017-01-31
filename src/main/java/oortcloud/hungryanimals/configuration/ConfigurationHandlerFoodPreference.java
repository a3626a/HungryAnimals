package oortcloud.hungryanimals.configuration;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.JsonParseException;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.properties.handler.HungryAnimalManager;

public class ConfigurationHandlerFoodPreference {

	public static void init(File directory) {
		if (!directory.exists())
			directory.mkdir();
		
		for (Class<? extends EntityAnimal> i : HungryAnimalManager.getInstance().getRegisteredAnimal()) {
			File iFile = new File(directory, EntityList.CLASS_TO_NAME.get(i)+".json");
			
			if (!directory.exists()) {
				try {
					directory.createNewFile();
				} catch (IOException e) {
					HungryAnimals.logger.error(e.getMessage());
				}
			}
		}
	}

	public static void createDefaultConfigurationFile() {
		URL url = ConfigurationHandlerFoodPreference.class.getResource("/assets/" + References.MODID + "/food_preferences/" + resource.getResourcePath() + ".json");

        if (url != null)
        {
            String s;

            try
            {
                s = Resources.toString(url, Charsets.UTF_8);
            }
            catch (IOException ioexception)
            {
                LootTableManager.LOGGER.warn("Couldn\'t load loot table {} from {}", new Object[] {resource, url, ioexception});
                return LootTable.EMPTY_LOOT_TABLE;
            }

            try
            {
                return net.minecraftforge.common.ForgeHooks.loadLootTable(LootTableManager.GSON_INSTANCE, resource, s, false);
            }
            catch (JsonParseException jsonparseexception)
            {
                LootTableManager.LOGGER.error("Couldn\'t load loot table {} from {}", new Object[] {resource, url, jsonparseexception});
                return LootTable.EMPTY_LOOT_TABLE;
            }
        }
        else
        {
            return null;
        }
	}
	
	public static void sync() {
		
	}
	
}
