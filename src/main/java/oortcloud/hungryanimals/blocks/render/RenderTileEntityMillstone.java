package oortcloud.hungryanimals.blocks.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.blocks.BlockMillstone;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.tileentities.TileEntityMillstone;

import org.lwjgl.opengl.GL11;

public class RenderTileEntityMillstone extends TileEntitySpecialRenderer {

	public static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/blocks/ModelMillstone.png");
	private ModelMillstone modelMillstone;

	public RenderTileEntityMillstone() {
		modelMillstone = new ModelMillstone();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTick, int parInt) {
		TileEntityMillstone millstone = (TileEntityMillstone) tileentity;
		int rotation = ((EnumFacing) tileentity.getWorld().getBlockState(tileentity.getPos()).getValue(BlockMillstone.FACING)).getHorizontalIndex();

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glRotatef(-90 * rotation, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
		GL11.glPushMatrix();

		this.bindTexture(texture);
		float height = (float) millstone.getHeight();
		float angle = millstone.getPowerNetwork().getAngle(partialTick);
		this.modelMillstone.renderModel(0.0625F, angle, height);

		GL11.glPopMatrix();
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
		GL11.glPushMatrix();
		if (millstone.getStackInSlot(0) != null) {
			ItemStack stack = millstone.getStackInSlot(0).copy();
			EntityItem item = new EntityItem(tileentity.getWorld(), 0, 0, 0, stack);
			item.hoverStart = angle / 20.F;

			RenderManager render = Minecraft.getMinecraft().getRenderManager();

			render.doRenderEntity(item, 0.5, 0, 0, 0, 0, false);
			render.doRenderEntity(item, -0.5, 0, 0, 0, 0, false);
			render.doRenderEntity(item, 0, 0, 0.5, 0, 0, false);
			render.doRenderEntity(item, 0, 0, -0.5, 0, 0, false);
		}
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

}
