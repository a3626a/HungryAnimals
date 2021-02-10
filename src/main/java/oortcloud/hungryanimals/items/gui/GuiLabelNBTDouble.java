package oortcloud.hungryanimals.items.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

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
		PlayerEntity player = ((PlayerEntity) Minecraft.getMinecraft().getRenderViewEntity());
		if (player != null) {
			// player can be null during launch / close
			CompoundNBT tag = player.getHeldItemMainhand().getTag();
			if (tag != null)
				data = tag.getDouble(key);
		}
	}

	public void draw() {
		fontRenderer.drawString(key + " : " + String.format("%.2f", data), x, y, color);
	}

}
