package oortcloud.hungryanimals.blocks.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWheel_ extends ModelBase {
	ModelRenderer Wheel1;
	ModelRenderer Wheel2;
	ModelRenderer Wheel3;
	ModelRenderer Wheel4;

	public ModelWheel_() {
		textureWidth = 128;
		textureHeight = 64;

		Wheel1 = new ModelRenderer(this, 0, 0);
		Wheel1.addBox(-5F, 0F, -2F, 10, 2, 4);
		Wheel1.setRotationPoint(0F, 15F, 0F);
		Wheel1.setTextureSize(128, 64);
		Wheel1.mirror = true;
		setRotation(Wheel1, 0F, 0F, 0F);
		Wheel2 = new ModelRenderer(this, 0, 0);
		Wheel2.addBox(-5F, 0F, -2F, 10, 2, 4);
		Wheel2.setRotationPoint(0F, 15F, 0F);
		Wheel2.setTextureSize(128, 64);
		Wheel2.mirror = true;
		setRotation(Wheel2, 0F, 0.7853982F, 0F);
		Wheel3 = new ModelRenderer(this, 0, 0);
		Wheel3.addBox(-5F, 0F, -2F, 10, 2, 4);
		Wheel3.setRotationPoint(0F, 15F, 0F);
		Wheel3.setTextureSize(128, 64);
		Wheel3.mirror = true;
		setRotation(Wheel3, 0F, 1.570796F, 0F);
		Wheel4 = new ModelRenderer(this, 0, 0);
		Wheel4.addBox(-5F, 0F, -2F, 10, 2, 4);
		Wheel4.setRotationPoint(0F, 15F, 0F);
		Wheel4.setTextureSize(128, 64);
		Wheel4.mirror = true;
		setRotation(Wheel4, 0F, 2.356194F, 0F);
	}

	public void renderModel(float f, float angle) {
		setRotation(Wheel1, 0, (float)((angle)*Math.PI/180), 0);
		setRotation(Wheel2, 0, (float)((angle)*Math.PI/180) + 0.7853982F, 0);
		setRotation(Wheel3, 0, (float)((angle)*Math.PI/180) + 1.570796F, 0);
		setRotation(Wheel4, 0, (float)((angle)*Math.PI/180) + 2.356194F, 0);
		Wheel1.render(f);
		Wheel2.render(f);
		Wheel3.render(f);
		Wheel4.render(f);
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Wheel1.render(f5);
		Wheel2.render(f5);
		Wheel3.render(f5);
		Wheel4.render(f5);
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
