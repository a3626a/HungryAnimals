package oortcloud.hungryanimals.entities.loot_tables;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;

import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.registries.ForgeRegistries;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.api.ILootTableRegistry;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;

/**
 * Loot Table Modification System Details. 
 * 1. It loads its built-in loot table inside Hungry Animals' datapack.
 *    These are called "Loot Overrides" and placed in ":modid/hungryanimals/loot_overrides"
 *    It uses custom loot manager to load.
 *    hungryanimals/loot_overrides/:domain/:key corresponds to loot_tables/:domain/:key.
 * 2. It injects loaded loot overrides to its corresponding loot tables. it follows these :
 *   1) This overrides requires formats for loot tables and loot overrides.
 *     (1) Each pool must have one entry.
 *     (2) Each entry must have 'item' type.
 *   2) For each loot overrides, it tries following :
 *     (1) Finds a corresponding loot table
 *     (2) For each pool of the loot overrides, gets the only entry of the table.
 *     (3) Finds an loot table entry which have same "name" with the loot overrides entry.
 *     (4) Replaces the loot table entry by the loot overrides entry.
 * 3. These overrides adds a new function "set_count_based_on_hunger".
 *    The function sets the stack size of the generated item stack based on the dead animal's weight.
 *
 * @author Oortcloud
 */

@Mod.EventBusSubscriber(Dist.DEDICATED_SERVER)
public class ModLootTables implements ILootTableRegistry, IResourceManagerReloadListener {

	private static Map<ResourceLocation, LootTable> tables = new HashMap<>();

	public static <T extends LootFunction> void register(LootFunction.Serializer<? extends T> serializer) {
		LootFunctionManager.registerFunction(serializer);
	}

	@Override
	public <T extends LootFunction> void registerFunction(LootFunction.Serializer<? extends T> serializer) {
		register(serializer);
	}

	@SubscribeEvent
	public static void FMLServerStartingEvent(FMLServerStartingEvent event) {
		event.getServer().getResourceManager().addReloadListener(new ModLootTables());
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		tables = new HashMap<>();
		for (ResourceLocation i : resourceManager.getAllResourceLocations("loot_overrides", s -> true)) {
			try {
				IResource iResource = resourceManager.getResource(i);
				JsonStreamParser reader = new JsonStreamParser(new InputStreamReader(iResource.getInputStream()));
				JsonElement jsonElement = reader.next();
				if (!(jsonElement instanceof JsonObject)) {
					HungryAnimals.logger.warn("cannot register loot table for {}.", i);
					continue;
				}
				JsonObject jsonObject = (JsonObject)jsonElement;

				LootTable table = ForgeHooks.loadLootTable(LootTableManager.GSON_INSTANCE, i, jsonObject, true, null);
				if (table != null) {
					tables.put(new ResourceLocation(i.getNamespace(), i.getPath()), table);
				} else {
					HungryAnimals.logger.warn("cannot register loot table for {}.", i);
				}
			} catch (IOException ignored) {
			}
		}
	}

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public void LootTableLoadEvent(LootTableLoadEvent event) throws IllegalArgumentException, IllegalAccessException {
		LootTable table = tables.get(event.getName());
		if (table == null) { 
			return;
		} else {
			if (event.getTable().isFrozen()) {
				HungryAnimals.logger.warn("Loot table for {} is frozen. Therefore, HungryAnimals can't modify it.", event.getName());
				return;
			}
		}

		// Disable loot override for non-hungry animals.
		boolean shouldOverride = false;
		ResourceLocation entityResource = new ResourceLocation(
				event.getName().getNamespace(),
				event.getName().getPath().replace("entities/", "")
		);
		EntityType<? extends Entity> entityType = RegistryObject.of(entityResource, ForgeRegistries.ENTITIES).get();
		if (entityType != null) {
			if (HungryAnimalManager.getInstance().isHungry(entityType)) {
				shouldOverride = true;
			}
		}

		for (LootPool i : table.pools) {
			List<LootEntry> iEntries = i.lootEntries;
			if (shouldOverride && iEntries.size() == 1) {
				LootEntry iEntry = iEntries.get(0);
				LootPool toRemove = null;
				if (iEntry instanceof ItemLootEntry) {
					for (LootPool j : event.getTable().pools) {
						List<LootEntry> jEntries = j.lootEntries;
						if (jEntries.size() == 1) {
							LootEntry jEntry = jEntries.get(0);
							if (jEntry instanceof ItemLootEntry) {
								if (((ItemLootEntry)iEntry).item == ((ItemLootEntry)jEntry).item) {
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
}