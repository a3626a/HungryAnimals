package oortcloud.hungryanimals.items.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class GuiLabelNBTAIList extends GuiPlacable {

	private static class AI {
		public String name;
		public boolean using;
		
		public AI(String name, boolean using) {
			this.name = name;
			this.using = using;
		}
	}
	
	private FontRenderer fontRenderer;
	private int color = 0xFFFFFF;
	private int colorOff = 0x606060;
	protected List<AI> data;

	protected GuiLabelNBTAIList(FontRenderer fontRenderer) {
		this.fontRenderer = fontRenderer;
	}

	@Override
	public void update() {
		EntityPlayer player = ((EntityPlayer) Minecraft.getMinecraft().getRenderViewEntity());
		if (player != null) {
			// player can be null during launch / close
			NBTTagCompound tag = player.getHeldItemMainhand().getTagCompound();
			if (tag != null) {
				data = new ArrayList<>();
				for (int i = 0; i < tag.getInteger("ais.length"); i++) {
					NBTTagCompound iTag = tag.getCompoundTag("ais."+i);
					data.add(new AI(iTag.getString("name"), iTag.getBoolean("using")));
				}
			}
		}
	}

	@Override
	public void draw() {
		for (int i = 0; i < data.size(); i++) {
			AI iAI = data.get(i);
			if (iAI.using) {
				fontRenderer.drawString(iAI.name, x, y+10*i, color);
			} else {
				fontRenderer.drawString(iAI.name, x, y+10*i, colorOff);
			}
		}
		
	}
}
