package oortcloud.hungryanimals.entities.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityZombieHorse;

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

	public boolean isRegistered(Class<? extends EntityAnimal> animal) {
		return registedClass.contains(animal);
	}

	public void init() {
		registerHungryAnimal(EntityCow.class);
		registerHungryAnimal(EntityChicken.class);
		registerHungryAnimal(EntityPig.class);
		registerHungryAnimal(EntityRabbit.class);
		registerHungryAnimal(EntitySheep.class);
		registerHungryAnimal(EntityMooshroom.class);
		registerHungryAnimal(EntityHorse.class);
		registerHungryAnimal(EntityMule.class);
		registerHungryAnimal(EntityDonkey.class);
		registerHungryAnimal(EntitySkeletonHorse.class);
		registerHungryAnimal(EntityZombieHorse.class);
	}

}
