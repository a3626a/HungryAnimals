package oortcloud.hungryanimals;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
import oortcloud.hungryanimals.api.HAPlugins;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.configuration.ConfigurationHandler;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.proxy.CommonProxy;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;
import oortcloud.hungryanimals.items.ModItems;
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
		public ItemStack getTabIconItem() {
			return Items.WHEAT.getDefaultInstance();
		}
	};

	public static Logger logger;

	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		simpleChannel = NetworkRegistry.INSTANCE.newSimpleChannel(References.MODNAME);
		logger = event.getModLog();
		HAPlugins.getInstance().init(event);
		ConfigurationHandler.init(event);
		RecipeAnimalGlue.init();
		ModBlocks.init();
		ModItems.init();
		proxy.registerEntities();
		proxy.registerEntityRendering();
		proxy.registerTileEntities();
		proxy.registerTileEntityRendering();
		proxy.registerKeyBindings();
		proxy.registerCapabilities();
		proxy.registerEventHandler();
		proxy.registerPacketHandler();
		HungryAnimalManager.getInstance().init();

		ConfigurationHandler.syncPre();
		
		if (Loader.isModLoaded("theoneprobe"))
			proxy.initTOP();
	}

	@Mod.EventHandler
	public static void Init(FMLInitializationEvent event) {
		ConfigurationHandler.sync();
		
		proxy.registerColors();
		
		CraftingHandler.init();
	}

	@Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		ConfigurationHandler.syncPost();
		proxy.injectRender();
	}

}
