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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.client.model.ItemLayerModel.BakedModel;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import sun.net.ftp.FtpClient.TransferType;

import com.google.common.primitives.Ints;

public class SmartModelItemSlingshot implements ISmartItemModel, IPerspectiveAwareModel {

	public static final ModelResourceLocation modelresourcelocation_shooting = new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemSlingShotName + "_shooting", "inventory");
	public static final ModelResourceLocation modelresourcelocation_normal = new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemSlingShotName, "inventory");
	public static final ResourceLocation textureresourcelocation = new ResourceLocation(References.RESOURCESPREFIX + "items/" + Strings.itemSlingShotName + "_string");

	private BakedModel model_normal;
	private BakedModel model_shooting;
	private BakedQuad leftString;
	private BakedQuad rightString;
	private TextureAtlasSprite texture;

	public SmartModelItemSlingshot(BakedModel iBakedModel) {
		this.model_normal = iBakedModel;
		this.model_shooting = (BakedModel) Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(modelresourcelocation_shooting);
		this.texture = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getTextureMap().getAtlasSprite(SmartModelItemSlingshot.textureresourcelocation.toString());
		float length = 0.6F;
		this.leftString = new BakedQuad(Ints.concat(vertexToInts(12.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 16, 0), vertexToInts(10.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 0, 0),
				vertexToInts(7.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 0, 32), vertexToInts(9.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 16, 32)), 0, EnumFacing.UP);

		this.rightString = new BakedQuad(Ints.concat(vertexToInts(6.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 16, 0), vertexToInts(4.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 0, 0),
				vertexToInts(7.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 0, 32), vertexToInts(9.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 16, 32)), 0, EnumFacing.UP);
	}

	@Override
	public List getFaceQuads(EnumFacing p_177551_1_) {
		return model_shooting.getFaceQuads(p_177551_1_);
	}

	@Override
	public List getGeneralQuads() {
		List<BakedQuad> combinedQuadsList = new ArrayList(model_shooting.getGeneralQuads());
		combinedQuadsList.add(leftString);
		combinedQuadsList.add(rightString);
		return combinedQuadsList;
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
	public TextureAtlasSprite getTexture() {
		return model_shooting.getTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return model_shooting.getItemCameraTransforms();
	}
	
	@Override
	public Pair<IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		if (cameraTransformType==TransformType.GUI) {
			return model_normal.handlePerspective(cameraTransformType);
		} else {
			return Pair.of((IBakedModel)this, model_shooting.handlePerspective(cameraTransformType).getRight());
		}
	}
	
	@Override
	public IBakedModel handleItemState(ItemStack itemStack) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if (player.getHeldItem() == itemStack) {
			int inuseTick = itemStack.getMaxItemUseDuration() - player.getItemInUseCount();
			float length = (float) (inuseTick / 150.0 + 0.9);

			this.leftString = new BakedQuad(Ints.concat(vertexToInts(12.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 16, 0), vertexToInts(10.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 0, 0),
					vertexToInts(7.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 0, 16), vertexToInts(9.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 16, 16)), 0, EnumFacing.UP);

			this.rightString = new BakedQuad(Ints.concat(vertexToInts(6.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 16, 0), vertexToInts(4.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 0, 0),
					vertexToInts(7.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 0, 16), vertexToInts(9.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 16, 16)), 0, EnumFacing.UP);

			if (inuseTick == itemStack.getMaxItemUseDuration()) {
				return model_normal;
			} else {
				return this;
			}
		} else {
			return model_normal;
		}
	}

	private int[] vertexToInts(float x, float y, float z, int color, float u, float v) {
		return new int[] { Float.floatToRawIntBits(x), Float.floatToRawIntBits(y), Float.floatToRawIntBits(z), color, Float.floatToRawIntBits(texture.getInterpolatedU(u)), Float.floatToRawIntBits(texture.getInterpolatedV(v)), 0 };
	}

}
