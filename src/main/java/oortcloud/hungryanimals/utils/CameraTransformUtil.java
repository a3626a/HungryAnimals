package oortcloud.hungryanimals.utils;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.TRSRTransformation;

public class CameraTransformUtil {
	public static Matrix4f getMatrix4fFromCamearaTransform(Vector3f rot, Vector3f trans, float scale) {
		Matrix3f rotationX = new Matrix3f();
		Matrix3f rotationY = new Matrix3f();
		Matrix3f rotationZ = new Matrix3f();
		rotationX.setIdentity();
		rotationY.setIdentity();
		rotationZ.setIdentity();
		rotationY.rotY((float) Math.toRadians(rot.y));
		rotationZ.rotZ((float) Math.toRadians(rot.z));
		rotationX.rotX((float) Math.toRadians(rot.x));
		rotationY.mul(rotationY, rotationZ);
		rotationY.mul(rotationY, rotationX);
		return new Matrix4f(rotationY, trans, scale);
	}
	
	
	public static Matrix4f getMatrix4f(IFlexibleBakedModel model, TransformType cameraTransformType) {
		ItemTransformVec3f trans = model.getItemCameraTransforms().getTransform(cameraTransformType);
		return TRSRTransformation.getMatrix(trans);
	}
}
