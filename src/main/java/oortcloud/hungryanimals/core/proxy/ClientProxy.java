package oortcloud.hungryanimals.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ItemLayerModel.BakedModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.api.nei.NEIHandler;
import oortcloud.hungryanimals.blocks.render.BlockRenderEventHandler;
import oortcloud.hungryanimals.blocks.render.RenderTileEntityAxle;
import oortcloud.hungryanimals.blocks.render.RenderTileEntityBlender;
import oortcloud.hungryanimals.blocks.render.RenderTileEntityCrankAnimal;
import oortcloud.hungryanimals.blocks.render.RenderTileEntityCrankPlayer;
import oortcloud.hungryanimals.blocks.render.RenderTileEntityMillstone;
import oortcloud.hungryanimals.blocks.render.RenderTileEntityThresher;
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
import oortcloud.hungryanimals.tileentities.TileEntityAxle;
import oortcloud.hungryanimals.tileentities.TileEntityBlender;
import oortcloud.hungryanimals.tileentities.TileEntityCrankAnimal;
import oortcloud.hungryanimals.tileentities.TileEntityCrankPlayer;
import oortcloud.hungryanimals.tileentities.TileEntityMillstone;
import oortcloud.hungryanimals.tileentities.TileEntityThresher;
import oortcloud.hungryanimals.tileentities.TileEntityTrough;

public class ClientProxy extends CommonProxy {

	public void registerEntityRendering() {
		RenderManager render = Minecraft.getMinecraft().getRenderManager();
		RenderingRegistry.registerEntityRenderingHandler(EntityBola.class, new RenderEntityBola(render));
		RenderingRegistry.registerEntityRenderingHandler(EntitySlingShotBall.class, new RenderEntitySlingShotBall(render));
	}

	public void registerBlockRendering() {
	}

	public void registerItemRendering() {
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockExcretaName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockExcretaName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockNiterBedName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockNiterBedName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockTrapCoverName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockTrapCoverName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockAxleName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockAxleName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockCrankPlayerName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockCrankPlayerName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockMillstoneName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockMillstoneName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockThresherName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockThresherName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockBlenderName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockBlenderName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockFloorCoverLeafName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockFloorCoverLeafName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockFloorCoverWoolName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockFloorCoverWoolName, "inventory"));
		mesher.register(GameRegistry.findItem(References.MODID, Strings.blockFloorCoverHayName), 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.blockFloorCoverHayName, "inventory"));

		mesher.register(ModItems.bola, 0, CameraTransformModelItemBola.modelresourcelocation_normal);
		mesher.register(ModItems.slingshot, 0, SmartModelItemSlingshot.modelresourcelocation_normal);
		//mesher.register(ModItems.slingshot, 0, SmartModelItemSlingshot.modelresourcelocation_shooting);
		mesher.register(ModItems.debugGlass, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemDebugGlassName, "inventory"));
		mesher.register(ModItems.trough, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemFoodBoxName, "inventory"));
		mesher.register(ModItems.wheel, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemWheelName, "inventory"));
		mesher.register(ModItems.manure, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemManureName, "inventory"));
		mesher.register(ModItems.woodash, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemWoodashName, "inventory"));
		mesher.register(ModItems.saltpeter, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemSaltpeterName, "inventory"));
		mesher.register(ModItems.tendon, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemTendonName, "inventory"));
		mesher.register(ModItems.straw, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemStrawName, "inventory"));
		mesher.register(ModItems.poppycrop, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemPoppyCropName, "inventory"));
		mesher.register(ModItems.poppyseed, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemPoppySeedName, "inventory"));
		mesher.register(ModItems.mixedFeed, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemMixedFeedName, "inventory"));
		mesher.register(ModItems.animalGlue, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemAnimalGlueName, "inventory"));
		mesher.register(ModItems.compositeWood, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemCompositeWoodName, "inventory"));
		mesher.register(ModItems.compositeWoodCasing, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemCompositeWoodCasingName, "inventory"));
		mesher.register(ModItems.blade, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemBladeName, "inventory"));
		mesher.register(ModItems.crankAnimal, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemCrankAnimalName, "inventory"));
		mesher.register(ModItems.oilpipet, 0, new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemOilPipetName, "inventory"));
		mesher.register(ModItems.belt, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemBeltName, "inventory");
			}
		});
	}

	public void registerTileEntityRendering() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTrough.class, new RenderTileEntityTrough());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAxle.class, new RenderTileEntityAxle());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrankPlayer.class, new RenderTileEntityCrankPlayer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityThresher.class, new RenderTileEntityThresher());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMillstone.class, new RenderTileEntityMillstone());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlender.class, new RenderTileEntityBlender());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrankAnimal.class, new RenderTileEntityCrankAnimal());
	}

	@Override
	public void registerItemModel() {
		ModelBakery.addVariantName(ModItems.bola, References.RESOURCESPREFIX + Strings.itemBolaName, References.RESOURCESPREFIX + Strings.itemBolaName + "_spin");
		ModelBakery.addVariantName(ModItems.slingshot, References.RESOURCESPREFIX + Strings.itemSlingShotName, References.RESOURCESPREFIX + Strings.itemSlingShotName + "_shooting");
	}

	@Override
	public void registerCustomBakedModel(ModelBakeEvent event) {
		Object object;
		object = event.modelRegistry.getObject(CameraTransformModelItemBola.modelresourcelocation_normal);
		if (object instanceof BakedModel) {
			BakedModel existingModel = (BakedModel) object;
			CameraTransformModelItemBola customModel = new CameraTransformModelItemBola(existingModel);
			event.modelRegistry.putObject(CameraTransformModelItemBola.modelresourcelocation_normal, customModel);
		}

		object = event.modelRegistry.getObject(SmartModelItemSlingshot.modelresourcelocation_normal);
		if (object instanceof BakedModel) {
			BakedModel existingModel = (BakedModel) object;
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
		FMLCommonHandler.instance().bus().register(debugOverlay);
		HungryAnimals.entityOverlay = new EntityOverlayHandler(Minecraft.getMinecraft());
		MinecraftForge.EVENT_BUS.register(HungryAnimals.entityOverlay);
		FMLCommonHandler.instance().bus().register(HungryAnimals.entityOverlay);
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
