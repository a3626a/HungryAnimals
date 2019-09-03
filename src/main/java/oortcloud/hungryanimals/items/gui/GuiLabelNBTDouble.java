package oortcloud.hungryanimals.items.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class GuiLabelNBTDouble extends GuiPlacable {

	protected String key;

	private FontRenderer fontRenderer;
	private int color = 0xFFFFFF;

	protected double data;

	protected GuiLabelNBTDouble(FontRenderer fontRenderer, String key) {
		this.fontRenderer = fontRenderer;
		this.key = key;
	}

	public void update() {
		EntityPlayer player = ((EntityPlayer) Minecraft.getMinecraft().getRenderViewEntity());
		if (player != null) {
			// player can be null during launch / close
			NBTTagCompound tag = player.getHeldItemMainhand().getTagCompound();
			if (tag != null)
				data = tag.getDouble(key);
		}
	}

	public void draw() {
		fontRenderer.drawString(key + " : " + String.format("%.2f", data), x, y, color);
	}

}
