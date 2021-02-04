package oortcloud.hungryanimals.sound_event;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSoundEvents {
    public static RegistryObject<SoundEvent> ENTITY_ARROW_SHOOT = RegistryObject.of(new ResourceLocation("minecraft:entity.arrow.shoot"), ForgeRegistries.SOUND_EVENTS);
}
