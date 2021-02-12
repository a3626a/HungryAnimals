package oortcloud.hungryanimals.core.proxy;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.api.theoneprobe.TOPCompatibility;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.core.network.*;
import oortcloud.hungryanimals.entities.EntityBola;
import oortcloud.hungryanimals.entities.EntitySlingShotBall;
import oortcloud.hungryanimals.entities.capability.CapabilityAgeable;
import oortcloud.hungryanimals.entities.capability.CapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.CapabilityProducingAnimal;
import oortcloud.hungryanimals.entities.capability.CapabilitySexual;
import oortcloud.hungryanimals.entities.capability.CapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityAgeable;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityProducingAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilitySexual;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.StorageAgeable;
import oortcloud.hungryanimals.entities.capability.StorageHungryAnimal;
import oortcloud.hungryanimals.entities.capability.StorageProducingAnimal;
import oortcloud.hungryanimals.entities.capability.StorageSexual;
import oortcloud.hungryanimals.entities.capability.StorageTamableAnimal;
import oortcloud.hungryanimals.entities.event.EntityEventHandler;
import oortcloud.hungryanimals.recipes.event.CraftingEventHandler;
import oortcloud.hungryanimals.tileentities.TileEntityTrough;

public class CommonProxy {

	public void registerTileEntities() {
		// TODO deprecation try new ResourceLocation(Strings.blockTroughName);
		GameRegistry.registerTileEntity(TileEntityTrough.class, References.MODID+"."+Strings.blockTroughName);
	}

	public void registerCapabilities() {
		CapabilityManager.INSTANCE.register(ICapabilityHungryAnimal.class, new StorageHungryAnimal(), CapabilityHungryAnimal::new);
		CapabilityManager.INSTANCE.register(ICapabilityTamableAnimal.class, new StorageTamableAnimal(), CapabilityTamableAnimal::new);
		CapabilityManager.INSTANCE.register(ICapabilityProducingAnimal.class, new StorageProducingAnimal(), CapabilityProducingAnimal::new);
		CapabilityManager.INSTANCE.register(ICapabilitySexual.class, new StorageSexual(), CapabilitySexual::new);
		CapabilityManager.INSTANCE.register(ICapabilityAgeable.class, new StorageAgeable(), CapabilityAgeable::new);
	}

	public void registerColors() {
	}
	
	public void registerEntityRendering() {
	}

	public void injectRender() {
	}
	
	public void registerTileEntityRendering() {
	}

	public void registerEventHandler() {
		MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
		MinecraftForge.EVENT_BUS.register(new CraftingEventHandler());
	}

	public void registerKeyBindings() {
	}

	public void registerPacketHandler() {
		int index = 0;
		ModPacketHandler.INSTANCE.registerMessage(
				index++,
				PacketServerDGEditInt.class,
				PacketServerDGEditInt::toBytes,
				PacketServerDGEditInt::new,
				PacketServerDGEditInt::onMessage
		);
		ModPacketHandler.INSTANCE.registerMessage(
				index++,
				PacketServerDGEditDouble.class,
				PacketServerDGEditDouble::toBytes,
				PacketServerDGEditDouble::new,
				PacketServerDGEditDouble::onMessage
		);
		ModPacketHandler.INSTANCE.registerMessage(
				index++,
				PacketServerDGSet.class,
				PacketServerDGSet::toBytes,
				PacketServerDGSet::new,
				PacketServerDGSet::onMessage
		);
		ModPacketHandler.INSTANCE.registerMessage(
				index++,
				PacketClientSpawnParticle.class,
				PacketClientSpawnParticle::toBytes,
				PacketClientSpawnParticle::new,
				PacketClientSpawnParticle::onMessage
		);
		ModPacketHandler.INSTANCE.registerMessage(
				index++,
				PacketClientSyncTamable.class,
				PacketClientSyncTamable::toBytes,
				PacketClientSyncTamable::new,
				PacketClientSyncTamable::onMessage
		);
		ModPacketHandler.INSTANCE.registerMessage(
				index++,
				PacketClientSyncHungry.class,
				PacketClientSyncHungry::toBytes,
				PacketClientSyncHungry::new,
				PacketClientSyncHungry::onMessage
		);
		ModPacketHandler.INSTANCE.registerMessage(
				index++,
				PacketClientSyncProducingFluid.class,
				PacketClientSyncProducingFluid::toBytes,
				PacketClientSyncProducingFluid::new,
				PacketClientSyncProducingFluid::onMessage
		);
		ModPacketHandler.INSTANCE.registerMessage(
				index++,
				PacketClientSyncProducingInteraction.class,
				PacketClientSyncProducingInteraction::toBytes,
				PacketClientSyncProducingInteraction::new,
				PacketClientSyncProducingInteraction::onMessage
		);
	}

}
