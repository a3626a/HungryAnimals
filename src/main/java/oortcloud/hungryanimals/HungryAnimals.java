package oortcloud.hungryanimals;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.api.HAPlugins;
import oortcloud.hungryanimals.block.ModBlocks;
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

	public static SimpleChannel simpleChannel;

	public static ItemGroup tabHungryAnimals = new ItemGroup("tabHungryAnimals") {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon() {
			return new ItemStack(ModItems.WHEAT.get());
		}
	};

	public static Logger logger;

	static {
	    FluidRegistry.enableUniversalBucket();
	}

	public HungryAnimals()
	{
		ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}


	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		simpleChannel = NetworkRegistry.newSimpleChannel(References.MODNAME);
		logger = event.getModLog();
		HAPlugins.getInstance().init(event);
		ConfigurationHandler.init(event);
		RecipeAnimalGlue.init();
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

		proxy.initCompt();
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
