package oortcloud.hungryanimals.entities.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.core.network.PacketGeneralServer;
import oortcloud.hungryanimals.core.network.SyncIndex;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;
import oortcloud.hungryanimals.potion.PotionHungryAnimals;

@SideOnly(Side.CLIENT)
public class EntityOverlayHandler extends Gui {

	protected static final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");
	private static final int flashTick = 15;
	
	private boolean isEnabled = false;
	private Minecraft mc;

	private EntityAnimal targetAnimal;
	// TODO what is it... i forgot
	private ExtendedPropertiesHungryAnimal targetProperty;

	public double bar_health;
	public double bar_hunger;
	public double bar_age;
	public double bar_taming;
	public int[] potions = new int[0];
	
	public EntityOverlayHandler(Minecraft mc) {
		this.mc = mc;
	}

	private void init(Minecraft mc) {
		potions = new int[0];
	}

	public void setOpened(boolean isOpened) {
		if (isOpened)
			this.init(this.mc);

		this.isEnabled = isOpened;
	}

	@SubscribeEvent
	public void onUpdate(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			Entity entity = mc.pointedEntity;
			if (entity != null) {
				if (entity instanceof EntityAnimal) {
					EntityAnimal animal = (EntityAnimal) entity;
					ExtendedPropertiesHungryAnimal property = (ExtendedPropertiesHungryAnimal) animal.getExtendedProperties(Strings.extendedPropertiesKey);
					if (property != null) {
						targetAnimal = animal;
						targetProperty = property;
						isEnabled = true;
					}
				}
			} else {
				isEnabled = false;
				targetAnimal = null;
				targetProperty = null;
			}
			
			if (isEnabled && mc.theWorld != null && mc.theWorld.getWorldTime()%5==0) {
				PacketGeneralServer msg = new PacketGeneralServer(SyncIndex.ENTITYOVERLAY_SYNC_REQUEST);
				msg.setInt(targetAnimal.getEntityId());
				HungryAnimals.simpleChannel.sendToServer(msg);
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
		int posX = res.getScaledWidth() / 2;
		int posY = res.getScaledHeight() / 2;
		
		if (potions.length != 0) {
			int index =  (((int)mc.theWorld.getWorldTime()/flashTick)%potions.length);
			this.mc.getTextureManager().bindTexture(inventoryBackground);
			Potion potion = Potion.REGISTRY.getObjectById(potions[index]);
			if (potion.hasStatusIcon()) {
				int l = potion.getStatusIconIndex();
				drawTexturedModalRect(posX, posY, 0 + (l % 8) * 18, 198 + (l / 8) * 18, 18, 18);
			}
			if (potion instanceof PotionHungryAnimals) {
				((PotionHungryAnimals) potion).renderGuiEffect(posX + 1, posY + 1, new PotionEffect(potion, 0), mc, this);
			}
		}
		
		/*
		for (int i = 0 ; i < potions.length; i++) {
			this.mc.getTextureManager().bindTexture(inventoryBackground);
			Potion potion = Potion.potionTypes[potions[i]];
			if (potion.hasStatusIcon()) {
				int l = potion.getStatusIconIndex();
				drawTexturedModalRect(posX + 18 * i, posY, 0 + (l % 8) * 18, 198 + (l / 8) * 18, 18, 18);
			}
			if (potion instanceof PotionHungryAnimals) {
				((PotionHungryAnimals) potion).renderGuiEffect(posX + 18 * i + 1, posY + 1, new PotionEffect(potion.id, 0), mc, this);
			}
		}
		*/
		
		drawRect(posX - 1, posY + 17, posX + 19, posY + 20, 0xFF000000);
		drawRect(posX, posY + 18, posX + (int) (18 * bar_hunger), posY + 19, 0xFF0000FF);

		drawRect(posX - 1, posY + 20, posX + 19, posY + 23, 0xFF000000);
		drawRect(posX, posY + 21, posX + (int) (18 * bar_health), posY + 22, 0xFF00FF00);

		drawRect(posX - 1, posY + 23, posX + 19, posY + 26, 0xFF000000);
		if (bar_age > 0) {
			drawRect(posX, posY + 24, posX + (int) (18 * Math.min(1,bar_age)), posY + 25, 0xFFFF00FF);
		} else {
			drawRect(posX, posY + 24, posX + (int) (18 * Math.min(1, -bar_age)), posY + 25, 0xFFFFFF00);
		}
		
		drawRect(posX - 1, posY + 26, posX + 19, posY + 29, 0xFF000000);
		if (bar_taming > 0) {
			drawRect(posX + 9, posY + 27, posX + 9 +(int) (9 * Math.min(1, bar_taming)), posY + 28, (0xFF000000)+(((int)(255*(1+Math.min(1, bar_taming))/2.0))<<8)+(((int)(255*(1-Math.min(1, bar_taming))/2.0))<<16));
		} else {
			drawRect(posX + 9, posY + 27, posX + 9 +(int) (9 * Math.max(-1, bar_taming)), posY + 28, (0xFF000000)+(((int)(255*(1+Math.max(-1, bar_taming))/2.0))<<8)+(((int)(255*(1-Math.max(-1, bar_taming))/2.0))<<16));
		}
	}
	
	public static void drawTexturedRect(int left, int up, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)left, (double)(up + height), 0.0D).tex(0.0D, 1.0D).endVertex();
        worldrenderer.pos((double)(left + width), (double)(up + height), 0.0D).tex(1.0D, 1.0D).endVertex();
        worldrenderer.pos((double)(left + width), (double)up, 0.0D).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos((double)left, (double)up, 0.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
	}
	
	public boolean getEnabled() {
		return isEnabled;
	}

}
