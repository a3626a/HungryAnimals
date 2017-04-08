package oortcloud.hungryanimals;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.configuration.ConfigurationHandler;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.network.HandlerGeneralClient;
import oortcloud.hungryanimals.core.network.HandlerGeneralServer;
import oortcloud.hungryanimals.core.network.HandlerPlayerServer;
import oortcloud.hungryanimals.core.network.PacketGeneralClient;
import oortcloud.hungryanimals.core.network.PacketGeneralServer;
import oortcloud.hungryanimals.core.network.PacketPlayerServer;
import oortcloud.hungryanimals.core.proxy.CommonProxy;
import oortcloud.hungryanimals.entities.ai.AIManager;
import oortcloud.hungryanimals.entities.loot_tables.LootTableModifier;
import oortcloud.hungryanimals.entities.properties.handler.HungryAnimalManager;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.potion.ModPotions;
import oortcloud.hungryanimals.recipes.CraftingHandler;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue;

@Mod(modid = References.MODID, name = References.MODNAME, version = References.VERSION)
public class HungryAnimals {
	@Mod.Instance
	public static HungryAnimals instance;

	@SidedProxy(clientSide = References.CLIENTPROXYLOCATION, serverSide = References.COMMONPROXYLOCATION)
	public static CommonProxy proxy;

	public static SimpleNetworkWrapper simpleChannel;

	public static CreativeTabs tabHungryAnimals = new CreativeTabs("tabHungryAnimals") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Items.WHEAT;
		}
	};

	public static Logger logger;

	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		RecipeAnimalGlue.init();
		ConfigurationHandler.init(event);
		ModBlocks.init();
		ModItems.init();
		ModPotions.init();
		proxy.registerItemModel();
		proxy.registerEntities();
		proxy.registerEntityRendering();
		proxy.registerTileEntities();
		proxy.registerTileEntityRendering();
		proxy.registerKeyBindings();
		proxy.registerCapabilities();
		LootTableModifier.init();
		HungryAnimalManager.getInstance().init();
		AIManager.getInstance().init();
		ConfigurationHandler.sync();
		
		if (Loader.isModLoaded("theoneprobe"))
			proxy.initTOP();
	}

	@Mod.EventHandler
	public static void Init(FMLInitializationEvent event) {
		proxy.registerItemRendering();

		CraftingHandler.init();
		proxy.registerEventHandler();

		simpleChannel = NetworkRegistry.INSTANCE.newSimpleChannel(References.MODNAME);
		simpleChannel.registerMessage(HandlerGeneralServer.class, PacketGeneralServer.class, 1, Side.SERVER);
		simpleChannel.registerMessage(HandlerGeneralClient.class, PacketGeneralClient.class, 5, Side.CLIENT);
		simpleChannel.registerMessage(HandlerPlayerServer.class, PacketPlayerServer.class, 4, Side.SERVER);
		
		if (Loader.isModLoaded("WAILA"))
			proxy.initWAILA();
	}

	@Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		ConfigurationHandler.postSync();
		if (Loader.isModLoaded("NotEnoughItems"))
			proxy.initNEI();
	}

}
