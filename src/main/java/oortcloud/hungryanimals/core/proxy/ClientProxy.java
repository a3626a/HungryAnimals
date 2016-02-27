package oortcloud.hungryanimals.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.api.nei.NEIHandler;
import oortcloud.hungryanimals.blocks.render.BlockRenderEventHandler;
import oortcloud.hungryanimals.blocks.render.RenderTileEntityTrough;
import oortcloud.hungryanimals.client.ClientRenderEventHandler;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.EntityBola;
import oortcloud.hungryanimals.entities.EntitySlingShotBall;
import oortcloud.hungryanimals.entities.render.EntityOverlayHandler;
import oortcloud.hungryanimals.entities.render.RenderEntityBola;
import oortcloud.hungryanimals.entities.render.RenderEntitySlingShotBall;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.items.gui.DebugOverlayHandler;
import oortcloud.hungryanimals.items.render.CameraTransformModelItemBola;
import oortcloud.hungryanimals.items.render.SmartModelItemSlingshot;
import oortcloud.hungryanimals.keybindings.ModKeyBindings;
import oortcloud.hungryanimals.tileentities.TileEntityTrough;

public class ClientProxy extends CommonProxy {

	public void registerEntityRendering() {
		RenderingRegistry.registerEntityRenderingHandler(EntityBola.class, RenderEntityBola::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySlingShotBall.class, RenderEntitySlingShotBall::new);
	}

	public void registerItemRendering() {
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockExcretaName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockExcretaName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockNiterBedName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockNiterBedName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockTrapCoverName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockTrapCoverName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockFloorCoverLeafName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockFloorCoverLeafName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockFloorCoverWoolName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockFloorCoverWoolName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockFloorCoverHayName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockFloorCoverHayName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockTroughName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockTroughName, "inventory"));
		
		mesher.register(ModItems.bola, 0, CameraTransformModelItemBola.modelresourcelocation_normal);
		mesher.register(ModItems.slingshot, 0, SmartModelItemSlingshot.modelresourcelocation_normal);
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
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTrough.class, new RenderTileEntityTrough());
	}

	@Override
	public void registerItemModel() {
		ModelBakery.registerItemVariants(ModItems.bola, CameraTransformModelItemBola.modelresourcelocation_normal, CameraTransformModelItemBola.modelresourcelocation_spin);
		ModelBakery.registerItemVariants(ModItems.slingshot, SmartModelItemSlingshot.modelresourcelocation_normal, SmartModelItemSlingshot.modelresourcelocation_shooting);
	}

	@Override
	public void registerCustomBakedModel(ModelBakeEvent event) {
		Object object;
		object = event.modelRegistry.getObject(CameraTransformModelItemBola.modelresourcelocation_normal);
		if (object instanceof IPerspectiveAwareModel) {
			IPerspectiveAwareModel existingModel = (IPerspectiveAwareModel) object;
			CameraTransformModelItemBola customModel = new CameraTransformModelItemBola(existingModel);
			event.modelRegistry.putObject(CameraTransformModelItemBola.modelresourcelocation_normal, customModel);
		}

		object = event.modelRegistry.getObject(SmartModelItemSlingshot.modelresourcelocation_normal);
		if (object instanceof IPerspectiveAwareModel) {
			IPerspectiveAwareModel existingModel = (IPerspectiveAwareModel) object;
			SmartModelItemSlingshot customModel = new SmartModelItemSlingshot(existingModel);
			event.modelRegistry.putObject(SmartModelItemSlingshot.modelresourcelocation_normal, customModel);
		}
	}

	@Override
	public void registerSprite(TextureStitchEvent event) {
		event.map.registerSprite(SmartModelItemSlingshot.textureresourcelocation);
	}

	@Override
	public void registerEventHandler() {
		super.registerEventHandler();
		MinecraftForge.EVENT_BUS.register(new BlockRenderEventHandler());
		MinecraftForge.EVENT_BUS.register(new ClientRenderEventHandler());
		DebugOverlayHandler debugOverlay = new DebugOverlayHandler(Minecraft.getMinecraft());
		MinecraftForge.EVENT_BUS.register(debugOverlay);
		HungryAnimals.entityOverlay = new EntityOverlayHandler(Minecraft.getMinecraft());
		MinecraftForge.EVENT_BUS.register(HungryAnimals.entityOverlay);
	}

	@Override
	public void registerKeyBindings() {
		ModKeyBindings.init();
	}
	
	@Override
	public void initNEI() {
		NEIHandler.init();
	}
	
}
