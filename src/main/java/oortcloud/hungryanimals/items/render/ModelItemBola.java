package oortcloud.hungryanimals.items.render;

import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.utils.CameraTransformUtil;

public class ModelItemBola implements IPerspectiveAwareModel {

	private IPerspectiveAwareModel model_normal;
	private IPerspectiveAwareModel model_spin;
	private float angle;

	public static final ModelResourceLocation modelresourcelocation_spin = new ModelResourceLocation(
			References.RESOURCESPREFIX + Strings.itemBolaName + "_spin", "inventory");
	public static final ModelResourceLocation modelresourcelocation_normal = new ModelResourceLocation(
			References.RESOURCESPREFIX + Strings.itemBolaName, "inventory");

	private static final float radiusFirst = (float) (Math.sqrt(2) * 6);
	private static final float radiusThird = (float) (Math.sqrt(2) * 2.25);

	public ModelItemBola(IPerspectiveAwareModel model) {
		this.model_normal = model;
		this.model_spin = (IPerspectiveAwareModel) Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.getModelManager().getModel(modelresourcelocation_spin);
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return model_spin.getQuads(state, side, rand);
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new ItemOverrideList(ImmutableList.of()) {
			@Override
			public IBakedModel handleItemState(IBakedModel originalModel, ItemStack itemStack, World world,
					EntityLivingBase entity) {
				EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				if (player.getHeldItemMainhand() == itemStack) {
					int inuseTick = itemStack.getMaxItemUseDuration() - player.getItemInUseCount();
					angle = 0.3f * inuseTick * inuseTick + 10 * inuseTick;
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
		return model_spin.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return model_spin.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return model_spin.isBuiltInRenderer();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return model_spin.getItemCameraTransforms();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return model_spin.getParticleTexture();
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		if (cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND) {
			Vector3f rotation = new Vector3f(0, -135, angle);
			Vector3f translation = new Vector3f(0,
					(float) ((2.5 + radiusFirst * Math.cos(Math.toRadians(angle + 45))) / 16.0F),
					(float) ((4.5 - radiusFirst * Math.sin(Math.toRadians(angle + 45))) / 16.0F));
			return Pair.of(this, CameraTransformUtil.getMatrix4fFromCamearaTransform(rotation, translation, 1.7F));
		}
		if (cameraTransformType == TransformType.THIRD_PERSON_RIGHT_HAND) {
			Vector3f rotation = new Vector3f(0, 90, angle);
			Vector3f translation = new Vector3f(-1.25F / 16.0F,
					(float) ((1.25 + radiusThird * Math.cos(Math.toRadians(angle + 45))) / 16.0F),
					(float) ((-0.5 + radiusThird * Math.sin(Math.toRadians(angle + 45))) / 16.0F));
			return Pair.of(this, CameraTransformUtil.getMatrix4fFromCamearaTransform(rotation, translation, 0.55F));
		}
		return model_normal.handlePerspective(cameraTransformType);
	}

}
