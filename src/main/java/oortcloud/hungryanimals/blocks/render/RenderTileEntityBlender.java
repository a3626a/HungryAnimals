package oortcloud.hungryanimals.blocks.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.tileentities.TileEntityBlender;

import org.lwjgl.opengl.GL11;

public class RenderTileEntityBlender extends TileEntitySpecialRenderer {

	public static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/blocks/ModelBlender.png");
	private ModelBlender modelBlender;

	public RenderTileEntityBlender() {
		modelBlender = new ModelBlender();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTick, int parInt) {

		TileEntityBlender blender = (TileEntityBlender) tileentity;

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
		GL11.glPushMatrix();

		this.bindTexture(texture);

		this.modelBlender.renderModel(0.0625F, blender.getPowerNetwork().getAngle(partialTick));

		GL11.glPopMatrix();
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);

		GL11.glRotatef(-blender.getPowerNetwork().getAngle(partialTick), 0.0F, 1.0F, 0.0F);

		for (int i = 0; i < blender.getSizeInventory(); i++) {
			ItemStack stack = blender.getStackInSlot(i);
			if (stack != null) {
				EntityItem item = new EntityItem(tileentity.getWorld(), 0, 0, 0, stack);
				item.hoverStart = blender.getPowerNetwork().getAngle(partialTick) / 20.0F;
				RenderManager render = Minecraft.getMinecraft().getRenderManager();
				switch (i) {
				case 0:
					render.doRenderEntity(item, +0.25, 0.1, +0.25, 0, 0, false);
					break;
				case 1:
					render.doRenderEntity(item, -0.25, 0.1, +0.25, 0, 0, false);
					break;
				case 2:
					render.doRenderEntity(item, -0.25, 0.1, -0.25, 0, 0, false);
					break;
				case 3:
					render.doRenderEntity(item, +0.25, 0.1, -0.25, 0, 0, false);
					break;
				}
			}
		}

		GL11.glPopMatrix();

	}

}
