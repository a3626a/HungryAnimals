package oortcloud.hungryanimals.blocks.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBelt extends ModelBase {
	ModelRenderer Belt1;
	ModelRenderer Belt5;

	public ModelBelt() {
		textureWidth = 128;
		textureHeight = 64;

		Belt1 = new ModelRenderer(this, 0, 0);
		Belt1.addBox(5F, 0F, -8F, 1, 2, 16);
		Belt1.setRotationPoint(0F, 15F, 0F);
		Belt1.setTextureSize(128, 64);
		Belt1.mirror = true;
		setRotation(Belt1, 0F, 0F, 0F);
		
		Belt5 = new ModelRenderer(this, 0, 0);
		Belt5.addBox(-6F, 0F, -8F, 1, 2, 16);
		Belt5.setRotationPoint(0F, 15F, 0F);
		Belt5.setTextureSize(128, 64);
		Belt5.mirror = true;
		setRotation(Belt5, 0F, 0F, 0F);
	}
	
	public void renderModel(float f, float angle) {
		setRotation(Belt1, 0, (float)((angle)*Math.PI/180), 0);
		setRotation(Belt5, 0, (float)((angle)*Math.PI/180), 0);
		Belt1.render(f);
		Belt5.render(f);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Belt1.render(f5);
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
