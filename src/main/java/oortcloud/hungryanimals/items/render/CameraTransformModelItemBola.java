package oortcloud.hungryanimals.items.render;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;

import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.utils.CameraTransformUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BreakingFour;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.client.model.ItemLayerModel.BakedModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

public class CameraTransformModelItemBola implements ISmartItemModel, IPerspectiveAwareModel {

	private BakedModel model_normal;
	private BakedModel model_spin;
	private float angle;

	public static final ModelResourceLocation modelresourcelocation_spin = new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemBolaName + "_spin", "inventory");
	public static final ModelResourceLocation modelresourcelocation_normal = new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemBolaName, "inventory");

	private static final float radiusFirst = (float) (Math.sqrt(2) * 6);
	private static final float radiusThird = (float) (Math.sqrt(2) * 2.25);

	public CameraTransformModelItemBola(BakedModel model) {
		this.model_normal = model;
		this.model_spin = (BakedModel) Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(modelresourcelocation_spin);
	}

	public List getFaceQuads(EnumFacing p_177551_1_) {
		return model_spin.getFaceQuads(p_177551_1_);
	}

	public List getGeneralQuads() {
		return model_spin.getGeneralQuads();
	}

	public boolean isAmbientOcclusion() {
		return model_spin.isAmbientOcclusion();
	}

	public boolean isGui3d() {
		return model_spin.isGui3d();
	}

	public boolean isBuiltInRenderer() {
		return model_spin.isBuiltInRenderer();
	}

	public TextureAtlasSprite getTexture() {
		return model_spin.getTexture();
	}

	public ItemCameraTransforms getItemCameraTransforms() {
		return model_spin.getItemCameraTransforms();
	}

	@Override
	public Pair<IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		if (cameraTransformType == TransformType.FIRST_PERSON) {
			Vector3f rotation = new Vector3f(0,-135,angle);
			Vector3f translation = new Vector3f(0, (float) ((2.5 + radiusFirst * Math.cos(Math.toRadians(angle + 45))) / 16.0F), (float) ((4.5 - radiusFirst * Math.sin(Math.toRadians(angle + 45))) / 16.0F));
			return Pair.of((IBakedModel) this, CameraTransformUtil.getMatrix4fFromCamearaTransform(rotation, translation, 1.7F));
		}
		if (cameraTransformType == TransformType.THIRD_PERSON) {
			Vector3f rotation = new Vector3f(0,90,angle);
			Vector3f translation = new Vector3f(-1.25F / 16.0F, (float) ((1.25 + radiusThird * Math.cos(Math.toRadians(angle + 45))) / 16.0F),
					(float) ((-0.5 + radiusThird * Math.sin(Math.toRadians(angle + 45))) / 16.0F));
			return Pair.of((IBakedModel) this, CameraTransformUtil.getMatrix4fFromCamearaTransform(rotation, translation, 0.55F));
		}
		return model_normal.handlePerspective(cameraTransformType);
	}

	@Override
	public IBakedModel handleItemState(ItemStack itemStack) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if (player.getHeldItem() == itemStack) {
			int inuseTick = itemStack.getMaxItemUseDuration() - player.getItemInUseCount();
			this.angle = 0.3f * inuseTick * inuseTick + 10 * inuseTick;
			if (inuseTick == itemStack.getMaxItemUseDuration()) {
				return model_normal;
			} else {
				return this;
			}
		} else {
			return model_normal;
		}
	}

}
