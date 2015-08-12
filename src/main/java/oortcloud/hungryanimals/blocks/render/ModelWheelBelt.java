package oortcloud.hungryanimals.blocks.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWheelBelt extends ModelBase {
	ModelRenderer Belt1;
	ModelRenderer Belt2;
	ModelRenderer Belt3;
	ModelRenderer Belt4;
	ModelRenderer Belt5;

	public ModelWheelBelt() {
		textureWidth = 128;
		textureHeight = 64;

		Belt1 = new ModelRenderer(this, 0, 26);
		Belt1.addBox(5F, 0F, -2.5F, 1, 2, 11);
		Belt1.setRotationPoint(0F, 15F, 0F);
		Belt1.setTextureSize(128, 64);
		Belt1.mirror = true;
		setRotation(Belt1, 0F, 0F, 0F);
		//Belt2.mirror = true;
		Belt2 = new ModelRenderer(this, 0, 40);
		Belt2.addBox(5F, 0F, -2.5F, 1, 2, 5);
		Belt2.setRotationPoint(0F, 15F, 0F);
		Belt2.setTextureSize(128, 64);
		Belt2.mirror = true;
		setRotation(Belt2, 0F, 0.7853982F, 0F);
		Belt2.mirror = false;
		Belt3 = new ModelRenderer(this, 0, 40);
		Belt3.addBox(5F, 0F, -2.5F, 1, 2, 5);
		Belt3.setRotationPoint(0F, 15F, 0F);
		Belt3.setTextureSize(128, 64);
		Belt3.mirror = true;
		setRotation(Belt3, 0F, 1.570796F, 0F);
		Belt4 = new ModelRenderer(this, 0, 40);
		Belt4.addBox(5F, 0F, -2.5F, 1, 2, 5);
		Belt4.setRotationPoint(0F, 15F, 0F);
		Belt4.setTextureSize(128, 64);
		Belt4.mirror = true;
		setRotation(Belt4, 0F, 2.356194F, 0F);
		Belt5 = new ModelRenderer(this, 0, 26);
		Belt5.addBox(-6F, 0F, -2.5F, 1, 2, 11);
		Belt5.setRotationPoint(0F, 15F, 0F);
		Belt5.setTextureSize(128, 64);
		Belt5.mirror = true;
		setRotation(Belt5, 0F, 0F, 0F);
	}

	public void renderModel(float f, float angle) {
		setRotation(Belt1, 0, (float)((angle)*Math.PI/180), 0);
		setRotation(Belt2, 0, (float)((angle+45)*Math.PI/180), 0);
		setRotation(Belt3, 0, (float)((angle+90)*Math.PI/180), 0);
		setRotation(Belt4, 0, (float)((angle+135)*Math.PI/180), 0);
		setRotation(Belt5, 0, (float)((angle)*Math.PI/180), 0);
		Belt1.render(f);
		Belt2.render(f);
		Belt3.render(f);
		Belt4.render(f);
		Belt5.render(f);
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Belt1.render(f5);
		Belt2.render(f5);
		Belt3.render(f5);
		Belt4.render(f5);
		Belt5.render(f5);
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
