package oortcloud.hungryanimals.blocks.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAxle extends ModelBase {
	ModelRenderer Axle;

	public ModelAxle() {
		textureWidth = 128;
		textureHeight = 64;

		Axle = new ModelRenderer(this, 0, 7);
		Axle.addBox(-1F, 0F, -1F, 2, 16, 2);
		Axle.setRotationPoint(0F, 8F, 0F);
		Axle.setTextureSize(128, 64);
		Axle.mirror = true;
		setRotation(Axle, 0F, 0F, 0F);
	}

	
	
	public void render(Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Axle.render(f5);
	}

	public void renderModel(float f, float angle) {
		setRotation(Axle, 0, (float)((angle)*Math.PI/180), 0);
		Axle.render(f);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3,
			float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

}
