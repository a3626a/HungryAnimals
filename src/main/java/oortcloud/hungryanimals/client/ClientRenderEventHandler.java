package oortcloud.hungryanimals.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.items.ModItems;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ClientRenderEventHandler {
	
	/*
	@SubscribeEvent
	public void renderHerbicideGrass(RenderWorldLastEvent event) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer render = tessellator.getWorldRenderer();
		float partialTickTime = event.partialTicks;
		EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().getRenderViewEntity();

		if (player == null) {
			return;
		}

		double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTickTime;
		double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTickTime;
		double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTickTime;

		GL11.glPushMatrix();
		GL11.glTranslated(-px, -py, -pz);

		if (player.getHeldItem() != null && player.getHeldItem().getItem() == ModItems.herbicide) {
			World world = player.worldObj;
			int x1 = (int) player.posX;
			int z1 = (int) player.posZ;
			int y1 = (int) player.posY;

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_LIGHTING);

			GL11.glColor4f(0.2f, 0.2f, 0.0f, 0.3f);

			for (int x = x1 - 16; x <= x1 + 16; x++) {
				for (int z = z1 - 16; z <= z1 + 16; z++) {
					for (int y = y1 - 16; y < y1 + 16; y++) {
						if (y >= 0 && y < 256) {
							BlockPos pos = new BlockPos(x, y, z);

							if (world.getBlockState(pos) == Blocks.grass.getDefaultState()) {

								render.startDrawingQuads();
								render.addVertex(x, y + 1.05, z);
								render.addVertex(x, y + 1.05, z + 1);
								render.addVertex(x + 1, y + 1.05, z + 1);
								render.addVertex(x + 1, y + 1.05, z);
								tessellator.draw();

							}
						}
					}
				}
			}
		}
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
	*/
	
	@SubscribeEvent
	public void renderDebugGlassEntity(RenderLivingEvent.Post event) {

		float radius = 0.5F;
		float height = 0.7F;

		EntityPlayer entity = (EntityPlayer) Minecraft.getMinecraft().getRenderViewEntity();
		EntityLivingBase animal = event.entity;
		RendererLivingEntity render = event.renderer;
		ItemStack stack = entity.getHeldItem();
		if (stack != null && stack.getItem() == ModItems.debugGlass) {
			NBTTagCompound tag = stack.getTagCompound();
			if (tag != null) {
				if (tag.hasKey("target") && tag.getInteger("target") == animal.getEntityId()) {
					Tessellator tessellator = Tessellator.getInstance();
					WorldRenderer renderer = tessellator.getWorldRenderer();
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					GL11.glColor4f(1, 0, 0, 1);
					OpenGlHelper.glBlendFunc(770, 771, 1, 0);
					float h = animal.height + height + 0.5F;
					float f = animal.height + 0.5F;
					GL11.glPushMatrix();
					GL11.glTranslated(event.x, event.y, event.z);
					GL11.glRotatef(4 * event.entity.worldObj.getWorldTime(), 0, 1, 0);
					renderer.startDrawingQuads();
					renderer.addVertex(+radius, +h, +radius);
					renderer.addVertex(+radius, +h, -radius);
					renderer.addVertex(-radius, +h, -radius);
					renderer.addVertex(-radius, +h, +radius);
					tessellator.draw();
					renderer.startDrawing(GL11.GL_TRIANGLES);
					renderer.addVertex(+radius, +h, -radius);
					renderer.addVertex(+radius, +h, +radius);
					renderer.addVertex(0, +f, 0);
					renderer.addVertex(-radius, +h, -radius);
					renderer.addVertex(+radius, +h, -radius);
					renderer.addVertex(0, +f, 0);
					renderer.addVertex(-radius, +h, +radius);
					renderer.addVertex(-radius, +h, -radius);
					renderer.addVertex(0, +f, 0);
					renderer.addVertex(+radius, +h, +radius);
					renderer.addVertex(-radius, +h, +radius);
					renderer.addVertex(0, +f, 0);
					tessellator.draw();
					GL11.glPopMatrix();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glColor4f(1, 1, 1, 1);
				}
			}
		}
	}
}
