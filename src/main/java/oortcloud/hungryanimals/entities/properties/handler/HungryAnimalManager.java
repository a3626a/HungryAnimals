package oortcloud.hungryanimals.entities.properties.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraftforge.common.config.Configuration;
import oortcloud.hungryanimals.configuration.ConfigurationHandlerAnimal;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryGeneral;

public class HungryAnimalManager {

	private static HungryAnimalManager INSTANCE;

	private List<Class<? extends EntityAnimal>> registedClass;

	public static HungryAnimalManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HungryAnimalManager();
		}
		return INSTANCE;
	}

	private HungryAnimalManager() {
		registedClass = new ArrayList<Class<? extends EntityAnimal>>();
	}

	public void registerHungryAnimal(Class<? extends EntityAnimal> animal) {
		if (!registedClass.contains(animal)) {
			registedClass.add(animal);
		}
	}

	public List<Class<? extends EntityAnimal>> getRegisteredAnimal() {
		return registedClass;
	}
	
	public void readFromConfig(Configuration config) {
		for (Entry<Class<? extends EntityAnimal>, MutablePair<AnimalCharacteristic, AnimalCharacteristic>> i : cMap.entrySet()) {
			String category = ConfigurationHandlerAnimal.categoryGenerator(i.getKey());
			AnimalCharacteristic iCharacteristic = i.getValue().getLeft();
			AnimalCharacteristic characteristic = new AnimalCharacteristic();

			//TODO READ FOOD PREFERENCES HERE

			for (Entry<IAttribute, Pair<Boolean, Double>> j : iCharacteristic.attributeMap.entrySet()) {
				characteristic.putAttribute(j.getKey(),config.get(category, j.getKey().getAttributeUnlocalizedName(), j.getValue().getRight()).getDouble(), j.getValue().getLeft());
			}
			
			i.getValue().setRight(characteristic);
		}
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
		registerHungryAnimal(EntityCow.class);
		registerHungryAnimal(EntityChicken.class);
		registerHungryAnimal(EntityPig.class);
		registerHungryAnimal(EntityRabbit.class);
		registerHungryAnimal(EntitySheep.class);
		
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

		characteristic_chicken.putAttribute(ModAttributes.hunger_bmr, 0.002, true);
		characteristic_chicken.putAttribute(ModAttributes.hunger_max, 150.0, true);
		characteristic_chicken.putAttribute(ModAttributes.courtship_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 20.0, true);
		characteristic_chicken.putAttribute(ModAttributes.courtship_probability, 0.0025, true);
		characteristic_chicken.putAttribute(ModAttributes.courtship_hungerCondition, 0.8, true);
		characteristic_chicken.putAttribute(ModAttributes.excretion_factor, 1 / 50.0, true);
		characteristic_chicken.putAttribute(ModAttributes.child_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 4.0, true);
		characteristic_chicken.putAttribute(SharedMonsterAttributes.MAX_HEALTH, 8.0, false);
		characteristic_chicken.putAttribute(SharedMonsterAttributes.MOVEMENT_SPEED, 0.15, false);

		characteristic_pig.putAttribute(ModAttributes.hunger_bmr, 0.004, true);
		characteristic_pig.putAttribute(ModAttributes.hunger_max, 400.0, true);
		characteristic_pig.putAttribute(ModAttributes.courtship_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 20.0, true);
		characteristic_pig.putAttribute(ModAttributes.courtship_probability, 0.0025, true);
		characteristic_pig.putAttribute(ModAttributes.courtship_hungerCondition, 0.8, true);
		characteristic_pig.putAttribute(ModAttributes.excretion_factor, 1 / 50.0, true);
		characteristic_pig.putAttribute(ModAttributes.child_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 4.0, true);
		characteristic_pig.putAttribute(SharedMonsterAttributes.MAX_HEALTH, 20.0, false);
		characteristic_pig.putAttribute(SharedMonsterAttributes.MOVEMENT_SPEED, 0.25, false);

		characteristic_rabbit.putAttribute(ModAttributes.hunger_bmr, 0.003, true);
		characteristic_rabbit.putAttribute(ModAttributes.hunger_max, 250.0, true);
		characteristic_rabbit.putAttribute(ModAttributes.courtship_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 20.0, true);
		characteristic_rabbit.putAttribute(ModAttributes.courtship_probability, 0.0025, true);
		characteristic_rabbit.putAttribute(ModAttributes.courtship_hungerCondition, 0.8, true);
		characteristic_rabbit.putAttribute(ModAttributes.excretion_factor, 1 / 50.0, true);
		characteristic_rabbit.putAttribute(ModAttributes.child_hunger, characteristic_cow.attributeMap.get(ModAttributes.hunger_max).getRight() / 4.0, true);
		characteristic_rabbit.putAttribute(SharedMonsterAttributes.MAX_HEALTH, 10.0, false);
		characteristic_rabbit.putAttribute(SharedMonsterAttributes.MOVEMENT_SPEED, 0.25, false);

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

}
