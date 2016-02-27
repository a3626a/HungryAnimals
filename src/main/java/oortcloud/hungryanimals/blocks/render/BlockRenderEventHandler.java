package oortcloud.hungryanimals.blocks.render;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.items.ItemBola;
import oortcloud.hungryanimals.items.ItemSlingShot;
import oortcloud.hungryanimals.items.render.CameraTransformModelItemBola;
import oortcloud.hungryanimals.items.render.SmartModelItemSlingshot;

@SideOnly(Side.CLIENT)
public class BlockRenderEventHandler {

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		HungryAnimals.proxy.registerCustomBakedModel(event);
	}
	
	@SubscribeEvent
	public void registerTextures(TextureStitchEvent  event) {
		HungryAnimals.proxy.registerSprite(event);
	} 
	
}
