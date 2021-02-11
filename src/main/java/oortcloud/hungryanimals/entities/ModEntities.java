package oortcloud.hungryanimals.entities;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, References.MODID);
    public static RegistryObject<EntityType<EntityBola>> BOLA = ENTITIES.register(
            Strings.entityBolaName,
            () -> EntityType.Builder
                    .<EntityBola>create(EntityBola::new, EntityClassification.MISC)
                    .size(0.25F, 0.25F)
                    .build(Strings.entityBolaName)
    );
    public static RegistryObject<EntityType<EntitySlingShotBall>> SLING_SHOT_BALL = ENTITIES.register(
            Strings.entitySlingShotBallName,
            () -> EntityType.Builder
                    .<EntitySlingShotBall>create(EntitySlingShotBall::new, EntityClassification.MISC)
                    .size(0.25F, 0.25F)
                    .build(Strings.entitySlingShotBallName)
    );
}
