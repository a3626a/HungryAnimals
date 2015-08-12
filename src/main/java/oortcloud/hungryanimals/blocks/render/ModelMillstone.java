package oortcloud.hungryanimals.blocks.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelMillstone2.tcn - TechneToTabulaImporter
 * Created using Tabula 5.1.0
 */
public class ModelMillstone extends ModelBase {
    public ModelRenderer Handle;
    public ModelRenderer liquid;
    public ModelRenderer TopStone;
    public ModelRenderer BottomStone;
    public ModelRenderer Bucket;
    public ModelRenderer top1;
    public ModelRenderer top2;
    public ModelRenderer top3;
    public ModelRenderer top4;
    public ModelRenderer top5;
    public ModelRenderer top6;
    public ModelRenderer top7;
    public ModelRenderer top8;
    public ModelRenderer top9;
    public ModelRenderer bot1;
    public ModelRenderer bot2;
    public ModelRenderer bot3;
    public ModelRenderer bot4;
    public ModelRenderer bot5;
    public ModelRenderer bot6;
    public ModelRenderer bot7;
    public ModelRenderer bot8;
    public ModelRenderer bot9;
    public ModelRenderer buc1;
    public ModelRenderer buc2;
    public ModelRenderer buc3;
    public ModelRenderer buc4;
    public ModelRenderer buc5;

