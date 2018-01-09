package oortcloud.hungryanimals.items.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;

public class GuiLabelNBTInteger extends GuiPlacable {

	protected String key;

	private FontRenderer fontRenderer;
	private int color = 0xFFFFFF;

	protected int data;

	protected GuiLabelNBTInteger(FontRenderer fontRenderer, String key) {
		this.fontRenderer = fontRenderer;
		this.key = key;
	}

	public void update() {
		EntityPlayer player = ((EntityPlayer) Minecraft.getMinecraft().getRenderViewEntity());
		if (player != null) {
			// player can be null during launch / close
			data = player.getHeldItemMainhand().getTagCompound().getInteger(key);
		}
	}

	public void draw() {
		fontRenderer.drawString(key + " : " + data, x, y, color);
	}

}
