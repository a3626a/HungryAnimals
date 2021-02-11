package oortcloud.hungryanimals.entities.event;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oortcloud.hungryanimals.block.ExcretaBlock;
import oortcloud.hungryanimals.block.ModBlocks;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.ai.handler.AIContainers;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.capability.ICapabilityAgeable;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityProducingAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderAgeable;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderProducingAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.capability.TamingLevel;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferences;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreference;
import oortcloud.hungryanimals.entities.handler.Cures;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;
import oortcloud.hungryanimals.entities.handler.InHeats;
import oortcloud.hungryanimals.potion.ModPotions;
import oortcloud.hungryanimals.utils.Pair;
import oortcloud.hungryanimals.utils.Tamings;

public class EntityEventHandler {

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (!(event.getEntity() instanceof MobEntity))
			return;

		MobEntity entity = (MobEntity) event.getEntity();

		if (!HungryAnimalManager.getInstance().isRegistered(entity.getClass()))
			return;

		ModAttributes.getInstance().applyAttributes(entity);

		if (!entity.getEntityWorld().isRemote)
			AIContainers.getInstance().apply(entity);

		// Disable Vanilla Egg Drop
		if (entity instanceof EntityChicken) {
			EntityChicken chicken = (EntityChicken) entity;
			chicken.timeUntilNextEgg = Integer.MAX_VALUE;
		}

		if (!entity.getEntityData().contains(References.MODID + ".isInitialized")) {
			// This is called only once for each animal
			// Should be used as "After Constructor Code Block"
			entity.setHealth(entity.getMaxHealth());

			ICapabilityHungryAnimal hungry = entity.getCapability(ProviderHungryAnimal.CAP).orElse(null);
			if (hungry != null) {
			ICapabilityAgeable ageable = entity.getCapability(ProviderAgeable.CAP).orElse(null);
			if (ageable != null && ageable.getAge() < 0) {
				// Baby created
				hungry.setWeight(hungry.getNormalWeight());
			}		
				double stomach = hungry.getMaxStomach() / 2.0;
				hungry.setStomach(stomach);
				hungry.setNutrient(stomach * 0.35);
			}
			
			entity.getEntityData().setBoolean(References.MODID + ".isInitialized", true);
		}

	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (!(event.getEntity() instanceof MobEntity))
			return;

		MobEntity entity = (MobEntity) event.getEntity();
		if (!HungryAnimalManager.getInstance().isRegistered(entity.getClass()))
			return;

