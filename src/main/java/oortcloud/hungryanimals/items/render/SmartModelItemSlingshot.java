/**
 * I referred the code of TheGreyGhost.
 * https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe15_item_smartitemmodel/ChessboardSmartItemModel.java
 */

package oortcloud.hungryanimals.items.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.items.ItemSlingShot;

import com.google.common.primitives.Ints;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class SmartModelItemSlingshot implements ISmartItemModel {

	public static final ModelResourceLocation modelresourcelocation_shooting = new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemSlingShotName + "_shooting", "inventory");
	public static final ModelResourceLocation modelresourcelocation = new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemSlingShotName, "inventory");
	public static final ResourceLocation textureresourcelocation = new ResourceLocation(References.RESOURCESPREFIX + "items/" + Strings.itemSlingShotName + "_string");

	private IBakedModel parent;
	private IBakedModel iBakedModel;
	private BakedQuad leftString;
	private BakedQuad rightString;
	private TextureAtlasSprite texture;

	public SmartModelItemSlingshot(IBakedModel iBakedModel) {
		this.parent = iBakedModel;
		this.iBakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(modelresourcelocation_shooting);
		this.texture = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getTextureMap().getAtlasSprite(SmartModelItemSlingshot.textureresourcelocation.toString());
		float length = 0.6F;
		this.leftString = new BakedQuad(Ints.concat(vertexToInts(12.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 16, 0), vertexToInts(10.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 0, 0),
				vertexToInts(7.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 0, 32), vertexToInts(9.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 16, 32)), 0, EnumFacing.UP);

		this.rightString = new BakedQuad(Ints.concat(vertexToInts(6.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 16, 0), vertexToInts(4.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 0, 0),
				vertexToInts(7.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 0, 32), vertexToInts(9.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 16, 32)), 0, EnumFacing.UP);
	}

	@Override
	public List getFaceQuads(EnumFacing p_177551_1_) {
		return iBakedModel.getFaceQuads(p_177551_1_);
	}

	@Override
	public List getGeneralQuads() {
		List<BakedQuad> combinedQuadsList = new ArrayList(iBakedModel.getGeneralQuads());
		combinedQuadsList.add(leftString);
		combinedQuadsList.add(rightString);
		return combinedQuadsList;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return iBakedModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return iBakedModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return iBakedModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getTexture() {
		return iBakedModel.getTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return iBakedModel.getItemCameraTransforms();
	}

	@Override
	public IBakedModel handleItemState(ItemStack itemStack) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		int inuseTick = itemStack.getMaxItemUseDuration() - player.getItemInUseCount();
		float length = (float) (inuseTick / 150.0 + 0.9);

		this.leftString = new BakedQuad(Ints.concat(vertexToInts(12.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 16, 0), vertexToInts(10.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 0, 0),
				vertexToInts(7.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 0, 16), vertexToInts(9.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 16, 16)), 0, EnumFacing.UP);

		this.rightString = new BakedQuad(Ints.concat(vertexToInts(6.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 16, 0), vertexToInts(4.0F / 16.0F, 13.0F / 16.0F, 8 / 16.0F, Color.WHITE.getRGB(), 0, 0),
				vertexToInts(7.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 0, 16), vertexToInts(9.0F / 16.0F, 13.0F / 16.0F, length, Color.WHITE.getRGB(), 16, 16)), 0, EnumFacing.UP);

		if (inuseTick == itemStack.getMaxItemUseDuration()) {
			return this.parent;
		} else {
			return this;
		}

	}

	private int[] vertexToInts(float x, float y, float z, int color, float u, float v) {
		return new int[] { Float.floatToRawIntBits(x), Float.floatToRawIntBits(y), Float.floatToRawIntBits(z), color, Float.floatToRawIntBits(texture.getInterpolatedU(u)), Float.floatToRawIntBits(texture.getInterpolatedV(v)), 0 };
	}

}
