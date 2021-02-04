package oortcloud.hungryanimals.stats;

import net.minecraft.item.Item;
import net.minecraft.stats.StatType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class ModStatTypes {
    public static RegistryObject<StatType<Item>> ITEM_USED = RegistryObject.of(new ResourceLocation("minecraft:used"), ForgeRegistries.STAT_TYPES);
}
