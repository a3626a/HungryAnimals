package oortcloud.hungryanimals.blocks.render;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.blocks.BlockTrough;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.tileentities.TileEntityTrough;

import org.lwjgl.opengl.GL11;

public class RenderTileEntityTrough extends TileEntitySpecialRenderer {

	public static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/blocks/ModelTrough.png");
	private ModelTrough model;
	
	public RenderTileEntityTrough() {
		model = new ModelTrough();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y,
			double z, float partialTick, int parInt) {
		
		TileEntityTrough foodbox = (TileEntityTrough) tileentity;
		IBlockState state = tileentity.getWorld().getBlockState(tileentity.getPos());
		if (state.getBlock() != ModBlocks.foodBox) return;
		EnumFacing rot = (EnumFacing)state.getValue(BlockTrough.FACING);
		int rotation = rot.getHorizontalIndex();
		
		GL11.glPushMatrix();
			GL11.glTranslatef((float)x+0.5F, (float)y+1.5F, (float)z+0.5F);
			GL11.glRotatef(-90*rotation, 0.0F, 1.0F, 0.0F);
			
			GL11.glTranslatef(0.0F, +0.0F, 0.0F);
			GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
			GL11.glPushMatrix();
			
			this.bindTexture(texture);
			this.model.renderModel(0.0625F);
			
			GL11.glPopMatrix();
		GL11.glPopMatrix();
		
		if (foodbox.stack != null) {
			ItemStack stack = foodbox.stack.copy();
			int num = stack.stackSize;
			stack.stackSize = 1;
			EntityItem item = new EntityItem(tileentity.getWorld(), 0, 0, 0, stack);
			item.hoverStart = (float) (rotation * Math.PI / 2);
			
			float interval = (float) ((2 - 2*0.25)/16.0F);
			
			RenderManager render = Minecraft.getMinecraft().getRenderManager();
			
			switch(rotation) {
			case 0 :
				for (int i = 0 ; i < num; i ++) {
					render.doRenderEntity(item,
							x+0.5+foodbox.random[i][0], y+0.25, z+0.25 + i*interval+foodbox.random[i][1], 0, 0, false);
				}
				break;
			case 1 :
				for (int i = 0 ; i < num; i ++) {
					render.doRenderEntity(item,
							x+0.75 - i*interval+foodbox.random[i][0], y+0.25, z+0.5+foodbox.random[i][1], 0, 0, false);
				}
				break;
			case 2 :
				for (int i = 0 ; i < num; i ++) {
					render.doRenderEntity(item,
							x+0.5+foodbox.random[i][0], y+0.25, z+0.75 - i*interval+foodbox.random[i][1], 0, 0, false);
				}
				break;
			case 3 :
				for (int i = 0 ; i < num; i ++) {
					render.doRenderEntity(item,
							x+0.25 + i*interval+foodbox.random[i][0], y+0.25, z+0.5+foodbox.random[i][1], 0, 0, false);
				}
				break;
			}
			
		}

	}

}