    public ModelMillstone() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.top5 = new ModelRenderer(this, 49, 28);
        this.top5.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.top5.addBox(-8.0F, 0.0F, -4.0F, 16, 5, 8, 0.0F);
        this.TopStone = new ModelRenderer(this, 0, 0);
        this.TopStone.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.TopStone.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.buc1 = new ModelRenderer(this, 0, 42);
        this.buc1.setRotationPoint(-3.0F, 20.0F, -9.0F);
        this.buc1.addBox(0.0F, 0.0F, 0.0F, 6, 4, 1, 0.0F);
        this.bot2 = new ModelRenderer(this, 0, 7);
        this.bot2.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.bot2.addBox(-5.0F, 0.0F, 6.0F, 10, 5, 1, 0.0F);
        this.top6 = new ModelRenderer(this, 69, 0);
        this.top6.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.top6.addBox(-7.0F, 0.0F, -5.0F, 14, 5, 1, 0.0F);
        this.top4 = new ModelRenderer(this, 50, 21);
        this.top4.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.top4.addBox(-7.0F, 0.0F, 4.0F, 14, 5, 1, 0.0F);
        this.bot1 = new ModelRenderer(this, 0, 0);
        this.bot1.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.bot1.addBox(-4.0F, 0.0F, 7.0F, 8, 5, 1, 0.0F);
        this.buc3 = new ModelRenderer(this, 15, 48);
        this.buc3.setRotationPoint(-3.0F, 20.0F, -9.0F);
        this.buc3.addBox(5.0F, 0.0F, -4.0F, 1, 4, 4, 0.0F);
        this.bot9 = new ModelRenderer(this, 31, 21);
        this.bot9.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.bot9.addBox(-4.0F, 0.0F, -8.0F, 8, 5, 1, 0.0F);
        this.buc4 = new ModelRenderer(this, 15, 42);
        this.buc4.setRotationPoint(-3.0F, 20.0F, -9.0F);
        this.buc4.addBox(0.0F, 0.0F, -5.0F, 6, 4, 1, 0.0F);
        this.top9 = new ModelRenderer(this, 81, 21);
        this.top9.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.top9.addBox(-4.0F, 0.0F, -8.0F, 8, 5, 1, 0.0F);
        this.top1 = new ModelRenderer(this, 50, 0);
        this.top1.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.top1.addBox(-4.0F, 0.0F, 7.0F, 8, 5, 1, 0.0F);
        this.Handle = new ModelRenderer(this, 30, 42);
        this.Handle.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.Handle.addBox(-1.0F, -6.0F, -1.0F, 2, 6, 2, 0.0F);
        this.bot6 = new ModelRenderer(this, 19, 0);
        this.bot6.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.bot6.addBox(-7.0F, 0.0F, -5.0F, 14, 5, 1, 0.0F);
        this.buc5 = new ModelRenderer(this, 0, 57);
        this.buc5.setRotationPoint(-3.0F, 20.0F, -9.0F);
        this.buc5.addBox(1.0F, 3.0F, -4.0F, 4, 1, 4, 0.0F);
        this.bot4 = new ModelRenderer(this, 0, 21);
        this.bot4.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.bot4.addBox(-7.0F, 0.0F, 4.0F, 14, 5, 1, 0.0F);
        this.bot5 = new ModelRenderer(this, 0, 28);
        this.bot5.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.bot5.addBox(-8.0F, 0.0F, -4.0F, 16, 5, 8, 0.0F);
        this.bot7 = new ModelRenderer(this, 23, 7);
        this.bot7.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.bot7.addBox(-6.0F, 0.0F, -6.0F, 12, 5, 1, 0.0F);
        this.buc2 = new ModelRenderer(this, 0, 48);
        this.buc2.setRotationPoint(-3.0F, 20.0F, -9.0F);
        this.buc2.addBox(0.0F, 0.0F, -4.0F, 1, 4, 4, 0.0F);
        this.bot3 = new ModelRenderer(this, 0, 14);
        this.bot3.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.bot3.addBox(-6.0F, 0.0F, 5.0F, 12, 5, 1, 0.0F);
        this.top3 = new ModelRenderer(this, 50, 14);
        this.top3.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.top3.addBox(-6.0F, 0.0F, 5.0F, 12, 5, 1, 0.0F);
        this.bot8 = new ModelRenderer(this, 27, 14);
        this.bot8.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.bot8.addBox(-5.0F, 0.0F, -7.0F, 10, 5, 1, 0.0F);
        this.top2 = new ModelRenderer(this, 50, 7);
        this.top2.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.top2.addBox(-5.0F, 0.0F, 6.0F, 10, 5, 1, 0.0F);
        this.Bucket = new ModelRenderer(this, 0, 0);
        this.Bucket.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Bucket.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.top8 = new ModelRenderer(this, 77, 14);
        this.top8.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.top8.addBox(-5.0F, 0.0F, -7.0F, 10, 5, 1, 0.0F);
        this.top7 = new ModelRenderer(this, 73, 7);
        this.top7.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.top7.addBox(-6.0F, 0.0F, -6.0F, 12, 5, 1, 0.0F);
        this.BottomStone = new ModelRenderer(this, 0, 0);
        this.BottomStone.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.BottomStone.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.liquid = new ModelRenderer(this, 17, 57);
        this.liquid.setRotationPoint(-2.0F, 23.0F, -13.0F);
        this.liquid.addBox(0.0F, 0.0F, 0.0F, 4, 0, 4, 0.0F);
        this.TopStone.addChild(this.top5);
        this.Bucket.addChild(this.buc1);
        this.BottomStone.addChild(this.bot2);
        this.TopStone.addChild(this.top6);
        this.TopStone.addChild(this.top4);
        this.BottomStone.addChild(this.bot1);
        this.Bucket.addChild(this.buc3);
        this.BottomStone.addChild(this.bot9);
        this.Bucket.addChild(this.buc4);
        this.TopStone.addChild(this.top9);
        this.TopStone.addChild(this.top1);
        this.BottomStone.addChild(this.bot6);
        this.Bucket.addChild(this.buc5);
        this.BottomStone.addChild(this.bot4);
        this.BottomStone.addChild(this.bot5);
        this.BottomStone.addChild(this.bot7);
        this.Bucket.addChild(this.buc2);
        this.BottomStone.addChild(this.bot3);
        this.TopStone.addChild(this.top3);
        this.BottomStone.addChild(this.bot8);
        this.TopStone.addChild(this.top2);
        this.TopStone.addChild(this.top8);
        this.TopStone.addChild(this.top7);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.TopStone.render(f5);
        this.Handle.render(f5);
        this.Bucket.render(f5);
        this.BottomStone.render(f5);
        this.liquid.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    public void renderModel(float f, float angle, float height) {
		setRotateAngle(TopStone, 0, (float) ((angle) * Math.PI / 180), 0);
		TopStone.render(f);
		BottomStone.render(f);
		setRotateAngle(Handle, 0, (float) ((angle) * Math.PI / 180), 0);
		Handle.render(f);
		Bucket.render(f);
		if (height != 0) {
			float temp = liquid.offsetY;
			liquid.offsetY -= height * 3 /16.0;
			liquid.render(f);
			liquid.offsetY = temp;
		}
	}
}
