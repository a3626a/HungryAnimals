package oortcloud.hungryanimals.blocks.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelBlender.tcn - TechneToTabulaImporter
 * Created using Tabula 5.1.0
 */
public class ModelBlender extends ModelBase {
    public ModelRenderer body;
    public ModelRenderer blade;
    public ModelRenderer Shape1;
    public ModelRenderer Shape2;
    public ModelRenderer Shape3;
    public ModelRenderer Shape4;
    public ModelRenderer Shape5;
    public ModelRenderer axle;
    public ModelRenderer blade1;
    public ModelRenderer blade2;
    public ModelRenderer blade3;
    public ModelRenderer blade4;
    public ModelRenderer blade1_;
    public ModelRenderer blade2_;
    public ModelRenderer blade3_;
    public ModelRenderer blade4_;

    public ModelBlender() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.axle = new ModelRenderer(this, 96, 0);
        this.axle.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.axle.addBox(-1.0F, 0.0F, -1.0F, 2, 15, 2, 0.0F);
        this.blade3 = new ModelRenderer(this, 0, 0);
        this.blade3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.blade3.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(blade3, 0.0F, 3.141592653589793F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.blade = new ModelRenderer(this, 0, 0);
        this.blade.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.blade.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.blade4 = new ModelRenderer(this, 0, 0);
        this.blade4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.blade4.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(blade4, 0.0F, -1.5707963267948966F, 0.0F);
        this.Shape5 = new ModelRenderer(this, 0, 22); 
        this.Shape5.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.Shape5.addBox(7.0F, -7.0F, -7.0F, 1, 7, 14, 0.0F);
        this.blade2 = new ModelRenderer(this, 0, 0);
        this.blade2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.blade2.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(blade2, 0.0F, 1.5707963267948966F, 0.0F);
        this.Shape2 = new ModelRenderer(this, 0, 53);
        this.Shape2.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.Shape2.addBox(-8.0F, -7.0F, 7.0F, 16, 7, 1, 0.0F);
        this.blade3_ = new ModelRenderer(this, 31, 18);
        this.blade3_.setRotationPoint(0.0F, 19.4F, 0.0F);
        this.blade3_.addBox(-0.5F, -4.0F, -7.0F, 1, 8, 6, 0.0F);
        this.setRotateAngle(blade3_, 0.0F, 0.0F, 0.3490658503988659F);
        this.blade4_ = new ModelRenderer(this, 31, 18);
        this.blade4_.setRotationPoint(0.0F, 19.4F, 0.0F);
        this.blade4_.addBox(-0.5F, -4.0F, -7.0F, 1, 8, 6, 0.0F);
        this.setRotateAngle(blade4_, 0.0F, 0.0F, 0.3490658503988659F);
        this.blade1_ = new ModelRenderer(this, 31, 18);
        this.blade1_.setRotationPoint(0.0F, 19.4F, 0.0F);
        this.blade1_.addBox(-0.5F, -4.0F, -7.0F, 1, 8, 6, 0.0F);
        this.setRotateAngle(blade1_, 0.0F, 0.0F, 0.3490658503988659F);
        this.Shape3 = new ModelRenderer(this, 0, 44);
        this.Shape3.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.Shape3.addBox(-8.0F, -7.0F, -8.0F, 16, 7, 1, 0.0F);
        this.Shape1 = new ModelRenderer(this, 31, 0);
        this.Shape1.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.Shape1.addBox(-8.0F, 0.0F, -8.0F, 16, 1, 16, 0.0F);
        this.Shape4 = new ModelRenderer(this, 0, 0);
        this.Shape4.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.Shape4.addBox(-8.0F, -7.0F, -7.0F, 1, 7, 14, 0.0F);
        this.blade2_ = new ModelRenderer(this, 31, 18);
        this.blade2_.setRotationPoint(0.0F, 19.4F, 0.0F);
        this.blade2_.addBox(-0.5F, -4.0F, -7.0F, 1, 8, 6, 0.0F);
        this.setRotateAngle(blade2_, 0.0F, 0.0F, 0.3490658503988659F);
        this.blade1 = new ModelRenderer(this, 0, 0);
        this.blade1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.blade1.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.blade.addChild(this.axle);
        this.blade.addChild(this.blade3);
        this.blade.addChild(this.blade4);
        this.body.addChild(this.Shape5);
        this.blade.addChild(this.blade2);
        this.body.addChild(this.Shape2);
        this.blade3.addChild(this.blade3_);
        this.blade4.addChild(this.blade4_);
        this.blade1.addChild(this.blade1_);
        this.body.addChild(this.Shape3);
        this.body.addChild(this.Shape1);
        this.body.addChild(this.Shape4);
        this.blade2.addChild(this.blade2_);
        this.blade.addChild(this.blade1);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.body.render(f5);
        this.blade.render(f5);
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
		this.setRotateAngle(blade, 0, (float)((angle)*Math.PI/180), 0);
		body.render(f);
		blade.render(f);
	}
}
