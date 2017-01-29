package oortcloud.hungryanimals.entities.properties.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import oortcloud.hungryanimals.configuration.ConfigurationHandlerAnimal;
import oortcloud.hungryanimals.configuration.util.ValueDropMeat;
import oortcloud.hungryanimals.configuration.util.ValueDropRandom;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState.HashBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryChicken;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryCow;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryGeneral;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryPig;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryRabbit;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungrySheep;
import oortcloud.hungryanimals.items.ModItems;

public class HungryAnimalManager {

	private static HungryAnimalManager INSTANCE;

	private ArrayList<Class<? extends EntityAnimal>> registedClass;
	private HashMap<Class<? extends EntityAnimal>, MutablePair<AnimalCharacteristic,AnimalCharacteristic>> cMap;
	// Left: default, Right: configured value
	private HashMap<Class<? extends EntityAnimal>, PropertyFactory> propertyMap;

	public static HungryAnimalManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HungryAnimalManager();
		}
		return INSTANCE;
	}

	private HungryAnimalManager() {
		registedClass = new ArrayList<Class<? extends EntityAnimal>>();
		cMap = new HashMap<Class<? extends EntityAnimal>, MutablePair<AnimalCharacteristic,AnimalCharacteristic>>();
		propertyMap = new HashMap<Class<? extends EntityAnimal>, PropertyFactory>();
	}

	public void registerHungryAnimal(Class<? extends EntityAnimal> animal, PropertyFactory propertyFactory) {
		if (!registedClass.contains(animal)) {
			registedClass.add(animal);
		}
		
		if (propertyFactory != null) {
			propertyMap.put(animal, propertyFactory);
		} else {
			propertyMap.put(animal, (property) -> new ExtendedPropertiesHungryGeneral(property.getClass()));
		}
		cMap.put(animal, MutablePair.of(new AnimalCharacteristic(), null));
	}

	/*
	public void setAnimalDefaultCharacteristic(Class<? extends EntityAnimal> animal, AnimalCharacteristic characteristic) {
		if (isRegistered(animal)) {
			if (cMap.containsKey(animal)) {
				cMap.get(animal).setLeft(characteristic);
			} else {
				cMap.put(animal, MutablePair.of(characteristic, null));
			}
		}
	}
	 */
	
	public AnimalCharacteristic getAnimalDefaultCharacteristic(Class<? extends EntityAnimal> animal) {
		if (isRegistered(animal)) {
			if (cMap.containsKey(animal)) {
				return cMap.get(animal).getLeft();
			}
		}
		return null;
	}
	
	public AnimalCharacteristic getAnimalCharacteristic(Class<? extends EntityAnimal> animal) {
		if (isRegistered(animal)) {
			if (cMap.containsKey(animal)) {
				return cMap.get(animal).getRight();
			}
		}
		return null;
	}

	public List<Class<? extends EntityAnimal>> getRegisteredAnimal() {
		return registedClass;
	}
	
	public void readFromConfig(Configuration config) {
		for (Entry<Class<? extends EntityAnimal>, MutablePair<AnimalCharacteristic, AnimalCharacteristic>> i : cMap.entrySet()) {
			String category = ConfigurationHandlerAnimal.categoryGenerator(i.getKey());
			AnimalCharacteristic iCharacteristic = i.getValue().getLeft();
			AnimalCharacteristic characteristic = new AnimalCharacteristic();

			ConfigurationHandlerAnimal.ByFoodRate(config, iCharacteristic.toStringHungerFood(), category, characteristic);
			ConfigurationHandlerAnimal.ByBlockRate(config, iCharacteristic.toStringHungerBlock(), category, characteristic);

			for (Entry<IAttribute, Pair<Boolean, Double>> j : iCharacteristic.attributeMap.entrySet()) {
				characteristic.putAttribute(j.getKey(),config.get(category, j.getKey().getAttributeUnlocalizedName(), j.getValue().getRight()).getDouble(), j.getValue().getLeft());
			}
			
			i.getValue().setRight(characteristic);
		}
	}

	public ExtendedPropertiesHungryAnimal createProperty(EntityAnimal animal) {
		return propertyMap.get(animal.getClass()).createProperty(animal);
	}

	public boolean isRegistered(Class<? extends EntityAnimal> animal) {
		return registedClass.contains(animal);
	}

	public void applyAttributes(ExtendedPropertiesHungryAnimal extendedProperty) {
		cMap.get(extendedProperty.entity.getClass()).getRight().applyAttributes(extendedProperty);
	}

	public void registerAttributes(EntityLivingBase entity) {
		cMap.get(entity.getClass()).getRight().registerAttributes(entity);
	}

	public void init() {
		propertyMap.clear();
		cMap.clear();

		registerHungryAnimal(EntityCow.class, (entity) -> new ExtendedPropertiesHungryCow());
		registerHungryAnimal(EntityChicken.class, (entity) -> new ExtendedPropertiesHungryChicken());
		registerHungryAnimal(EntityPig.class, (entity) -> new ExtendedPropertiesHungryPig());
		registerHungryAnimal(EntityRabbit.class, (entity) -> new ExtendedPropertiesHungryRabbit());
		registerHungryAnimal(EntitySheep.class, (entity) -> new ExtendedPropertiesHungrySheep());
		
		AnimalCharacteristic characteristic_chicken = getAnimalDefaultCharacteristic(EntityChicken.class);
		AnimalCharacteristic characteristic_cow = getAnimalDefaultCharacteristic(EntityCow.class);
		AnimalCharacteristic characteristic_pig = getAnimalDefaultCharacteristic(EntityPig.class);
		AnimalCharacteristic characteristic_rabbit = getAnimalDefaultCharacteristic(EntityRabbit.class);
		AnimalCharacteristic characteristic_sheep = getAnimalDefaultCharacteristic(EntitySheep.class);

		characteristic_cow.putAttribute(ModAttributes.hunger_bmr, 0.005, true);
		characteristic_cow.putAttribute(ModAttributes.hunger_max, 500.0, true);
		characteristic_cow.putAttribute(ModAttributes.courtship_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 20.0, true);
		characteristic_cow.putAttribute(ModAttributes.courtship_probability, 0.0025, true);
		characteristic_cow.putAttribute(ModAttributes.courtship_hungerCondition, 0.8, true);
		characteristic_cow.putAttribute(ModAttributes.excretion_factor, 1 / 50.0, true);
		characteristic_cow.putAttribute(ModAttributes.child_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 4.0, true);
		characteristic_cow.putAttribute(ModAttributes.milk_delay, (double) (5 * 60 * 20), true);
		characteristic_cow.putAttribute(ModAttributes.milk_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 20.0, true);
		characteristic_cow.putAttribute(SharedMonsterAttributes.MAX_HEALTH, 30.0, false);
		characteristic_cow.putAttribute(SharedMonsterAttributes.MOVEMENT_SPEED, 0.2, false);
		characteristic_cow.hunger_food.put(new HashItemType(Items.WHEAT), 50.0);
		characteristic_cow.hunger_food.put(new HashItemType(Items.REEDS), 20.0);
		characteristic_cow.hunger_block.put(new HashBlockState(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS)), 15.0);
		characteristic_cow.hunger_block.put(new HashBlockState(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN)), 15.0);
		characteristic_cow.hunger_block.put(new HashBlockState(Blocks.WHEAT.getDefaultState().withProperty(BlockCrops.AGE, 7)), 50.0);

		characteristic_chicken.putAttribute(ModAttributes.hunger_bmr, 0.002, true);
		characteristic_chicken.putAttribute(ModAttributes.hunger_max, 150.0, true);
		characteristic_chicken.putAttribute(ModAttributes.courtship_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 20.0, true);
		characteristic_chicken.putAttribute(ModAttributes.courtship_probability, 0.0025, true);
		characteristic_chicken.putAttribute(ModAttributes.courtship_hungerCondition, 0.8, true);
		characteristic_chicken.putAttribute(ModAttributes.excretion_factor, 1 / 50.0, true);
		characteristic_chicken.putAttribute(ModAttributes.child_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 4.0, true);
		characteristic_chicken.putAttribute(SharedMonsterAttributes.MAX_HEALTH, 8.0, false);
		characteristic_chicken.putAttribute(SharedMonsterAttributes.MOVEMENT_SPEED, 0.15, false);
		characteristic_chicken.hunger_food.put(new HashItemType(Items.WHEAT_SEEDS), 20.0);
		characteristic_chicken.hunger_food.put(new HashItemType(Items.PUMPKIN_SEEDS), 25.0);
		characteristic_chicken.hunger_food.put(new HashItemType(Items.MELON_SEEDS), 25.0);
		characteristic_chicken.hunger_block.put(new HashBlockState(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS)), 15.0);
		characteristic_chicken.hunger_block.put(new HashBlockState(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN)), 15.0);
		characteristic_chicken.hunger_block.put(new HashBlockState(Blocks.WHEAT.getDefaultState().withProperty(BlockCrops.AGE, 0)), 20.0);

		characteristic_pig.putAttribute(ModAttributes.hunger_bmr, 0.004, true);
		characteristic_pig.putAttribute(ModAttributes.hunger_max, 400.0, true);
		characteristic_pig.putAttribute(ModAttributes.courtship_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 20.0, true);
		characteristic_pig.putAttribute(ModAttributes.courtship_probability, 0.0025, true);
		characteristic_pig.putAttribute(ModAttributes.courtship_hungerCondition, 0.8, true);
		characteristic_pig.putAttribute(ModAttributes.excretion_factor, 1 / 50.0, true);
		characteristic_pig.putAttribute(ModAttributes.child_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 4.0, true);
		characteristic_pig.putAttribute(SharedMonsterAttributes.MAX_HEALTH, 20.0, false);
		characteristic_pig.putAttribute(SharedMonsterAttributes.MOVEMENT_SPEED, 0.25, false);
		characteristic_pig.hunger_food.put(new HashItemType(Items.CARROT), 40.0);
		characteristic_pig.hunger_food.put(new HashItemType(Items.ROTTEN_FLESH), 15.0);
		characteristic_pig.hunger_block.put(new HashBlockState(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS)), 15.0);
		characteristic_pig.hunger_block.put(new HashBlockState(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN)), 15.0);
		characteristic_pig.hunger_block.put(new HashBlockState(Blocks.CARROTS.getDefaultState().withProperty(BlockCrops.AGE, 7)), 40.0);

		characteristic_rabbit.putAttribute(ModAttributes.hunger_bmr, 0.003, true);
		characteristic_rabbit.putAttribute(ModAttributes.hunger_max, 250.0, true);
		characteristic_rabbit.putAttribute(ModAttributes.courtship_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 20.0, true);
		characteristic_rabbit.putAttribute(ModAttributes.courtship_probability, 0.0025, true);
		characteristic_rabbit.putAttribute(ModAttributes.courtship_hungerCondition, 0.8, true);
		characteristic_rabbit.putAttribute(ModAttributes.excretion_factor, 1 / 50.0, true);
		characteristic_rabbit.putAttribute(ModAttributes.child_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 4.0, true);
		characteristic_rabbit.putAttribute(SharedMonsterAttributes.MAX_HEALTH, 10.0, false);
		characteristic_rabbit.putAttribute(SharedMonsterAttributes.MOVEMENT_SPEED, 0.25, false);
		characteristic_rabbit.hunger_food.put(new HashItemType(Items.CARROT), 40.0);
		characteristic_rabbit.hunger_food.put(new HashItemType(Items.GOLDEN_CARROT), 150.0);
		characteristic_rabbit.hunger_food.put(new HashItemType(Item.getItemFromBlock(Blocks.YELLOW_FLOWER)), 20.0);
		characteristic_rabbit.hunger_block.put(new HashBlockState(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS)), 15.0);
		characteristic_rabbit.hunger_block.put(new HashBlockState(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN)), 15.0);
		characteristic_rabbit.hunger_block.put(new HashBlockState(Blocks.YELLOW_FLOWER), 20.0);
		characteristic_rabbit.hunger_block.put(new HashBlockState(Blocks.CARROTS.getDefaultState().withProperty(BlockCrops.AGE, 7)), 40.0);

		characteristic_sheep.putAttribute(ModAttributes.hunger_bmr, 0.004, true);
		characteristic_sheep.putAttribute(ModAttributes.hunger_max, 400.0, true);
		characteristic_sheep.putAttribute(ModAttributes.courtship_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 20.0, true);
		characteristic_sheep.putAttribute(ModAttributes.courtship_probability, 0.0025, true);
		characteristic_sheep.putAttribute(ModAttributes.courtship_hungerCondition, 0.8, true);
		characteristic_sheep.putAttribute(ModAttributes.excretion_factor, 1 / 50.0, true);
		characteristic_sheep.putAttribute(ModAttributes.child_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 4.0, true);
		characteristic_sheep.putAttribute(SharedMonsterAttributes.MAX_HEALTH, 20.0, false);
		characteristic_sheep.putAttribute(SharedMonsterAttributes.MOVEMENT_SPEED, 0.20, false);
		characteristic_sheep.putAttribute(ModAttributes.wool_delay, (double) (5 * 60 * 20), true);
		characteristic_sheep.putAttribute(ModAttributes.wool_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 20.0, true);
		characteristic_sheep.hunger_food.put(new HashItemType(Items.WHEAT), 50.0);
		characteristic_sheep.hunger_food.put(new HashItemType(Items.REEDS), 20.0);
		characteristic_sheep.hunger_block.put(new HashBlockState(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS)), 15.0);
		characteristic_sheep.hunger_block.put(new HashBlockState(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN)), 15.0);
		characteristic_sheep.hunger_block.put(new HashBlockState(Blocks.WHEAT.getDefaultState().withProperty(BlockCrops.AGE, 7)), 50.0);

		// default_chicken.hunger_food.put(new HashItemType(ModItems.poppyseed),
		// 20.0);
		// default_chicken.hunger_food.put(new HashItemType(ModItems.mixedFeed),
		// 80.0);
		// default_cow.hunger_food.put(new HashItemType(ModItems.straw), 10.0);
		// default_cow.hunger_food.put(new HashItemType(ModItems.mixedFeed),
		// 80.0);
		// property_chicken.crank_food_consumption =
		// property_chicken.hunger_bmr*2.0;
		// default_chicken.crank_production =
		// TileEntityCrankAnimal.powerProduction*(default_chicken.crank_food_consumption/default_cow.crank_food_consumption);
		// default_pig.hunger_food.put(new HashItemType(ModItems.mixedFeed),
		// 80.0);
		// default_pig.crank_production =
		// TileEntityCrankAnimal.powerProduction*(default_pig.crank_food_consumption/default_cow.crank_food_consumption);
		// default_rabbit.crank_production =
		// TileEntityCrankAnimal.powerProduction*(default_rabbit.crank_food_consumption/default_cow.crank_food_consumption);
		// default_rabbit.hunger_food.put(new HashItemType(ModItems.mixedFeed),
		// 80.0);
		// default_sheep.crank_production =
		// TileEntityCrankAnimal.powerProduction*(default_sheep.crank_food_consumption/default_cow.crank_food_consumption);
		// default_sheep.hunger_food.put(new HashItemType(ModItems.straw),
		// 10.0);
		// default_sheep.hunger_food.put(new HashItemType(ModItems.mixedFeed),
		// 80.0);
		// property_pig.crank_food_consumption = property_pig.hunger_bmr * 2.0;
		// property_rabbit.crank_food_consumption = property_rabbit.hunger_bmr *
		// 2.0;
		// property_sheep.crank_food_consumption = property_sheep.hunger_bmr *
		// 2.0;
	}

	public static void setBasicCharacteristic(Class<? extends EntityAnimal> animalclass) {
		AnimalCharacteristic characteristic = getInstance().getAnimalDefaultCharacteristic(animalclass);
		characteristic.putAttribute(ModAttributes.hunger_bmr, 0.005, true);
		characteristic.putAttribute(ModAttributes.hunger_max, 500.0, true);
		characteristic.putAttribute(ModAttributes.courtship_hunger, characteristic.attributeMap.get(ModAttributes.hunger_max).getRight() / 20.0, true);
		characteristic.putAttribute(ModAttributes.courtship_probability, 0.0025, true);
		characteristic.putAttribute(ModAttributes.courtship_hungerCondition, 0.8, true);
		characteristic.putAttribute(ModAttributes.excretion_factor, 1 / 50.0, true);
		characteristic.putAttribute(ModAttributes.child_hunger, characteristic.attributeMap.get(ModAttributes.hunger_max).getRight() / 4.0, true);
		characteristic.putAttribute(SharedMonsterAttributes.MAX_HEALTH, 30.0, false);
		characteristic.putAttribute(SharedMonsterAttributes.MOVEMENT_SPEED, 0.2, false);
		characteristic.hunger_food.put(new HashItemType(Items.WHEAT), 50.0);
		characteristic.hunger_food.put(new HashItemType(Items.REEDS), 20.0);
		characteristic.hunger_block.put(new HashBlockState(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS)), 15.0);
		characteristic.hunger_block.put(new HashBlockState(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN)), 15.0);
		characteristic.hunger_block.put(new HashBlockState(Blocks.WHEAT.getDefaultState().withProperty(BlockCrops.AGE, 7)), 50.0);
	}

	@FunctionalInterface
	public interface PropertyFactory {
		public ExtendedPropertiesHungryAnimal createProperty(EntityAnimal entity);
	}

}
