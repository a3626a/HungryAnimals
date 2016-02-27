package oortcloud.hungryanimals.entities.properties.handler;

import java.util.HashMap;
import java.util.Map.Entry;

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
import oortcloud.hungryanimals.configuration.util.HashBlockState;
import oortcloud.hungryanimals.configuration.util.HashItemType;
import oortcloud.hungryanimals.configuration.util.ValueDropMeat;
import oortcloud.hungryanimals.configuration.util.ValueDropRandom;
import oortcloud.hungryanimals.configuration.util.ValueDropRare;
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

	private HashMap<Class<? extends EntityAnimal>, AnimalCharacteristic> defaultCharacteristicMap;
	private HashMap<Class<? extends EntityAnimal>, AnimalCharacteristic> characteristicMap;
	private HashMap<Class<? extends EntityAnimal>, PropertyFactory> propertyMap;

	public static HungryAnimalManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HungryAnimalManager();
		}
		return INSTANCE;
	}

	private HungryAnimalManager() {
		defaultCharacteristicMap = new HashMap<Class<? extends EntityAnimal>, AnimalCharacteristic>();
		characteristicMap = new HashMap<Class<? extends EntityAnimal>, AnimalCharacteristic>();
		propertyMap = new HashMap<Class<? extends EntityAnimal>, PropertyFactory>();
	}

	public void registerHungryAnimal(Class<? extends EntityAnimal> animal, PropertyFactory propertyFactory) {
		if (propertyFactory != null) {
			propertyMap.put(animal, propertyFactory);
		} else {
			propertyMap.put(animal, (property) -> new ExtendedPropertiesHungryGeneral(property.getClass()));
		}
	}

	public void setAnimalDefaultCharacteristic(Class<? extends EntityAnimal> animal, AnimalCharacteristic characteristic) {
		if (isRegistered(animal)) {
			defaultCharacteristicMap.put(animal, characteristic);
		}
	}

	public AnimalCharacteristic getAnimalCharacteristic(Class<? extends EntityAnimal> animal) {
		if (isRegistered(animal)) {
			if (characteristicMap.containsKey(animal)) {
				return characteristicMap.get(animal);
			} else {
				AnimalCharacteristic characteristic = getBasicCharacteristic();
				characteristicMap.put(animal, characteristic);
				return characteristic;
			}
		}
		return null;
	}

	public void readFromConfig(Configuration config) {
		for (Entry<Class<? extends EntityAnimal>, AnimalCharacteristic> i : defaultCharacteristicMap.entrySet()) {
			String category = ConfigurationHandlerAnimal.categoryGenerator(i.getKey());
			AnimalCharacteristic iCharacteristic = i.getValue();
			AnimalCharacteristic characteristic = new AnimalCharacteristic();

			ConfigurationHandlerAnimal.readDropMeat(config, iCharacteristic.toStringDropMeat(), category, characteristic);
			ConfigurationHandlerAnimal.readDropRandom(config, iCharacteristic.toStringDropRandom(), category, characteristic);
			ConfigurationHandlerAnimal.readDropRare(config, iCharacteristic.toStringDropRare(), category, characteristic);
			ConfigurationHandlerAnimal.ByFoodRate(config, iCharacteristic.toStringHungerFood(), category, characteristic);
			ConfigurationHandlerAnimal.ByBlockRate(config, iCharacteristic.toStringHungerBlock(), category, characteristic);

			for (Entry<IAttribute, Double> j : iCharacteristic.attributeMap.entrySet()) {
				characteristic.attributeMap.put(j.getKey(), config.get(category, j.getKey().getAttributeUnlocalizedName(), j.getValue()).getDouble());
			}
			
			characteristicMap.put(i.getKey(), characteristic);
		}
	}

	public ExtendedPropertiesHungryAnimal createProperty(EntityAnimal animal) {
		return propertyMap.get(animal.getClass()).createProperty(animal);
	}

	public boolean isRegistered(Class<? extends EntityAnimal> animal) {
		return propertyMap.containsKey(animal);
	}

	public void applyAttributes(ExtendedPropertiesHungryAnimal extendedProperty) {
		characteristicMap.get(extendedProperty.entity.getClass()).applyAttributes(extendedProperty);
	}

	public void registerAttributes(EntityLivingBase entity) {
		characteristicMap.get(entity.getClass()).registerAttributes(entity);
	}

	public void init() {
		propertyMap.clear();
		characteristicMap.clear();
		defaultCharacteristicMap.clear();

		AnimalCharacteristic characteristic_chicken = new AnimalCharacteristic();
		AnimalCharacteristic characteristic_cow = new AnimalCharacteristic();
		AnimalCharacteristic characteristic_pig = new AnimalCharacteristic();
		AnimalCharacteristic characteristic_rabbit = new AnimalCharacteristic();
		AnimalCharacteristic characteristic_sheep = new AnimalCharacteristic();

		characteristic_cow.attributeMap.put(ModAttributes.hunger_bmr, 0.005);
		characteristic_cow.attributeMap.put(ModAttributes.hunger_max, 500.0);
		characteristic_cow.attributeMap.put(ModAttributes.courtship_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max) / 20.0);
		characteristic_cow.attributeMap.put(ModAttributes.courtship_probability, 0.0025);
		characteristic_cow.attributeMap.put(ModAttributes.courtship_hungerCondition, 0.8);
		characteristic_cow.attributeMap.put(ModAttributes.excretion_factor, 1 / 50.0);
		characteristic_cow.attributeMap.put(ModAttributes.child_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max) / 4.0);
		characteristic_cow.attributeMap.put(ModAttributes.milk_delay, (double) (5 * 60 * 20));
		characteristic_cow.attributeMap.put(ModAttributes.milk_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max) / 20.0);
		characteristic_cow.attributeMap.put(SharedMonsterAttributes.maxHealth, 30.0);
		characteristic_cow.attributeMap.put(SharedMonsterAttributes.movementSpeed, 0.2);
		characteristic_cow.drop_meat.add(new ValueDropMeat(Items.beef, 5, 10));
		characteristic_cow.drop_random.add(new ValueDropRandom(Items.leather, 5, 10));
		characteristic_cow.drop_random.add(new ValueDropRandom(ModItems.tendon, 2, 3));
		characteristic_cow.hunger_food.put(new HashItemType(Items.wheat), 50.0);
		characteristic_cow.hunger_food.put(new HashItemType(Items.reeds), 20.0);
		characteristic_cow.hunger_block.put(new HashBlockState(Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS)), 15.0);
		characteristic_cow.hunger_block.put(new HashBlockState(Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN)), 15.0);
		characteristic_cow.hunger_block.put(new HashBlockState(Blocks.wheat.getDefaultState().withProperty(BlockCrops.AGE, 7)), 50.0);

		characteristic_chicken.attributeMap.put(ModAttributes.hunger_bmr, 0.002);
		characteristic_chicken.attributeMap.put(ModAttributes.hunger_max, 150.0);
		characteristic_chicken.attributeMap.put(ModAttributes.courtship_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max) / 20.0);
		characteristic_chicken.attributeMap.put(ModAttributes.courtship_probability, 0.0025);
		characteristic_chicken.attributeMap.put(ModAttributes.courtship_hungerCondition, 0.8);
		characteristic_chicken.attributeMap.put(ModAttributes.excretion_factor, 1 / 50.0);
		characteristic_chicken.attributeMap.put(ModAttributes.child_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max) / 4.0);
		characteristic_chicken.attributeMap.put(SharedMonsterAttributes.maxHealth, 8.0);
		characteristic_chicken.attributeMap.put(SharedMonsterAttributes.movementSpeed, 0.15);
		characteristic_chicken.drop_meat.add(new ValueDropMeat(Items.chicken, 2, 4));
		characteristic_chicken.drop_random.add(new ValueDropRandom(Items.feather, 3, 6));
		characteristic_chicken.hunger_food.put(new HashItemType(Items.wheat_seeds), 20.0);
		characteristic_chicken.hunger_food.put(new HashItemType(Items.pumpkin_pie), 25.0);
		characteristic_chicken.hunger_food.put(new HashItemType(Items.melon_seeds), 25.0);
		characteristic_chicken.hunger_block.put(new HashBlockState(Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS)), 15.0);
		characteristic_chicken.hunger_block.put(new HashBlockState(Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN)), 15.0);
		characteristic_chicken.hunger_block.put(new HashBlockState(Blocks.wheat.getDefaultState().withProperty(BlockCrops.AGE, 0)), 20.0);

		characteristic_pig.attributeMap.put(ModAttributes.hunger_bmr, 0.004);
		characteristic_pig.attributeMap.put(ModAttributes.hunger_max, 400.0);
		characteristic_pig.attributeMap.put(ModAttributes.courtship_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max) / 20.0);
		characteristic_pig.attributeMap.put(ModAttributes.courtship_probability, 0.0025);
		characteristic_pig.attributeMap.put(ModAttributes.courtship_hungerCondition, 0.8);
		characteristic_pig.attributeMap.put(ModAttributes.excretion_factor, 1 / 50.0);
		characteristic_pig.attributeMap.put(ModAttributes.child_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max) / 4.0);
		characteristic_pig.attributeMap.put(SharedMonsterAttributes.maxHealth, 20.0);
		characteristic_pig.attributeMap.put(SharedMonsterAttributes.movementSpeed, 0.25);
		characteristic_pig.drop_meat.add(new ValueDropMeat(Items.porkchop, 4, 8));
		characteristic_pig.drop_random.add(new ValueDropRandom(ModItems.tendon, 1, 2));
		characteristic_pig.hunger_food.put(new HashItemType(Items.carrot), 40.0);
		characteristic_pig.hunger_food.put(new HashItemType(Items.rotten_flesh), 15.0);
		characteristic_pig.hunger_block.put(new HashBlockState(Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS)), 15.0);
		characteristic_pig.hunger_block.put(new HashBlockState(Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN)), 15.0);
		characteristic_pig.hunger_block.put(new HashBlockState(Blocks.carrots.getDefaultState().withProperty(BlockCrops.AGE, 7)), 40.0);

		characteristic_rabbit.attributeMap.put(ModAttributes.hunger_bmr, 0.003);
		characteristic_rabbit.attributeMap.put(ModAttributes.hunger_max, 250.0);
		characteristic_rabbit.attributeMap.put(ModAttributes.courtship_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max) / 20.0);
		characteristic_rabbit.attributeMap.put(ModAttributes.courtship_probability, 0.0025);
		characteristic_rabbit.attributeMap.put(ModAttributes.courtship_hungerCondition, 0.8);
		characteristic_rabbit.attributeMap.put(ModAttributes.excretion_factor, 1 / 50.0);
		characteristic_rabbit.attributeMap.put(ModAttributes.child_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max) / 4.0);
		characteristic_rabbit.attributeMap.put(SharedMonsterAttributes.maxHealth, 10.0);
		characteristic_rabbit.attributeMap.put(SharedMonsterAttributes.movementSpeed, 0.25);
		characteristic_rabbit.drop_meat.add(new ValueDropMeat(Items.rabbit, 1, 2));
		characteristic_rabbit.drop_random.add(new ValueDropRandom(Items.rabbit_hide, 1, 2));
		characteristic_rabbit.drop_rare.add(new ValueDropRare(Items.rabbit_foot, 0.025));
		characteristic_rabbit.hunger_food.put(new HashItemType(Items.carrot), 40.0);
		characteristic_rabbit.hunger_food.put(new HashItemType(Items.golden_carrot), 150.0);
		characteristic_rabbit.hunger_food.put(new HashItemType(Item.getItemFromBlock(Blocks.yellow_flower)), 20.0);
		characteristic_rabbit.hunger_block.put(new HashBlockState(Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS)), 15.0);
		characteristic_rabbit.hunger_block.put(new HashBlockState(Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN)), 15.0);
		characteristic_rabbit.hunger_block.put(new HashBlockState(Blocks.yellow_flower), 20.0);
		characteristic_rabbit.hunger_block.put(new HashBlockState(Blocks.carrots.getDefaultState().withProperty(BlockCrops.AGE, 7)), 40.0);

		characteristic_sheep.attributeMap.put(ModAttributes.hunger_bmr, 0.004);
		characteristic_sheep.attributeMap.put(ModAttributes.hunger_max, 400.0);
		characteristic_sheep.attributeMap.put(ModAttributes.courtship_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max) / 20.0);
		characteristic_sheep.attributeMap.put(ModAttributes.courtship_probability, 0.0025);
		characteristic_sheep.attributeMap.put(ModAttributes.courtship_hungerCondition, 0.8);
		characteristic_sheep.attributeMap.put(ModAttributes.excretion_factor, 1 / 50.0);
		characteristic_sheep.attributeMap.put(ModAttributes.child_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max) / 4.0);
		characteristic_sheep.attributeMap.put(SharedMonsterAttributes.maxHealth, 20.0);
		characteristic_sheep.attributeMap.put(SharedMonsterAttributes.movementSpeed, 0.20);
		characteristic_sheep.attributeMap.put(ModAttributes.wool_delay, (double) (5 * 60 * 20));
		characteristic_sheep.attributeMap.put(ModAttributes.wool_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max) / 20.0);
		characteristic_sheep.drop_meat.add(new ValueDropMeat(Items.mutton, 3, 6));
		characteristic_sheep.drop_random.add(new ValueDropRandom(ModItems.tendon, 1, 2));
		characteristic_sheep.hunger_food.put(new HashItemType(Items.wheat), 50.0);
		characteristic_sheep.hunger_food.put(new HashItemType(Items.reeds), 20.0);
		characteristic_sheep.hunger_block.put(new HashBlockState(Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS)), 15.0);
		characteristic_sheep.hunger_block.put(new HashBlockState(Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN)), 15.0);
		characteristic_sheep.hunger_block.put(new HashBlockState(Blocks.wheat.getDefaultState().withProperty(BlockCrops.AGE, 7)), 50.0);

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

		registerHungryAnimal(EntityCow.class, (entity) -> new ExtendedPropertiesHungryCow());
		registerHungryAnimal(EntityChicken.class, (entity) -> new ExtendedPropertiesHungryChicken());
		registerHungryAnimal(EntityPig.class, (entity) -> new ExtendedPropertiesHungryPig());
		registerHungryAnimal(EntityRabbit.class, (entity) -> new ExtendedPropertiesHungryRabbit());
		registerHungryAnimal(EntitySheep.class, (entity) -> new ExtendedPropertiesHungrySheep());

		setAnimalDefaultCharacteristic(EntityCow.class, characteristic_cow);
		setAnimalDefaultCharacteristic(EntityChicken.class, characteristic_chicken);
		setAnimalDefaultCharacteristic(EntityPig.class, characteristic_pig);
		setAnimalDefaultCharacteristic(EntityRabbit.class, characteristic_rabbit);
		setAnimalDefaultCharacteristic(EntitySheep.class, characteristic_sheep);
	}

	public AnimalCharacteristic getBasicCharacteristic() {
		AnimalCharacteristic characteristic = new AnimalCharacteristic();
		characteristic.attributeMap.put(ModAttributes.hunger_bmr, 0.005);
		characteristic.attributeMap.put(ModAttributes.hunger_max, 500.0);
		characteristic.attributeMap.put(ModAttributes.courtship_hunger, characteristic.attributeMap.get(ModAttributes.hunger_max) / 20.0);
		characteristic.attributeMap.put(ModAttributes.courtship_probability, 0.0025);
		characteristic.attributeMap.put(ModAttributes.courtship_hungerCondition, 0.8);
		characteristic.attributeMap.put(ModAttributes.excretion_factor, 1 / 50.0);
		characteristic.attributeMap.put(ModAttributes.child_hunger, characteristic.attributeMap.get(ModAttributes.hunger_max) / 4.0);
		characteristic.attributeMap.put(SharedMonsterAttributes.maxHealth, 30.0);
		characteristic.attributeMap.put(SharedMonsterAttributes.movementSpeed, 0.2);
		characteristic.drop_meat.add(new ValueDropMeat(Items.beef, 5, 10));
		characteristic.drop_random.add(new ValueDropRandom(Items.leather, 5, 10));
		characteristic.drop_random.add(new ValueDropRandom(ModItems.tendon, 2, 3));
		characteristic.hunger_food.put(new HashItemType(Items.wheat), 50.0);
		characteristic.hunger_food.put(new HashItemType(Items.reeds), 20.0);
		characteristic.hunger_block.put(new HashBlockState(Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS)), 15.0);
		characteristic.hunger_block.put(new HashBlockState(Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN)), 15.0);
		characteristic.hunger_block.put(new HashBlockState(Blocks.wheat.getDefaultState().withProperty(BlockCrops.AGE, 7)), 50.0);
		return characteristic;
	}

	@FunctionalInterface
	public interface PropertyFactory {
		public ExtendedPropertiesHungryAnimal createProperty(EntityAnimal entity);
	}

}
