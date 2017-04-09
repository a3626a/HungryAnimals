package oortcloud.hungryanimals.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import oortcloud.hungryanimals.blocks.render.BlockRenderEventHandler;
import oortcloud.hungryanimals.blocks.render.RenderTileEntityTrough;
import oortcloud.hungryanimals.client.ClientRenderEventHandler;
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

public class ClientProxy extends CommonProxy {

	public void registerEntityRendering() {
		RenderingRegistry.registerEntityRenderingHandler(EntityBola.class, RenderEntityBola::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySlingShotBall.class, RenderEntitySlingShotBall::new);
	}

	public void registerItemRendering() {
		//mesher.register(Item.REGISTRY.getObject(new ResourceLocation(References.MODID, Strings.blockFloorCoverLeafName)), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockFloorCoverLeafName, "inventory"));
		//mesher.register(Item.REGISTRY.getObject(new ResourceLocation(References.MODID, Strings.blockFloorCoverWoolName)), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockFloorCoverWoolName, "inventory"));
		//mesher.register(Item.REGISTRY.getObject(new ResourceLocation(References.MODID, Strings.blockFloorCoverHayName)), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockFloorCoverHayName, "inventory"));
		
		//ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(ModBlocks.excreta), 0, new ModelResourceLocation(ModBlocks.excreta.getRegistryName(), "inventory"));
		//ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(ModBlocks.niterBed), 0, new ModelResourceLocation(ModBlocks.niterBed.getRegistryName(), "inventory"));
		//ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(ModBlocks.trough), 0, new ModelResourceLocation(ModBlocks.trough.getRegistryName(), "inventory"));
		
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
	}

	public void registerTileEntityRendering() {
		ClientRegistry.<TileEntityTrough>bindTileEntitySpecialRenderer(TileEntityTrough.class, new RenderTileEntityTrough());
	}

	@Override
	public void registerItemModel() {
		ModelBakery.registerItemVariants(ModItems.bola, ModelItemBola.modelresourcelocation_normal, ModelItemBola.modelresourcelocation_spin);
		ModelBakery.registerItemVariants(ModItems.slingshot, ModelItemSlingshot.modelresourcelocation_normal, ModelItemSlingshot.modelresourcelocation_shooting);
	}

	@Override
	public void registerCustomBakedModel(ModelBakeEvent event) {
		
		Object object;
		object = event.getModelRegistry().getObject(ModelItemBola.modelresourcelocation_normal);
		if (object instanceof IPerspectiveAwareModel) {
			IPerspectiveAwareModel existingModel = (IPerspectiveAwareModel) object;
			ModelItemBola customModel = new ModelItemBola(existingModel);
			event.getModelRegistry().putObject(ModelItemBola.modelresourcelocation_normal, customModel);
		}

		object = event.getModelRegistry().getObject(ModelItemSlingshot.modelresourcelocation_normal);
		if (object instanceof IPerspectiveAwareModel) {
			IPerspectiveAwareModel existingModel = (IPerspectiveAwareModel) object;
			ModelItemSlingshot customModel = new ModelItemSlingshot(existingModel);
			event.getModelRegistry().putObject(ModelItemSlingshot.modelresourcelocation_normal, customModel);
		}
		
	}

	@Override
	public void registerSprite(TextureStitchEvent event) {
		event.getMap().registerSprite(ModelItemSlingshot.textureresourcelocation);
	}

	@Override
	public void registerEventHandler() {
		super.registerEventHandler();
		MinecraftForge.EVENT_BUS.register(new BlockRenderEventHandler());
		MinecraftForge.EVENT_BUS.register(new ClientRenderEventHandler());
		DebugOverlayHandler debugOverlay = new DebugOverlayHandler(Minecraft.getMinecraft());
		MinecraftForge.EVENT_BUS.register(debugOverlay);
	}

	@Override
	public void registerKeyBindings() {
		ModKeyBindings.init();
	}
	
	@Override
	public void initNEI() {
		//NEIHandler.init();
	}
	
}
