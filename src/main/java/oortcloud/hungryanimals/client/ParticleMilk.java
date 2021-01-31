package oortcloud.hungryanimals.client;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import oortcloud.hungryanimals.core.lib.References;

public class ParticleMilk extends Particle {

	private static final ResourceLocation PARTICLES_TEXTURE = new ResourceLocation(References.MODID + ":textures/particle/particles.png");

	public ParticleMilk(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		setRBGColorF(1.0F, 1.0F, 1.0F);
		this.particleTextureIndexX = 1 + this.rand.nextInt(3);
		this.particleTextureIndexY = 0;
		this.setSize(0.01F, 0.01F);
		this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
		this.particleGravity = 0.04F;

		this.motionX = xSpeedIn;
		this.motionY = ySpeedIn + 0.1D;
		this.motionZ = zSpeedIn;
	}

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= (double)this.particleGravity;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.particleMaxAge-- <= 0)
        {
            this.setExpired();
        }

        if (this.onGround)
        {
            if (Math.random() < 0.5D)
            {
                this.setExpired();
            }

            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }

        BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
        BlockState BlockState = this.world.getBlockState(blockpos);
        Material material = BlockState.getMaterial();

        if (material.isLiquid() || material.isSolid())
        {
            double d0;

            if (BlockState.getBlock() instanceof BlockLiquid)
            {
                d0 = (double)(1.0F - BlockLiquid.getLiquidHeightPercent(((Integer)BlockState.getValue(BlockLiquid.LEVEL)).intValue()));
            }
            else
            {
                d0 = BlockState.getBoundingBox(this.world, blockpos).maxY;
            }

            double d1 = (double)MathHelper.floor(this.posY) + d0;

            if (this.posY < d1)
            {
                this.setExpired();
            }
        }
    }
	
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY,
			float rotationXZ) {

		Particle.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double) partialTicks;
		Particle.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double) partialTicks;
		Particle.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double) partialTicks;
		Particle.cameraViewDir = entityIn.getLook(partialTicks);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.alphaFunc(516, 0.003921569F);

		GlStateManager.depthMask(true);
		Minecraft.getMinecraft().getTextureManager().bindTexture(PARTICLES_TEXTURE);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

		super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
		Tessellator.getInstance().draw();

		GlStateManager.depthMask(true);
		GlStateManager.disableBlend();
		GlStateManager.alphaFunc(516, 0.1F);
	}

	@Override
	public int getFXLayer() {
		return 3;
	}

}
