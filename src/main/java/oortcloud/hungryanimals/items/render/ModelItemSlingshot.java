/**
 * I referred the code of TheGreyGhost.
 * https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe15_item_smartitemmodel/ChessboardSmartItemModel.java
 */

package oortcloud.hungryanimals.items.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;

public class ModelItemSlingshot implements IBakedModel {

	public static final ModelResourceLocation modelresourcelocation_shooting = new ModelResourceLocation(
			References.RESOURCESPREFIX + Strings.itemSlingShotName + "_shooting", "inventory");
	public static final ModelResourceLocation modelresourcelocation_normal = new ModelResourceLocation(
			References.RESOURCESPREFIX + Strings.itemSlingShotName, "inventory");
	public static final ResourceLocation textureresourcelocation = new ResourceLocation(
			References.RESOURCESPREFIX + "items/" + Strings.itemSlingShotName + "_string");

	private IBakedModel model_normal;
	private IBakedModel model_shooting;
	private BakedQuad leftString;
	private BakedQuad rightString;
	private TextureAtlasSprite texture;

	public ModelItemSlingshot(IBakedModel model_normal, IBakedModel model_shooting, TextureAtlasSprite texture) {
		this.model_normal = model_normal;
		this.model_shooting = model_shooting;
		this.texture = texture;
		float length = 0.6F;
		this.leftString = new BakedQuad(
				Ints.concat(vertexToInts(12.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 16, 0),
						vertexToInts(10.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 0, 0),
						vertexToInts(7.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 0, 32),
						vertexToInts(9.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 16, 32)),
				0, EnumFacing.UP, texture, true, net.minecraft.client.renderer.vertex.DefaultVertexFormats.ITEM);

		this.rightString = new BakedQuad(
				Ints.concat(vertexToInts(6.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 16, 0),
						vertexToInts(4.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 0, 0),
						vertexToInts(7.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 0, 32),
						vertexToInts(9.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 16, 32)),
				0, EnumFacing.UP, texture, true, net.minecraft.client.renderer.vertex.DefaultVertexFormats.ITEM);
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		List<BakedQuad> combinedQuadsList = new ArrayList<BakedQuad>(model_shooting.getQuads(state, side, rand));
		combinedQuadsList.add(leftString);
		combinedQuadsList.add(rightString);
		return combinedQuadsList;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new ItemOverrideList(ImmutableList.of()) {
			@Override
			public IBakedModel handleItemState(IBakedModel originalModel, ItemStack itemStack, World world,
					EntityLivingBase entity) {
				EntityPlayer player = Minecraft.getMinecraft().player;
				if (player.getActiveItemStack() == itemStack) {
					int inuseTick = itemStack.getMaxItemUseDuration() - player.getItemInUseCount();
					float length = (float) (inuseTick / 150.0 + 0.9);

					leftString = new BakedQuad(
							Ints.concat(vertexToInts(12.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 16, 0),
									vertexToInts(10.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 0, 0),
									vertexToInts(7.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 0, 16),
									vertexToInts(9.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 16, 16)),
							0, EnumFacing.UP, texture, true, net.minecraft.client.renderer.vertex.DefaultVertexFormats.ITEM);

					rightString = new BakedQuad(
							Ints.concat(vertexToInts(6.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 16, 0),
									vertexToInts(4.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 0, 0),
									vertexToInts(7.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 0, 16),
									vertexToInts(9.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 16, 16)),
							0, EnumFacing.UP, texture, true, net.minecraft.client.renderer.vertex.DefaultVertexFormats.ITEM);

					if (inuseTick == itemStack.getMaxItemUseDuration()) {
						return model_normal;
					} else {
						return originalModel;
					}
				} else {
					return model_normal;
				}
			}
		};
	}

	@Override
	public boolean isAmbientOcclusion() {
		return model_shooting.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return model_shooting.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return model_shooting.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return model_shooting.getParticleTexture();
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		if (cameraTransformType == TransformType.GUI) {
			return model_normal.handlePerspective(cameraTransformType);
		} else {
			return Pair.of(this, model_shooting.handlePerspective(cameraTransformType).getRight());
		}
	}

	private int[] vertexToInts(float x, float y, float z, int color, float u, float v) {
		return new int[] { Float.floatToRawIntBits(x), Float.floatToRawIntBits(y), Float.floatToRawIntBits(z), color,
				Float.floatToRawIntBits(texture.getInterpolatedU(u)),
				Float.floatToRawIntBits(texture.getInterpolatedV(v)), 0 };
	}

}
