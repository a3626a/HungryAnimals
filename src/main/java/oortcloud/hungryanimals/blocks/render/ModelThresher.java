package oortcloud.hungryanimals.blocks.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * thresher__ - Oortcloud
 * Created using Tabula 5.1.0
 */
public class ModelThresher extends ModelBase {
    public ModelRenderer body;
    public ModelRenderer shape1;
    public ModelRenderer shape2;
    public ModelRenderer shape3;
    public ModelRenderer shape4;
    public ModelRenderer shape5;
    public ModelRenderer shape7;
    public ModelRenderer shape8;

    public ModelThresher() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.shape8 = new ModelRenderer(this, 9, 0);
        this.shape8.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape8.addBox(-1.0F, -9.0F, -6.0F, 2, 1, 4, 0.0F);
        this.shape5 = new ModelRenderer(this, 0, 38);
        this.shape5.setRotationPoint(0.0F, -15.0F, 0.0F);
        this.shape5.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.shape4 = new ModelRenderer(this, 0, 28);
        this.shape4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape4.addBox(-1.0F, -13.0F, -8.0F, 2, 2, 7, 0.0F);
        this.shape3 = new ModelRenderer(this, 0, 17);
        this.shape3.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.shape3.addBox(-1.0F, -4.0F, -8.0F, 2, 8, 2, 0.0F);
        this.shape2 = new ModelRenderer(this, 0, 7);
        this.shape2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape2.addBox(-1.0F, -3.0F, -8.0F, 2, 2, 7, 0.0F);
        this.shape7 = new ModelRenderer(this, 9, 0);
        this.shape7.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape7.addBox(-1.0F, -6.0F, -6.0F, 2, 1, 4, 0.0F);
        this.shape1 = new ModelRenderer(this, 0, 0);
        this.shape1.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.shape1.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.body.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.body.addChild(this.shape8);
        this.body.addChild(this.shape5);
        this.body.addChild(this.shape4);
        this.body.addChild(this.shape3);
        this.body.addChild(this.shape2);
        this.body.addChild(this.shape7);
        this.body.addChild(this.shape1);
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
		setRotateAngle(body, 0, (float)((angle)*Math.PI/180), 0);
		body.render(f);
	}
}
