package oortcloud.hungryanimals.api;

import net.minecraft.world.storage.loot.LootFunction;

public interface ILootTableRegistry {
	public <T extends LootFunction> void registerFunction(LootFunction.Serializer <? extends T > serializer);
}
