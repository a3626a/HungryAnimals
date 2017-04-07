package oortcloud.hungryanimals.entities.loot_tables;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oortcloud.hungryanimals.core.lib.References;

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
 * @author LeeChangHwan(a3626a, Oortcloud)
 */

public class LootTableModifier {

	private static final LootTableManager manager = new LootTableManager(null);

	private static final ResourceLocation chicken = new ResourceLocation(References.MODID, "entities/chicken");
	private static final ResourceLocation cow = new ResourceLocation(References.MODID, "entities/cow");
	private static final ResourceLocation pig = new ResourceLocation(References.MODID, "entities/pig");
	private static final ResourceLocation rabbit = new ResourceLocation(References.MODID, "entities/rabbit");
	private static final ResourceLocation sheep = new ResourceLocation(References.MODID, "entities/sheep");

	private static LootTable loot_table_chicken;
	private static LootTable loot_table_cow;
	private static LootTable loot_table_pig;
	private static LootTable loot_table_rabbit;
	private static LootTable loot_table_sheep;

	public static void init() {
		LootFunctionManager.registerFunction(new SetCountBaseOnHunger.Serializer());
		loot_table_chicken = manager.getLootTableFromLocation(chicken);
		loot_table_cow = manager.getLootTableFromLocation(cow);
		loot_table_pig = manager.getLootTableFromLocation(pig);
		loot_table_rabbit = manager.getLootTableFromLocation(rabbit);
		loot_table_sheep = manager.getLootTableFromLocation(sheep);
	}

	@SubscribeEvent
	public void LootTableLoadEvent(LootTableLoadEvent event) {
		if (event.getName().equals(LootTableList.ENTITIES_CHICKEN)) {
			event.getTable().getPool("main").removeEntry("minecraft:feather");
			event.getTable().getPool("main").addEntry(loot_table_chicken.getPool("feather").getEntry("minecraft:feather"));
			event.getTable().getPool("pool1").removeEntry("minecraft:chicken");
			event.getTable().getPool("pool1").addEntry(loot_table_chicken.getPool("meat").getEntry("minecraft:chicken"));
		}
		if (event.getName().equals(LootTableList.ENTITIES_COW)) {
			event.getTable().getPool("main").removeEntry("minecraft:leather");
			event.getTable().getPool("main").addEntry(loot_table_cow.getPool("leather").getEntry("minecraft:leather"));
			event.getTable().getPool("pool1").removeEntry("minecraft:beef");
			event.getTable().getPool("pool1").addEntry(loot_table_cow.getPool("meat").getEntry("minecraft:beef"));
			event.getTable().addPool(loot_table_cow.getPool("tendon"));
		}
		if (event.getName().equals(LootTableList.ENTITIES_PIG)) {
			event.getTable().getPool("main").removeEntry("minecraft:porkchop");
			event.getTable().getPool("main").addEntry(loot_table_pig.getPool("meat").getEntry("minecraft:porkchop"));
			event.getTable().addPool(loot_table_pig.getPool("tendon"));
		}
		if (event.getName().equals(LootTableList.ENTITIES_RABBIT)) {

			event.getTable().getPool("main").removeEntry("minecraft:rabbit_hide");
			event.getTable().getPool("main").addEntry(loot_table_rabbit.getPool("rabbit_hide").getEntry("minecraft:rabbit_hide"));
			event.getTable().getPool("pool1").removeEntry("minecraft:rabbit");
			event.getTable().getPool("pool1").addEntry(loot_table_rabbit.getPool("meat").getEntry("minecraft:rabbit"));
			event.getTable().getPool("pool2").removeEntry("minecraft:rabbit_foot");
			event.getTable().getPool("pool2").addEntry(loot_table_rabbit.getPool("rabbit_foot").getEntry("minecraft:rabbit_foot"));
		}
		if (event.getName().equals(LootTableList.ENTITIES_SHEEP)) {
			event.getTable().getPool("main").removeEntry("minecraft:mutton");
			event.getTable().getPool("main").addEntry(loot_table_sheep.getPool("meat").getEntry("minecraft:mutton"));
			event.getTable().addPool(loot_table_sheep.getPool("tendon"));
		}
	}

}
