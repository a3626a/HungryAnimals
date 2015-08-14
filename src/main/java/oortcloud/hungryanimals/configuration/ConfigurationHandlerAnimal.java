package oortcloud.hungryanimals.configuration;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Configuration;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.configuration.util.DropMeat;
import oortcloud.hungryanimals.configuration.util.DropRandom;
import oortcloud.hungryanimals.configuration.util.DropRare;
import oortcloud.hungryanimals.configuration.util.HashBlock;
import oortcloud.hungryanimals.configuration.util.HashItem;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryChicken;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryCow;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryPig;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryRabbit;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungrySheep;
import oortcloud.hungryanimals.entities.properties.handler.GenericEntityManager;
import oortcloud.hungryanimals.entities.properties.handler.GenericPropertiesHandler;
import oortcloud.hungryanimals.entities.properties.handler.GenericProperty;
import oortcloud.hungryanimals.items.ModItems;

import org.apache.commons.lang3.ArrayUtils;

public class ConfigurationHandlerAnimal {

	public static Configuration config;

	public static final Class[] defualt_class = { EntityChicken.class, EntityCow.class, EntityPig.class, EntityRabbit.class, EntitySheep.class };

	public static final String CATEGORY_Generic = "Generic";
	public static final String KEY_entities = "Added Mod Entities";

	public static final String KEY_hunger_bmr = "HungerUsage: basic rate";
	public static final String KEY_hunger_max = "Abiltiy: max hunger";
	public static final String KEY_hunger_food = "HungerAcquisition: byFood Rate";
	public static final String KEY_hunger_block = "HungerAcquisition: byBlock Rate";
	public static final String KEY_drop_meat = "Ability: items drops that the amount is decided by hunger value";
	public static final String KEY_drop_random = "Ability: items drops that the amount is decided randomly";
	public static final String KEY_drop_rare = "Ability: items drops that is very rare";
	public static final String KEY_courtship_hunger = "HungerUsage: to do the courting";
	public static final String KEY_courtship_probability = "Behavior: probability to do the courting per a tick";
	public static final String KEY_courtship_hungerCondition = "Behavior: needed ratio(hunger/maxhunger) to do the courting";
	public static final String KEY_excretion_factor = "Abiltiy: needed amount of hunger consumption to produce a pile of excreta";
	public static final String KEY_child_hunger = "HungerUsage: to make a child";
	public static final String KEY_milk_hunger = "HungerUsage: to produce a bucket of milk";
	public static final String KEY_milk_delay = "Behavior: ticks needed to produce a bucket of milk";
	public static final String KEY_wool_hunger = "HungerUsage: Wool";
	public static final String KEY_wool_delay = "Behavior: ticks needed to produce a block of wool";

	public static void init(File file) {
		config = new Configuration(file);
		config.load();
	}

	public static String categoryGenerator(Class<? extends EntityAnimal> entityClass) {
		return (String) EntityList.classToStringMapping.get(entityClass);
	}

