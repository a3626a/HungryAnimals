package oortcloud.hungryanimals.entities.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
import oortcloud.hungryanimals.entities.attributes.AttributeManager;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceManager;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState.HashBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;
import oortcloud.hungryanimals.entities.properties.handler.HungryAnimalManager;
import oortcloud.hungryanimals.potion.ModPotions;

public class ExtendedPropertiesHungryAnimal implements IExtendedEntityProperties {

	public double taming_factor = 0.998;

	public EntityAnimal entity;
	public World worldObj;
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

		AttributeManager.getInstance().registerAttributes(this.entity);
	}

	public void postInit() {
		AttributeManager.getInstance().applyAttributes(this.entity);
		acceptProperty();
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
