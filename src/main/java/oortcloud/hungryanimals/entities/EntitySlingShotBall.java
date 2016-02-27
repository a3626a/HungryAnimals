package oortcloud.hungryanimals.entities;

import java.util.List;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntitySlingShotBall extends Entity implements IProjectile {
	private Entity shootingEntity;
	private int ticksInAir;

	public EntitySlingShotBall(World world) {
		super(world);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.25F, 0.25F);
	}

	public EntitySlingShotBall(World world, double posx, double posy, double posz) {
		super(world);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.25F, 0.25F);
		this.setPosition(posx, posy, posz);
	}

	public EntitySlingShotBall(World world, double posx, double posy, double posz, double speed, float rotationYaw, float rotationPitch) {
		super(world);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.25F, 0.25F);
		this.setPosition(posx, posy, posz);
		this.setThrowableHeading(speed, rotationYaw, rotationPitch);
	}

	public EntitySlingShotBall(World world, EntityLivingBase player, float speed) {
		super(world);
		this.renderDistanceWeight = 10.0D;
		this.shootingEntity = player;

		this.setSize(0.25F, 0.25F);
		this.setLocationAndAngles(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ, player.rotationYaw, player.rotationPitch);
		this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		this.posY -= 0.10000000149011612D;
		this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, speed * 1.5F, 1.0F);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		++this.ticksInAir;
		Vec3 vec31 = new Vec3(this.posX, this.posY, this.posZ);
		Vec3 vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec3, false, true, false);
		vec31 = new Vec3(this.posX, this.posY, this.posZ);
		vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

		if (movingobjectposition != null) {
			vec3 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
		}

		Entity entity = null;
		List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
		double d0 = 0.0D;
		int i;
		float f1;

		for (i = 0; i < list.size(); ++i) {
			Entity entity1 = (Entity) list.get(i);

			if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5)) {
				f1 = 0.3F;
				AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().expand((double) f1, (double) f1, (double) f1);
				MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);

				if (movingobjectposition1 != null) {
					double d1 = vec31.distanceTo(movingobjectposition1.hitVec);

					if (d1 < d0 || d0 == 0.0D) {
						entity = entity1;
						d0 = d1;
					}
				}
			}
		}

		if (entity != null) {
			movingobjectposition = new MovingObjectPosition(entity);
		}

		if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;

			if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
				movingobjectposition = null;
			}
		}

		if (movingobjectposition != null) {
			if (movingobjectposition.entityHit != null) {

				DamageSource damagesource = null;

				if (this.shootingEntity == null) {
					damagesource = DamageSource.causeThrownDamage(this, this);
				} else {
					damagesource = DamageSource.causeThrownDamage(this, this.shootingEntity);
				}

				if (movingobjectposition.entityHit.attackEntityFrom(damagesource, 2.0F)) {
					if (movingobjectposition.entityHit instanceof EntityLivingBase) {
						EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;

						if (this.shootingEntity != null && this.shootingEntity instanceof EntityLivingBase) {
							EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
							EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) this.shootingEntity, entitylivingbase);
						}

						if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
							((EntityPlayerMP) this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
						}
					}

					this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

					if (!(movingobjectposition.entityHit instanceof EntityEnderman)) {
						this.setDead();
					}
				}
			} else {
				this.setDead();
			}
		}
		double g = 0.04;
		double f = 0.998;

		this.motionY -= g;

		this.motionX *= f;
		this.motionY *= f;
		this.motionZ *= f;

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
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

	/**
	 * Similar to setArrowHeading, it's point the throwable entity to a x, y, z
	 * direction.
	 */
	public void setThrowableHeading(double x, double y, double z, float speed, float vari) {
		float f2 = MathHelper.sqrt_double(x * x + y * y + z * z);
		x /= (double) f2;
		y /= (double) f2;
		z /= (double) f2;
		x += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) vari;
		y += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) vari;
		z += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) vari;
		x *= (double) speed;
		y *= (double) speed;
		z *= (double) speed;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f3 = MathHelper.sqrt_double(x * x + z * z);
		this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(y, (double) f3) * 180.0D / Math.PI);
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

}