		ModAttributes.getInstance().registerAttributes(entity);
	}

	@SubscribeEvent
	public void onMobEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		if (!(event.getEntity() instanceof MobEntity))
			return;

		MobEntity animal = (MobEntity) event.getEntity();

		if (!HungryAnimalManager.getInstance().isRegistered(animal.getClass()))
			return;

		if (animal.getEntityWorld().getTotalWorldTime() % 20 == 0) {
			if (!animal.getEntityWorld().isRemote) {
				updateHunger(animal);
				updateCourtship(animal);
				updateExcretion(animal);
				updateTaming(animal);
				updateEnvironmentalEffet(animal);
				updateRecovery(animal);

				ICapabilityHungryAnimal capHungry = animal.getCapability(ProviderHungryAnimal.CAP).orElse(null);
				if (capHungry != null)
					if (capHungry.getWeight() < capHungry.getStarvinglWeight()) {
						onStarve(animal);
					}

				ICapabilityProducingAnimal capProducing = animal.getCapability(ProviderProducingAnimal.CAP).orElse(null);
				if (capProducing != null) {
					capProducing.update();
				}

				ICapabilityAgeable ageable = animal.getCapability(ProviderAgeable.CAP).orElse(null);
				if (ageable != null && ageable.getAge() < 0) {
					// TODO I HATE THIS BUSY LOOP. IS THERE ANY BETTER SOLUTION?
					if (!animal.isPotionActive(ModPotions.potionYoung)) {
						animal.addPotionEffect(
								new EffectInstance(ModPotions.potionYoung, Integer.MAX_VALUE, 0, false, false));
						if (animal.getHealth() > animal.getMaxHealth()) {
							animal.setHealth(animal.getMaxHealth());
						}
					}
				}
			}
		}
	}

	private void updateHunger(MobEntity entity) {
		ICapabilityHungryAnimal cap = entity.getCapability(ProviderHungryAnimal.CAP).orElse(null);

		if (cap == null)
			return;

		double nutrient = cap.getNutrient();
		double stomach = cap.getStomach();
		double digest = entity.getAttribute(ModAttributes.hunger_stomach_digest).getValue();

		ICapabilityAgeable ageable = entity.getCapability(ProviderAgeable.CAP).orElse(null);
		
		if (ageable != null && ageable.getAge() < 0) {
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

		double default_bmr = entity.getAttribute(ModAttributes.hunger_weight_bmr).getValue();
		double default_weight = entity.getAttribute(ModAttributes.hunger_weight_normal).getValue();
		double bmr = default_bmr * Math.pow(cap.getWeight() / default_weight, 3.0 / 4.0);

		cap.addWeight(-bmr);
	}

	private void updateCourtship(MobEntity entity) {
		if (!(entity instanceof AnimalEntity))
			return;
		
		AnimalEntity animal = (AnimalEntity)entity;
		
		ICapabilityHungryAnimal hungry = animal.getCapability(ProviderHungryAnimal.CAP).orElse(null);
		ICapabilityAgeable ageable = animal.getCapability(ProviderAgeable.CAP).orElse(null);
		
		if (hungry == null || ageable == null)
			return;

		double courtship_stomach_condition = animal.getAttribute(ModAttributes.courtship_stomach_condition)
				.getValue();
		double courtship_probability = animal.getAttribute(ModAttributes.courtship_probability)
				.getValue();
		double child_weight = animal.getAttribute(ModAttributes.hunger_weight_normal_child).getValue()
				/ 2.0;

		if (ageable.getAge() == 0 && !animal.isInLove()
				&& hungry.getStomach() / hungry.getMaxStomach() > courtship_stomach_condition
				&& hungry.getWeight() - child_weight > hungry.getStarvinglWeight()
				&& animal.getRNG().nextDouble() < courtship_probability) {
			animal.setInLove(null);
			hungry.addWeight(-entity.getAttribute(ModAttributes.courtship_weight).getValue());
		}
	}

	private void updateExcretion(MobEntity entity) {
		ICapabilityHungryAnimal cap = entity.getCapability(ProviderHungryAnimal.CAP).orElse(null);

		if (cap == null)
			return;

		if (cap.getExcretion() > 1) {
			cap.addExcretion(-1);
			BlockPos pos = entity.getPosition();
			BlockState meta = entity.getEntityWorld().getBlockState(pos);
			Block block = meta.getBlock();

			if (block == ModBlocks.EXCRETA.get()) {
				int exc = ((ExcretaBlock.EnumType) meta.getValue(ExcretaBlock.CONTENT)).getExcreta();
				int man = ((ExcretaBlock.EnumType) meta.getValue(ExcretaBlock.CONTENT)).getManure();
				if (exc + man < 4) {
					entity.getEntityWorld().setBlockState(pos,
							meta.with(ExcretaBlock.CONTENT, ExcretaBlock.EnumType.getValue(exc + 1, man)), 2);
				} else if (exc + man == 4) {

				}
			} else if (block.isAir(meta, entity.getEntityWorld(), pos)
					|| block.isReplaceable(entity.getEntityWorld(), pos)) {
				entity.getEntityWorld().setBlockState(pos, ModBlocks.EXCRETA.get().getDefaultState()
						.with(ExcretaBlock.CONTENT, ExcretaBlock.EnumType.getValue(1, 0)), 2);
			} else {
				// TODO When there's no place to put block
			}
		}
	}

	private void updateEnvironmentalEffet(MobEntity entity) {
		if (!HungryAnimalManager.getInstance().isRegistered(entity.getClass()))
			return;

		ICapabilityAgeable ageable = entity.getCapability(ProviderAgeable.CAP).orElse(null);
		if (ageable == null)
			return;
		
		BlockState floor = entity.getEntityWorld().getBlockState(entity.getPosition().down());
		if (floor.getBlock() == ModBlocks.FLOOR_COVER_LEAF.get()) {
			int j = ageable.getAge();
			if (j < 0) {
				j += (int) ((entity.getRNG().nextInt(4) + 1) / 4.0);
				ageable.setAge(j);
			}
		}
		if (floor.getBlock() == ModBlocks.FLOOR_COVER_WOOL.get()) {
			int j = ageable.getAge();
			if (j > 0) {
				j -= (int) ((entity.getRNG().nextInt(4) + 1) / 4.0);
				ageable.setAge(j);
			}
		}
	}

	private void updateTaming(MobEntity entity) {
		double radius = 16;

		if (entity.getEntityWorld().getTotalWorldTime() % 200 == 0) {
			ICapabilityTamableAnimal cap = entity.getCapability(ProviderTamableAnimal.CAP).orElse(null);

			if (cap == null)
				return;

			List<PlayerEntity> players = entity.getEntityWorld().getEntitiesWithinAABB(PlayerEntity.class,
					entity.getBoundingBox().grow(radius));
			double tamingFactorWild = entity.getAttribute(ModAttributes.taming_factor_near_wild)
					.getValue();
			double tamingFactorTamed = entity.getAttribute(ModAttributes.taming_factor_near_tamed)
					.getValue();

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

	private void updateRecovery(MobEntity entity) {
		ICapabilityHungryAnimal cap = entity.getCapability(ProviderHungryAnimal.CAP).orElse(null);

		if (cap == null)
			return;

		if (entity.getHealth() < entity.getMaxHealth() && cap.getStomach() / cap.getMaxStomach() > 0.8
				&& (entity.getEntityWorld().getWorldTime() % 200) == 0) {
			entity.heal(1.0F);
			cap.addWeight(-cap.getNormalWeight() / entity.getMaxHealth());
		}
	}

	public void onStarve(MobEntity entity) {
		entity.attackEntityFrom(DamageSource.STARVE, 0.5F);
	}

	@SubscribeEvent
	public void onMobEntityAttackedByPlayer(LivingAttackEvent event) {
		if (!(event.getEntity() instanceof MobEntity))
			return;

		MobEntity entity = (MobEntity) event.getEntity();

		ICapabilityTamableAnimal cap = entity.getCapability(ProviderTamableAnimal.CAP).orElse(null);

		if (cap == null)
			return;

		DamageSource source = event.getSource();
		if (!entity.isEntityInvulnerable(source)) {
			if (source.getTrueSource() instanceof PlayerEntity) {
				cap.addTaming(-4 / entity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getValue()
						* event.getAmount());
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
		if (!(event.getTarget() instanceof MobEntity))
			return;

		MobEntity entity = (MobEntity) event.getTarget();
		if (!HungryAnimalManager.getInstance().isRegistered(entity.getClass()))
			return;
		Pair<Boolean, ActionResultType> result = interact(event, entity);
		event.setCanceled(result.left);
		event.setCancellationResult(result.right);
	}

	private Pair<Boolean, ActionResultType> interact(EntityInteract event, MobEntity entity) {
		if (event.getItemStack().isEmpty())
			return new Pair<Boolean, ActionResultType>(false, null);
		return interact(event, event.getHand(), event.getItemStack(), entity);
	}

	private Pair<Boolean, ActionResultType> interact(EntityInteract event, Hand hand, ItemStack itemstack,
			MobEntity entity) {
		ICapabilityHungryAnimal capHungry = entity.getCapability(ProviderHungryAnimal.CAP).orElse(null);
		ICapabilityTamableAnimal capTaming = entity.getCapability(ProviderTamableAnimal.CAP).orElse(null);
		IFoodPreference<ItemStack> prefItem = FoodPreferences.getInstance().REGISTRY_ITEM.get(entity.getClass());

		boolean flagEat = false;
		boolean flagCure = false;
		int heat = 0;
		Item item = itemstack.getItem();
		TamingLevel tamingLevel = Tamings.getLevel(capTaming);
		if (capHungry != null && prefItem.canEat(capHungry, itemstack) && tamingLevel == TamingLevel.TAMED) {
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
			entity.addPotionEffect(new EffectInstance(ModPotions.potionInheat, heat, 1));
		}
		if (flagEat || flagCure || heat > 0) {
			itemstack.shrink(1);
			// Play Animation
			return new Pair<Boolean, ActionResultType>(true, ActionResultType.SUCCESS);
		}

		ICapabilityProducingAnimal capProducing = entity.getCapability(ProviderProducingAnimal.CAP).orElse(null);
		if (capProducing != null) {
			ActionResultType action = capProducing.interact(event, hand, itemstack);
			if (action != ActionResultType.PASS) {
				return new Pair<Boolean, ActionResultType>(true, action);
			}
		}

		return cancelEvent(item, itemstack, entity, capHungry, tamingLevel);
	}

	private Pair<Boolean, ActionResultType> cancelEvent(Item item, ItemStack itemstack, MobEntity entity,
			ICapabilityHungryAnimal capHungry, TamingLevel tamingLevel) {
		// Skip Event. TODO Too Dirty Here.
		// For horses, they do not implement isBreedingItem properly
		if (entity.getClass() == EntityCow.class) {
			if (item == Items.BUCKET) {
				return new Pair<Boolean, ActionResultType>(true, ActionResultType.PASS);
			}
		}
		if (entity.getClass() == EntityMooshroom.class) {
			if (item == Items.BOWL) {
				return new Pair<Boolean, ActionResultType>(true, ActionResultType.PASS);
			}
			if (item == Items.BUCKET) {
				return new Pair<Boolean, ActionResultType>(true, ActionResultType.PASS);
			}
		}
		if (entity instanceof AbstractHorse) {
			if (item == Items.WHEAT || item == Items.SUGAR || item == Item.getItemFromBlock(Blocks.HAY_BLOCK)
					|| item == Items.APPLE || item == Items.GOLDEN_CARROT || item == Items.GOLDEN_APPLE) {
				return new Pair<Boolean, ActionResultType>(true, ActionResultType.PASS);
			}
		}
		if (entity instanceof WolfEntity && tamingLevel != TamingLevel.TAMED) {
			// For wolves, to disable feed bones before tamed
			if (item == Items.BONE) {
				return new Pair<Boolean, ActionResultType>(true, ActionResultType.PASS);
			}
		}
		if (entity instanceof EntityOcelot) {
			if (item == Items.FISH) {
				if (tamingLevel != TamingLevel.TAMED) {
					// For ocelots, to disable feed fish before tamed
					return new Pair<Boolean, ActionResultType>(true, ActionResultType.PASS);
				} else {
					if (!((TameableEntity) entity).isTamed()) {
						// Can feed wild(Vanilla) animal fish
						return new Pair<Boolean, ActionResultType>(false, null);
					} else {
						// Can not feed tamed(Vanilla) animal fish
						return new Pair<Boolean, ActionResultType>(true, ActionResultType.PASS);
					}
				}
			}
		}
		if (entity instanceof SheepEntity) {
			/*
			 * if (item == Items.SHEARS) { // TODO How to disable all 'shearing' items...!
			 * return new Pair<Boolean, ActionResultType>(true, ActionResultType.PASS); }
			 */
		}
		// Skipping Event to Entity
		if (capHungry != null && entity instanceof AnimalEntity) {
			if (((AnimalEntity)entity).isBreedingItem(itemstack)) {
				return new Pair<Boolean, ActionResultType>(true, ActionResultType.PASS);
			}
		}
		return new Pair<Boolean, ActionResultType>(false, null);
	}

	private void eatFoodBonus(MobEntity entity, ICapabilityHungryAnimal capHungry,
			ICapabilityTamableAnimal capTaming, ItemStack item) {
		// TODO It must merged with AI's eatFoodBonus for better maintenance
		if (item.isEmpty())
			return;

		IFoodPreference<ItemStack> pref = FoodPreferences.getInstance().REGISTRY_ITEM.get(entity.getClass());

		double nutrient = pref.getNutrient(item);
		capHungry.addNutrient(nutrient);

		double stomach = pref.getStomach(item);
		capHungry.addStomach(stomach);
		
		ICapabilityAgeable ageable = entity.getCapability(ProviderAgeable.CAP).orElse(null);
		if (ageable != null && ageable.getAge() < 0) {
			CompoundNBT tag = item.getTag();
			if (tag == null || !tag.contains("isNatural") || !tag.getBoolean("isNatural")) {
				int duration = (int) (nutrient
						/ entity.getAttribute(ModAttributes.hunger_weight_bmr).getValue());
				entity.addPotionEffect(new EffectInstance(ModPotions.potionGrowth, duration, 1));
			}
		}

		CompoundNBT tag = item.getTag();
		if (tag == null || !tag.contains("isNatural") || !tag.getBoolean("isNatural")) {
			double taming_factor = entity.getAttribute(ModAttributes.taming_factor_food).getValue();
			if (capTaming != null) {
				capTaming.addTaming(taming_factor
						/ entity.getAttribute(ModAttributes.hunger_weight_bmr).getValue() * nutrient);
			}
		}
	}

	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent event) {
		Entity attacker = event.getSource().getTrueSource();

		if (attacker == null)
			return;

		ICapabilityTamableAnimal cap = attacker.getCapability(ProviderTamableAnimal.CAP).orElse(null);

		if (Tamings.getLevel(cap) != TamingLevel.TAMED) {
			for (ItemEntity i : event.getDrops()) {
				CompoundNBT tag = i.getItem().getTag();
				if (tag == null) {
					tag = new CompoundNBT();
					i.getItem().setTagCompound(tag);
				}
				tag.setBoolean("isNatural", true);
			}
		}
	}
}
