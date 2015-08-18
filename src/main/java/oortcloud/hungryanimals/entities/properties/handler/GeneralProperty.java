package oortcloud.hungryanimals.entities.properties.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.block.BlockCrops;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.configuration.util.DropMeat;
import oortcloud.hungryanimals.configuration.util.DropRandom;
import oortcloud.hungryanimals.configuration.util.DropRare;
import oortcloud.hungryanimals.configuration.util.HashBlock;
import oortcloud.hungryanimals.configuration.util.HashItem;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.tileentities.TileEntityCrankAnimal;

public class GeneralProperty {

	public double hunger_max;
	public double hunger_bmr;
	public HashMap<HashItem, Double> hunger_food = new HashMap<HashItem, Double>();
	public HashMap<HashBlock, Double> hunger_block = new HashMap<HashBlock, Double>();
	public ArrayList<DropMeat> drop_meat = new ArrayList<DropMeat>();
	public ArrayList<DropRandom> drop_random = new ArrayList<DropRandom>();
	public ArrayList<DropRare> drop_rare = new ArrayList<DropRare>();
	public double courtship_hunger;
	public double courtship_probability;
	public double courtship_hungerCondition;
	public double excretion_factor;
	public double child_hunger;
	public double attribute_maxhealth;
	public double attribute_movespeed;
	public double crank_production;
	public double crank_food_consumption;

	public static GeneralProperty default_chicken = new GeneralProperty();
	public static GeneralProperty default_cow = new GeneralProperty();
	public static GeneralProperty default_pig = new GeneralProperty();
	public static GeneralProperty default_rabbit = new GeneralProperty();
	public static GeneralProperty default_sheep = new GeneralProperty();
	public static final GeneralProperty[] default_generalproperty = { GeneralProperty.default_chicken, GeneralProperty.default_cow, GeneralProperty.default_pig, GeneralProperty.default_rabbit, GeneralProperty.default_sheep };
	
