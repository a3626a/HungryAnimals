package oortcloud.hungryanimals.entities.event;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
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
import oortcloud.hungryanimals.entities.capability.TamingLevel;
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

		if (!entity.getEntityWorld().isRemote && entity.getEntityWorld().getTotalWorldTime() % 20 == 0) {
			updateHunger(entity);
			updateCourtship(entity);
			updateExcretion(entity);
			updateTaming(entity);
			updateEnvironmentalEffet(entity);
			updateRecovery(entity);

			if (entity.getCapability(ProviderHungryAnimal.CAP, null).getWeight() < 0.5
					* entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_weight_normal).getAttributeValue()) {
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
		ICapabilityHungryAnimal cap = entity.getCapability(ProviderHungryAnimal.CAP, null);
		double nutrient = cap.getNutrient();
		double stomach = cap.getStomach();
		double digest = entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_stomach_digest).getAttributeValue();

		if (stomach > 0) {
			if (digest > stomach) {
				digest = stomach;
			}

			double nutrient_digest = nutrient / stomach * digest;

			cap.addNutrient(-nutrient_digest);
			cap.addWeight(nutrient_digest);
			cap.addStomach(-digest);
		}
		
		double default_bmr = entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_weight_bmr).getAttributeValue();
		double weight_factor = cap.getWeight()/entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_weight_normal).getAttributeValue();
		
		cap.addWeight(-weight_factor*default_bmr);
	}

	private void updateCourtship(EntityAnimal entity) {
		ICapabilityHungryAnimal cap = entity.getCapability(ProviderHungryAnimal.CAP, null);

		if (entity.getGrowingAge() == 0 && !entity.isInLove()
				&& cap.getStomach() / cap.getMaxStomach() > entity.getAttributeMap().getAttributeInstance(ModAttributes.courtship_stomach_condition)
						.getAttributeValue()
				&& entity.getRNG().nextDouble() < entity.getAttributeMap().getAttributeInstance(ModAttributes.courtship_probability).getAttributeValue()) {
			entity.setInLove(null);
			cap.addWeight(-entity.getAttributeMap().getAttributeInstance(ModAttributes.courtship_weight).getAttributeValue());
		}
	}

	private void updateExcretion(EntityAnimal entity) {
		ICapabilityHungryAnimal cap = entity.getCapability(ProviderHungryAnimal.CAP, null);

		if (cap.getExcretion() > 1) {
			cap.addExcretion(-1);
			BlockPos pos = entity.getPosition();
			IBlockState meta = entity.getEntityWorld().getBlockState(pos);
			Block block = meta.getBlock();

			if (block == ModBlocks.excreta) {
				int exc = ((BlockExcreta.EnumType) meta.getValue(BlockExcreta.CONTENT)).getExcreta();
				int man = ((BlockExcreta.EnumType) meta.getValue(BlockExcreta.CONTENT)).getManure();
				if (exc + man < 4) {
					entity.getEntityWorld().setBlockState(pos, meta.withProperty(BlockExcreta.CONTENT, BlockExcreta.EnumType.getValue(exc + 1, man)), 2);
				} else if (exc + man == 4) {

				}
			} else if (block.isAir(meta, entity.getEntityWorld(), pos) || block.isReplaceable(entity.getEntityWorld(), pos)) {
				entity.getEntityWorld().setBlockState(pos,
						ModBlocks.excreta.getDefaultState().withProperty(BlockExcreta.CONTENT, BlockExcreta.EnumType.getValue(1, 0)), 2);
			} else {
				// TODO When there's no place to put block
			}
		}
	}

	private void updateEnvironmentalEffet(EntityAnimal entity) {
		IBlockState floor = entity.getEntityWorld().getBlockState(entity.getPosition().down());
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
			// TODO floorcover ironbar
		}
	}

	private void updateTaming(EntityAnimal entity) {
		double radius = 16;

		if ((entity.getEntityWorld().getWorldTime() + entity.getEntityId()) % 100 == 0) {
			ArrayList<EntityPlayer> players = (ArrayList<EntityPlayer>) entity.getEntityWorld().getEntitiesWithinAABB(EntityPlayer.class,
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
		if (entity.getHealth() < entity.getMaxHealth() && cap.getStomach() / cap.getMaxStomach() > 0.8 && (entity.getEntityWorld().getWorldTime() % 200) == 0) {
			entity.heal(1.0F);
			cap.addWeight(-entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_weight_normal).getAttributeValue() / entity.getMaxHealth());
		}
	}

	public void onStarve(EntityAnimal entity) {
		entity.attackEntityFrom(DamageSource.STARVE, 0.5F);
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
			if (source.getTrueSource() instanceof EntityPlayer) {
				cap.addTaming(-4 / entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue() * event.getAmount());
			}
		}
	}

	/**
	 * Synchronization btw Client and Server is CRITICAL here.
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onInteract(EntityInteract event) {
		if (!(event.getTarget() instanceof EntityAnimal))
			return;

		EntityAnimal entity = (EntityAnimal) event.getTarget();
		if (!HungryAnimalManager.getInstance().isRegistered(entity.getClass()))
			return;
		Pair<Boolean, EnumActionResult> result = interact(event, entity);
		event.setCanceled(result.left);
		event.setCancellationResult(result.right);
	}

	private Pair<Boolean, EnumActionResult> interact(EntityInteract event, EntityAnimal entity) {
		if (event.getItemStack().isEmpty())
			return new Pair<Boolean, EnumActionResult>(false, null);
		return interact(event, event.getHand(), event.getItemStack(), entity);
	}

	private Pair<Boolean, EnumActionResult> interact(EntityInteract event, EnumHand hand, ItemStack itemstack, EntityAnimal entity) {
		ICapabilityHungryAnimal capHungry = entity.getCapability(ProviderHungryAnimal.CAP, null);
		ICapabilityTamableAnimal capTaming = entity.getCapability(ProviderTamableAnimal.CAP, null);
		IFoodPreference<ItemStack> prefItem = FoodPreferenceManager.getInstance().REGISTRY_ITEM.get(entity.getClass());

		boolean flagEat = false;
		boolean flagCure = false;
		int heat = 0;
		Item item = itemstack.getItem();
		if (prefItem.canEat(capHungry, itemstack) && capTaming.getTamingLevel() == TamingLevel.TAMED) {
			flagEat = true;
		}
		if (entity.isPotionActive(ModPotions.potionDisease) && capTaming.getTamingLevel() == TamingLevel.TAMED) {
			if (item == ItemBlock.getItemFromBlock(Blocks.RED_MUSHROOM) || item == ItemBlock.getItemFromBlock(Blocks.BROWN_MUSHROOM)) {
				flagCure = true;
			}
		}
		if (!entity.isPotionActive(ModPotions.potionInheat) && capTaming.getTamingLevel() == TamingLevel.TAMED) {
			if (item == Items.GOLDEN_APPLE) {
				heat = 1200;
			}
			if (item == Items.GOLDEN_CARROT) {
				heat = 600;
			}
		}

		if (flagEat) {
			eatFoodBonus(entity, capHungry, capTaming, itemstack);
		}
		if (flagCure) {
			entity.removePotionEffect(ModPotions.potionDisease);
		}
		if (heat > 0) {
			entity.addPotionEffect(new PotionEffect(ModPotions.potionInheat, heat, 1));
		}
		if (flagEat || flagCure || heat > 0) {
			itemstack.shrink(1);

			// Play Animation

			return new Pair<Boolean, EnumActionResult>(true, EnumActionResult.SUCCESS);
		}

		// Skipping Event to Entity
		if (entity.isBreedingItem(itemstack)) {
			return new Pair<Boolean, EnumActionResult>(true, EnumActionResult.PASS);
		}
		// For horses, they do not implement isBreedingItem properly
		if (item == Items.WHEAT || item == Items.SUGAR || item == Item.getItemFromBlock(Blocks.HAY_BLOCK) || item == Items.APPLE || item == Items.GOLDEN_CARROT
				|| item == Items.GOLDEN_APPLE) {
			return new Pair<Boolean, EnumActionResult>(true, EnumActionResult.PASS);
		}

		return new Pair<Boolean, EnumActionResult>(false, null);
	}

	private void eatFoodBonus(EntityAnimal entity, ICapabilityHungryAnimal capHungry, ICapabilityTamableAnimal capTaming, ItemStack item) {
		// TODO It must merged with AI's eatFoodBonus to increase maintainance
		if (item.isEmpty())
			return;

		IFoodPreference<ItemStack> pref = FoodPreferenceManager.getInstance().REGISTRY_ITEM.get(entity.getClass());

		double nutrient = pref.getNutrient(item);
		capHungry.addNutrient(nutrient);

		double stomach = pref.getStomach(item);
		capHungry.addStomach(stomach);

		if (entity.getGrowingAge() < 0) {
			NBTTagCompound tag = item.getTagCompound();
			if (tag == null || !tag.hasKey("isNatural") || !tag.getBoolean("isNatural")) {
				int duration = (int) (nutrient / entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_weight_bmr).getAttributeValue());
				entity.addPotionEffect(new PotionEffect(ModPotions.potionGrowth, duration, 1));
			}
		}

		NBTTagCompound tag = item.getTagCompound();
		if (tag == null || !tag.hasKey("isNatural") || !tag.getBoolean("isNatural")) {
			capTaming.addTaming(0.0001 / entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_weight_bmr).getAttributeValue() * nutrient);
		}
	}

	public static class Pair<A, B> {

		public A left;
		public B right;

		public Pair(A left, B right) {
			this.left = left;
			this.right = right;
		}
	}
}
