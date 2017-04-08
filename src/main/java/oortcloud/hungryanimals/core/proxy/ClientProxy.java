package oortcloud.hungryanimals.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import oortcloud.hungryanimals.blocks.render.BlockRenderEventHandler;
import oortcloud.hungryanimals.blocks.render.RenderTileEntityTrough;
import oortcloud.hungryanimals.client.ClientRenderEventHandler;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
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
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		mesher.register(Item.REGISTRY.getObject(new ResourceLocation(References.MODID, Strings.blockExcretaName)), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockExcretaName, "inventory"));
		mesher.register(Item.REGISTRY.getObject(new ResourceLocation(References.MODID, Strings.blockNiterBedName)), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockNiterBedName, "inventory"));
		mesher.register(Item.REGISTRY.getObject(new ResourceLocation(References.MODID, Strings.blockTrapCoverName)), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockTrapCoverName, "inventory"));
		mesher.register(Item.REGISTRY.getObject(new ResourceLocation(References.MODID, Strings.blockFloorCoverLeafName)), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockFloorCoverLeafName, "inventory"));
		mesher.register(Item.REGISTRY.getObject(new ResourceLocation(References.MODID, Strings.blockFloorCoverWoolName)), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockFloorCoverWoolName, "inventory"));
		mesher.register(Item.REGISTRY.getObject(new ResourceLocation(References.MODID, Strings.blockFloorCoverHayName)), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockFloorCoverHayName, "inventory"));
		mesher.register(Item.REGISTRY.getObject(new ResourceLocation(References.MODID, Strings.blockTroughName)), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockTroughName, "inventory"));
		
		mesher.register(ModItems.bola, 0, ModelItemBola.modelresourcelocation_normal);
		mesher.register(ModItems.slingshot, 0, ModelItemSlingshot.modelresourcelocation_normal);
		mesher.register(ModItems.debugGlass, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemDebugGlassName, "inventory"));
		mesher.register(ModItems.trough, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemTroughBoxName, "inventory"));
		mesher.register(ModItems.manure, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemManureName, "inventory"));
		mesher.register(ModItems.woodash, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemWoodashName, "inventory"));
		mesher.register(ModItems.saltpeter, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemSaltpeterName, "inventory"));
		mesher.register(ModItems.tendon, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemTendonName, "inventory"));
		mesher.register(ModItems.animalGlue, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemAnimalGlueName, "inventory"));
		mesher.register(ModItems.compositeWood, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemCompositeWoodName, "inventory"));
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
