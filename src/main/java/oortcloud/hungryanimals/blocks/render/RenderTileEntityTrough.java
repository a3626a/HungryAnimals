package oortcloud.hungryanimals.blocks.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.blocks.BlockTrough;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.tileentities.TileEntityTrough;

public class RenderTileEntityTrough extends TileEntitySpecialRenderer<TileEntityTrough> {

	public static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/blocks/modeltrough.png");
	private ModelTrough model;

	public RenderTileEntityTrough() {
		model = new ModelTrough();
	}

	@Override
	public void render(TileEntityTrough foodbox, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		BlockState state = foodbox.getWorld().getBlockState(foodbox.getPos());
		if (state.getBlock() != ModBlocks.trough)
			return;
		Direction rot = (Direction) state.getValue(BlockTrough.FACING);
		int rotation = rot.getHorizontalIndex();

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glRotatef(-90 * rotation, 0.0F, 1.0F, 0.0F);

		GL11.glTranslatef(0.0F, +0.0F, 0.0F);
		GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
		GL11.glPushMatrix();

		this.bindTexture(texture);
		this.model.renderModel(0.0625F);

		GL11.glPopMatrix();
		GL11.glPopMatrix();

		if (!foodbox.stack.isEmpty()) {
			ItemStack stack = foodbox.stack.copy();
			int num = stack.getCount();
			stack.setCount(1);
			EntityItem item = new EntityItem(foodbox.getWorld(), 0, 0, 0, stack);
			item.hoverStart = (float) (rotation * Math.PI / 2);

			float interval = (float) ((2 - 2 * 0.25) / 16.0F);

			RenderManager render = Minecraft.getMinecraft().getRenderManager();

			switch (rotation) {
			case 0:
				for (int i = 0; i < num; i++) {
					render.renderEntity(item, x + 0.5 + foodbox.random[i][0], y + 0.25, z + 0.25 + i * interval + foodbox.random[i][1], 0, 0, false);
				}
				break;
			case 1:
				for (int i = 0; i < num; i++) {
					render.renderEntity(item, x + 0.75 - i * interval + foodbox.random[i][0], y + 0.25, z + 0.5 + foodbox.random[i][1], 0, 0, false);
				}
				break;
			case 2:
				for (int i = 0; i < num; i++) {
					render.renderEntity(item, x + 0.5 + foodbox.random[i][0], y + 0.25, z + 0.75 - i * interval + foodbox.random[i][1], 0, 0, false);
				}
				break;
			case 3:
				for (int i = 0; i < num; i++) {
					render.renderEntity(item, x + 0.25 + i * interval + foodbox.random[i][0], y + 0.25, z + 0.5 + foodbox.random[i][1], 0, 0, false);
				}
				break;
			}

		}

	}

}
