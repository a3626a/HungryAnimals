package oortcloud.hungryanimals.items.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.MobEntityBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.keybindings.ModKeyBindings;

@OnlyIn(Dist.CLIENT)
public class DebugOverlayHandler extends Gui {
	private boolean isEnabled = false;
	private Minecraft mc;

	private GuiLabelNBTInteger targetEntityID;
	private List<GuiPlacable> labels;

	private int index;

	private static final int START_X = 10;
	private static final int START_Y = 10;
	private static final int WIDTH = 200;
	private static final int FONTHEIGHT = 10;

	public DebugOverlayHandler(Minecraft mc) {
		this.mc = mc;
	}

	private void init(Minecraft mc) {
		index = 0;
		labels = new ArrayList<GuiPlacable>();
		targetEntityID = new GuiLabelNBTInteger(mc.fontRenderer, "target");
		labels.add(targetEntityID);
		labels.add(new GuiLabelNBTEditableDouble(mc.fontRenderer, "weight", 10, targetEntityID));
		labels.add(new GuiLabelNBTEditableDouble(mc.fontRenderer, "nutrient", 1, targetEntityID));
		labels.add(new GuiLabelNBTEditableDouble(mc.fontRenderer, "stomach", 1, targetEntityID));
		labels.add(new GuiLabelNBTEditableDouble(mc.fontRenderer, "excretion", 0.1, targetEntityID));
		labels.add(new GuiLabelNBTEditableDouble(mc.fontRenderer, "taming", 0.5, targetEntityID));
		labels.add(new GuiLabelNBTEditableInteger(mc.fontRenderer, "age", 60 * 20, targetEntityID));
		labels.add(new GuiLabelNBTAIList(mc.fontRenderer));
		int cnt = 0;
		for (GuiPlacable i : labels) {
			i.setPosition(START_X, START_Y + FONTHEIGHT * cnt);
			cnt++;
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
				Entity entity = mc.getRenderViewEntity();
				if (entity != null && entity instanceof MobEntityBase) {
					MobEntityBase living = (MobEntityBase) entity;
					ItemStack stack = getDebugGlass(living);
					if (!stack.isEmpty()) {
						this.setOpened(true);
					}
				} else {
					this.setOpened(false);
				}
			} else {
				Entity entity = mc.getRenderViewEntity();
				if (entity != null && entity instanceof MobEntityBase) {
					MobEntityBase living = (MobEntityBase) entity;
					ItemStack stack = getDebugGlass(living);
					if (stack.isEmpty()) {
						this.setOpened(false);
						return;
					}
				} else {
					this.setOpened(false);
					return;
				}
				for (GuiPlacable i : labels) {
					i.update();
				}
			}
		}
	}

	@SubscribeEvent
	public void onDrawOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() != ElementType.ALL)
			return;
		if (!this.isEnabled)
			return;

		ScaledResolution res = event.getResolution();
		this.drawGradientRect(0, 0, WIDTH, res.getScaledHeight(), -1072689136, -804253680);

		Entity entity = mc.world.getEntityByID(targetEntityID.data);
		if (entity != null) {
			ResourceLocation resource = EntityList.getKey(entity);
			if (resource != null) {
				String text = (String) resource.toString();
				if (text != null) {
					this.drawString(mc.fontRenderer, text, 0, START_Y - FONTHEIGHT, 0xFFFFFF);
				}
			}
		}

		for (int i = 0; i < labels.size(); i++) {
			if (i == index) {
				this.drawGradientRect(0, START_Y + FONTHEIGHT * i, WIDTH, START_Y + FONTHEIGHT * (i + 1), 0x80A0A0A0, 0x80FFFFFF);
			}
			labels.get(i).draw();
		}
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (this.isEnabled) {
			if (ModKeyBindings.keyDown.isPressed()) {
				if (index < labels.size() - 1)
					index++;
			}
			if (ModKeyBindings.keyUp.isPressed()) {
				if (index > 0)
					index--;
			}
			if (ModKeyBindings.keyRight.isPressed()) {
				if (labels.get(index) instanceof IEditable) {
					((IEditable) labels.get(index)).increase();
				}
			}
			if (ModKeyBindings.keyLeft.isPressed()) {
				if (labels.get(index) instanceof IEditable) {
					((IEditable) labels.get(index)).decrease();
				}
			}
		}
	}

	private ItemStack getDebugGlass(MobEntityBase player) {
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		if (stack != null && stack.getItem() == ModItems.DEBUG_GLASS.get() && stack.getTag() != null) {
			return stack;
		}
		stack = player.getHeldItem(EnumHand.OFF_HAND);
		if (stack != null && stack.getItem() == ModItems.DEBUG_GLASS.get() && stack.getTag() != null) {
			return stack;
		}
		return ItemStack.EMPTY;
	}
}
