package oortcloud.hungryanimals.entities.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.entities.EntitySlingShotBall;

import org.lwjgl.opengl.GL11;

public class RenderEntitySlingShotBall  extends Render {

	public RenderEntitySlingShotBall(RenderManager renderManager) {
		super(renderManager);
	}

	public void doRender(EntitySlingShotBall entity, double x,
			double y, double z, float p_76986_8_,
			float p_76986_9_) {

        GL11.glPushMatrix();
        GL11.glTranslated(x, y+0.25F, z);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(0.25F, 0.2F, 0.2F, 1.0F);
        Render.renderOffsetAABB(AxisAlignedBB.fromBounds(-0.25F,-0.25F,-0.25F,0.25F,0.25F,0.25F), 0, 0, 0);
        GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
		
	}

	/**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntitySlingShotBall p_110775_1_)
    {
        return null;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntitySlingShotBall)p_110775_1_);
    }
    
    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntitySlingShotBall)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

}
