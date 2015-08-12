package oortcloud.hungryanimals.entities.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import oortcloud.hungryanimals.configuration.util.DropMeat;
import oortcloud.hungryanimals.configuration.util.DropRandom;
import oortcloud.hungryanimals.configuration.util.DropRare;
import oortcloud.hungryanimals.configuration.util.HashBlock;
import oortcloud.hungryanimals.configuration.util.HashItem;

public class ExtendedPropertiesHungryRabbit extends ExtendedPropertiesHungryAnimal {
	public static final String key = "extendedPropertiesHungryRabbit";

	public EntityRabbit entity;

	public static double default_hunger_max;
	public static double default_hunger_bmr;
	public static HashMap<HashItem, Double> default_hunger_food = new HashMap<HashItem, Double>();
	public static HashMap<HashBlock, Double> default_hunger_block = new HashMap<HashBlock, Double>();
	public static ArrayList<DropMeat> default_drop_meat = new ArrayList<DropMeat>();
	public static ArrayList<DropRandom> default_drop_random = new ArrayList<DropRandom>();
	public static ArrayList<DropRare> default_drop_rare = new ArrayList<DropRare>();
	public static double default_courtship_hunger;
	public static double default_courtship_probability;
	public static double default_courtship_hungerCondition;
	public static double default_excretion_factor;
	public static double default_child_hunger;

	@Override
	public void init(Entity entity, World world) {
		super.init(entity, world);
		this.entity = (EntityRabbit) entity;

		hunger_max = default_hunger_max;
		hunger_bmr = default_hunger_bmr;
		hunger_food = default_hunger_food;
		hunger_block = default_hunger_block;
		drop_meat = default_drop_meat;
		drop_random = default_drop_random;
		drop_rare = default_drop_rare;
		courtship_hunger = default_courtship_hunger;
		courtship_probability = default_courtship_probability;
		courtship_hungerCondition = default_courtship_hungerCondition;
		excretion_factor = default_excretion_factor;
		child_hunger = default_child_hunger;
		taming_factor = 0.998;

		this.hunger = this.hunger_max / 2.0;
		this.excretion = 0;
		this.taming = -2;
	}
	
	public void postInit() {
		super.postInit();
		this.removeAI(EntityRabbit.class.getDeclaredClasses());
		//this.removeAI(new Class[] {Class.forName("net.minecraft.entity.passive.EntityRabbit.AIPanic"), Class.forName("net.minecraft.entity.passive.EntityRabbit.AIRaidFarm"), Class.forName("net.minecraft.entity.passive.EntityRabbit.AIAvoidEntity") });
	}

	@Override
	protected void applyEntityAttributes() {
		this.entity.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
		this.entity.heal(this.entity.getMaxHealth());
		this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
	}

	@Override
	public void dropFewItems(boolean isHitByPlayer, int looting, List<EntityItem> drops) {
		super.dropFewItems(isHitByPlayer, looting, drops);
		if (entity.isBurning()) {
			for (EntityItem i : drops) {
				if (i.getEntityItem().getItem() == Items.rabbit) {
					i.setEntityItemStack(new ItemStack(Items.cooked_rabbit, i.getEntityItem().stackSize));
				}
			}
		}
	}
}
