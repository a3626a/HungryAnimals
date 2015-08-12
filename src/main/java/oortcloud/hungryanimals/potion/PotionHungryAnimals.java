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
	
	protected PotionHungryAnimals(int potionID, ResourceLocation location, boolean badEffect, int potionColor) {
		super(potionID, location, badEffect, potionColor);
		texture = location;
	}

	@Override
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int Width = sr.getScaledWidth();
		int Height = sr.getScaledHeight();
		mc.renderEngine.bindTexture(texture);
		mc.currentScreen.drawTexturedModalRect(x, y, 0, 0, 16, 16);
	}
	
	public void renderGuiEffect(int x, int y, PotionEffect effect, Minecraft mc, Gui gui) {
		mc.getTextureManager().bindTexture(texture);
		EntityOverlayHandler.drawTexturedRect(x, y, 16, 16);
	}
	
}
