package oortcloud.hungryanimals.core.proxy;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.configuration.ConfigurationEventHandler;
import oortcloud.hungryanimals.core.handler.WorldEventHandler;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.EntityBola;
import oortcloud.hungryanimals.entities.EntitySlingShotBall;
import oortcloud.hungryanimals.entities.event.EntityEventHandler;
import oortcloud.hungryanimals.recipes.event.CraftingEventHandler;
import oortcloud.hungryanimals.tileentities.TileEntityTrough;

public class CommonProxy {

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityTrough.class, Strings.blockTroughName);
	}

	public void registerEntities() {
		EntityRegistry.registerModEntity(EntityBola.class, Strings.entityBolaName, Strings.entityBolaID, HungryAnimals.instance, 80, 3, true);
		EntityRegistry.registerModEntity(EntitySlingShotBall.class, Strings.entitySlingShotBallName, Strings.entitySlingShotBallID, HungryAnimals.instance, 80, 3, true);
	}

	public void registerEntityRendering() {
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
		MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
		MinecraftForge.EVENT_BUS.register(new ConfigurationEventHandler());
		MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
		MinecraftForge.EVENT_BUS.register(new CraftingEventHandler());
	}

	public void registerKeyBindings() {
	}

	public void initNEI() {
	}

	public void initWAILA() {
		//FMLInterModComms.sendMessage("Waila", "register", "oortcloud.hungryanimals.api.waila.HUDHandlerHungryAnimals.callbackRegister");
	}

}