	static {
		default_cow.hunger_bmr = 0.005;
		default_cow.hunger_max = 500;
		default_cow.courtship_hunger = default_cow.hunger_max / 20.0;
		default_cow.courtship_probability = 0.0025;
		default_cow.courtship_hungerCondition = 0.8;
		default_cow.excretion_factor = 1 / 50.0;
		default_cow.child_hunger = default_cow.hunger_max / 4.0;
		default_cow.attribute_maxhealth = 30.0;
		default_cow.attribute_movespeed = 0.2;
		default_cow.crank_food_consumption = default_cow.hunger_bmr*2.0;
		default_cow.crank_production = TileEntityCrankAnimal.powerProduction;
		default_cow.drop_meat.add(new DropMeat(Items.beef,5,10));
		default_cow.drop_random.add(new DropRandom(Items.leather,5,10));
		default_cow.drop_random.add(new DropRandom(ModItems.tendon,2,3));
		default_cow.hunger_food.put(new HashItem(Items.wheat), 50.0);
		default_cow.hunger_food.put(new HashItem(Items.reeds), 20.0);
		default_cow.hunger_food.put(new HashItem(ModItems.straw), 10.0);
		default_cow.hunger_food.put(new HashItem(ModItems.mixedFeed), 80.0);
		default_cow.hunger_block.put(new HashBlock(Blocks.tallgrass), 15.0);
		default_cow.hunger_block.put(new HashBlock(Blocks.wheat.getDefaultState().withProperty(BlockCrops.AGE, 7)), 50.0);
		
		default_chicken.hunger_bmr = 0.002;
		default_chicken.hunger_max = 150;
		default_chicken.courtship_hunger = default_chicken.hunger_max / 20.0;
		default_chicken.courtship_probability = 0.0025;
		default_chicken.courtship_hungerCondition = 0.8;
		default_chicken.excretion_factor = 1 / 50.0;
		default_chicken.child_hunger = default_chicken.hunger_max / 4.0;
		default_chicken.attribute_maxhealth = 8.0;
		default_chicken.attribute_movespeed = 0.15;
		default_chicken.crank_food_consumption = default_chicken.hunger_bmr*2.0;
		default_chicken.crank_production = TileEntityCrankAnimal.powerProduction*(default_chicken.crank_food_consumption/default_cow.crank_food_consumption);
		default_chicken.drop_meat.add(new DropMeat(Items.chicken,2,4));
		default_chicken.drop_random.add(new DropRandom(Items.feather,3,6));
		default_chicken.hunger_food.put(new HashItem(Items.wheat_seeds), 20.0);
		default_chicken.hunger_food.put(new HashItem(Items.pumpkin_pie), 25.0);
		default_chicken.hunger_food.put(new HashItem(Items.melon_seeds), 25.0);
		default_chicken.hunger_food.put(new HashItem(ModItems.poppyseed), 20.0);
		default_chicken.hunger_food.put(new HashItem(ModItems.mixedFeed), 80.0);
		default_chicken.hunger_block.put(new HashBlock(Blocks.tallgrass), 15.0);
		default_chicken.hunger_block.put(new HashBlock(Blocks.wheat.getDefaultState().withProperty(BlockCrops.AGE, 0)), 20.0);
		
		default_pig.hunger_bmr = 0.004;
		default_pig.hunger_max = 400;
		default_pig.courtship_hunger = default_pig.hunger_max / 20.0;
		default_pig.courtship_probability = 0.0025;
		default_pig.courtship_hungerCondition = 0.8;
		default_pig.excretion_factor = 1 / 50.0;
		default_pig.child_hunger = default_pig.hunger_max / 4.0;
		default_pig.attribute_maxhealth = 20.0;
		default_pig.attribute_movespeed = 0.25;
		default_pig.crank_food_consumption = default_pig.hunger_bmr*2.0;
		default_pig.crank_production = TileEntityCrankAnimal.powerProduction*(default_pig.crank_food_consumption/default_cow.crank_food_consumption);
		default_pig.drop_meat.add(new DropMeat(Items.porkchop,4,8));
		default_pig.drop_random.add(new DropRandom(ModItems.tendon,1,2));
		default_pig.hunger_food.put(new HashItem(Items.carrot), 40.0);
		default_pig.hunger_food.put(new HashItem(Items.rotten_flesh), 15.0);
		default_pig.hunger_food.put(new HashItem(ModItems.mixedFeed), 80.0);
		default_pig.hunger_block.put(new HashBlock(Blocks.tallgrass), 15.0);
		default_pig.hunger_block.put(new HashBlock(Blocks.carrots.getDefaultState().withProperty(BlockCrops.AGE, 7)), 40.0);
		
		default_rabbit.hunger_bmr = 0.003;
		default_rabbit.hunger_max = 250;
		default_rabbit.courtship_hunger = default_rabbit.hunger_max / 20.0;
		default_rabbit.courtship_probability = 0.0025;
		default_rabbit.courtship_hungerCondition = 0.8;
		default_rabbit.excretion_factor = 1 / 50.0;
		default_rabbit.child_hunger = default_rabbit.hunger_max / 4.0;
		default_rabbit.attribute_maxhealth = 10.0;
		default_rabbit.attribute_movespeed = 0.25;
		default_rabbit.crank_food_consumption = default_rabbit.hunger_bmr*2.0;
		default_rabbit.crank_production = TileEntityCrankAnimal.powerProduction*(default_rabbit.crank_food_consumption/default_cow.crank_food_consumption);
		default_rabbit.drop_meat.add(new DropMeat(Items.rabbit,1,2));
		default_rabbit.drop_random.add(new DropRandom(Items.rabbit_hide,1,2));
		default_rabbit.drop_rare.add(new DropRare(Items.rabbit_foot,0.025));
		default_rabbit.hunger_food.put(new HashItem(Items.carrot), 40.0);
		default_rabbit.hunger_food.put(new HashItem(Items.golden_carrot), 150.0);
		default_rabbit.hunger_food.put(new HashItem(Item.getItemFromBlock(Blocks.yellow_flower)), 20.0);
		default_rabbit.hunger_food.put(new HashItem(ModItems.mixedFeed), 80.0);
		default_rabbit.hunger_block.put(new HashBlock(Blocks.tallgrass), 15.0);
		default_rabbit.hunger_block.put(new HashBlock(Blocks.yellow_flower), 20.0);
		default_rabbit.hunger_block.put(new HashBlock(Blocks.carrots.getDefaultState().withProperty(BlockCrops.AGE, 7)), 40.0);
		
		default_sheep.hunger_bmr = 0.004;
		default_sheep.hunger_max = 400;
		default_sheep.courtship_hunger = default_sheep.hunger_max / 20.0;
		default_sheep.courtship_probability = 0.0025;
		default_sheep.courtship_hungerCondition = 0.8;
		default_sheep.excretion_factor = 1 / 50.0;
		default_sheep.child_hunger = default_sheep.hunger_max / 4.0;
		default_sheep.attribute_maxhealth = 20.0;
		default_sheep.attribute_movespeed = 0.20;
		default_sheep.crank_food_consumption = default_sheep.hunger_bmr*2.0;
		default_sheep.crank_production = TileEntityCrankAnimal.powerProduction*(default_sheep.crank_food_consumption/default_cow.crank_food_consumption);
		default_sheep.drop_meat.add(new DropMeat(Items.mutton,3,6));
		default_sheep.drop_random.add(new DropRandom(ModItems.tendon,1,2));
		default_sheep.hunger_food.put(new HashItem(Items.wheat), 50.0);
		default_sheep.hunger_food.put(new HashItem(Items.reeds), 20.0);
		default_sheep.hunger_food.put(new HashItem(ModItems.straw), 10.0);
		default_sheep.hunger_food.put(new HashItem(ModItems.mixedFeed), 80.0);
		default_sheep.hunger_block.put(new HashBlock(Blocks.tallgrass), 15.0);
		default_sheep.hunger_block.put(new HashBlock(Blocks.wheat.getDefaultState().withProperty(BlockCrops.AGE, 7)), 50.0);
	}
	
	public String[] toStringHungerFood() {
		Set<HashItem> keys = hunger_food.keySet();
		String[] ret = new String[keys.size()];
		Iterator keyIterator = keys.iterator();
		int next=0;
		while (keyIterator.hasNext()) {
			HashItem i = (HashItem) keyIterator.next();
			double value = hunger_food.get(i);
			ret[next++] = i.toString()+"=("+value+")";
		}
		return ret;
	}
	
	public String[] toStringHungerBlock() {
		Set<HashBlock> keys = hunger_block.keySet();
		String[] ret = new String[keys.size()];
		int next=0;
		for (HashBlock i : keys) {
			double value = hunger_block.get(i);
			ret[next++] = i.toString()+"=("+value+")";
		}
		return ret;
	}
	
	public String[] toStringDropMeat() {
		String[] ret = new String[drop_meat.size()];
		int next=0;
		for (DropMeat i : drop_meat) {
			ret[next++] = i.toString();
		}
		return ret;
	}
	
	public String[] toStringDropRandom() {
		String[] ret = new String[drop_random.size()];
		int next=0;
		for (DropRandom i : drop_random) {
			ret[next++] = i.toString();
		}
		return ret;
	}
	
	public String[] toStringDropRare() {
		String[] ret = new String[drop_rare.size()];
		int next=0;
		for (DropRare i : drop_rare) {
			ret[next++] = i.toString();
		}
		return ret;
	}
}
