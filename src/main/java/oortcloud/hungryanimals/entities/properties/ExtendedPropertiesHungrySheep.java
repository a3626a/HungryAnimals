package oortcloud.hungryanimals.entities.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import oortcloud.hungryanimals.configuration.util.DropMeat;
import oortcloud.hungryanimals.configuration.util.DropRandom;
import oortcloud.hungryanimals.configuration.util.DropRare;
import oortcloud.hungryanimals.configuration.util.HashBlock;
import oortcloud.hungryanimals.configuration.util.HashItem;
import oortcloud.hungryanimals.items.ModItems;

public class ExtendedPropertiesHungrySheep extends ExtendedPropertiesHungryAnimal {

	public static final String key = "extendedPropertiesHungrySheep";

	public EntitySheep entity;
	public int wool;

	public static int wool_delay;
	public static double wool_hunger;

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
	public static int default_wool_delay;
	public static double default_wool_hunger;

	@Override
	public void init(Entity entity, World world) {
		super.init(entity, world);
		this.entity = (EntitySheep) entity;

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
		wool_delay = default_wool_delay;
		wool_hunger = default_wool_hunger;

		this.hunger = this.hunger_max / 2.0;
		this.excretion = 0;
		this.taming = -2;
		this.wool = this.wool_delay;
	}

	@Override
	public void update() {
		if (!this.worldObj.isRemote) {
			if (this.wool > 0 && this.entity.getSheared()) {
				this.wool--;
			}
			if (this.wool == 0 && this.entity.getSheared()) {
				this.entity.setSheared(false);
				this.subHunger(this.wool_hunger);
				this.wool = this.wool_delay;
			}
		}
		super.update();
	}

	public void postInit() {
		super.postInit();
		this.removeAI(new Class[] { EntityAIEatGrass.class });
	}

	@Override
	protected void applyEntityAttributes() {
		this.entity.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
		this.entity.heal(this.entity.getMaxHealth());
		this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20D);
	}

	@Override
	public void dropFewItems(boolean isHitByPlayer, int looting, List<EntityItem> drops) {
		super.dropFewItems(isHitByPlayer, looting, drops);
		if (entity.isBurning()) {
			for (EntityItem i : drops) {
				if (i.getEntityItem().getItem() == Items.mutton) {
					i.setEntityItemStack(new ItemStack(Items.cooked_mutton, i.getEntityItem().stackSize));
				}
			}
		}
		if (this.entity.getGrowingAge() >= 0) {
			if (!this.entity.getSheared()) {
				this.entity.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), (int)((2 + this.entity.getRNG().nextInt(2))* (1+(entity.getRNG().nextInt(1 + looting)) / 3.0)),
						this.entity.getFleeceColor().getMetadata()), 0.0F);
			}
		}
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound tag = new NBTTagCompound();
		compound.setTag(key, tag);
		tag.setDouble("hunger", this.hunger);
		tag.setDouble("excretion", this.excretion);
		tag.setDouble("tamedValue", this.taming);
		tag.setInteger("wool", this.wool);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound tag = (NBTTagCompound) compound.getTag(key);
		if (tag != null) {
			this.hunger = tag.getDouble("hunger");
			this.excretion = tag.getDouble("excretion");
			this.taming = tag.getDouble("tamedValue");
			this.wool = tag.getInteger("wool");
		}
	}

}
