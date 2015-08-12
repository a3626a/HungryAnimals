package oortcloud.hungryanimals.core.proxy;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.configuration.ConfigurationEventHandler;
import oortcloud.hungryanimals.core.handler.FMLEventHandler;
import oortcloud.hungryanimals.core.handler.ForgeEventHandler;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.EntityBola;
import oortcloud.hungryanimals.entities.EntitySlingShotBall;
import oortcloud.hungryanimals.entities.event.EntityEventHandler;
import oortcloud.hungryanimals.keybindings.ModKeyBindings;
import oortcloud.hungryanimals.recipes.event.CraftingEventHandler;
import oortcloud.hungryanimals.tileentities.TileEntityAxle;
import oortcloud.hungryanimals.tileentities.TileEntityBelt;
import oortcloud.hungryanimals.tileentities.TileEntityBlender;
import oortcloud.hungryanimals.tileentities.TileEntityCrankAnimal;
import oortcloud.hungryanimals.tileentities.TileEntityCrankPlayer;
import oortcloud.hungryanimals.tileentities.TileEntityTrough;
import oortcloud.hungryanimals.tileentities.TileEntityMillstone;
import oortcloud.hungryanimals.tileentities.TileEntityThresher;

public class CommonProxy {

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityTrough.class, Strings.blockFoodBoxName);
		GameRegistry.registerTileEntity(TileEntityAxle.class, Strings.blockAxleName);
		GameRegistry.registerTileEntity(TileEntityBelt.class, Strings.blockBeltName);
		GameRegistry.registerTileEntity(TileEntityCrankPlayer.class, Strings.blockCrankPlayerName);
		GameRegistry.registerTileEntity(TileEntityThresher.class, Strings.blockThresherName);
		GameRegistry.registerTileEntity(TileEntityMillstone.class, Strings.blockMillstoneName);
		GameRegistry.registerTileEntity(TileEntityBlender.class, Strings.blockBlenderName);
		GameRegistry.registerTileEntity(TileEntityCrankAnimal.class, Strings.blockCrankAnimalName);
	}

	public void registerEntities() {
		EntityRegistry.registerModEntity(EntityBola.class, Strings.entityBolaName, Strings.entityBolaID, HungryAnimals.instance, 80, 3, true);
		EntityRegistry.registerModEntity(EntitySlingShotBall.class, Strings.entitySlingShotBallName, Strings.entitySlingShotBallID, HungryAnimals.instance, 80, 3, true);
	}

	public void registerEntityRendering() {
	}

	public void registerBlockRendering() {
	}

	public void registerTileEntityRendering() {
	}

	public void registerItemModel() {
	}

	public void registerItemRendering() {
	}

	public void registerCustomBakedModel(ModelBakeEvent event) {
	}

	public void registerSprite(TextureStitchEvent event) {
	}

	public void registerEventHandler() {
		FMLCommonHandler.instance().bus().register(new FMLEventHandler());
		FMLCommonHandler.instance().bus().register(new ConfigurationEventHandler());
		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
		MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
		MinecraftForge.EVENT_BUS.register(new CraftingEventHandler());
	}

	public void registerKeyBindings() {
	}

}
