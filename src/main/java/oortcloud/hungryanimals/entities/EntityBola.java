package oortcloud.hungryanimals.entities;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntityBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBola extends Entity implements IProjectile {
	
	@SuppressWarnings("unchecked")
	private static final Predicate<Entity> BOLA_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity entity)
        {
        	if (entity == null)
        		return false;
            return entity.canBeCollidedWith();
        }
    });
	
	private Entity shootingEntity;
	private int ticksInAir;
	
	public EntityBola(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
	}

	public EntityBola(World world, double posx, double posy, double posz) {
		this(world);
		this.setPosition(posx, posy, posz);
	}

	public EntityBola(World world, MobEntityBase shooter) {
		this(world, shooter.posX, shooter.posY + (double)shooter.getEyeHeight() - 0.1, shooter.posZ);
		this.shootingEntity = shooter;
	}

	@SideOnly(Side.CLIENT)
	@Override
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 10.0D;

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * getRenderDistanceWeight();
        return distance < d0 * d0;
    }

	@Override
	protected void entityInit() {
	}
	
    public void shoot(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy)
    {
        float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        float f1 = -MathHelper.sin(pitch * 0.017453292F);
        float f2 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        this.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
        this.motionX += shooter.motionX;
        this.motionZ += shooter.motionZ;

        if (!shooter.onGround)
        {
            this.motionY += shooter.motionY;
        }
    }
	
    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy)
    {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double)f;
        y = y / (double)f;
        z = z / (double)f;
        x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        x = x * (double)velocity;
        y = y * (double)velocity;
        z = z * (double)velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
        this.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }
    
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z)
    {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * (180D / Math.PI));
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }
    
	@Override
	public void onUpdate() {
		super.onUpdate();

		++this.ticksInAir;
		Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		RayTraceResult raytraceresult = this.getEntityWorld().rayTraceBlocks(vec3d1, vec3d, false, true, false);
		vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

		if (raytraceresult != null) {
			vec3d = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
		}

		Entity entity = this.findEntityOnPath(vec3d1, vec3d);

        if (entity != null)
        {
            raytraceresult = new RayTraceResult(entity);
        }
		
        if (raytraceresult != null && raytraceresult.entityHit != null && raytraceresult.entityHit instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)raytraceresult.entityHit;

            if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer))
            {
                raytraceresult = null;
            }
        }

		if (raytraceresult != null && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
			this.onHit(raytraceresult);
		}
		
		this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
		
		double g = 0.04;
		double f = 0.99;

		if (this.isInWater())
        {
            for (int i = 0; i < 4; ++i)
            {
                this.getEntityWorld().spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ, new int[0]);
            }

            f = 0.6F;
        }
		
		this.motionX *= f;
		this.motionY *= f;
		this.motionZ *= f;

		if (!this.hasNoGravity())
        {
            this.motionY -= g;
        }
		
		this.setPosition(posX, posY, posZ);
		this.doBlockCollisions();
	}


	protected void onHit(RayTraceResult raytraceResultIn)
    {
		Entity entity = raytraceResultIn.entityHit;
		
		if (entity != null) {
			DamageSource damagesource;

			if (this.shootingEntity == null) {
				damagesource = DamageSource.causeThrownDamage(this, this);
			} else {
				damagesource = DamageSource.causeThrownDamage(this, this.shootingEntity);
			}

			if (entity.attackEntityFrom(damagesource, 1.0F)) {
				if (entity instanceof MobEntityBase) {
					MobEntityBase MobEntitybase = (MobEntityBase)entity;
					
					if (this.shootingEntity instanceof MobEntityBase)
                    {
                        EnchantmentHelper.applyThornEnchantments(MobEntitybase, this.shootingEntity);
                        EnchantmentHelper.applyArthropodEnchantments((MobEntityBase)this.shootingEntity, MobEntitybase);
                    }
					
					MobEntitybase.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 1200, 3));
					
					//TODO reveal what does  SPacketChangeGameState(6, 0.0F) do
					if (this.shootingEntity != null && MobEntitybase != this.shootingEntity && MobEntitybase instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
	                {
	                    ((EntityPlayerMP)this.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
	                }
				}

				this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

                if (!(entity instanceof EntityEnderman))
                {
                    this.setDead();
                }
			}
		} else {
			// Hit the ground
			this.setDead();
		}
    }
	
	@Nullable
    protected Entity findEntityOnPath(Vec3d start, Vec3d end)
    {
        Entity entity = null;
        List<Entity> list = this.getEntityWorld().getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D), BOLA_TARGETS);
        double d0 = 0.0D;

        for (int i = 0; i < list.size(); ++i)
        {
            Entity entity1 = list.get(i);

            if (entity1 != this.shootingEntity || this.ticksInAir >= 5)
            {
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.3);
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

                if (raytraceresult != null)
                {
                    double d1 = start.squareDistanceTo(raytraceresult.hitVec);

                    if (d1 < d0 || d0 == 0.0D)
                    {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        return entity;
    }

	@Override
	protected void readEntityFromNBT(CompoundNBT p_70037_1_) {

	}

	@Override
	protected void writeEntityToNBT(CompoundNBT p_70014_1_) {
	}

	@Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

	@Override
    public boolean canBeAttackedWithItem()
    {
        return false;
    }
	
	@Override
    public float getEyeHeight()
    {
        return 0.0F;
    }
}