	public static void sync() {

		HungryAnimals.logger.info("Configuration: Animal start");
		
		GenericPropertiesHandler.getInstance().init();
		GenericEntityManager.getInstance().init();

		for (String i : config.get(CATEGORY_Generic, KEY_entities, ArrayUtils.EMPTY_STRING_ARRAY).getStringList()) {
			HungryAnimals.logger.info("Configuration: finding entity " + i + " in Entity List...");
			Class entityClass = (Class) EntityList.stringToClassMapping.get(i);
			if (entityClass != null && EntityAnimal.class.isAssignableFrom(entityClass)) {
				HungryAnimals.logger.info("Configuration: have detected mod entity: " + i);
				GenericEntityManager.getInstance().entities.add(entityClass);
			}
		}

		for (Object i : EntityList.classToStringMapping.keySet()) {
			if (EntityAnimal.class.isAssignableFrom((Class)i)) {
				HungryAnimals.logger.info("Configuration: Registered Entity Class " + (String)EntityList.classToStringMapping.get(i) + " is compatible.");
			} else {
				HungryAnimals.logger.info("Configuration: Registered Entity Class " + (String)EntityList.classToStringMapping.get(i) + " is not compatible.");
			}
		}
		
		for (Class<? extends EntityAnimal> i : GenericEntityManager.getInstance().entities) {
			GenericProperty iProperty = new GenericProperty();
			String category = categoryGenerator(i);
			iProperty.default_hunger_bmr = config.get(category, KEY_hunger_bmr, 0.001).getDouble();
			iProperty.default_hunger_max = config.get(category, KEY_hunger_max, 100).getDouble();
			iProperty.default_courtship_hunger = config.get(category, KEY_courtship_hunger, iProperty.default_hunger_max / 20.0).getDouble();
			iProperty.default_courtship_probability = config.get(category, KEY_courtship_probability, 0.0025).getDouble();
			iProperty.default_courtship_hungerCondition = config.get(category, KEY_courtship_hungerCondition, 0.8).getDouble();
			iProperty.default_excretion_factor = 1 / config.get(category, KEY_excretion_factor, 50).getDouble();
			iProperty.default_child_hunger = config.get(category, KEY_child_hunger, iProperty.default_hunger_max / 4.0).getDouble();

			readDropMeat(new String[] {}, category, iProperty);
			readDropRandom(new String[] {}, category, iProperty);
			readDropRare(new String[] {}, category, iProperty);
			ByFoodRate(new String[] {}, category, iProperty);
			ByBlockRate(new String[] {}, category, iProperty);

			GenericPropertiesHandler.getInstance().propertyMap.put(i, iProperty);
		}

		for (Class i : defualt_class) {
			GenericEntityManager.getInstance().entities.add(i);
		}

		GenericProperty chicken = new GenericProperty();
		String categoryChicken = categoryGenerator(EntityChicken.class);
		chicken.default_hunger_bmr = config.get(categoryChicken, KEY_hunger_bmr, 0.002).getDouble();
		chicken.default_hunger_max = config.get(categoryChicken, KEY_hunger_max, 150).getDouble();
		chicken.default_courtship_hunger = config.get(categoryChicken, KEY_courtship_hunger, chicken.default_hunger_max / 20.0).getDouble();
		chicken.default_courtship_probability = config.get(categoryChicken, KEY_courtship_probability, 0.0025).getDouble();
		chicken.default_courtship_hungerCondition = config.get(categoryChicken, KEY_courtship_hungerCondition, 0.8).getDouble();
		chicken.default_excretion_factor = 1 / config.get(categoryChicken, KEY_excretion_factor, 50).getDouble();
		chicken.default_child_hunger = config.get(categoryChicken, KEY_child_hunger, chicken.default_hunger_max / 4.0).getDouble();
		readDropMeat(new String[] { "((" + Item.itemRegistry.getNameForObject(Items.chicken) + "),2,4)" }, categoryChicken, chicken);
		readDropRandom(new String[] { "((" + Item.itemRegistry.getNameForObject(Items.feather) + "),3,6)" }, categoryChicken, chicken);
		readDropRare(new String[] {}, categoryChicken, chicken);
		ByFoodRate(
				new String[] { "(" + Item.itemRegistry.getNameForObject(Items.wheat_seeds) + ")=(20.0)", "(" + Item.itemRegistry.getNameForObject(Items.pumpkin_seeds) + ")=(25.0)",
						"(" + Item.itemRegistry.getNameForObject(Items.melon_seeds) + ")=(25.0)", "(" + Item.itemRegistry.getNameForObject(ModItems.poppyseed) + ")=(20.0)",
						"(" + Item.itemRegistry.getNameForObject(ModItems.mixedFeed) + ")=(80.0)" }, categoryChicken, chicken);
		ByBlockRate(new String[] { "(" + Block.blockRegistry.getNameForObject(Blocks.tallgrass) + ")=(15.0)", "(" + Block.blockRegistry.getNameForObject(Blocks.wheat) + ",((age,0)))=(50.0)" }, categoryChicken, chicken);
		GenericPropertiesHandler.getInstance().propertyMap.put(EntityChicken.class, chicken);

		GenericProperty cow = new GenericProperty();
		String categoryCow = categoryGenerator(EntityCow.class);
		cow.default_hunger_bmr = config.get(categoryCow, KEY_hunger_bmr, 0.005).getDouble();
		cow.default_hunger_max = config.get(categoryCow, KEY_hunger_max, 500).getDouble();
		cow.default_courtship_hunger = config.get(categoryCow, KEY_courtship_hunger, cow.default_hunger_max / 20.0).getDouble();
		cow.default_courtship_probability = config.get(categoryCow, KEY_courtship_probability, 0.0025).getDouble();
		cow.default_courtship_hungerCondition = config.get(categoryCow, KEY_courtship_hungerCondition, 0.8).getDouble();
		cow.default_excretion_factor = 1 / config.get(categoryCow, KEY_excretion_factor, 50).getDouble();
		cow.default_child_hunger = config.get(categoryCow, KEY_child_hunger, cow.default_hunger_max / 4.0).getDouble();
		ExtendedPropertiesHungryCow.default_milk_delay = config.get(categoryCow, KEY_milk_delay, 5 * 60 * 20).getInt();
		ExtendedPropertiesHungryCow.default_milk_hunger = config.get(categoryCow, KEY_milk_hunger, cow.default_hunger_max / 20.0).getDouble();
		readDropMeat(new String[] { "((" + Item.itemRegistry.getNameForObject(Items.beef) + "),5,10)" }, categoryCow, cow);
		readDropRandom(new String[] { "((" + Item.itemRegistry.getNameForObject(Items.leather) + "),5,10)", "((" + Item.itemRegistry.getNameForObject(ModItems.tendon) + "),2,3)" }, categoryCow, cow);
		readDropRare(new String[] {}, categoryCow, cow);
		ByFoodRate(new String[] { "(" + Item.itemRegistry.getNameForObject(Items.wheat) + ")=(50.0)", "(" + Item.itemRegistry.getNameForObject(Items.reeds) + ")=(20.0)",
				"(" + Item.itemRegistry.getNameForObject(ModItems.straw) + ")=(10.0)", "(" + Item.itemRegistry.getNameForObject(ModItems.mixedFeed) + ")=(80.0)" }, categoryCow, cow);
		ByBlockRate(new String[] { "(" + Block.blockRegistry.getNameForObject(Blocks.tallgrass) + ")=(15.0)", "(" + Block.blockRegistry.getNameForObject(Blocks.wheat) + ",((age,7)))=(50.0)" }, categoryCow, cow);
		GenericPropertiesHandler.getInstance().propertyMap.put(EntityCow.class, cow);

		GenericProperty pig = new GenericProperty();
		String categoryPig = categoryGenerator(EntityPig.class);
		pig.default_hunger_bmr = config.get(categoryPig, KEY_hunger_bmr, 0.004).getDouble();
		pig.default_hunger_max = config.get(categoryPig, KEY_hunger_max, 400).getDouble();
		pig.default_courtship_hunger = config.get(categoryPig, KEY_courtship_hunger, pig.default_hunger_max / 20.0).getDouble();
		pig.default_courtship_probability = config.get(categoryPig, KEY_courtship_probability, 0.0025).getDouble();
		pig.default_courtship_hungerCondition = config.get(categoryPig, KEY_courtship_hungerCondition, 0.8).getDouble();
		pig.default_excretion_factor = 1 / config.get(categoryPig, KEY_excretion_factor, 50).getDouble();
		pig.default_child_hunger = config.get(categoryPig, KEY_child_hunger, pig.default_hunger_max / 4.0).getDouble();
		readDropMeat(new String[] { "((" + Item.itemRegistry.getNameForObject(Items.porkchop) + "),4,8)" }, categoryPig, pig);
		readDropRandom(new String[] { "((" + Item.itemRegistry.getNameForObject(ModItems.tendon) + "),1,2)" }, categoryPig, pig);
		readDropRare(new String[] {}, categoryPig, pig);
		ByFoodRate(new String[] { "(" + Item.itemRegistry.getNameForObject(Items.carrot) + ")=(40.0)", "(" + Item.itemRegistry.getNameForObject(Items.rotten_flesh) + ")=(15.0)",
				"(" + Item.itemRegistry.getNameForObject(ModItems.mixedFeed) + ")=(80.0)" }, categoryPig, pig);
		ByBlockRate(new String[] { "(" + Block.blockRegistry.getNameForObject(Blocks.tallgrass) + ")=(15.0)", "(" + Block.blockRegistry.getNameForObject(Blocks.carrots) + ",((age,7)))=(40.0)" }, categoryPig, pig);
		GenericPropertiesHandler.getInstance().propertyMap.put(EntityPig.class, pig);

		GenericProperty rabbit = new GenericProperty();
		String categoryRabbit = categoryGenerator(EntityRabbit.class);
		rabbit.default_hunger_bmr = config.get(categoryRabbit, KEY_hunger_bmr, 0.003).getDouble();
		rabbit.default_hunger_max = config.get(categoryRabbit, KEY_hunger_max, 250).getDouble();
		rabbit.default_courtship_hunger = config.get(categoryRabbit, KEY_courtship_hunger, rabbit.default_hunger_max / 20.0).getDouble();
		rabbit.default_courtship_probability = config.get(categoryRabbit, KEY_courtship_probability, 0.0025).getDouble();
		rabbit.default_courtship_hungerCondition = config.get(categoryRabbit, KEY_courtship_hungerCondition, 0.8).getDouble();
		rabbit.default_excretion_factor = 1 / config.get(categoryRabbit, KEY_excretion_factor, 50).getDouble();
		rabbit.default_child_hunger = config.get(categoryRabbit, KEY_child_hunger, rabbit.default_hunger_max / 4.0).getDouble();
		readDropMeat(new String[] { "((" + Item.itemRegistry.getNameForObject(Items.rabbit) + "),1,2)" }, categoryRabbit, rabbit);
		readDropRandom(new String[] { "((" + Item.itemRegistry.getNameForObject(Items.rabbit_hide) + "),1,2)" }, categoryRabbit, rabbit);
		readDropRare(new String[] { "((" + Item.itemRegistry.getNameForObject(Items.rabbit_foot) + "),0.025)" }, categoryRabbit, rabbit);
		ByFoodRate(
				new String[] { "(" + Item.itemRegistry.getNameForObject(Items.carrot) + ")=(40.0)", "(" + Item.itemRegistry.getNameForObject(ItemBlock.getItemFromBlock(Blocks.yellow_flower)) + ")=(20.0)",
						"(" + Item.itemRegistry.getNameForObject(Items.golden_carrot) + ")=(150.0)", "(" + Item.itemRegistry.getNameForObject(ModItems.mixedFeed) + ")=(80.0)" }, categoryRabbit, rabbit);
		ByBlockRate(
				new String[] { "(" + Block.blockRegistry.getNameForObject(Blocks.tallgrass) + ")=(15.0)", "(" + Block.blockRegistry.getNameForObject(Blocks.yellow_flower) + ")=(20.0)",
						"(" + Block.blockRegistry.getNameForObject(Blocks.carrots) + ",((age,7)))=(40.0)" }, categoryRabbit, rabbit);
		GenericPropertiesHandler.getInstance().propertyMap.put(EntityRabbit.class, rabbit);

		GenericProperty sheep = new GenericProperty();
		String categorySheep = categoryGenerator(EntitySheep.class);
		sheep.default_hunger_bmr = config.get(categorySheep, KEY_hunger_bmr, 0.004).getDouble();
		sheep.default_hunger_max = config.get(categorySheep, KEY_hunger_max, 400).getDouble();
		sheep.default_courtship_hunger = config.get(categorySheep, KEY_courtship_hunger, sheep.default_hunger_max / 20.0).getDouble();
		sheep.default_courtship_probability = config.get(categorySheep, KEY_courtship_probability, 0.0025).getDouble();
		sheep.default_courtship_hungerCondition = config.get(categorySheep, KEY_courtship_hungerCondition, 0.8).getDouble();
		sheep.default_excretion_factor = 1 / config.get(categorySheep, KEY_excretion_factor, 50).getDouble();
		sheep.default_child_hunger = config.get(categorySheep, KEY_child_hunger, sheep.default_hunger_max / 4.0).getDouble();
		ExtendedPropertiesHungrySheep.default_wool_delay = config.get(categorySheep, KEY_wool_delay, 5 * 60 * 20).getInt();
		ExtendedPropertiesHungrySheep.default_wool_hunger = config.get(categorySheep, KEY_wool_hunger, sheep.default_hunger_max / 20.0).getDouble();
		readDropMeat(new String[] { "((" + Item.itemRegistry.getNameForObject(Items.mutton) + "),3,6)" }, categorySheep, sheep);
		readDropRandom(new String[] { "((" + Item.itemRegistry.getNameForObject(ModItems.tendon) + "),1,2)" }, categorySheep, sheep);
		readDropRare(new String[] {}, categorySheep, sheep);
		ByFoodRate(new String[] { "(" + Item.itemRegistry.getNameForObject(Items.wheat) + ")=(50.0)", "(" + Item.itemRegistry.getNameForObject(Items.reeds) + ")=(20.0)",
				"(" + Item.itemRegistry.getNameForObject(ModItems.straw) + ")=(10.0)", "(" + Item.itemRegistry.getNameForObject(ModItems.mixedFeed) + ")=(80.0)" }, categorySheep, sheep);
		ByBlockRate(new String[] { "(" + Block.blockRegistry.getNameForObject(Blocks.tallgrass) + ")=(15.0)", "(" + Block.blockRegistry.getNameForObject(Blocks.wheat) + ",((age,7)))=(50.0)" }, categorySheep, sheep);
		GenericPropertiesHandler.getInstance().propertyMap.put(EntitySheep.class, sheep);

		config.save();

	}

