package oortcloud.hungryanimals.entities;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
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

public class EntityBola extends Entity implements IProjectile {
	
	private static final Predicate<Entity> BOLA_TARGETS;
	
	private Entity shootingEntity;
	private int ticksInAir;

	static {
		ArrayList<Predicate<Entity>> arg = new ArrayList<Predicate<Entity>>();
		arg.add(EntitySelectors.NOT_SPECTATING);
		arg.add(EntitySelectors.IS_ALIVE);
		arg.add(new Predicate<Entity>()
	    {
	        public boolean apply(@Nullable Entity entity)
	        {
	            return entity.canBeCollidedWith();
	        }
	    });
		BOLA_TARGETS = Predicates.<Entity>and(arg);
	}
	
	public EntityBola(World world) {
		super(world);
		this.setSize(1.0F, 0.25F);
	}

	public EntityBola(World world, double posx, double posy, double posz, double speed, float rotationYaw, float rotationPitch) {
		super(world);
		this.setSize(1.0F, 0.25F);
		this.setPosition(posx, posy, posz);
		this.setThrowableHeading(speed, rotationYaw, rotationPitch);
	}

	public EntityBola(World world, EntityPlayer player, double d, float rotationYaw, float rotationPitch) {
		this(world, player.posX, player.posY + player.getEyeHeight(), player.posZ, d, rotationYaw, rotationPitch);
		this.shootingEntity = player;
	}

	public EntityBola(World world, EntityPlayer player, float d) {
		super(world);
		this.shootingEntity = player;

		this.setSize(1.0F, 0.25F);
		this.setLocationAndAngles(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ, player.rotationYaw, player.rotationPitch);
		this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		this.posY -= 0.1;
		this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);

		this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, d * 1.5F, 1.0F);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		++this.ticksInAir;
		Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		RayTraceResult raytraceresult = this.worldObj.rayTraceBlocks(vec3d1, vec3d, false, true, false);
		vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

		if (raytraceresult != null) {
			vec3d = new Vec3d(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);
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

		if (raytraceresult != null) {
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
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ, new int[0]);
            }

            f = 0.6F;
        }
		
		this.motionX *= f;
		this.motionY *= f;
		this.motionZ *= f;

		if (!this.func_189652_ae())
        {
            this.motionY -= g;
        }
		
		this.setPosition(posX, posY, posZ);
		this.doBlockCollisions();
	}

	public Entity getShootingEntity() {
		return this.shootingEntity;
	}

	public void setThrowableHeading(double speed, float rotationYaw, float rotationPitch) {
		this.prevRotationYaw = this.rotationYaw = rotationYaw;
		this.prevRotationPitch = this.rotationPitch = rotationPitch;

		this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));

		this.motionX *= speed;
		this.motionY *= speed;
		this.motionZ *= speed;
	}

	@Override
	protected void entityInit() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		// TODO Auto-generated method stub

	}

	/**
	 * Similar to setArrowHeading, it's point the throwable entity to a x, y, z
	 * direction.
	 */
	public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy)
    {
        float f = MathHelper.sqrt_double(x * x + y * y + z * z);
        x = x / (double)f;
        y = y / (double)f;
        z = z / (double)f;
        x = x + this.rand.nextGaussian() * 0.0075D * (double)inaccuracy;
        y = y + this.rand.nextGaussian() * 0.0075D * (double)inaccuracy;
        z = z + this.rand.nextGaussian() * 0.0075D * (double)inaccuracy;
        x = x * (double)velocity;
        y = y * (double)velocity;
        z = z * (double)velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f1 = MathHelper.sqrt_double(x * x + z * z);
        this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
        this.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }
	
	@Nullable
    protected Entity findEntityOnPath(Vec3d start, Vec3d end)
    {
        Entity entity = null;
        List<Entity> list = this.worldObj.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expandXyz(1.0D), BOLA_TARGETS);
        double d0 = 0.0D;

        for (int i = 0; i < list.size(); ++i)
        {
            Entity entity1 = list.get(i);

            if (entity1 != this.shootingEntity || this.ticksInAir >= 5)
            {
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz(0.30000001192092896D);
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
				if (entity instanceof EntityLivingBase) {
					EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
					
					if (this.shootingEntity instanceof EntityLivingBase)
                    {
                        EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
                        EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)this.shootingEntity, entitylivingbase);
                    }
					
					entitylivingbase.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 1200, 3));
					
					//TODO reveal what does  SPacketChangeGameState(6, 0.0F) do
					if (this.shootingEntity != null && entitylivingbase != this.shootingEntity && entitylivingbase instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
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
	
	 /**
     * Checks if the entity is in range to render.
     */
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
	
}
