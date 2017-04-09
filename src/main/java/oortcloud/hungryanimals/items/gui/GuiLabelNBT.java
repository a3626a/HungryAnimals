package oortcloud.hungryanimals.items.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;

public class GuiLabelNBT extends Gui {

	protected String key;
	protected EnumFormat format;

	private int x;
	private int y;
	private FontRenderer fontRenderer;
	private int color = 0xFFFFFF;

	protected int intData;
	protected double doubleData;

	protected GuiLabelNBT(FontRenderer fontRenderer, String key, EnumFormat format) {
		this.fontRenderer = fontRenderer;
		this.key = key;
		this.format = format;
	}

	public static GuiLabelNBT createIntegerNBT(FontRenderer fontRenderer, String key) {
		return new GuiLabelNBT(fontRenderer, key, EnumFormat.INT);
	}

	public static GuiLabelNBT createDoubleNBT(FontRenderer fontRenderer, String key) {
		return new GuiLabelNBT(fontRenderer, key, EnumFormat.DOUBLE);
	}

	public void setPosition(int x, int y) {	
		this.x = x;
		this.y = y;
	}

	public void update() {
		EntityPlayer player = ((EntityPlayer) Minecraft.getMinecraft().getRenderViewEntity());
		if (player != null) {
			switch (format) {
			case INT:
				intData = player.getHeldItemMainhand().getTagCompound().getInteger(key);
				break;
			case DOUBLE:
				doubleData = player.getHeldItemMainhand().getTagCompound().getDouble(key);
				break;
			}
		}
	}

	public void draw() {
		switch (format) {
		case INT:
			fontRenderer.drawString(key + " : " + intData, x, y, color);
			break;
		case DOUBLE:
			fontRenderer.drawString(key + " : " + String.format("%.2f", doubleData), x, y, color);
			break;
		}
	}

	protected enum EnumFormat {
		INT, DOUBLE;
	}

}
