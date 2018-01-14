package oortcloud.hungryanimals.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

/*
 * For rendering HUD and inventory
 * I highly refer this :
 * https://github.com/lumien231/Random-Things/blob/master/src/main/java/lumien/randomthings/potion/PotionBase.java
 */

public class PotionHungryAnimals extends Potion {

	public ResourceLocation texture;
	
	protected PotionHungryAnimals(ResourceLocation location, boolean badEffect, int potionColor) {
		super(badEffect, potionColor);
		texture = location;
	}
	
	@Override
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
		super.renderHUDEffect(x, y, effect, mc, alpha);
		
		mc.renderEngine.bindTexture(texture);

		GlStateManager.enableBlend();
		Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
	}
	
	@Override
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
		super.renderInventoryEffect(x, y, effect, mc);

		mc.renderEngine.bindTexture(texture);

		GlStateManager.enableBlend();
		Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
	}
	
}
