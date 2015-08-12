package oortcloud.hungryanimals.blocks.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * crankanimal - oortcloud
 * Created using Tabula 5.1.0
 */
public class ModelCrankAnimal extends ModelBase {
    public ModelRenderer body;
    public ModelRenderer axle1;
    public ModelRenderer axle2;
    public ModelRenderer axle3;
    public ModelRenderer shape2;
    public ModelRenderer shape3;
    public ModelRenderer shape4;
    public ModelRenderer shape1;

    public ModelCrankAnimal() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.shape1 = new ModelRenderer(this, 9, 0);
        this.shape1.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.shape1.addBox(2.0F, -1.0F, -1.0F, 22, 2, 2, 0.0F);
        this.shape3 = new ModelRenderer(this, 9, 0);
        this.shape3.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.shape3.addBox(2.0F, -1.0F, -1.0F, 22, 2, 2, 0.0F);
        this.setRotateAngle(shape3, 0.0F, 3.141592653589793F, 0.0F);
        this.shape4 = new ModelRenderer(this, 9, 0);
        this.shape4.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.shape4.addBox(2.0F, -1.0F, -1.0F, 22, 2, 2, 0.0F);
        this.setRotateAngle(shape4, 0.0F, -1.5707963267948966F, 0.0F);
        this.axle1 = new ModelRenderer(this, 0, 0);
        this.axle1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.axle1.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.body.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.axle2 = new ModelRenderer(this, 0, 5);
        this.axle2.setRotationPoint(-2.0F, 2.0F, -2.0F);
        this.axle2.addBox(0.0F, 0.0F, 0.0F, 4, 12, 4, 0.0F);
        this.shape2 = new ModelRenderer(this, 9, 0);
        this.shape2.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.shape2.addBox(2.0F, -1.0F, -1.0F, 22, 2, 2, 0.0F);
        this.setRotateAngle(shape2, 0.0F, 1.5707963267948966F, 0.0F);
        this.axle3 = new ModelRenderer(this, 0, 22);
        this.axle3.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.axle3.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, 0.0F);
        this.body.addChild(this.shape1);
        this.body.addChild(this.shape3);
        this.body.addChild(this.shape4);
        this.body.addChild(this.axle1);
        this.body.addChild(this.axle2);
        this.body.addChild(this.shape2);
        this.body.addChild(this.axle3);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.body.render(f5);
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
		this.setRotateAngle(body, 0, (float)((angle)*Math.PI/180), 0);
		body.render(f);
	}
}
