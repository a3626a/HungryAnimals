package oortcloud.hungryanimals.entities.event;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oortcloud.hungryanimals.blocks.BlockExcreta;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;

public class EntityEventHandler {

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		IExtendedEntityProperties property = event.getEntity().getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			((ExtendedPropertiesHungryAnimal) property).postInit();
		}
	}

	@SubscribeEvent
	public void onLivingEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		IExtendedEntityProperties property = event.getEntity().getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			((ExtendedPropertiesHungryAnimal) property).update();
		}
	}

	public void update() {
		if (!this.worldObj.isRemote) {

			updateHunger();
			updateCourtship();
			updateExcretion();
			updateTaming();
			updateEnvironmentalEffet();
			updateRecovery();

			if (this.getHungry() == 0) {
				onStarve();
			}
		}
	}

	private void updateHunger() {
		/*
		 * double vel = (!this.entity.isAirBorne) ? this.entity.motionX
		 * this.entity.motionX + this.entity.motionY this.entity.motionY +
		 * this.entity.motionZ this.entity.motionZ : 0; vel = 20 *
		 * Math.sqrt(vel); this.subHunger(this.hunger_bmr * (1 + vel / 2.0));
		 */
		this.subHunger(entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_bmr).getAttributeValue());
	}

	private void updateCourtship() {
		if (this.entity.getGrowingAge() == 0 && !this.entity.isInLove()
				&& this.getHungry() > entity.getAttributeMap()
						.getAttributeInstance(ModAttributes.courtship_hungerCondition).getAttributeValue()
				&& this.entity.getRNG().nextDouble() < entity.getAttributeMap()
						.getAttributeInstance(ModAttributes.courtship_probability).getAttributeValue()) {
			this.entity.setInLove(null);
			this.subHunger(
					entity.getAttributeMap().getAttributeInstance(ModAttributes.courtship_hunger).getAttributeValue());
		}
	}

	private void updateExcretion() {
		if (this.excretion > 1) {
			this.excretion -= 1;
			int x = (int) this.entity.posX;
			int y = (int) this.entity.posY;
			int z = (int) this.entity.posZ;

			BlockPos pos = new BlockPos(x, y, z);
			IBlockState meta = this.worldObj.getBlockState(pos);
			Block block = meta.getBlock();

			if (block == ModBlocks.excreta) {
				int exc = ((BlockExcreta.EnumType) meta.getValue(BlockExcreta.CONTENT)).getExcreta();
				int man = ((BlockExcreta.EnumType) meta.getValue(BlockExcreta.CONTENT)).getManure();
				if (exc + man < 4) {
					this.worldObj.setBlockState(pos,
							meta.withProperty(BlockExcreta.CONTENT, BlockExcreta.EnumType.getValue(exc + 1, man)), 2);
				} else if (exc + man == 4) {

				}
			} else if (block.isAir(meta, this.worldObj, pos) || block.isReplaceable(this.worldObj, pos)) {
				this.worldObj.setBlockState(pos, ModBlocks.excreta.getDefaultState().withProperty(BlockExcreta.CONTENT,
						BlockExcreta.EnumType.getValue(1, 0)), 2);
			} else {
				// TODO When there's no place to put block
			}
		}
	}

	private void updateEnvironmentalEffet() {
		IBlockState floor = worldObj.getBlockState(entity.getPosition().down());
		if (floor.getBlock() == ModBlocks.floorcover_leaf) {
			int j = this.entity.getGrowingAge();
			if (j < 0) {
				j += (int) (this.entity.getRNG().nextInt(4) / 4.0);
				this.entity.setGrowingAge(j);
			}
		}
		if (floor.getBlock() == ModBlocks.floorcover_wool) {
			int j = this.entity.getGrowingAge();
			if (j > 0) {
				j -= (int) (this.entity.getRNG().nextInt(4) / 4.0);
				this.entity.setGrowingAge(j);
			}
		}
		if (floor.getBlock() == ModBlocks.floorcover_ironbar) {
			// TODO fllorcover ironbar
		}
	}

	private void updateTaming() {
		double radius = 16;

		if ((this.worldObj.getWorldTime() + this.entity.getEntityId()) % 100 == 0) {
			ArrayList<EntityPlayer> players = (ArrayList<EntityPlayer>) this.worldObj.getEntitiesWithinAABB(
					EntityPlayer.class, entity.getEntityBoundingBox().expand(radius, radius, radius));
			if (players.isEmpty()) {
				if (this.taming > 0)
					this.taming *= this.taming_factor;
			} else {
				if (this.taming < 0)
					this.taming *= this.taming_factor;
			}
		}
	}

	private void updateRecovery() {
		if (this.entity.getHealth() < this.entity.getMaxHealth() && this.getHungry() > 0.8
				&& (this.worldObj.getWorldTime() % 200) == 0) {
			this.entity.heal(1.0F);
			this.subHunger(entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_max).getAttributeValue()
					/ this.entity.getMaxHealth());
		}
	}

	public void onStarve() {
		this.entity.attackEntityFrom(DamageSource.starve, 0.5F);
	}
	
	@SubscribeEvent
	public void onLivingEntityAttackedByPlayer(LivingAttackEvent event) {
		ICapabilityTamableAnimal cap = event.getEntity().getCapability(ProviderTamableAnimal.CAP, null);

		if (cap == null)
			return;

		EntityLivingBase entity = (EntityLivingBase) event.getEntity();
		DamageSource source = event.getSource();

		if (!entity.isEntityInvulnerable(source)) {
			if (source.getSourceOfDamage() instanceof EntityPlayer) {
				cap.addTaming(-4 / entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue()
						* event.getAmount());
			}
		}
	}

	@SubscribeEvent
	public void onInteract(EntityInteract event) {
		IExtendedEntityProperties property = event.getEntity().getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			event.setCanceled(((ExtendedPropertiesHungryAnimal) property).interact(event.getEntityPlayer()));
		}
	}

}
