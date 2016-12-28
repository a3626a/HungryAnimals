package oortcloud.hungryanimals.potion;

import oortcloud.hungryanimals.entities.render.EntityOverlayHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class PotionHungryAnimals extends Potion {

	public ResourceLocation texture;
	
	protected PotionHungryAnimals(ResourceLocation location, boolean badEffect, int potionColor) {
		super(badEffect, potionColor);
		texture = location;
		Potion.REGISTRY.putObject(texture, this);
	}

	@Override
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
		mc.getTextureManager().bindTexture(texture);
		EntityOverlayHandler.drawTexturedRect(x+6, y+7, 16, 16);
	}
	
	public void renderGuiEffect(int x, int y, PotionEffect effect, Minecraft mc, Gui gui) {
		mc.getTextureManager().bindTexture(texture);
		EntityOverlayHandler.drawTexturedRect(x, y, 16, 16);
	}
	
}
