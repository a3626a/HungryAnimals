package oortcloud.hungryanimals.blocks.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.blocks.BlockAxle;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.items.ItemBelt;
import oortcloud.hungryanimals.tileentities.TileEntityAxle;
import oortcloud.hungryanimals.tileentities.TileEntityPowerTransporter;

import org.lwjgl.opengl.GL11;

public class RenderTileEntityAxle extends TileEntitySpecialRenderer {

	public static final ResourceLocation texture_Axle = new ResourceLocation(References.MODID, "textures/blocks/ModelAxle.png");
	public static final ResourceLocation texture_Wheel = new ResourceLocation(References.MODID, "textures/blocks/ModelWheel.png");
	private ModelAxle modelAxle;
	private ModelWheel modelWheel;

	public RenderTileEntityAxle() {
		modelAxle = new ModelAxle();
		modelWheel = new ModelWheel();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTick, int p_180535_9_) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
		GL11.glPushMatrix();

		TileEntityAxle axle = (TileEntityAxle) tileentity;
		IBlockState state = tileentity.getWorld().getBlockState(tileentity.getPos());
		if (state.getBlock() != ModBlocks.axle)
			return;

		if (state.getValue(BlockAxle.VARIANT) == Boolean.FALSE) {
			this.bindTexture(texture_Axle);
			this.modelAxle.renderModel(0.0625F, axle.getPowerNetwork().getAngle(partialTick));
		}
		if (state.getValue(BlockAxle.VARIANT) == Boolean.TRUE) {
			this.bindTexture(texture_Axle);
			this.modelAxle.renderModel(0.0625F, axle.getPowerNetwork().getAngle(partialTick));
			this.bindTexture(texture_Wheel);
			this.modelWheel.renderModel(0.0625F, axle.getPowerNetwork().getAngle(partialTick));
		}
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x+0.5F, (float) y+0.5F, (float) z+0.5F);
		if (state.getValue(BlockAxle.VARIANT) == Boolean.TRUE) {
			if (ItemBelt.isConnected(getWorld(), axle)) {
				// Use degree, not radian
				double offsetX = axle.getConnectedAxle().getX()-axle.getPos().getX();
				double offsetZ = axle.getConnectedAxle().getZ()-axle.getPos().getZ();
				double externalAngle = (Math.toDegrees(Math.atan2(offsetZ, offsetX))+360)%360;
				double distance = Math.sqrt(axle.getConnectedAxle().distanceSq(axle.getPos()));
				float internalAngle = (axle.getPowerNetwork().getAngle(partialTick)+90)%360;
				GL11.glRotatef(-internalAngle, 0, 1.0F, 0);
				externalAngle=(externalAngle-internalAngle+360)%360;
				GL11.glRotatef((float) (-45*((int)externalAngle/45)), 0, 1.0F, 0);
				externalAngle=(externalAngle-45*((int)externalAngle/45)+360)%360;
				
				Tessellator tessellator = Tessellator.getInstance();
				WorldRenderer renderer = tessellator.getWorldRenderer();
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_CULL_FACE);
				GL11.glColor4f(0.78F, 0.36F, 0.21F, 1);
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				drawBelt(tessellator,renderer,2/16.0,-5/16.0,-2/16.0,-5/16.0);
				drawBelt(tessellator,renderer,-2/16.0,-5/16.0,-5/16.0,-2/16.0);
				drawBelt(tessellator,renderer,-5/16.0,-2/16.0,-5/16.0,+2/16.0);
				drawBelt(tessellator,renderer,-5/16.0,+2/16.0,-2/16.0,+5/16.0);
				drawBelt(tessellator,renderer,2/16.0,-5/16.0,2/16.0+distance/2.0*Math.cos(Math.toRadians(externalAngle)),-5/16.0+distance/2.0*Math.sin(Math.toRadians(externalAngle)));
				drawBelt(tessellator,renderer,-2/16.0,+5/16.0,-2/16.0+distance/2.0*Math.cos(Math.toRadians(externalAngle)),+5/16.0+distance/2.0*Math.sin(Math.toRadians(externalAngle)));
				GL11.glEnable(GL11.GL_CULL_FACE);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glColor4f(1, 1, 1, 1);
			}
		}
		GL11.glPopMatrix();
	}
	
	public void drawBelt(Tessellator tessellator, WorldRenderer renderer, double x1, double z1, double x2, double z2) {
		renderer.startDrawingQuads();
		renderer.addVertex(x1, 2/16.0, z1);
		renderer.addVertex(x1, -2/16.0, z1);
		renderer.addVertex(x2, -2/16.0, z2);
		renderer.addVertex(x2, 2/16.0, z2);
		tessellator.draw();
	}

}
