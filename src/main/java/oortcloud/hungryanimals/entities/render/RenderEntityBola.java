package oortcloud.hungryanimals.entities.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.EntityBola;

@OnlyIn(Dist.CLIENT)
public class RenderEntityBola extends Render<EntityBola> {
	public RenderEntityBola(RenderManager renderManager) {
		super(renderManager);
	}

	private static final ResourceLocation bolaTextures = new ResourceLocation(
			References.MODID + ":textures/entities/" + Strings.entityBolaName + ".png");

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render
	 * is generic (Render<T extends Entity) and this method has signature public
	 * void func_76986_a(T entity, double d, double d1, double d2, float f,
	 * float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(EntityBola bola, double x, double y, double z, float p_76986_8_, float partial) {
		bindEntityTexture(bola);

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(((bola.getEntityWorld().getWorldTime() + partial) % 20) * 13, 0.0F, 1.0F, 0.0F);
		GlStateManager.disableCull();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder renderer = tessellator.getBuffer();

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		renderer.pos(1, 0.5, 0).tex(0, 0).endVertex();
		renderer.pos(1, 0, 0).tex(0, 1).endVertex();
		renderer.pos(-1, 0, 0).tex(1, 1).endVertex();
		renderer.pos(-1, 0.5, 0).tex(1, 0).endVertex();
		
		renderer.pos(0, 0.5, 1).tex(0, 0).endVertex();
		renderer.pos(0, 0, 1).tex(0, 1).endVertex();
		renderer.pos(0, 0, -1).tex(1, 1).endVertex();
		renderer.pos(0, 0.5, -1).tex(1, 0).endVertex();
		tessellator.draw();

		GlStateManager.enableCull();
		GL11.glPopMatrix();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityBola entity) {
		return bolaTextures;
	}

}