package oortcloud.hungryanimals.entities.event;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oortcloud.hungryanimals.blocks.BlockExcreta;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.entities.ai.AIManager;
import oortcloud.hungryanimals.entities.attributes.AttributeManager;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceManager;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreference;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;
import oortcloud.hungryanimals.potion.ModPotions;

public class EntityEventHandler {

	private static final double taming_factor = 0.998;

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (!(event.getEntity() instanceof EntityAnimal))
			return;

		EntityAnimal entity = (EntityAnimal) event.getEntity();
		if (!HungryAnimalManager.getInstance().isRegistered(entity.getClass()))
			return;

		AttributeManager.getInstance().applyAttributes(entity);		
		entity.setHealth(entity.getMaxHealth());
		AIManager.getInstance().REGISTRY.get(entity.getClass()).registerAI(entity);
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (!(event.getEntity() instanceof EntityAnimal))
			return;

		EntityAnimal entity = (EntityAnimal) event.getEntity();
		if (!HungryAnimalManager.getInstance().isRegistered(entity.getClass()))
			return;
		
		AttributeManager.getInstance().registerAttributes(entity);
	}
	
	@SubscribeEvent
	public void onLivingEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		if (!(event.getEntity() instanceof EntityAnimal))
			return;

		EntityAnimal entity = (EntityAnimal) event.getEntity();
		if (!HungryAnimalManager.getInstance().isRegistered(entity.getClass()))
			return;

		if (!entity.worldObj.isRemote) {
			updateHunger(entity);
			updateCourtship(entity);
			updateExcretion(entity);
			updateTaming(entity);
			updateEnvironmentalEffet(entity);
			updateRecovery(entity);

			if (entity.getCapability(ProviderHungryAnimal.CAP, null).getHunger() == 0) {
				onStarve(entity);
			}
		}
	}

	private void updateHunger(EntityAnimal entity) {
		/*
		 * double vel = (!this.entity.isAirBorne) ? this.entity.motionX
		 * this.entity.motionX + this.entity.motionY this.entity.motionY +
		 * this.entity.motionZ this.entity.motionZ : 0; vel = 20 *
		 * Math.sqrt(vel); this.subHunger(this.hunger_bmr * (1 + vel / 2.0));
		 */
		entity.getCapability(ProviderHungryAnimal.CAP, null)
				.addHunger(-entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_bmr).getAttributeValue());
	}

	private void updateCourtship(EntityAnimal entity) {
		ICapabilityHungryAnimal cap = entity.getCapability(ProviderHungryAnimal.CAP, null);

		if (entity.getGrowingAge() == 0 && !entity.isInLove()
				&& cap.getHunger() / cap.getMaxHunger() > entity.getAttributeMap().getAttributeInstance(ModAttributes.courtship_hungerCondition)
						.getAttributeValue()
				&& entity.getRNG().nextDouble() < entity.getAttributeMap().getAttributeInstance(ModAttributes.courtship_probability).getAttributeValue()) {
			entity.setInLove(null);
			cap.addHunger(-entity.getAttributeMap().getAttributeInstance(ModAttributes.courtship_hunger).getAttributeValue());
		}
	}

	private void updateExcretion(EntityAnimal entity) {
		ICapabilityHungryAnimal cap = entity.getCapability(ProviderHungryAnimal.CAP, null);

		if (cap.getExcretion() > 1) {
			cap.addExcretion(-1);
			BlockPos pos = entity.getPosition();
			IBlockState meta = entity.worldObj.getBlockState(pos);
			Block block = meta.getBlock();

			if (block == ModBlocks.excreta) {
				int exc = ((BlockExcreta.EnumType) meta.getValue(BlockExcreta.CONTENT)).getExcreta();
				int man = ((BlockExcreta.EnumType) meta.getValue(BlockExcreta.CONTENT)).getManure();
				if (exc + man < 4) {
					entity.worldObj.setBlockState(pos, meta.withProperty(BlockExcreta.CONTENT, BlockExcreta.EnumType.getValue(exc + 1, man)), 2);
				} else if (exc + man == 4) {

				}
			} else if (block.isAir(meta, entity.worldObj, pos) || block.isReplaceable(entity.worldObj, pos)) {
				entity.worldObj.setBlockState(pos, ModBlocks.excreta.getDefaultState().withProperty(BlockExcreta.CONTENT, BlockExcreta.EnumType.getValue(1, 0)),
						2);
			} else {
				// TODO When there's no place to put block
			}
		}
	}

	private void updateEnvironmentalEffet(EntityAnimal entity) {
		IBlockState floor = entity.worldObj.getBlockState(entity.getPosition().down());
		if (floor.getBlock() == ModBlocks.floorcover_leaf) {
			int j = entity.getGrowingAge();
			if (j < 0) {
				j += (int) (entity.getRNG().nextInt(4) / 4.0);
				entity.setGrowingAge(j);
			}
		}
		if (floor.getBlock() == ModBlocks.floorcover_wool) {
			int j = entity.getGrowingAge();
			if (j > 0) {
				j -= (int) (entity.getRNG().nextInt(4) / 4.0);
				entity.setGrowingAge(j);
			}
		}
		if (floor.getBlock() == ModBlocks.floorcover_ironbar) {
			// TODO fllorcover ironbar
		}
	}

	private void updateTaming(EntityAnimal entity) {
		double radius = 16;

		if ((entity.worldObj.getWorldTime() + entity.getEntityId()) % 100 == 0) {
			ArrayList<EntityPlayer> players = (ArrayList<EntityPlayer>) entity.worldObj.getEntitiesWithinAABB(EntityPlayer.class,
					entity.getEntityBoundingBox().expand(radius, radius, radius));
			ICapabilityTamableAnimal cap = entity.getCapability(ProviderTamableAnimal.CAP, null);
			if (players.isEmpty()) {
				if (cap.getTaming() > 0) {
					cap.setTaming(cap.getTaming() * taming_factor);
				}
			} else {
				if (cap.getTaming() < 0) {
					cap.setTaming(cap.getTaming() * taming_factor);
				}
			}
		}
	}

	private void updateRecovery(EntityAnimal entity) {
		ICapabilityHungryAnimal cap = entity.getCapability(ProviderHungryAnimal.CAP, null);
		if (entity.getHealth() < entity.getMaxHealth() && cap.getHunger() / cap.getMaxHunger() > 0.8 && (entity.worldObj.getWorldTime() % 200) == 0) {
			entity.heal(1.0F);
			cap.addHunger(-entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_max).getAttributeValue() / entity.getMaxHealth());
		}
	}

	public void onStarve(EntityAnimal entity) {
		entity.attackEntityFrom(DamageSource.starve, 0.5F);
	}

	@SubscribeEvent
	public void onLivingEntityAttackedByPlayer(LivingAttackEvent event) {
		if (!(event.getEntity() instanceof EntityAnimal))
			return;

		EntityAnimal entity = (EntityAnimal) event.getEntity();
		if (!HungryAnimalManager.getInstance().isRegistered(entity.getClass()))
			return;
		
		ICapabilityTamableAnimal cap = entity.getCapability(ProviderTamableAnimal.CAP, null);
		DamageSource source = event.getSource();
		if (!entity.isEntityInvulnerable(source)) {
			if (source.getSourceOfDamage() instanceof EntityPlayer) {
				cap.addTaming(-4 / entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue() * event.getAmount());
			}
		}
	}

	@SubscribeEvent
	public void onInteract(EntityInteract event) {
		if (!(event.getEntity() instanceof EntityAnimal))
			return;

		EntityAnimal entity = (EntityAnimal) event.getEntity();
		if (!HungryAnimalManager.getInstance().isRegistered(entity.getClass()))
			return;
		event.setCanceled(interact(event, entity));
	}

	private boolean interact(EntityInteract event, EntityAnimal entity) {
		ItemStack stackMain = event.getEntityPlayer().getHeldItemMainhand();
		ItemStack stackOff = event.getEntityPlayer().getHeldItemOffhand();
		if (stackMain == null && stackOff == null)
			return false;

		if (!interact(event, stackMain, entity)) {
			if (!interact(event, stackOff, entity)) {
				return false;
			}
			return true;
		}
		return true;

	}

	private boolean interact(EntityInteract event, ItemStack item, EntityAnimal entity) {
		ICapabilityHungryAnimal capHungry = entity.getCapability(ProviderHungryAnimal.CAP, null);
		ICapabilityTamableAnimal capTaming = entity.getCapability(ProviderTamableAnimal.CAP, null);
		IFoodPreference<ItemStack> prefItem = FoodPreferenceManager.getInstance().REGISTRY_ITEM.get(entity.getClass());
		if (prefItem.canEat(capHungry, item) && capTaming.getTaming() >= 1) {
			eatFoodBonus(entity, capHungry, capTaming, item);
			item.stackSize--;
			// TODO Check automated itemstack removal when its stacksize reaches
			// to 0
			return true;
		} else if (entity.isPotionActive(ModPotions.potionDisease) && capTaming.getTaming() >= 1) {
			if (item.getItem() == ItemBlock.getItemFromBlock(Blocks.RED_MUSHROOM) || item.getItem() == ItemBlock.getItemFromBlock(Blocks.BROWN_MUSHROOM)) {
				entity.removePotionEffect(ModPotions.potionDisease);
				item.stackSize--;
				// TODO Check automated itemstack removal when its stacksize
				// reaches to 0
			}
			return true;
		}

		if (entity.isBreedingItem(item)) {
			return true;
		}

		return false;
	}

	private void eatFoodBonus(EntityAnimal entity, ICapabilityHungryAnimal capHungry, ICapabilityTamableAnimal capTaming, ItemStack item) {
		// TODO It must merged with AI's eatFoodBonus to increase maintainance
		if (item == null)
			return;

		double hunger = FoodPreferenceManager.getInstance().REGISTRY_ITEM.get(entity.getClass()).getHunger(item);
		capHungry.addHunger(hunger);

		if (entity.getGrowingAge() < 0) {
			NBTTagCompound tag = item.getTagCompound();
			if (tag == null || !tag.hasKey("isNatural") || !tag.getBoolean("isNatural")) {
				int duration = (int) (hunger / entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_bmr).getAttributeValue());
				entity.addPotionEffect(new PotionEffect(ModPotions.potionGrowth, duration, 1));
			}
		}

		NBTTagCompound tag = item.getTagCompound();
		if (tag == null || !tag.hasKey("isNatural") || !tag.getBoolean("isNatural")) {
			capTaming.addTaming(0.0001 / entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_bmr).getAttributeValue() * hunger);
		}
	}
}
