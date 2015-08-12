package oortcloud.hungryanimals.entities.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.EntityBola;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderEntityBola extends Render
{
    public RenderEntityBola(RenderManager renderManager) {
		super(renderManager);
	}

	private static final ResourceLocation bolaTextures = new ResourceLocation(References.MODID+":textures/entities/"
			+ Strings.entityBolaName + ".png");

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityBola bola, double x, double y, double z, float p_76986_8_, float p_76986_9_)
    {
        this.bindEntityTexture(bola);
        
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glRotatef(((bola.worldObj.getWorldTime()+p_76986_9_)%20)*13, 0.0F, 1.0F, 0.0F);
        GL11.glDisable(GL11.GL_CULL_FACE);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer renderer = Tessellator.getInstance().getWorldRenderer();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        
        renderer.startDrawingQuads();
        renderer.addVertexWithUV(1.0D, 0.50D, 0.0D, 0, 0);
        renderer.addVertexWithUV(1.0D, 0.0D, 0.0D, 0, 1.0F);
        renderer.addVertexWithUV(-1.0D, 0.0D, 0.0D, 1.0F, 1.0F);
        renderer.addVertexWithUV(-1.0D, 0.50D, 0.0D, 1.0F, 0);
        tessellator.draw();
        
        renderer.startDrawingQuads();
        renderer.addVertexWithUV(0.0D, 0.5D, 1.0D, 0, 0);
        renderer.addVertexWithUV(0.0D, 0.0D, 1.0D, 0, 1.0F);
        renderer.addVertexWithUV(0.0D, 0.0D, -1.0D, 1.0F, 1.0F);
        renderer.addVertexWithUV(0.0D, 0.5D, -1.0D, 1.0F, 0);
        tessellator.draw();

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityBola p_110775_1_)
    {
        return bolaTextures;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityBola)p_110775_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityBola)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}