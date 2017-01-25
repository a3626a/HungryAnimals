package oortcloud.hungryanimals.entities.loot_tables;

import java.util.Random;

import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oortcloud.hungryanimals.core.lib.References;

public class LootTableModifier {

	public static final ResourceLocation chick = new ResourceLocation(References.MODID, "chicken");
	
	public static void init() {
		LootFunctionManager.registerFunction(new SetCountBaseOnHunger.Serializer());
		LootTableList.register(chick);
		LootTableList.register(new ResourceLocation(References.MODID, "cow"));
		LootTableList.register(new ResourceLocation(References.MODID, "pig"));
		LootTableList.register(new ResourceLocation(References.MODID, "rabbit"));
		LootTableList.register(new ResourceLocation(References.MODID, "sheep"));
	}
	
	@SubscribeEvent
	public void LootTableLoadEvent(LootTableLoadEvent event) {
		
		if (event.getName().equals(LootTableList.ENTITIES_CHICKEN)) {
			System.out.println("닭근혜찡");
		}
		
		if (event.getName().equals(chick)) {
			System.out.println("닭근혜찡2");
		}
		
	}
	
}
