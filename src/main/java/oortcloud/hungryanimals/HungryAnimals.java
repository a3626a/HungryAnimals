package oortcloud.hungryanimals;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import oortcloud.hungryanimals.core.proxy.ClientProxy;
import oortcloud.hungryanimals.entities.ModEntities;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.generation.Conditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import oortcloud.hungryanimals.api.HAPlugins;
import oortcloud.hungryanimals.block.ModBlocks;
import oortcloud.hungryanimals.configuration.ConfigurationHandler;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.proxy.CommonProxy;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.recipes.CraftingHandler;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue;

@Mod(References.MODID)
public class HungryAnimals {
	public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

	public static ItemGroup tabHungryAnimals = new ItemGroup("tabHungryAnimals") {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon() {
			return new ItemStack(ModItems.WHEAT.get());
		}
	};

	public static final Logger logger = LogManager.getLogger(References.MODID);

	static {
	    FluidRegistry.enableUniversalBucket();
	}

	public HungryAnimals()
	{
		ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		Conditions.CONDITIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModAttributes.ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus());
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		HAPlugins.getInstance().init(event);
		ConfigurationHandler.init(event);
		RecipeAnimalGlue.init();
		proxy.registerEntityRendering();
		proxy.registerTileEntities();
		proxy.registerTileEntityRendering();
		proxy.registerKeyBindings();
		proxy.registerCapabilities();
		proxy.registerEventHandler();
		proxy.registerPacketHandler();
		HungryAnimalManager.getInstance().init();

		ConfigurationHandler.syncPre();
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
