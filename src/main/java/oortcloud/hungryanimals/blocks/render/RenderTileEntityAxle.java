package oortcloud.hungryanimals.blocks.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.blocks.BlockAxle;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.tileentities.TileEntityAxle;
import oortcloud.hungryanimals.tileentities.TileEntityEnergyTransporter;

import org.lwjgl.opengl.GL11;

public class RenderTileEntityAxle extends TileEntitySpecialRenderer {

	public static final ResourceLocation texture_Axle = new ResourceLocation(
			References.MODID, "textures/blocks/ModelAxle.png");
	public static final ResourceLocation texture_Wheel = new ResourceLocation(
			References.MODID, "textures/blocks/ModelWheel.png");
	private ModelAxle modelAxle;
	private ModelWheel modelWheel;
	private ModelWheelBelt modelBelt;

	public RenderTileEntityAxle() {
		modelAxle = new ModelAxle();
		modelWheel = new ModelWheel();
		modelBelt = new ModelWheelBelt();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x,
			double y, double z, float partialTick, int p_180535_9_) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.0F, +0.0F, 0.0F);
		GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
		GL11.glPushMatrix();
		
		TileEntityAxle axle = (TileEntityAxle) tileentity;
		IBlockState state = tileentity.getWorld().getBlockState(tileentity.getPos());
		if (state.getBlock() != ModBlocks.axle) return;
		
		if (state.getValue(BlockAxle.VARIANT) == BlockAxle.EnumType.AXLE) {
			/*
			if (axle.lastRotating) {
				this.bindTexture(texture_Axle);
				this.modelAxle.renderModel(0.0625F, (tileentity.getWorld().getWorldTime()+partialTick)*9);
			} else {
				this.bindTexture(texture_Axle);
				this.modelAxle.renderModel(0.0625F, 0);	
			}
			*/
			this.bindTexture(texture_Axle);
			this.modelAxle.renderModel(0.0625F, axle.getNetwork().getAngle(partialTick));
		}
		if (state.getValue(BlockAxle.VARIANT) == BlockAxle.EnumType.WHEEL) {
			/*
			if (axle.lastRotating) {
				this.bindTexture(texture_Axle);
				this.modelAxle.renderModel(0.0625F, (tileentity.getWorld().getWorldTime()+partialTick)*9);
				this.bindTexture(texture_Wheel);
				this.modelWheel.renderModel(0.0625F, (tileentity.getWorld().getWorldTime()+partialTick)*9);
			} else {
				this.bindTexture(texture_Axle);
				this.modelAxle.renderModel(0.0625F, 0);
				this.bindTexture(texture_Wheel);
				this.modelWheel.renderModel(0.0625F, 0);
			}
			*/
			this.bindTexture(texture_Axle);
			this.modelAxle.renderModel(0.0625F, axle.getNetwork().getAngle(partialTick));
			this.bindTexture(texture_Wheel);
			this.modelWheel.renderModel(0.0625F, axle.getNetwork().getAngle(partialTick));
		}
		if (state.getValue(BlockAxle.VARIANT) == BlockAxle.EnumType.BELT) {
			/*
			if (((TileEntityEnergyTransporter)tileentity).lastRotating) {
				this.bindTexture(texture_Axle);
				this.modelAxle.renderModel(0.0625F, (tileentity.getWorld().getWorldTime()+partialTick)*9);
				this.bindTexture(texture_Wheel);
				this.modelWheel.renderModel(0.0625F, (tileentity.getWorld().getWorldTime()+partialTick)*9);
				this.bindTexture(texture_Axle);
				this.modelBelt.renderModel(0.0625F, ((EnumFacing)state.getValue(BlockAxle.FACING)).getHorizontalIndex()*90);
			} else {
				this.bindTexture(texture_Axle);
				this.modelAxle.renderModel(0.0625F, 0);	
				this.bindTexture(texture_Wheel);
				this.modelWheel.renderModel(0.0625F, 0);
				this.bindTexture(texture_Axle);
				this.modelBelt.renderModel(0.0625F, ((EnumFacing)state.getValue(BlockAxle.FACING)).getHorizontalIndex()*90);
			}
			*/
			this.bindTexture(texture_Axle);
			this.modelAxle.renderModel(0.0625F, axle.getNetwork().getAngle(partialTick));
			this.bindTexture(texture_Wheel);
			this.modelWheel.renderModel(0.0625F, axle.getNetwork().getAngle(partialTick));
			this.bindTexture(texture_Axle);
			this.modelBelt.renderModel(0.0625F, ((EnumFacing)state.getValue(BlockAxle.FACING)).getHorizontalIndex()*90);
		}

		GL11.glPopMatrix();
		GL11.glPopMatrix();
		
	}

}
