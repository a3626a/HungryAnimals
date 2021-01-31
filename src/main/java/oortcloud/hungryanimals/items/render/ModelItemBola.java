package oortcloud.hungryanimals.items.render;

import java.util.List;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.MobEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.items.ModItems;

public class ModelItemBola implements IBakedModel {

	private IBakedModel model_normal;
	private IBakedModel model_spin;
	private float angleMainhand;

	public static final ModelResourceLocation modelresourcelocation_spin = new ModelResourceLocation(
			References.RESOURCESPREFIX + Strings.itemBolaName + "_spin", "inventory");
	public static final ModelResourceLocation modelresourcelocation_normal = new ModelResourceLocation(ModItems.bola.getRegistryName(), "inventory");

	private static final float radiusFirst = (float) (Math.sqrt(2) * 6);

	public ModelItemBola(IBakedModel model, IBakedModel spin) {
		this.model_normal = model;
		this.model_spin = spin;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, long rand) {
		return model_spin.getQuads(state, side, rand);
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new ItemOverrideList(ImmutableList.of()) {
			@Override
			public IBakedModel handleItemState(IBakedModel originalModel, ItemStack itemStack, @Nullable World world, @Nullable MobEntityBase entity) {
				EntityPlayer player = Minecraft.getMinecraft().player;
				if (player.getActiveItemStack() == itemStack) {
					int inuseTick = itemStack.getMaxItemUseDuration() - player.getItemInUseCount();
					angleMainhand = 0.3f * inuseTick * inuseTick + 10 * inuseTick;
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
	public TextureAtlasSprite getParticleTexture() {
		return model_spin.getParticleTexture();
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		if (cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND || cameraTransformType == TransformType.THIRD_PERSON_RIGHT_HAND) {
			Matrix4f mat = (Matrix4f) model_spin.handlePerspective(cameraTransformType).getRight().clone();
			Matrix4f rot = new Matrix4f();
			float theta = angleMainhand;
			float radian = (float) Math.toRadians(45+theta);
			rot.rotZ((float) Math.toRadians(angleMainhand));
			rot.setTranslation(new Vector3f(
					(float) (radiusFirst * Math.cos(radian) / 16.0F) - (float) (radiusFirst * Math.cos(Math.toRadians(45)) / 16.0F),
					(float) (radiusFirst * Math.sin(radian) / 16.0F) - (float) (radiusFirst * Math.sin(Math.toRadians(45)) / 16.0F),
					0));
			mat.mul(rot);
			return Pair.of(this, mat);
		}
		if (cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND || cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND) {
			Matrix4f mat = (Matrix4f) model_spin.handlePerspective(cameraTransformType).getRight().clone();
			Matrix4f rot = new Matrix4f();
			float theta = -angleMainhand;
			float radian = (float) Math.toRadians(135+theta);
			rot.rotZ((float) Math.toRadians(theta));
			rot.setTranslation(new Vector3f(
					(float) (radiusFirst * Math.cos(radian) / 16.0F) - (float) (radiusFirst * Math.cos(Math.toRadians(135)) / 16.0F),
					(float) (radiusFirst * Math.sin(radian) / 16.0F) - (float) (radiusFirst * Math.sin(Math.toRadians(135)) / 16.0F),
					0));
					
			mat.mul(rot);
			return Pair.of(this, mat);
		}
		return model_normal.handlePerspective(cameraTransformType);
	}

}
