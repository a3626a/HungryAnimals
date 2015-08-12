package oortcloud.hungryanimals.blocks.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.tileentities.TileEntityCrankAnimal;

import org.lwjgl.opengl.GL11;

public class RenderTileEntityCrankAnimal extends TileEntitySpecialRenderer {

	public static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/blocks/ModelCrankAnimal.png");
	private ModelCrankAnimal modelCrank;

	public RenderTileEntityCrankAnimal() {
		modelCrank = new ModelCrankAnimal();
	}

	private double interpolateValue(double start, double end, double pct) {
		return start + (end - start) * pct;
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTick, int p_180535_9_) {

		TileEntityCrankAnimal crank = (TileEntityCrankAnimal) tileentity;

		if (!crank.isPrimary())
			return;

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.0F, +0.0F, 0.0F);
		GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
		GL11.glPushMatrix();

		this.bindTexture(texture);
		this.modelCrank.renderModel(0.0625F, crank.getNetwork().getAngle(partialTick));

		GL11.glPopMatrix();
		GL11.glPopMatrix();

		EntityLiving leashedAnimal = crank.getLeashedAnimal();
		
		if (leashedAnimal != null ) {
			double angle = Math.toRadians(crank.getNetwork().getAngle(partialTick)+90);
			double posX = crank.getPos().getX() + 0.5 + 1.3*Math.cos(angle);
			double posY = crank.getPos().getY() - 1;
			double posZ = crank.getPos().getZ() + 0.5 + 1.3*Math.sin(angle);
			
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			double d12 = this.interpolateValue((double)leashedAnimal.prevRenderYawOffset, (double)leashedAnimal.renderYawOffset, (double)partialTick) * 0.01745329238474369D + (Math.PI / 2D);
            double d5 = Math.cos(d12) * (double)leashedAnimal.width * 0.4D;
            double d6 = Math.sin(d12) * (double)leashedAnimal.width * 0.4D;
			x += + 0.5 + 1.3*Math.cos(angle);
			y += -1;
			z += + 0.5 + 1.3*Math.sin(angle);
			double d13 = this.interpolateValue(leashedAnimal.prevPosX, leashedAnimal.posX, (double) partialTick) + d5;
			double d14 = this.interpolateValue(leashedAnimal.prevPosY, leashedAnimal.posY, (double) partialTick) + leashedAnimal.getEyeHeight()*0.8;
			double d15 = this.interpolateValue(leashedAnimal.prevPosZ, leashedAnimal.posZ, (double) partialTick) + d5;
			double d16 = (d13-posX);
			double d17 = (d14-posY);
			double d18 = (d15-posZ);
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			boolean flag = true;
			double d19 = 0.025D;
			worldrenderer.startDrawing(5);
			int i;
			float f2;

			for (i = 0; i <= 24; ++i) {
				if (i % 2 == 0) {
					worldrenderer.setColorRGBA_F(0.5F, 0.4F, 0.3F, 1.0F);
				} else {
					worldrenderer.setColorRGBA_F(0.35F, 0.28F, 0.21000001F, 1.0F);
				}

				f2 = (float) i / 24.0F;
				worldrenderer
						.addVertex(x + d16 * (double) f2 + 0.0D, y + d17 * (double) (f2 * f2 + f2) * 0.5D + (double) ((24.0F - (float) i) / 18.0F + 0.125F), z + d18 * (double) f2);
				worldrenderer.addVertex(x + d16 * (double) f2 + 0.025D, y + d17 * (double) (f2 * f2 + f2) * 0.5D + (double) ((24.0F - (float) i) / 18.0F + 0.125F) + 0.025D, z + d18
						* (double) f2);
			}

			tessellator.draw();
			worldrenderer.startDrawing(5);

			for (i = 0; i <= 24; ++i) {
				if (i % 2 == 0) {
					worldrenderer.setColorRGBA_F(0.5F, 0.4F, 0.3F, 1.0F);
				} else {
					worldrenderer.setColorRGBA_F(0.35F, 0.28F, 0.21000001F, 1.0F);
				}

				f2 = (float) i / 24.0F;
				worldrenderer.addVertex(x + d16 * (double) f2 + 0.0D, y + d17 * (double) (f2 * f2 + f2) * 0.5D + (double) ((24.0F - (float) i) / 18.0F + 0.125F) + 0.025D, z + d18
						* (double) f2);
				worldrenderer.addVertex(x + d16 * (double) f2 + 0.025D, y + d17 * (double) (f2 * f2 + f2) * 0.5D + (double) ((24.0F - (float) i) / 18.0F + 0.125F), z + d18
						* (double) f2 + 0.025D);
			}

			tessellator.draw();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
			GlStateManager.enableCull();
		}
		
	}

}
