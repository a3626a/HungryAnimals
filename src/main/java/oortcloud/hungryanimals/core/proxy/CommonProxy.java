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
import oortcloud.hungryanimals.configuration.ConfigurationEventHandler;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.core.network.HandlerServerDGEditDouble;
import oortcloud.hungryanimals.core.network.HandlerServerDGEditInt;
import oortcloud.hungryanimals.core.network.HandlerServerDGSet;
import oortcloud.hungryanimals.core.network.PacketClientSpawnParticle;
import oortcloud.hungryanimals.core.network.PacketClientSyncHungry;
import oortcloud.hungryanimals.core.network.PacketClientSyncProducingFluid;
import oortcloud.hungryanimals.core.network.PacketClientSyncProducingInteraction;
import oortcloud.hungryanimals.core.network.PacketClientSyncTamable;
import oortcloud.hungryanimals.core.network.PacketServerDGEditDouble;
import oortcloud.hungryanimals.core.network.PacketServerDGEditInt;
import oortcloud.hungryanimals.core.network.PacketServerDGSet;
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
import oortcloud.hungryanimals.entities.loot_tables.ModLootTables;
import oortcloud.hungryanimals.recipes.event.CraftingEventHandler;
import oortcloud.hungryanimals.tileentities.TileEntityTrough;

public class CommonProxy {

	public void registerTileEntities() {
		// TODO deprecation try new ResourceLocation(Strings.blockTroughName);
		GameRegistry.registerTileEntity(TileEntityTrough.class, References.MODID+"."+Strings.blockTroughName);
	}

	public void registerEntities() {
		EntityRegistry.registerModEntity(new ResourceLocation(References.MODID, Strings.entityBolaName), EntityBola.class, Strings.entityBolaName, Strings.entityBolaID, HungryAnimals.instance, 80, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(References.MODID, Strings.entitySlingShotBallName), EntitySlingShotBall.class, Strings.entitySlingShotBallName, Strings.entitySlingShotBallID, HungryAnimals.instance, 80,
				3, true);
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
		MinecraftForge.EVENT_BUS.register(new ConfigurationEventHandler());
		MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
		MinecraftForge.EVENT_BUS.register(new CraftingEventHandler());
	}

	public void registerKeyBindings() {
	}

	public void initCompt() {
		if (Loader.isModLoaded("theoneprobe"))
			initTOP();

		if (Loader.isModLoaded("animania"))
			initAnimania();
	}

	public void initNEI() {
	}

	public void initWAILA() {
		// FMLInterModComms.sendMessage("Waila", "register",
		// "oortcloud.hungryanimals.api.waila.HUDHandlerHungryAnimals.callbackRegister");
	}
	
	private void initTOP() {
		 TOPCompatibility.register();
	}

	private void initAnimania() {

	}

	public void registerPacketHandler() {
		HungryAnimals.simpleChannel.registerMessage(HandlerServerDGEditInt.class, PacketServerDGEditInt.class, 0, Side.SERVER);
		HungryAnimals.simpleChannel.registerMessage(HandlerServerDGEditDouble.class, PacketServerDGEditDouble.class, 1, Side.SERVER);
		HungryAnimals.simpleChannel.registerMessage(HandlerServerDGSet.class, PacketServerDGSet.class, 2, Side.SERVER);
		HungryAnimals.simpleChannel.registerMessage((message, ctx)->{return null;}, PacketClientSpawnParticle.class, 3, Side.CLIENT);
		HungryAnimals.simpleChannel.registerMessage((message, ctx)->{return null;}, PacketClientSyncTamable.class, 4, Side.CLIENT);
		HungryAnimals.simpleChannel.registerMessage((message, ctx)->{return null;}, PacketClientSyncHungry.class, 5, Side.CLIENT);
		HungryAnimals.simpleChannel.registerMessage((message, ctx)->{return null;}, PacketClientSyncProducingFluid.class, 6, Side.CLIENT);
		HungryAnimals.simpleChannel.registerMessage((message, ctx)->{return null;}, PacketClientSyncProducingInteraction.class, 7, Side.CLIENT);
	}

}
