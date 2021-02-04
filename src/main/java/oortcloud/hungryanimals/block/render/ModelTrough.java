package oortcloud.hungryanimals.block.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTrough extends ModelBase {
	ModelRenderer bottom;
	ModelRenderer long1;
	ModelRenderer long2;
	ModelRenderer short1;
	ModelRenderer short2;

	public ModelTrough() {
		textureWidth = 256;
		textureHeight = 128;

		bottom = new ModelRenderer(this, 0, 0);
		bottom.addBox(0F, 0F, 0F, 16, 2, 32);
		bottom.setRotationPoint(-8F, 22F, -8F);
		bottom.setTextureSize(256, 128);
		bottom.mirror = true;
		setRotation(bottom, 0F, 0F, 0F);
		long1 = new ModelRenderer(this, 0, 36);
		long1.addBox(0F, 0F, 0F, 2, 6, 32);
		long1.setRotationPoint(-8F, 16F, -8F);
		long1.setTextureSize(256, 128);
		long1.mirror = true;
		setRotation(long1, 0F, 0F, 0F);
		long2 = new ModelRenderer(this, 0, 76);
		long2.addBox(0F, 0F, 0F, 2, 6, 32);
		long2.setRotationPoint(6F, 16F, -8F);
		long2.setTextureSize(256, 128);
		long2.mirror = true;
		setRotation(long2, 0F, 0F, 0F);
		short1 = new ModelRenderer(this, 70, 55);
		short1.addBox(0F, 0F, 0F, 12, 6, 2);
		short1.setRotationPoint(-6F, 16F, 22F);
		short1.setTextureSize(256, 128);
		short1.mirror = true;
		setRotation(short1, 0F, 0F, 0F);
		short2 = new ModelRenderer(this, 70, 40);
		short2.addBox(0F, 0F, 0F, 12, 6, 2);
		short2.setRotationPoint(-6F, 16F, -8F);
		short2.setTextureSize(256, 128);
		short2.mirror = true;
		setRotation(short2, 0F, 0F, 0F);
	}

	public void renderModel(float f) {
		bottom.render(f);
		long1.render(f);
		long2.render(f);
		short1.render(f);
		short2.render(f);
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		bottom.render(f5);
		long1.render(f5);
		long2.render(f5);
		short1.render(f5);
		short2.render(f5);
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
