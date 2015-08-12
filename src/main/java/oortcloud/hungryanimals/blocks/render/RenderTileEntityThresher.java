package oortcloud.hungryanimals.blocks.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.blocks.BlockThresher;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.tileentities.TileEntityThresher;

import org.lwjgl.opengl.GL11;

public class RenderTileEntityThresher extends TileEntitySpecialRenderer {

	public static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/blocks/ModelThresher__.png");
	private ModelThresher__ modelThresher;

	public RenderTileEntityThresher() {
		modelThresher = new ModelThresher__();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTick, int parInt) {

		TileEntityThresher thresher = (TileEntityThresher) tileentity;
		float angle = thresher.getNetwork().getAngle(partialTick);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
		GL11.glPushMatrix();

		this.bindTexture(texture);
		this.modelThresher.renderModel(0.0625F, angle);

		GL11.glPopMatrix();
		GL11.glPopMatrix();

		if (thresher.getStackInSlot(0) != null) {
			ItemStack stack = thresher.getStackInSlot(0).copy();
			EntityItem item = new EntityItem(tileentity.getWorld(), 0, 0, 0, stack);
			item.hoverStart = angle / 20.F;

			RenderManager render = Minecraft.getMinecraft().getRenderManager();

			render.doRenderEntity(item, x + 0.5, y + 0.2, z + 0.5, 0, 0, false);
		}
	}

}
