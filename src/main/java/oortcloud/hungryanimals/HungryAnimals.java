package oortcloud.hungryanimals;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.RecipeSorter;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.command.CommandTickRate;
import oortcloud.hungryanimals.configuration.ConfigurationHandler;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.network.HandlerGeneralClient;
import oortcloud.hungryanimals.core.network.HandlerGeneralServer;
import oortcloud.hungryanimals.core.network.HandlerPlayerServer;
import oortcloud.hungryanimals.core.network.HandlerTileEntityClient;
import oortcloud.hungryanimals.core.network.HandlerTileEntityServer;
import oortcloud.hungryanimals.core.network.PacketGeneralClient;
import oortcloud.hungryanimals.core.network.PacketGeneralServer;
import oortcloud.hungryanimals.core.network.PacketPlayerServer;
import oortcloud.hungryanimals.core.network.PacketTileEntityClient;
import oortcloud.hungryanimals.core.network.PacketTileEntityServer;
import oortcloud.hungryanimals.core.proxy.CommonProxy;
import oortcloud.hungryanimals.entities.render.EntityOverlayHandler;
import oortcloud.hungryanimals.fluids.ModFluids;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.potion.ModPotions;
import oortcloud.hungryanimals.recipes.CraftingHandler;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue;
import oortcloud.hungryanimals.recipes.RecipeBlender;
import oortcloud.hungryanimals.recipes.RecipeMillstone;
import oortcloud.hungryanimals.recipes.RecipeThresher;

import org.apache.logging.log4j.Logger;

@Mod(modid = References.MODID, name = References.MODNAME, version = References.VERSION)
public class HungryAnimals {
	@Mod.Instance
	public static HungryAnimals instance;

	@SidedProxy(clientSide = References.CLIENTPROXYLOCATION, serverSide = References.COMMONPROXYLOCATION)
	public static CommonProxy proxy;

	public static SimpleNetworkWrapper simpleChannel;

	@SideOnly(Side.CLIENT)
	public static EntityOverlayHandler entityOverlay;

	public static CreativeTabs tabHungryAnimals = new CreativeTabs("tabHungryAnimals") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Items.wheat;
		}
	};

	public static Logger logger;

	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		RecipeThresher.init();
		RecipeMillstone.init();
		RecipeBlender.init();
		RecipeAnimalGlue.init();
		ConfigurationHandler.init(event);
		ModBlocks.init();
		ModItems.init();
		ModPotions.init();
		ModFluids.init();
		proxy.registerEntities();
		proxy.registerBlockRendering();
		proxy.registerItemModel();
		proxy.registerTileEntityRendering();
		proxy.registerTileEntities();
		proxy.registerKeyBindings();
		ConfigurationHandler.sync();
	}

	@Mod.EventHandler
	public static void Init(FMLInitializationEvent event) {
		proxy.registerItemRendering();
		proxy.registerEntityRendering();

		CraftingHandler.init();
		proxy.registerEventHandler();

		simpleChannel = NetworkRegistry.INSTANCE.newSimpleChannel(References.MODNAME);
		simpleChannel.registerMessage(HandlerGeneralServer.class, PacketGeneralServer.class, 1, Side.SERVER);
		simpleChannel.registerMessage(HandlerGeneralClient.class, PacketGeneralClient.class, 5, Side.CLIENT);
		simpleChannel.registerMessage(HandlerTileEntityServer.class, PacketTileEntityServer.class, 2, Side.SERVER);
		simpleChannel.registerMessage(HandlerTileEntityClient.class, PacketTileEntityClient.class, 3, Side.CLIENT);
		simpleChannel.registerMessage(HandlerPlayerServer.class, PacketPlayerServer.class, 4, Side.SERVER);
	}

	@Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		ConfigurationHandler.postSync();
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandTickRate());
	}

}
