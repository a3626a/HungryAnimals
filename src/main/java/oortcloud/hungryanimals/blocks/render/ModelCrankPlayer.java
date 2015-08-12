package oortcloud.hungryanimals.blocks.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelCrankPlayer.tcn - TechneToTabulaImporter
 * Created using Tabula 5.1.0
 */
public class ModelCrankPlayer extends ModelBase {
    public ModelRenderer Handle;
    public ModelRenderer Handle_;
    public ModelRenderer Axle;

    public ModelCrankPlayer() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.Handle_ = new ModelRenderer(this, 0, 0);
        this.Handle_.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.Handle_.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 9, 0.0F);
        this.Axle = new ModelRenderer(this, 0, 12);
        this.Axle.setRotationPoint(0.0F, 18.0F, 0.0F);
        this.Axle.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.Handle = new ModelRenderer(this, 0, 0);
        this.Handle.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Handle.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.Handle.addChild(this.Handle_);
        this.Handle.addChild(this.Axle);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.Handle.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

	public void renderModel(float f, float angle) {
		this.setRotateAngle(Handle, 0, (float)((angle)*Math.PI/180), 0);
		Handle.render(f);
	}
}
