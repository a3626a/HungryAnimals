package oortcloud.hungryanimals.entities.event;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oortcloud.hungryanimals.blocks.BlockExcreta;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.entities.ai.handler.AIContainers;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.capability.TamingLevel;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferences;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreference;
import oortcloud.hungryanimals.entities.handler.Cures;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;
import oortcloud.hungryanimals.entities.handler.InHeats;
import oortcloud.hungryanimals.potion.ModPotions;
import oortcloud.hungryanimals.utils.Tamings;

public class EntityEventHandler {

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (!(event.getEntity() instanceof EntityAnimal))
			return;

		EntityAnimal entity = (EntityAnimal) event.getEntity();

		ModAttributes.getInstance().applyAttributes(entity);
		
		// TODO applyAttributes로 인해 영향을 받았을 때만!
		if (true) {
			entity.setHealth(entity.getMaxHealth());
		}
		
		if (!entity.getEntityWorld().isRemote)
			AIContainers.getInstance().apply(entity);
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (!(event.getEntity() instanceof EntityAnimal))
			return;

		EntityAnimal entity = (EntityAnimal) event.getEntity();

		ModAttributes.getInstance().registerAttributes(entity);
	}

	@SubscribeEvent
	public void onLivingEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		if (!(event.getEntity() instanceof EntityAnimal))
			return;

		EntityAnimal entity = (EntityAnimal) event.getEntity();

		if (!entity.getEntityWorld().isRemote && entity.getEntityWorld().getTotalWorldTime() % 20 == 0) {
			updateHunger(entity);
			updateCourtship(entity);
			updateExcretion(entity);
			updateTaming(entity);
			updateEnvironmentalEffet(entity);
			updateRecovery(entity);

			if (entity.hasCapability(ProviderHungryAnimal.CAP, null)) {
				ICapabilityHungryAnimal cap = entity.getCapability(ProviderHungryAnimal.CAP, null);
				if (cap.getWeight() < cap.getStarvinglWeight()) {
					onStarve(entity);
				}
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
		if (!entity.hasCapability(ProviderHungryAnimal.CAP, null)) {
			return;
		}
		
		ICapabilityHungryAnimal cap = entity.getCapability(ProviderHungryAnimal.CAP, null);
		
		double nutrient = cap.getNutrient();
		double stomach = cap.getStomach();
		double digest = entity.getEntityAttribute(ModAttributes.hunger_stomach_digest).getAttributeValue();

		if (entity.getGrowingAge() < 0) {
			// Child
			// Childhood growth acceleration
			if (cap.getWeight() < cap.getNormalWeight()) {
				digest *= 2.0;
			}
		}

		if (stomach > 0) {
			if (digest > stomach) {
				digest = stomach;
			}

			double nutrient_digest = nutrient / stomach * digest;

			cap.addNutrient(-nutrient_digest);
			cap.addWeight(nutrient_digest);
			cap.addStomach(-digest);
		}

		double default_bmr = entity.getEntityAttribute(ModAttributes.hunger_weight_bmr).getAttributeValue();
		double default_weight = entity.getEntityAttribute(ModAttributes.hunger_weight_normal).getAttributeValue();
		double bmr = default_bmr * Math.pow(cap.getWeight() / default_weight, 3.0 / 4.0);

		cap.addWeight(-bmr);
	}

	private void updateCourtship(EntityAnimal entity) {
		if (!entity.hasCapability(ProviderHungryAnimal.CAP, null)) {
			return;
		}
		
		ICapabilityHungryAnimal cap = entity.getCapability(ProviderHungryAnimal.CAP, null);

		double courtship_stomach_condition = entity.getEntityAttribute(ModAttributes.courtship_stomach_condition).getAttributeValue();
		double courtship_probability = entity.getEntityAttribute(ModAttributes.courtship_probability).getAttributeValue();
		double child_weight = entity.getEntityAttribute(ModAttributes.hunger_weight_normal_child).getAttributeValue() / 2.0;

		if (entity.getGrowingAge() == 0 && !entity.isInLove() && cap.getStomach() / cap.getMaxStomach() > courtship_stomach_condition
				&& cap.getWeight() - child_weight > cap.getStarvinglWeight() && entity.getRNG().nextDouble() < courtship_probability) {
			entity.setInLove(null);
			cap.addWeight(-entity.getEntityAttribute(ModAttributes.courtship_weight).getAttributeValue());
		}
	}

	private void updateExcretion(EntityAnimal entity) {
		if (!entity.hasCapability(ProviderHungryAnimal.CAP, null)) {
			return;
		}
		
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
		if (!HungryAnimalManager.getInstance().isRegistered(entity.getClass()))
			return;
		
		IBlockState floor = entity.getEntityWorld().getBlockState(entity.getPosition().down());
		if (floor.getBlock() == ModBlocks.floorcover_leaf) {
			int j = entity.getGrowingAge();
			if (j < 0) {
				j += (int) ((entity.getRNG().nextInt(4) + 1) / 4.0);
				entity.setGrowingAge(j);
			}
		}
		if (floor.getBlock() == ModBlocks.floorcover_wool) {
			int j = entity.getGrowingAge();
			if (j > 0) {
				j -= (int) ((entity.getRNG().nextInt(4) + 1) / 4.0);
				entity.setGrowingAge(j);
			}
		}
		if (floor.getBlock() == ModBlocks.floorcover_ironbar) {
			// TODO floorcover ironbar
		}
	}

	private void updateTaming(EntityAnimal entity) {
		double radius = 16;

		if (entity.getEntityWorld().getTotalWorldTime() % 200 == 0) {
			if (entity.hasCapability(ProviderTamableAnimal.CAP, null)) {
				List<EntityPlayer> players = entity.getEntityWorld().getEntitiesWithinAABB(EntityPlayer.class, entity.getEntityBoundingBox().grow(radius));
				ICapabilityTamableAnimal cap = entity.getCapability(ProviderTamableAnimal.CAP, null);
				double tamingFactorWild = entity.getEntityAttribute(ModAttributes.taming_factor_near_wild).getAttributeValue();
				double tamingFactorTamed = entity.getEntityAttribute(ModAttributes.taming_factor_near_tamed).getAttributeValue();

				if (players.isEmpty()) {
					if (cap.getTaming() > 0) {
						cap.setTaming(cap.getTaming() * tamingFactorTamed);
					}
				} else {
					if (cap.getTaming() < 0) {
						cap.setTaming(cap.getTaming() * tamingFactorWild);
					}
				}
			}
		}
	}

	private void updateRecovery(EntityAnimal entity) {
		if (!entity.hasCapability(ProviderHungryAnimal.CAP, null)) {
			return;
		}
		
		ICapabilityHungryAnimal cap = entity.getCapability(ProviderHungryAnimal.CAP, null);
		if (entity.getHealth() < entity.getMaxHealth() && cap.getStomach() / cap.getMaxStomach() > 0.8 && (entity.getEntityWorld().getWorldTime() % 200) == 0) {
			entity.heal(1.0F);
			cap.addWeight(-cap.getNormalWeight() / entity.getMaxHealth());
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

		if (entity.hasCapability(ProviderTamableAnimal.CAP, null)) {
			ICapabilityTamableAnimal cap = entity.getCapability(ProviderTamableAnimal.CAP, null);
			DamageSource source = event.getSource();
			if (!entity.isEntityInvulnerable(source)) {
				if (source.getTrueSource() instanceof EntityPlayer) {
					cap.addTaming(-4 / entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue() * event.getAmount());
				}
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
		IFoodPreference<ItemStack> prefItem = FoodPreferences.getInstance().REGISTRY_ITEM.get(entity.getClass());

		boolean flagEat = false;
		boolean flagCure = false;
		int heat = 0;
		Item item = itemstack.getItem();
		TamingLevel tamingLevel = Tamings.getLevel(capTaming);
		if (prefItem.canEat(capHungry, itemstack) && tamingLevel == TamingLevel.TAMED) {
			flagEat = true;
		}
		if (entity.isPotionActive(ModPotions.potionDisease) && tamingLevel == TamingLevel.TAMED) {
			flagCure = Cures.getInstance().isCure(itemstack);
		}
		if (!entity.isPotionActive(ModPotions.potionInheat) && tamingLevel == TamingLevel.TAMED) {
			heat = InHeats.getInstance().getDuration(itemstack);
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

		// For horses, they do not implement isBreedingItem properly
		if (entity instanceof AbstractHorse) {
			if (item == Items.WHEAT || item == Items.SUGAR || item == Item.getItemFromBlock(Blocks.HAY_BLOCK) || item == Items.APPLE
					|| item == Items.GOLDEN_CARROT || item == Items.GOLDEN_APPLE) {
				return new Pair<Boolean, EnumActionResult>(true, EnumActionResult.PASS);
			}
		}
		if (entity instanceof EntityWolf && tamingLevel != TamingLevel.TAMED) {
			// For wolves, to disable feed bones before tamed
			if (item == Items.BONE) {
				return new Pair<Boolean, EnumActionResult>(true, EnumActionResult.PASS);
			}
		}
		if (entity instanceof EntityOcelot) {
			if (item == Items.FISH) {
				if (tamingLevel != TamingLevel.TAMED) {
					// For ocelots, to disable feed fish before tamed
					return new Pair<Boolean, EnumActionResult>(true, EnumActionResult.PASS);

				} else {
					if (!((EntityTameable) entity).isTamed()) {
						// Can feed wild(Vanilla) animal fish
						return new Pair<Boolean, EnumActionResult>(false, null);
					} else {
						// Can not feed tamed(Vanilla) animal fish
						return new Pair<Boolean, EnumActionResult>(true, EnumActionResult.PASS);
					}
				}
			}
		}
		// Skipping Event to Entity
		if (entity.isBreedingItem(itemstack)) {
			return new Pair<Boolean, EnumActionResult>(true, EnumActionResult.PASS);
		}

		return new Pair<Boolean, EnumActionResult>(false, null);
	}

	private void eatFoodBonus(EntityAnimal entity, ICapabilityHungryAnimal capHungry, ICapabilityTamableAnimal capTaming, ItemStack item) {
		// TODO It must merged with AI's eatFoodBonus to increase maintainance
		if (item.isEmpty())
			return;

		IFoodPreference<ItemStack> pref = FoodPreferences.getInstance().REGISTRY_ITEM.get(entity.getClass());

		double nutrient = pref.getNutrient(item);
		capHungry.addNutrient(nutrient);

		double stomach = pref.getStomach(item);
		capHungry.addStomach(stomach);

		if (entity.getGrowingAge() < 0) {
			NBTTagCompound tag = item.getTagCompound();
			if (tag == null || !tag.hasKey("isNatural") || !tag.getBoolean("isNatural")) {
				int duration = (int) (nutrient / entity.getEntityAttribute(ModAttributes.hunger_weight_bmr).getAttributeValue());
				entity.addPotionEffect(new PotionEffect(ModPotions.potionGrowth, duration, 1));
			}
		}

		NBTTagCompound tag = item.getTagCompound();
		if (tag == null || !tag.hasKey("isNatural") || !tag.getBoolean("isNatural")) {
			double taming_factor = entity.getEntityAttribute(ModAttributes.taming_factor_food).getAttributeValue();
			if (capTaming != null) {
				capTaming.addTaming(taming_factor / entity.getEntityAttribute(ModAttributes.hunger_weight_bmr).getAttributeValue() * nutrient);
			}
		}
	}

	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent event) {
		Entity attacker = event.getSource().getTrueSource();

		if (attacker instanceof EntityWolf) {
			EntityWolf wolf = (EntityWolf) attacker;
			if (!wolf.isTamed()) {
				for (EntityItem i : event.getDrops()) {
					if (!i.getItem().hasTagCompound()) {
						i.getItem().setTagCompound(new NBTTagCompound());
					}
					NBTTagCompound tag = i.getItem().getTagCompound();
					tag.setBoolean("isNatural", true);
				}
			}
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
