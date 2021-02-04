package oortcloud.hungryanimals.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEnchantments {
    public static RegistryObject<Enchantment> INFINITY = RegistryObject.of(new ResourceLocation("minecraft:infinity"), ForgeRegistries.ENCHANTMENTS);
}
