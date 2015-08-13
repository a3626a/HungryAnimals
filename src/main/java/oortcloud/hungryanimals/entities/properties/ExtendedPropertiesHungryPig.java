package oortcloud.hungryanimals.entities.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import oortcloud.hungryanimals.configuration.util.DropMeat;
import oortcloud.hungryanimals.configuration.util.DropRandom;
import oortcloud.hungryanimals.configuration.util.DropRare;
import oortcloud.hungryanimals.configuration.util.HashBlock;
import oortcloud.hungryanimals.configuration.util.HashItem;
import oortcloud.hungryanimals.entities.ai.EntityAITemptEatableItem;
import oortcloud.hungryanimals.items.ModItems;

public class ExtendedPropertiesHungryPig extends ExtendedPropertiesHungryAnimal {

	public static final String key = "extendedPropertiesHungryPig";

	public EntityPig entity;

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
		this.entity = (EntityPig) entity;

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

	@Override
	protected void applyEntityAttributes() {
		this.entity.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
		this.entity.heal(this.entity.getMaxHealth());
		this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}
	
	@Override
	public void postInit() {
		super.postInit();
		this.entity.tasks.addTask(4, new EntityAITempt(this.entity,1.5D, Items.carrot_on_a_stick,false));
	}

	@Override
	public void dropFewItems(boolean isHitByPlayer, int looting, List<EntityItem> drops) {
		super.dropFewItems(isHitByPlayer, looting, drops);
		if (this.entity.getSaddled()) {
			this.entity.dropItem(Items.saddle, 1);
		}
		if (entity.isBurning()) {
			for (EntityItem i : drops) {
				if (i.getEntityItem().getItem() == Items.porkchop) {
					i.setEntityItemStack(new ItemStack(Items.cooked_porkchop, i.getEntityItem().stackSize));
				}
			}
		}
	}

}