	private static void readDropMeat(String[] defaultfood, String category, GenericProperty target) {
		String[] drops;
		drops = config.get(category, KEY_drop_meat, defaultfood).getStringList();
		for (String i : drops) {
			DropMeat j = ConfigurationHelper.instance.getDropMeat(i);
			if (j != null) {
				target.default_drop_meat.add(j);
			} else {
				System.out.println("\"" + i + "\" is not added. Format error");
				continue;
			}
		}
	}

	private static void readDropRandom(String[] defaultfood, String category, GenericProperty target) {
		String[] drops;
		drops = config.get(category, KEY_drop_random, defaultfood).getStringList();
		for (String i : drops) {
			DropRandom j = ConfigurationHelper.instance.getDropRandom(i);
			if (j != null) {
				target.default_drop_random.add(j);
			} else {
				System.out.println("\"" + i + "\" is not added. Format error");
				continue;
			}
		}
	}

	private static void readDropRare(String[] defaultfood, String category, GenericProperty target) {
		String[] drops;
		drops = config.get(category, KEY_drop_rare, defaultfood).getStringList();
		for (String i : drops) {
			DropRare j = ConfigurationHelper.instance.getDropRare(i);
			if (j != null) {
				target.default_drop_rare.add(j);
			} else {
				System.out.println("\"" + i + "\" is not added. Format error");
				continue;
			}
		}
	}

