package oortcloud.hungryanimals.entities.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import oortcloud.hungryanimals.blocks.BlockExcreta;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.configuration.util.ValueDropMeat;
import oortcloud.hungryanimals.configuration.util.ValueDropRandom;
import oortcloud.hungryanimals.configuration.util.ValueDropRare;
import oortcloud.hungryanimals.entities.ai.EntityAIAvoidPlayer;
import oortcloud.hungryanimals.entities.ai.EntityAIMateModified;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToEatBlock;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToEatItem;
import oortcloud.hungryanimals.entities.ai.EntityAIMoveToTrough;
import oortcloud.hungryanimals.entities.ai.EntityAITemptEdibleItem;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceManager;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState.HashBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;
import oortcloud.hungryanimals.entities.properties.handler.HungryAnimalManager;
import oortcloud.hungryanimals.entities.properties.handler.ModAttributes;
import oortcloud.hungryanimals.potion.ModPotions;

public class ExtendedPropertiesHungryAnimal implements IExtendedEntityProperties {

	public double taming_factor = 0.998;

	public EntityAnimal entity;
	public World worldObj;
	public EntityAIMoveToTrough ai_moveToFoodbox;
	// public EntityAICrank ai_crank;

	public void acceptProperty() {
		entity.setHealth(this.entity.getMaxHealth());

		taming_factor = 0.998;
		hunger = entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_max).getAttributeValue() / 2.0;
		excretion = 0;
		taming = -2;
	}

	@Override
	public void init(Entity entity, World world) {
		this.entity = (EntityAnimal) entity;
		this.worldObj = world;
		this.ai_moveToFoodbox = new EntityAIMoveToTrough(this.entity, this, 1.0D);

		HungryAnimalManager.getInstance().registerAttributes(this.entity);
	}

	public void postInit() {
		HungryAnimalManager.getInstance().applyAttributes(this);
		acceptProperty();
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

	public boolean interact(EntityPlayer entity) {
		ItemStack stack = entity.getCurrentEquippedItem();
		if (stack == null)
			return false;

		if (this.canEatFood(stack) && this.taming >= 1) {
			this.eatFoodBonus(stack);
			stack.stackSize--;
			if (stack.stackSize == 0) {
				entity.inventory.setInventorySlotContents(entity.inventory.currentItem, null);
			}
			return true;
		} else if (this.entity.isPotionActive(ModPotions.potionDisease) && this.taming >= 1) {
			if (stack.getItem() == ItemBlock.getItemFromBlock(Blocks.RED_MUSHROOM)
					|| stack.getItem() == ItemBlock.getItemFromBlock(Blocks.BROWN_MUSHROOM)) {
				this.entity.removePotionEffect(ModPotions.potionDisease);
				stack.stackSize--;
				if (stack.stackSize == 0) {
					entity.inventory.setInventorySlotContents(entity.inventory.currentItem, null);
				}
			}
			return true;
		}

		if (this.entity.isBreedingItem(stack)) {
			return true;
		}
		// To avoid vanilla breeding.

		return false;
	}
	
}
