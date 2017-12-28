package oortcloud.hungryanimals.core.proxy;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.blocks.render.RenderTileEntityTrough;
import oortcloud.hungryanimals.client.ClientRenderEventHandler;
import oortcloud.hungryanimals.core.network.HandlerEntityClient;
import oortcloud.hungryanimals.core.network.HandlerGeneralClient;
import oortcloud.hungryanimals.core.network.PacketEntityClient;
import oortcloud.hungryanimals.core.network.PacketGeneralClient;
import oortcloud.hungryanimals.entities.EntityBola;
import oortcloud.hungryanimals.entities.EntitySlingShotBall;
import oortcloud.hungryanimals.entities.render.RenderEntityBola;
import oortcloud.hungryanimals.entities.render.RenderEntitySlingShotBall;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.items.gui.DebugOverlayHandler;
import oortcloud.hungryanimals.items.render.ModelItemBola;
import oortcloud.hungryanimals.items.render.ModelItemSlingshot;
import oortcloud.hungryanimals.keybindings.ModKeyBindings;
import oortcloud.hungryanimals.tileentities.TileEntityTrough;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	public void registerEntityRendering() {
		RenderingRegistry.registerEntityRenderingHandler(EntityBola.class, RenderEntityBola::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySlingShotBall.class, RenderEntitySlingShotBall::new);
	}

	public void registerTileEntityRendering() {
		ClientRegistry.<TileEntityTrough>bindTileEntitySpecialRenderer(TileEntityTrough.class, new RenderTileEntityTrough());
	}
	
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(ModBlocks.floorcover_hay), 0,
				new ModelResourceLocation(ModBlocks.floorcover_hay.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(ModBlocks.floorcover_leaf), 0,
				new ModelResourceLocation(ModBlocks.floorcover_leaf.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(ModBlocks.floorcover_wool), 0,
				new ModelResourceLocation(ModBlocks.floorcover_wool.getRegistryName(), "inventory"));

		ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(ModBlocks.excreta), 0,
				new ModelResourceLocation(ModBlocks.excreta.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(ModBlocks.niterBed), 0,
				new ModelResourceLocation(ModBlocks.niterBed.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(ModBlocks.trough), 0,
				new ModelResourceLocation(ModBlocks.trough.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(ModBlocks.trapcover), 0,
				new ModelResourceLocation(ModBlocks.trapcover.getRegistryName(), "inventory"));
		
		ModelLoader.setCustomModelResourceLocation(ModItems.bola, 0, ModelItemBola.modelresourcelocation_normal);
		ModelLoader.setCustomModelResourceLocation(ModItems.slingshot, 0, ModelItemSlingshot.modelresourcelocation_normal);
		ModelLoader.setCustomModelResourceLocation(ModItems.debugGlass, 0, new ModelResourceLocation(ModItems.debugGlass.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.trough, 0, new ModelResourceLocation(ModItems.trough.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.manure, 0, new ModelResourceLocation(ModItems.manure.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.woodash, 0, new ModelResourceLocation(ModItems.woodash.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.saltpeter, 0, new ModelResourceLocation(ModItems.saltpeter.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.tendon, 0, new ModelResourceLocation(ModItems.tendon.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.animalGlue, 0, new ModelResourceLocation(ModItems.animalGlue.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.compositeWood, 0, new ModelResourceLocation(ModItems.compositeWood.getRegistryName(), "inventory"));
		
		ModelBakery.registerItemVariants(ModItems.bola, ModelItemBola.modelresourcelocation_normal, ModelItemBola.modelresourcelocation_spin);
		ModelBakery.registerItemVariants(ModItems.slingshot, ModelItemSlingshot.modelresourcelocation_normal,
				ModelItemSlingshot.modelresourcelocation_shooting);
    }
	
    @SubscribeEvent
	public static void registerCustomBakedModel(ModelBakeEvent event) {
		IBakedModel bola_normal = event.getModelRegistry().getObject(ModelItemBola.modelresourcelocation_normal);
		IBakedModel bola_spin = event.getModelRegistry().getObject(ModelItemBola.modelresourcelocation_spin);
		ModelItemBola bolaModel = new ModelItemBola(bola_normal, bola_spin);
		event.getModelRegistry().putObject(ModelItemBola.modelresourcelocation_normal, bolaModel);

		IBakedModel slingshot_normal = event.getModelRegistry().getObject(ModelItemSlingshot.modelresourcelocation_normal);
		IBakedModel slingshot_shooting = event.getModelRegistry().getObject(ModelItemSlingshot.modelresourcelocation_shooting);
		TextureAtlasSprite texture = event.getModelManager().getTextureMap().getAtlasSprite(ModelItemSlingshot.textureresourcelocation.toString());
		ModelItemSlingshot slingshotModel = new ModelItemSlingshot(slingshot_normal, slingshot_shooting, texture);
		event.getModelRegistry().putObject(ModelItemSlingshot.modelresourcelocation_normal, slingshotModel);
	}

    @SubscribeEvent
	public static void registerSprite(TextureStitchEvent event) {
		event.getMap().registerSprite(ModelItemSlingshot.textureresourcelocation);
	}

	@Override
	public void registerEventHandler() {
		super.registerEventHandler();
		MinecraftForge.EVENT_BUS.register(new ClientRenderEventHandler());
		DebugOverlayHandler debugOverlay = new DebugOverlayHandler(Minecraft.getMinecraft());
		MinecraftForge.EVENT_BUS.register(debugOverlay);
	}

	@Override
	public void registerKeyBindings() {
		ModKeyBindings.init();
	}

	@Override
	public void registerColors() {
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
			public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
				return ColorizerFoliage.getFoliageColorBasic();
			}
		}, ModBlocks.floorcover_leaf);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				return ColorizerFoliage.getFoliageColorBasic();
			}
		}, ModBlocks.floorcover_leaf);
	}

	@Override
	public void initNEI() {
		// NEIHandler.init();
	}

	@Override
	public void registerPacketHandler() {
		super.registerPacketHandler();
		HungryAnimals.simpleChannel.registerMessage(HandlerGeneralClient.class, PacketGeneralClient.class, 5, Side.CLIENT);
		HungryAnimals.simpleChannel.registerMessage(HandlerEntityClient.class, PacketEntityClient.class, 6, Side.CLIENT);
	}

}
