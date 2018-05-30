package oortcloud.hungryanimals.entities.loot_tables;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunction.Serializer;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.api.ILootTableRegistry;

/**
 * Loot Table Modification System Details. 
 * 1. It loads its built-in loot table by custom loot manager. Therefore, loot tables can not be modified by JSONs in world directory.
 * 2. It injects loaded built-in loot tables to its corresponding vanilla loot tables. it follows these : 
 * 1) This modifier requires formats for target/built-in loot tables.
 * (1) Each pool must have one entry.
 * (2) Each entry must have 'item' type.
 * 2) For each built-in loot tables, this modifier tries following :
 * (1) Finds a corresponding vanilla loot table 
 * (2) For each pool of the built-in loot table, gets the only entry of the table.
 * (3) Finds an vanilla entry which have same "name" with the built-in entry.
 * (4) Replaces the vanilla entry by the built-in entry.
 * 3. This modifier adds a new function "set_count_based_on_hunger". The function sets the stack size of the generated item stack based on the dead animal's hunger.
 * 4. If a user makes loot table json files for vanilla loot tables, this modification is skipped.
 * 5. Furthermore, it is essential for the user to modify and to add json files for the configuration purpose.
 * I removed old(-1.8.9) configurations system for mob-drop.
 * The user also have to make json files for each user registered animals from other mods.
 * 
 * @author Oortcloud
 */

public class ModLootTables implements ILootTableRegistry {

	private static Gson GSON_INSTANCE;
	
	private static Map<ResourceLocation, LootTable> tables;

	private static final Field pools = ReflectionHelper.findField(LootTable.class, "pools", "field_186466_c");
	private static final Field lootEntries = ReflectionHelper.findField(LootPool.class, "lootEntries", "field_186453_a");

	public static void init(Path path) {
		tables = new HashMap<ResourceLocation, LootTable>();
		try {
			GSON_INSTANCE = (Gson) ReflectionHelper.findField(LootTableManager.class, "GSON_INSTANCE", "field_186526_b").get(null);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			HungryAnimals.logger.error("Java Reflection problem occured at ModLootTables. Please report this to author(oortcloud)");
			e.printStackTrace();
		}
	}

	public static <T extends LootFunction> void register(LootFunction.Serializer<? extends T> serializer) {
		LootFunctionManager.registerFunction(serializer);
	}

	@SuppressWarnings("unchecked")
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void LootTableLoadEventCancel(LootTableLoadEvent event) throws IllegalArgumentException, IllegalAccessException {
		LootTable table = tables.get(event.getName());
		if (table == null) {
			return;
		}

		for (LootPool i : (List<LootPool>) pools.get(table)) {
			List<LootEntry> iEntries = (List<LootEntry>) lootEntries.get(i);
			if (iEntries.size() == 1) {
				LootEntry iEntry = iEntries.get(0);
				LootPool toRemove = null;
				if (iEntry instanceof LootEntryItem) {
					for (LootPool j : (List<LootPool>) pools.get(event.getTable())) {
						List<LootEntry> jEntries = (List<LootEntry>) lootEntries.get(j);
						if (jEntries.size() == 1) {
							LootEntry jEntry = jEntries.get(0);
							if (jEntry instanceof LootEntryItem) {
								if (iEntry.getEntryName().equals(jEntry.getEntryName())) {
									toRemove = j;
									break;
								}
							}
						}
					}
				}
				if (toRemove != null) {
					event.getTable().removePool(toRemove.getName());
				}
			}
			event.getTable().addPool(i);
		}
	}

	@Override
	public <T extends LootFunction> void registerFunction(Serializer<? extends T> serializer) {
		register(serializer);
	}
	
	public static void register(ResourceLocation resource, JsonElement jsonElement) {
		LootTable table = ForgeHooks.loadLootTable(GSON_INSTANCE, resource, jsonElement.toString(), true, null);
		if (table != null) {
			resource = new ResourceLocation(resource.getResourceDomain(), "entities/"+resource.getResourcePath());
			tables.put(resource, table);
		} else {
			HungryAnimals.logger.warn("cannot register loot table for {}.", resource);
		}
	}
	
}