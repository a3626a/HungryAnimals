package oortcloud.hungryanimals.items.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.keybindings.ModKeyBindings;

@SideOnly(Side.CLIENT)
public class DebugOverlayHandler extends Gui {
	private boolean isEnabled = false;
	private Minecraft mc;
	private ItemStack debugGlass;

	private GuiLabelNBT targetEntityID;
	private GuiLabelNBTEditable hunger;
	private GuiLabelNBT excretion;
	private GuiLabelNBTEditable taming;
	private GuiLabelNBT age;
	private GuiLabelNBT[] labels;

	private int index;

	private static final int START_X = 10;
	private static final int START_Y = 10;
	private static final int WIDTH = 100;
	private static final int FONTHEIGHT = 10;

	public DebugOverlayHandler(Minecraft mc) {
		this.mc = mc;
	}

	private void init(Minecraft mc) {
		index = 0;

		labels = new GuiLabelNBT[5];
		targetEntityID = GuiLabelNBT.createIntegerNBT(mc.fontRendererObj, "target");
		labels[0] = targetEntityID;
		hunger = GuiLabelNBTEditable.createDoubleNBT(mc.fontRendererObj, "hunger", 10, targetEntityID);
		labels[1] = hunger;
		excretion = GuiLabelNBT.createDoubleNBT(mc.fontRendererObj, "excretion");
		labels[2] = excretion;
		taming = GuiLabelNBTEditable.createDoubleNBT(mc.fontRendererObj, "taming", 0.1, targetEntityID);
		labels[3] = taming;
		age = GuiLabelNBT.createIntegerNBT(mc.fontRendererObj, "age");
		labels[4] = age;
		for (int i = 0; i < labels.length; i++) {
			labels[i].setPosition(START_X, START_Y + FONTHEIGHT * i);
		}
	}

	public void setOpened(boolean isOpened) {
		if (isOpened)
			this.init(this.mc);

		this.isEnabled = isOpened;
	}

	@SubscribeEvent
	public void onUpdate(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			if (!this.isEnabled) {
				EntityPlayer entity = (EntityPlayer) mc.getRenderViewEntity();
				if (entity != null) {
					ItemStack stack = entity.getHeldItem();
					if (stack != null && stack.getItem() == ModItems.debugGlass && stack.getTagCompound() != null) {
						debugGlass = stack;
						this.setOpened(true);
					}
				}
			} else {
				EntityPlayer entity = (EntityPlayer) mc.getRenderViewEntity();
				if (entity != null) {
					ItemStack stack = entity.getHeldItem();
					if (stack != null && stack.getItem() == ModItems.debugGlass && stack.getTagCompound() != null) {
					} else {
						this.setOpened(false);
						return;
					}
				}
				for (int i = 0; i < labels.length; i++) {
					labels[i].update();
				}
			}
		}
	}

	@SubscribeEvent
	public void onDrawOverlay(RenderGameOverlayEvent.Post event) {
		if (event.type != ElementType.ALL)
			return;
		if (!this.isEnabled)
			return;

		ScaledResolution res = event.resolution;
		this.drawGradientRect(0, 0, WIDTH, res.getScaledHeight(), -1072689136, -804253680);
		
		Entity entity = mc.theWorld.getEntityByID(targetEntityID.intData);
		if (entity != null) {
			String text = (String)EntityList.classToStringMapping.get(entity.getClass());
			if (text != null) {
				if (entity instanceof EntityAnimal) text+=" (Compatible)";
				this.drawString(mc.fontRendererObj, text, 0, START_Y - FONTHEIGHT, 0xFFFFFF);
			}
		}
		
		for (int i = 0; i < labels.length; i++) {
			if (i == index) {
				this.drawGradientRect(0, START_Y + FONTHEIGHT * i, WIDTH, START_Y + FONTHEIGHT * (i+1), 0x80A0A0A0, 0x80FFFFFF);
			}
			labels[i].draw();
		}
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (this.isEnabled) {
			if (ModKeyBindings.keyDown.isPressed()) {
				if (index < labels.length - 1)
					index++;
			}
			if (ModKeyBindings.keyUp.isPressed()) {
				if (index > 0)
					index--;
			}
			if (ModKeyBindings.keyRight.isPressed()) {
				if (labels[index] instanceof GuiLabelNBTEditable) {
					((GuiLabelNBTEditable)labels[index]).increase();
				}
			}
			if (ModKeyBindings.keyLeft.isPressed()) {
				if (labels[index] instanceof GuiLabelNBTEditable) {
					((GuiLabelNBTEditable)labels[index]).decrease();
				}
			}
		}
	}
}
