package oortcloud.hungryanimals.entities.properties.handler;

import java.util.ArrayList;
import java.util.List;
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
import oortcloud.hungryanimals.entities.attributes.AttributeManager;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;

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

	public void init() {
		registerHungryAnimal(EntityCow.class);
		registerHungryAnimal(EntityChicken.class);
		registerHungryAnimal(EntityPig.class);
		registerHungryAnimal(EntityRabbit.class);
		registerHungryAnimal(EntitySheep.class);

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
