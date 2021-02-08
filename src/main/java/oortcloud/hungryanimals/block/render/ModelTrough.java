package oortcloud.hungryanimals.block.render;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelTrough extends Model {
	RendererModel bottom;
	RendererModel long1;
	RendererModel long2;
	RendererModel short1;
	RendererModel short2;

	public ModelTrough() {
		textureWidth = 256;
		textureHeight = 128;

		bottom = new RendererModel(this, 0, 0);
		bottom.addBox(0F, 0F, 0F, 16, 2, 32);
		bottom.setRotationPoint(-8F, 22F, -8F);
		bottom.setTextureSize(256, 128);
		bottom.mirror = true;
		long1 = new RendererModel(this, 0, 36);
		long1.addBox(0F, 0F, 0F, 2, 6, 32);
		long1.setRotationPoint(-8F, 16F, -8F);
		long1.setTextureSize(256, 128);
		long1.mirror = true;
		long2 = new RendererModel(this, 0, 76);
		long2.addBox(0F, 0F, 0F, 2, 6, 32);
		long2.setRotationPoint(6F, 16F, -8F);
		long2.setTextureSize(256, 128);
		long2.mirror = true;
		short1 = new RendererModel(this, 70, 55);
		short1.addBox(0F, 0F, 0F, 12, 6, 2);
		short1.setRotationPoint(-6F, 16F, 22F);
		short1.setTextureSize(256, 128);
		short1.mirror = true;
		short2 = new RendererModel(this, 70, 40);
		short2.addBox(0F, 0F, 0F, 12, 6, 2);
		short2.setRotationPoint(-6F, 16F, -8F);
		short2.setTextureSize(256, 128);
		short2.mirror = true;
	}

	public void renderModel() {
		bottom.render(0.0625F);
		long1.render(0.0625F);
		long2.render(0.0625F);
		short1.render(0.0625F);
		short2.render(0.0625F);
	}
}
