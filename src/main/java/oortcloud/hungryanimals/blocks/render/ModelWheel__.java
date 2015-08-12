package oortcloud.hungryanimals.blocks.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelWheel.tcn - TechneToTabulaImporter
 * Created using Tabula 5.1.0
 */
public class ModelWheel__ extends ModelBase {
    public ModelRenderer Wheel;
    public ModelRenderer shahpe1;
    public ModelRenderer shahpe2;
    public ModelRenderer shahpe3;
    public ModelRenderer shahpe4;
    public ModelRenderer shahpe5;

    public ModelWheel__() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.shahpe1 = new ModelRenderer(this, 0, 0);
        this.shahpe1.setRotationPoint(0.0F, 15.0F, 3.0F);
        this.shahpe1.addBox(-3.0F, 0.0F, 1.0F, 6, 2, 1, 0.0F);
        this.Wheel = new ModelRenderer(this, 0, 0);
        this.Wheel.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Wheel.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.shahpe5 = new ModelRenderer(this, 0, 21);
        this.shahpe5.setRotationPoint(0.0F, 15.0F, -3.0F);
        this.shahpe5.addBox(-3.0F, 0.0F, -2.0F, 6, 2, 1, 0.0F);
        this.shahpe2 = new ModelRenderer(this, 0, 4);
        this.shahpe2.setRotationPoint(0.0F, 15.0F, 2.0F);
        this.shahpe2.addBox(-4.0F, 0.0F, 1.0F, 8, 2, 1, 0.0F);
        this.shahpe4 = new ModelRenderer(this, 0, 17);
        this.shahpe4.setRotationPoint(0.0F, 15.0F, -2.0F);
        this.shahpe4.addBox(-4.0F, 0.0F, -2.0F, 8, 2, 1, 0.0F);
        this.shahpe3 = new ModelRenderer(this, 0, 8);
        this.shahpe3.setRotationPoint(0.0F, 15.0F, 0.0F);
        this.shahpe3.addBox(-5.0F, 0.0F, -3.0F, 10, 2, 6, 0.0F);
        this.Wheel.addChild(this.shahpe1);
        this.Wheel.addChild(this.shahpe5);
        this.Wheel.addChild(this.shahpe2);
        this.Wheel.addChild(this.shahpe4);
        this.Wheel.addChild(this.shahpe3);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.Wheel.render(f5);
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
		this.setRotateAngle(Wheel, 0, (float)((angle)*Math.PI/180), 0);
		Wheel.render(f);
	}
}