	/**
	 * 
	 * @param defaultfood
	 *            : (item)=(hunger)
	 * @param category
	 * @param target
	 */
	private static void ByFoodRate(String[] defaultfood, String category, GenericProperty target) {
		String[] food;
		food = config.get(category, KEY_hunger_food, defaultfood).getStringList();
		for (String i : food) {
			String[] split = StringParser.splitByLevel(i.replaceAll(" ", ""), '=');

			if (split.length == 2) {
				HashItem item = ConfigurationHelper.instance.getHashItem(split[0]);
				double hunger = Double.parseDouble(StringParser.reduceLevel(split[1]));
				target.default_hunger_food.put(item, hunger);

			} else {
				System.out.println("\"" + i + "\" is not added. Format error");
				continue;
			}
		}
	}

	private static void ByBlockRate(String[] defaultBlock, String category, GenericProperty target) {
		String[] block;
		block = config.get(category, KEY_hunger_block, defaultBlock).getStringList();
		for (String i : block) {
			String[] split = StringParser.splitByLevel(i.replaceAll(" ", ""), '=');

			if (split.length == 2) {
				HashBlock hashblock = ConfigurationHelper.instance.getHashBlock(split[0]);
				double hunger = Double.parseDouble(StringParser.reduceLevel(split[1]));
				target.default_hunger_block.put(hashblock, hunger);

			} else {
				System.out.println("\"" + i + "\" is not added. Format error");
				continue;
			}
		}
	}

}
