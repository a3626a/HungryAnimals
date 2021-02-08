package oortcloud.hungryanimals.client;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.client.event.RenderLivingEvent;
import oortcloud.hungryanimals.items.ModItems;
import com.mojang.blaze3d.platform.GLX;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientRenderEventHandler {
	/*
	@SubscribeEvent
	public void renderHerbicideGrass(RenderWorldLastEvent event) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer render = tessellator.getWorldRenderer();
		float partialTickTime = event.partialTicks;
		PlayerEntity player = (PlayerEntity) Minecraft.getMinecraft().getRenderViewEntity();

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
	public static void renderDebugGlassEntity(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
		float radius = 0.5F;
		float height = 0.7F;
		 
		Entity entity = Minecraft.getInstance().getRenderViewEntity();
		if (entity == null)
			return;
		
		if (!(entity instanceof PlayerEntity)) {
			return;
		}
		
		PlayerEntity player = (PlayerEntity)entity;
		
		LivingEntity animal = event.getEntity();
		ItemStack stack = player.getHeldItemMainhand();
		if (!stack.isEmpty() && stack.getItem() == ModItems.DEBUG_GLASS.get()) {
			CompoundNBT tag = stack.getTag();
			if (tag != null) {
				if (tag.contains("target") && tag.getInt("target") == animal.getEntityId()) {
					Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder renderer = tessellator.getBuffer();
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					GL11.glColor4f(1, 0, 0, 1);
					GLX.glBlendFuncSeparate(770, 771, 1, 0);
					float h = animal.getHeight() + height + 0.5F;
					float f = animal.getHeight() + 0.5F;
					GL11.glPushMatrix();
					GL11.glTranslated(event.getX(), event.getY(), event.getZ());
					GL11.glRotatef(4 * event.getEntity().getEntityWorld().getGameTime(), 0, 1, 0);
					renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
					renderer.pos(+radius, +h, +radius).endVertex();
					renderer.pos(+radius, +h, -radius).endVertex();
					renderer.pos(-radius, +h, -radius).endVertex();
					renderer.pos(-radius, +h, +radius).endVertex();
					tessellator.draw();
					renderer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
					renderer.pos(+radius, +h, -radius).endVertex();
					renderer.pos(+radius, +h, +radius).endVertex();
					renderer.pos(0, +f, 0).endVertex();
					renderer.pos(-radius, +h, -radius).endVertex();
					renderer.pos(+radius, +h, -radius).endVertex();
					renderer.pos(0, +f, 0).endVertex();
					renderer.pos(-radius, +h, +radius).endVertex();
					renderer.pos(-radius, +h, -radius).endVertex();
					renderer.pos(0, +f, 0).endVertex();
					renderer.pos(+radius, +h, +radius).endVertex();
					renderer.pos(-radius, +h, +radius).endVertex();
					renderer.pos(0, +f, 0).endVertex();
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
