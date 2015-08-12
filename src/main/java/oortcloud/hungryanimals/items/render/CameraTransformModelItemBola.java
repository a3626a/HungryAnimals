package oortcloud.hungryanimals.items.render;

import java.util.Iterator;
import java.util.List;

import javax.vecmath.Vector3f;

import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BreakingFour;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Lists;

public class CameraTransformModelItemBola implements IBakedModel {

	IBakedModel model;
    protected ItemCameraTransforms cameraTransforms;
	public static final ModelResourceLocation modelresourcelocation = new ModelResourceLocation(References.RESOURCESPREFIX + Strings.itemBolaName + "_spin", "inventory");
    
    private static final float radiusFirst = (float) (Math.sqrt(2)*6);
    private static final float radiusThird = (float) (Math.sqrt(2)*2.25);
    public CameraTransformModelItemBola(IBakedModel model)
    {
    	this.model = model;
    	this.cameraTransforms = model.getItemCameraTransforms();
    }


    public List getFaceQuads(EnumFacing p_177551_1_)
    {
        return model.getFaceQuads(p_177551_1_);
    }

    public List getGeneralQuads()
    {
        return model.getGeneralQuads();
    }

    public boolean isAmbientOcclusion()
    {
        return model.isAmbientOcclusion();
    }

    public boolean isGui3d()
    {
        return model.isGui3d();
    }

    public boolean isBuiltInRenderer()
    {
        return model.isBuiltInRenderer();
    }

    public TextureAtlasSprite getTexture()
    {
        return model.getTexture();
    }

    public ItemCameraTransforms getItemCameraTransforms()
    {
        return this.cameraTransforms;
    }

    public void setRotation(float angle)
    {
		ItemTransformVec3f transThird = new ItemTransformVec3f(new Vector3f(0,
				90, angle), new Vector3f(-1.25F/16.0F, (float) ((1.25+radiusThird*Math.cos(Math.toRadians(angle+45)))/16.0F), (float) ((-0.5+radiusThird*Math.sin(Math.toRadians(angle+45)))/16.0F)), new Vector3f(0.55F, 0.55F,
				0.55F));
		ItemTransformVec3f transFirst = new ItemTransformVec3f(new Vector3f(0,
				-135, angle), new Vector3f(0, (float) ((2.5+radiusFirst*Math.cos(Math.toRadians(angle+45)))/16.0F), (float) ((4.5-radiusFirst*Math.sin(Math.toRadians(angle+45)))/16.0F)), new Vector3f(1.7F, 1.7F,
				1.7F));
		
        this.cameraTransforms = new ItemCameraTransforms(transThird,
				transFirst,
				model.getItemCameraTransforms().head,
				model.getItemCameraTransforms().gui);
    }
    
}
